package com.tokopedia.tkpd.flashsale.presentation.presentation.list

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.Result
import com.tokopedia.tkpd.flashsale.presentation.data.request.DoFlashSaleProductSubmissionRequest
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.ProductDeleteResult
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.ProductSubmissionResult
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.presentation.domain.usecase.DoFlashSaleProductDeleteUseCase
import com.tokopedia.tkpd.flashsale.presentation.domain.usecase.DoFlashSaleProductSubmissionUseCase
import com.tokopedia.tkpd.flashsale.presentation.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LandingContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase,
    private val doFlashSaleProductSubmissionUseCase: DoFlashSaleProductSubmissionUseCase,
    private val doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase
) : BaseViewModel(dispatchers.main){

    private val _tabsMetadata = MutableStateFlow<Result<List<TabMetadata>>>(Result.Loading)
    val tabsMetadata = _tabsMetadata.asStateFlow()

    private val _submitProductResult = MutableStateFlow<Result<ProductSubmissionResult>>(Result.Loading)
    val submitProductResult = _submitProductResult.asStateFlow()

    private val _deleteProductResult = MutableStateFlow<Result<ProductDeleteResult>>(Result.Loading)
    val deleteProductResult = _deleteProductResult.asStateFlow()

    fun getTabsMetaData() {
        _tabsMetadata.value = Result.Loading

        launchCatchError(
            dispatchers.io,
            block = {
               val response = getFlashSaleListForSellerMetaUseCase.execute()

                _tabsMetadata.value = Result.Success(response)
            },
            onError = { error ->
                _tabsMetadata.value = Result.Failure(error)
            }
        )

    }

    fun submitProduct() {
        _tabsMetadata.value = Result.Loading

        launchCatchError(
            dispatchers.io,
            block = {
                val warehouses = listOf(
                    DoFlashSaleProductSubmissionRequest.ProductData.Warehouse(12181276, 4000, 1)
                )
                val productData = listOf(
                    DoFlashSaleProductSubmissionRequest.ProductData(10393, 5338343889, warehouses)
                )
                val params = DoFlashSaleProductSubmissionUseCase.Param(938228, productData, "122402171660132340376")
                val response = doFlashSaleProductSubmissionUseCase.execute(params)

                _submitProductResult.value = Result.Success(response)
            },
            onError = { error ->
                _submitProductResult.value = Result.Failure(error)
            }
        )

    }

    fun deleteProduct() {
        _tabsMetadata.value = Result.Loading

        launchCatchError(
            dispatchers.io,
            block = {
                val params = DoFlashSaleProductDeleteUseCase.Param(938228, listOf(1,2,3,4), "122402171660132340376")
                val response = doFlashSaleProductDeleteUseCase.execute(params)

                _deleteProductResult.value = Result.Success(response)
            },
            onError = { error ->
                _deleteProductResult.value = Result.Failure(error)
            }
        )

    }
}