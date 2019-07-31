package com.tokopedia.checkout.view.feature.addressoptions

import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.cachemanager.db.model.PersistentCacheDbModel
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.AddressListModel
import com.tokopedia.checkout.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel
import com.tokopedia.transactionanalytics.CheckoutAnalyticsChangeAddress
import rx.Subscriber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val EMPTY_STRING: String = ""

/**
 * Created by fajarnuha on 2019-05-24.
 */
class AddressListPresenter @Inject constructor(
        val usecase: GetAddressCornerUseCase,
        val analytics: CheckoutAnalyticsChangeAddress
) : AddressListContract.Presenter {

    private var mView: AddressListContract.View? = null
    private var mCurrentQuery: String = ""
    private var mCurrentPage: Int = 1
    private var mHasNext: Boolean = false

    override fun attachView(view: AddressListContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getAddress() {
        usecase.execute(EMPTY_STRING)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace()
                }
                .subscribe(getAddressHandler(EMPTY_STRING))
    }

    override fun searchAddress(query: String) {
        usecase.execute(query)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace()
                }
                .subscribe(getAddressHandler(query))
    }

    override fun loadMore() {
        // Always true until has_next property is ready from backend, still works fine
        // if (!mHasNext) return
        usecase.loadMore(mCurrentQuery, mCurrentPage + 1)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace()
                }
                .subscribe(
                        { result ->
                            mCurrentPage++
                            mView?.updateList(result.listAddress.toMutableList())
                        }, { e -> mView?.showError(e) }, {}
                )
    }

    override fun saveLastCorner(model: RecipientAddressModel) {
        PersistentCacheManager.instance.put("last_chosen_corner", model, TimeUnit.DAYS.toMillis(7))
    }

    override fun getLastCorner(): RecipientAddressModel? {
        return PersistentCacheManager.instance.get(
                "last_chosen_corner", RecipientAddressModel::class.java, null)
    }

    private fun getAddressHandler(query: String): Subscriber<AddressListModel> =
            object : Subscriber<AddressListModel>() {
                override fun onNext(t: AddressListModel?) {
                    t?.let {
                        mView?.setToken(it.token)
                        mHasNext = it.hasNext ?: false
                        mCurrentQuery = query
                        mCurrentPage = 1
                        if (it.listAddress.isNotEmpty()) {
                            mView?.showList(it.listAddress.toMutableList())
                        } else {
                            mView?.showListEmpty()
                            analytics.eventSearchResultNotFound(query)
                        }
                    }
                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    mView?.showError(e ?: Throwable())
                }
            }

}