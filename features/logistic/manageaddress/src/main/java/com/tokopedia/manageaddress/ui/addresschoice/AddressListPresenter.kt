package com.tokopedia.manageaddress.ui.addresschoice

import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import rx.Subscriber
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
        usecase.unsubscribe()
        mView = null
    }

    override fun getAddress(prevState: Int, localChosenAddrId: Int, isWhitelistChosenAddress: Boolean) {
        usecase.execute(EMPTY_STRING, prevState, localChosenAddrId, isWhitelistChosenAddress)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace()
                }
                .subscribe(getAddressHandler(EMPTY_STRING))
    }

    override fun searchAddress(query: String, prevState: Int, localChosenAddrId: Int, isWhitelistChosenAddress: Boolean) {
         usecase.execute(query, prevState, localChosenAddrId, isWhitelistChosenAddress)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace()
                }
                .subscribe(getAddressHandler(query))
    }

    override fun loadMore(prevState: Int, localChosenAddrId: Int, isWhitelistChosenAddress: Boolean) {
        // Always true until has_next property is ready from backend, still works fine
        // if (!mHasNext) return
        usecase.loadMore(mCurrentQuery, mCurrentPage + 1, prevState, localChosenAddrId, isWhitelistChosenAddress)
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