package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class ProductAdsListViewModel @Inject constructor(
        private val context: Context,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val getEtalaseListUseCase: GetEtalaseListUseCase,
        private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase) : BaseViewModel(dispatcher) {
    private var totalCount = 0

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit), onError: ((Throwable) -> Unit)) {
        launch {
            getEtalaseListUseCase.setParams(userSession.shopId ?: "0")
            getEtalaseListUseCase.execute(
                    {
                        onSuccess(it.shopShowcasesByShopID.result)
                    },
                    {
                        onError(it)
                    })
        }
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, onSuccess: ((List<TopAdsProductModel>, eof: Boolean) -> Unit),
                    onEmpty: (() -> Unit), onError: ((Throwable) -> Unit)) {
        launch {
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
        }
    }

    fun addSemuaProduk(): ResponseEtalase.Data.ShopShowcasesByShopID.Result {
        return ResponseEtalase.Data.ShopShowcasesByShopID.Result(totalCount, "", "Semua Etalase", 0)
    }
}