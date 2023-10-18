package com.tokopedia.pdpsimulation.activateCheckout.helper

import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Content
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails


object DataMapper {

    fun mapToInstallationDetail(tenureDetail: TenureDetail): TenureSelectedModel? {

        val priceText = tenureDetail.monthly_installment.orEmpty()
        val tenure = tenureDetail.tenure.toString()
        val contentList: MutableList<Content> = ArrayList()
        tenureDetail.installmentDetails?.let {
            tenureDetail.installmentDetails.detailContent.map {
                Content(
                    it.title.orEmpty(),
                    it.value.orEmpty(),
                    it.type,
                    it.titleFormattedLight,
                    it.titleFormattedDark
                )
            }.toCollection(contentList)
            val installmentDetails =
                InstallmentDetails(tenureDetail.installmentDetails.header.orEmpty(), contentList)
            return TenureSelectedModel(priceText, tenure, installmentDetails)
        }
        return null
    }
}
