package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.model.BlacklistKeyword
import com.tokopedia.product.addedit.detail.domain.model.GetProductTitleValidationResponse
import com.tokopedia.product.addedit.detail.domain.model.NegativeKeyword
import com.tokopedia.product.addedit.detail.domain.model.TypoDetection
import com.tokopedia.product.addedit.detail.presentation.model.TitleValidationModel

object ProductTitleValidationMapper {

    fun mapToUiModel(productName: String, response: GetProductTitleValidationResponse): TitleValidationModel {
        response.getProductTitleValidation.apply {
            return TitleValidationModel(
                    productName,
                    mapErrorKeywords(blacklistKeyword),
                    mapWarningKeywords(negativeKeyword, typoDetection),
                    blacklistKeyword.isNotEmpty(),
                    negativeKeyword.isNotEmpty(),
                    typoDetection.isNotEmpty()
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