package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetSellerOnboarding
import com.tokopedia.tokomember_seller_dashboard.model.SellerHomeTextBenefitItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroVideoItem

object TokomemberIntroMapper {

    fun getTokomemberIntroData(membershipData: MembershipData): TokomemberIntroItem {
        val arrayListOfIntroData = arrayListOf<Visitable<*>>()
        val sellerOnboarding = membershipData.membershipGetSellerOnboarding
        val sellerBenefit = sellerOnboarding?.sellerHomeContent?.sellerHomeText?.sellerHomeTextBenefit
     //   arrayListOfIntroData.add(getIntroHeaderData(sellerOnboarding))
      //  arrayListOfIntroData.add(getIntroVideoData(sellerOnboarding))
        if (!sellerBenefit.isNullOrEmpty()) {
            for (item in sellerOnboarding.sellerHomeContent.sellerHomeText.sellerHomeTextBenefit) {
                arrayListOfIntroData.add(getIntroTextData(item))
            }
        }
        return TokomemberIntroItem(tokoVisitable = arrayListOfIntroData)

    }

    private fun getIntroHeaderData(sellerOnboarding: MembershipGetSellerOnboarding?): TokomemberIntroHeaderItem {
        return TokomemberIntroHeaderItem(
            title = sellerOnboarding?.sellerHomeContent?.sellerHomeText?.title?.getOrNull(0) ?: "",
            description = sellerOnboarding?.sellerHomeContent?.sellerHomeText?.title?.getOrNull(0)
                ?: ""
        )
    }

    private fun getIntroTextData(sellerHomeTextBenefit: SellerHomeTextBenefitItem?): TokomemberIntroTextItem {
        return TokomemberIntroTextItem(
            text = sellerHomeTextBenefit?.benefit,
            imgUrl = sellerHomeTextBenefit?.iconURL
        )
    }

    private fun getIntroVideoData(sellerOnboarding: MembershipGetSellerOnboarding?): TokomemberIntroVideoItem {
        return TokomemberIntroVideoItem(
            url = sellerOnboarding?.sellerHomeContent?.sellerHomeInfo?.infoURL,
            type = sellerOnboarding?.sellerHomeContent?.sellerHomeInfo?.type
        )
    }
}