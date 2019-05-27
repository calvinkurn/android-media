package com.tokopedia.checkout.view.feature.addressoptions

import com.tokopedia.checkout.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel
import rx.Subscriber
import javax.inject.Inject

const val EMPTY_STRING: String = ""

/**
 * Created by fajarnuha on 2019-05-24.
 */
class AddressListPresenter
@Inject constructor(val usecase: GetAddressCornerUseCase) : AddressListContract.Presenter {

    private var mView: ISearchAddressListView<List<RecipientAddressModel>>? = null
    private var mCurrentQuery: String = ""
    private var mCurrentPage: Int = 1
    private var mHasNext: Boolean = false

    override fun attachView(view: ISearchAddressListView<List<RecipientAddressModel>>) {
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
                    mView?.stopTrace() }
                .subscribe(getAddressHandler(EMPTY_STRING))
    }

    override fun searchAddress(query: String) {
        usecase.execute(query)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace() }
                .subscribe(getAddressHandler(query))
    }

    override fun loadMore() {
        if (!mHasNext) return
        usecase.loadMore(mCurrentQuery, mCurrentPage + 1)
                .doOnSubscribe { mView?.showLoading() }
                .doOnTerminate {
                    mView?.hideLoading()
                    mView?.stopTrace() }
                .subscribe(
                { result ->
                    mCurrentPage++
                    mView?.updateList(result.listAddress)
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
                            mView?.showList(it.listAddress)
                        } else mView?.showListEmpty()
                    }
                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    mView?.showError(e)
                }
            }

}