package com.tokopedia.product_ar.util

import com.google.gson.Gson
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ProductArResponse
import com.tokopedia.product_ar.model.ProductArUiModel

object ProductArUseCaseMapperTest {

    fun mapIntoUiModel(response: ProductArResponse, gson: Gson): ProductArUiModel {
        return ProductArUiModel(
                optionBgImage = response.data.optionBgImage,
                provider = response.data.provider,
                options = response.data.productArs.associateBy(
                        {
                            it.productID
                        }, {
                    it.copy(providerDataCompiled = convertToObject(gson, it.providerData))
                })
        )
    }

    private fun convertToObject(gson: Gson, modifaceRawJson: String): ModifaceProvider {
        return gson.fromJson(modifaceRawJson, Array<ModifaceProvider>::class.java).firstOrNull()
                ?: ModifaceProvider()
    }
}