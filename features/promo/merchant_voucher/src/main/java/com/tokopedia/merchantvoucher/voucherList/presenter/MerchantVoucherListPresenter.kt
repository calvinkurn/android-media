package com.tokopedia.merchantvoucher.voucherList.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.UseMerchantVoucherUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherListPresenter @Inject
constructor(private val getShopInfoUseCase: GetShopInfoUseCase,
            private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
            private val useMerchantVoucherUseCase: UseMerchantVoucherUseCase,
            private val deleteShopInfoUseCase: DeleteShopInfoCacheUseCase,
            private val userSessionInterface: UserSessionInterface)
    : BaseDaggerPresenter<MerchantVoucherListView>(){

    var voucherCodeInProgress:String = ""

    fun isLogin() = (userSessionInterface.isLoggedIn)
    fun isMyShop(shopId: String) = (userSessionInterface.shopId == shopId)

    fun getShopInfo(shopId: String) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view?.onErrorGetShopInfo(e)
            }

            override fun onNext(shopInfo: ShopInfo) {
                view?.onSuccessGetShopInfo(shopInfo)
            }
        })
    }

    fun getVoucherList(shopId: String, numVoucher: Int = 0) {
        getMerchantVoucherListUseCase.execute(GetMerchantVoucherListUseCase.createRequestParams(shopId, numVoucher),
                object : Subscriber<ArrayList<MerchantVoucherModel>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onErrorGetMerchantVoucherList(e)
            }

            override fun onNext(merchantVoucherModelList: ArrayList<MerchantVoucherModel>) {
                val merchantViewModelList: ArrayList<MerchantVoucherViewModel> = ArrayList()
                for (merchantVoucherModel in merchantVoucherModelList) {
                    val viewModel = MerchantVoucherViewModel(merchantVoucherModel)
                    viewModel.enableButtonUse = true
                    merchantViewModelList.add(viewModel)
                }
                view?.onSuccessGetMerchantVoucherList(merchantViewModelList)
            }
        })
    }

    fun useMerchantVoucher(voucherCode: String, voucherId:Int) {
        if (voucherCodeInProgress.equals(voucherCode)) {
            return;
        }
        voucherCodeInProgress = voucherCode
        useMerchantVoucherUseCase.unsubscribe()
        useMerchantVoucherUseCase.execute(UseMerchantVoucherUseCase.createRequestParams(voucherCode, voucherId),
                object : Subscriber<UseMerchantVoucherQueryResult>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        voucherCodeInProgress = ""
                        view?.onErrorUseVoucher(e)
                    }

                    override fun onNext(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
                        // success should be true
                        voucherCodeInProgress = ""
                        view?.onSuccessUseVoucher(useMerchantVoucherQueryResult)
                    }
                })
    }

    fun clearCache() {
        deleteShopInfoUseCase.executeSync()
        getMerchantVoucherListUseCase.clearCache()
    }

    override fun detachView() {
        super.detachView()
        getShopInfoUseCase.unsubscribe()
        getMerchantVoucherListUseCase.unsubscribe()
        useMerchantVoucherUseCase.unsubscribe()
    }
}