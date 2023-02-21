package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.model.BlacklistKeyword
import com.tokopedia.product.addedit.detail.domain.model.GetProductTitleValidationResponse
import com.tokopedia.product.addedit.detail.domain.model.NegativeKeyword
import com.tokopedia.product.addedit.detail.domain.model.TypoDetection
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductResponse
import com.tokopedia.product.addedit.detail.presentation.model.TitleValidationModel

object ProductTitleValidationMapper {

    fun mapToUiModel(productNameInput: String, response: ValidateProductResponse): TitleValidationModel {
        response.productValidateV3.data.apply {
            return TitleValidationModel(
                title = productNameInput,
                errorKeywords = productName,
                isBlacklistKeyword = productName.isNotEmpty(),
                typoCorrections = emptyList()
            )
        }
    }

    fun mapToUiModel(productName: String, response: GetProductTitleValidationResponse): TitleValidationModel {
        response.getProductTitleValidation.apply {
            return TitleValidationModel(
                productName,
                mapErrorKeywords(blacklistKeyword),
                mapWarningKeywords(negativeKeyword, typoDetection),
                false, // disable blacklist checking
                negativeKeyword.isNotEmpty(),
                false, // disable typo checking due to BE issue
                emptyList() // disable typo checking due to BE issue
            )
        }
    }

    private fun mapWarningKeywords(
        negativeKeyword: List<NegativeKeyword>,
        typoDetection: List<TypoDetection>
    ): List<String> {
        val result = mutableListOf<String>()

        negativeKeyword.forEach {
            result.add(it.keyword)
        }

        typoDetection.forEach {
            result.add(it.incorrect)
        }

        return result
    }

    private fun mapErrorKeywords(blacklistKeyword: List<BlacklistKeyword>) = blacklistKeyword.map {
        it.keyword
    }
}
