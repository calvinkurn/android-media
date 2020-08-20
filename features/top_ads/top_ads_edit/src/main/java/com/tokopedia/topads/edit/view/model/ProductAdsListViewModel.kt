package com.tokopedia.topads.edit.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.edit.usecase.GetEtalaseUseCase
import com.tokopedia.topads.edit.usecase.GetProductUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductAdsListViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val getProductUseCase: GetProductUseCase,
        private val getEtalaseUseCase: GetEtalaseUseCase) : BaseViewModel(dispatcher) {
    private var totalCount = 0

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit)) {
        getEtalaseUseCase.setParams()
        getEtalaseUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.shopShowcasesByShopID.result)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, onSuccess: ((List<ResponseProductList.Result.TopadsGetListProduct.Data>) -> Unit),
                    onEmpty: (() -> Unit), onError: ((Throwable) -> Unit)) {
        getProductUseCase.setParams(keyword, etalaseId, sortBy, isPromoted, rows, start)
        getProductUseCase.executeQuerySafeMode(
                {
                    if (it.topadsGetListProduct.data.isEmpty()) {
                        onEmpty()
                    } else {
                        onSuccess(it.topadsGetListProduct.data)
                        if (etalaseId.isEmpty()) {
                            totalCount = it.topadsGetListProduct.data.size
                        }
                    }
                },
                {
                    onError(it)
                })
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