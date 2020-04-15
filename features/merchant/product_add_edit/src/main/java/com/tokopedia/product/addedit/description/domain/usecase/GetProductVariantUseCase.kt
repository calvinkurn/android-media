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
        val categoryId = params.getString(PARAM_CATEGORY_ID, "")
        val useDefault = params.getBoolean(PARAM_USE_DEFAULT, true)
        val variantData = productVariantRepo.getVariant(categoryId, useDefault)
        return sortByStatus(variantData)
    }

    private fun sortByStatus(variantData: List<ProductVariantByCatModel>)
            : List<ProductVariantByCatModel> = variantData.sortedByDescending { it.status }

    companion object {
        const val PARAM_CATEGORY_ID = "cat_id"
        const val PARAM_USE_DEFAULT = "use_default"

        @JvmStatic
        fun createRequestParams(categoryId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_CATEGORY_ID, categoryId)
            requestParams.putBoolean(PARAM_USE_DEFAULT, true)
            return requestParams
        }
    }
}