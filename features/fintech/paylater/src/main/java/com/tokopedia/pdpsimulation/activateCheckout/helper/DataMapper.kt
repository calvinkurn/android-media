package com.tokopedia.pdpsimulation.activateCheckout.helper

import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.paylater.domain.model.Content
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails


object DataMapper {

    fun mapToInstallationDetail(tenureDetail: TenureDetail): TenureSelectedModel {
        lateinit var tenureSelectModel: TenureSelectedModel
        tenureSelectModel.priceText = "${tenureDetail.monthly_installment}X${tenureDetail.tenure}"
        val contentList: MutableList<Content> = ArrayList()
        for(i in 0 until tenureDetail.installment_details.detailContent.size)
        {
            val content = Content(tenureDetail.installment_details.detailContent[i].title,
                tenureDetail.installment_details.detailContent[i].value,
                tenureDetail.installment_details.detailContent[i].type)
            contentList.add(content)
        }
         tenureSelectModel.installmentDetails = InstallmentDetails(tenureDetail.installment_details.header,contentList);
        return tenureSelectModel
    }
}