package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetSellerOnboarding
import com.tokopedia.tokomember_seller_dashboard.model.SellerHomeTextBenefitItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.*

object TokomemberIntroMapper {

    fun getTokomemberIntroData(membershipData: MembershipData): TokomemberIntroItem {
        val arrayListOfIntroData = arrayListOf<Visitable<*>>()
        val sellerOnboarding = membershipData.membershipGetSellerOnboarding
        val sellerBenefit = sellerOnboarding?.sellerHomeContent?.sellerHomeText?.sellerHomeTextBenefit
        if (!sellerBenefit.isNullOrEmpty()) {
            for (item in sellerOnboarding.sellerHomeContent.sellerHomeText.sellerHomeTextBenefit) {
                arrayListOfIntroData.add(getIntroTextData(item))
                arrayListOfIntroData.add(getIntroImageData(item))
            }
            arrayListOfIntroData.add(getIntroButtonData())
        }
        return TokomemberIntroItem(tokoVisitable = arrayListOfIntroData)
    }

    private fun getIntroTextData(sellerHomeTextBenefit: SellerHomeTextBenefitItem?): TokomemberIntroTextItem {
        return TokomemberIntroTextItem(
            text = sellerHomeTextBenefit?.benefit,
            imgUrl = sellerHomeTextBenefit?.iconURL
        )
    }

    private fun getIntroImageData(sellerHomeTextBenefit: SellerHomeTextBenefitItem?): TokomemberIntroBenefitImageItem {
        return TokomemberIntroBenefitImageItem(
            imgUrl = sellerHomeTextBenefit?.imageURL
        )
    }

    private fun getIntroButtonData(): TokomemberIntroButtonItem {
        return TokomemberIntroButtonItem(
            text = "Pelajari Lebih Lanjut",
            url = ""
        )
    }
}