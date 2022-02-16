package com.tokopedia.pdpsimulation.activateCheckout.helper

import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Content
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails


object DataMapper {

    fun mapToInstallationDetail(tenureDetail: TenureDetail): TenureSelectedModel {

        val priceText = tenureDetail.monthly_installment
        val tenure = tenureDetail.tenure.toString()
        val contentList: MutableList<Content> = ArrayList()
        for (i in 0 until tenureDetail.installment_details.detailContent.size) {
            val content = Content(
                tenureDetail.installment_details.detailContent[i].title,
                tenureDetail.installment_details.detailContent[i].value,
                tenureDetail.installment_details.detailContent[i].type
            )
            contentList.add(content)
        }
        val installmentDetails =
            InstallmentDetails(tenureDetail.installment_details.header, contentList)
        return TenureSelectedModel(priceText, tenure, installmentDetails)
    }
}