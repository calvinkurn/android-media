package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,November,2019
 */
class ProductAdsListViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val getEtalaseListUseCase: GetEtalaseListUseCase,
        private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : BaseViewModel(dispatcher.main) {
    private var totalCount = 0

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    getEtalaseListUseCase.setParams(userSession.shopId ?: "0")
                    getEtalaseListUseCase.execute(
                            {
                                onSuccess(it.shopShowcasesByShopID.result)
                            },
                            {
                                onError(it)
                            })
                },
                onError = {
                    onError(it)
                })
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, onSuccess: ((List<TopAdsProductModel>, eof: Boolean) -> Unit),
                    onEmpty: (() -> Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    topAdsGetListProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, userSession.shopId.toIntOrZero())
                    topAdsGetListProductUseCase.execute(
                            {
                                if (it.topadsGetListProduct.data.isEmpty()) {
                                    onEmpty()
                                } else {
                                    if (etalaseId.isEmpty()) {
                                        totalCount = it.topadsGetListProduct.data.size
                                    }
                                    onSuccess(it.topadsGetListProduct.data, it.topadsGetListProduct.eof)
                                }
                            },
                            {
                                onError(it)
                            })
                },
                onError = {
                    onError(it)
                }
        )
    }

    fun addSemuaProduk(): ResponseEtalase.Data.ShopShowcasesByShopID.Result {
        return ResponseEtalase.Data.ShopShowcasesByShopID.Result(totalCount, "", "Semua Etalase", 0)
    }
}