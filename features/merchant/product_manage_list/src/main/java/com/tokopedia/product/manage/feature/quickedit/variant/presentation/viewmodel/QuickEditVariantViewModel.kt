package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.mapToProductVariants
import com.tokopedia.product.manage.feature.quickedit.variant.domain.GetProductVariantUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditVariantViewModel @Inject constructor(
    private val getProductVariantUseCase: GetProductVariantUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val getProductVariantsResult: LiveData<Result<List<ProductVariant>>>
        get() = _getProductVariantsResult

    private val _getProductVariantsResult = MutableLiveData<Result<List<ProductVariant>>>()

    fun getProductVariants(productId: String) {
        launchCatchError(block = {
            val productVariants = withContext(dispatchers.io) {
                val requestParams = GetProductVariantUseCase.createRequestParams(productId)
                val response = getProductVariantUseCase.execute(requestParams)

                val variant = response.getProductV3.variant
                mapToProductVariants(variant)
            }

            _getProductVariantsResult.value = Success(productVariants)
        }) {
            _getProductVariantsResult.value = Fail(it)
        }
    }
}