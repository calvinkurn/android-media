package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_TYPE_CAMPAIGN
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.user.session.UserSession

class AffiliateDiscoBannerVH(
    itemView: View,
    private val promotionClickInterface: PromotionClickInterface?
) :
    AbstractViewHolder<AffiliateDiscoBannerUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_disco_banner_list_item
    }

    private val discoBannerImage =
        itemView.findViewById<ImageUnify>(R.id.affiliate_disco_banner_image)

    override fun bind(element: AffiliateDiscoBannerUiModel?) {
        discoBannerImage.setImageUrl(element?.article?.imageBanner.toString())
        discoBannerImage.setOnClickListener {
            sendBannerClickEvent(element?.article)
            promotionClickInterface?.onPromotionClick(
                itemID = element?.article?.pageId.toString(),
                itemName = element?.article?.title.orEmpty(),
                itemImage = element?.article?.imageBanner.orEmpty(),
                itemURL = element?.article?.appUrl.orEmpty(),
                commison = element?.article?.commission?.percentage.toString(),
                type = PAGE_TYPE_CAMPAIGN,
                position = bindingAdapterPosition,
                ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                    true,
                    "",
                    element?.article?.additionalInformation?.getOrNull(0)?.htmlText.toString(),
                    label = AffiliatePromotionBottomSheetParams.SSAInfo.Label("", "")
                )
            )
        }
    }

    private fun sendBannerClickEvent(
        item: AffiliateDiscoveryCampaignResponse.RecommendedAffiliateDiscoveryCampaign.Data.Campaign?
    ) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_EVENT_DISCO_BANNER,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            item?.pageId.toString(),
            UserSession(itemView.context).userId
        )
    }
}
