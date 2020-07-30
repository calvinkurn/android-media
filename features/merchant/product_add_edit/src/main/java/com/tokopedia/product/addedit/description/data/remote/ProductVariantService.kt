package com.tokopedia.product.addedit.description.data.remote

import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantResponse
import com.tokopedia.url.TokopediaUrl
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductVariantService {

    companion object {
        val BASE_URL = TokopediaUrl.getInstance().TOME
        const val GET_VARIANT_BY_CAT_PATH = "/v1/web-service/category/get_variant"
        const val CAT_ID = "cat_id"
        const val USE_DEFAULT = "use_default"
    }

    @GET(GET_VARIANT_BY_CAT_PATH)
    suspend fun getVariant(
            @Query(CAT_ID) categoryId: String,
            @Query(USE_DEFAULT) useDefault: Boolean
    ): ProductVariantResponse
}