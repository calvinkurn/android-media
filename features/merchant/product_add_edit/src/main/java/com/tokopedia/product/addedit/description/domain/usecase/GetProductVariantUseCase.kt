package com.tokopedia.product.addedit.description.domain.usecase

import com.tokopedia.product.addedit.description.data.GetProductVariantRepository
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductVariantUseCase @Inject constructor(
        private val productVariantRepo: GetProductVariantRepository
) : UseCase<List<ProductVariantByCatModel>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<ProductVariantByCatModel> {
        val categoryId = params.getString(PARAM_INPUT, "")
        return productVariantRepo.getVariant(categoryId)
    }

    companion object {
        const val PARAM_INPUT = "cat_id"

        @JvmStatic
        fun createRequestParams(param: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_INPUT, param)
            return requestParams
        }
    }
}