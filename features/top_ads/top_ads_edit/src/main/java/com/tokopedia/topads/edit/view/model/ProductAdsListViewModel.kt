package com.tokopedia.topads.edit.view.model

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

class ProductAdsListViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val getProductUseCase: TopAdsGetListProductUseCase,
        private val getEtalaseUseCase: GetEtalaseListUseCase) : BaseViewModel(dispatcher) {
    private var totalCount = 0

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit)) {
        launch {
            getEtalaseUseCase.setParams(userSession.shopId)
            getEtalaseUseCase.execute(
                    {
                        onSuccess(it.shopShowcasesByShopID.result)
                    },
                    {
                        it.printStackTrace()
                    }
            )
        }
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int,
                    start: Int, onSuccess: ((List<TopAdsProductModel>, eof: Boolean) -> Unit),
                    onEmpty: (() -> Unit), onError: ((Throwable) -> Unit)) {
        launch {
            getProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start, userSession.shopId.toIntOrZero())
            getProductUseCase.execute(
                    {
                        if (it.topadsGetListProduct.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topadsGetListProduct.data, it.topadsGetListProduct.eof)
                            if (etalaseId.isEmpty()) {
                                totalCount = it.topadsGetListProduct.data.size
                            }
                        }
                    },
                    {
                        onError(it)
                    }
            )
        }
    }

    fun addSemuaProduk(): ResponseEtalase.Data.ShopShowcasesByShopID.Result {
        return ResponseEtalase.Data.ShopShowcasesByShopID.Result(totalCount, "", "Semua Etalase", 0)
    }

    public override fun onCleared() {
        super.onCleared()
        getEtalaseUseCase.cancelJobs()
        getProductUseCase.cancelJobs()
    }

}