package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.PAGE_TYPE_CAMPAIGN
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.ImageUnify

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
            promotionClickInterface?.onPromotionClick(
                itemID = element?.article?.pageId.toString(),
                itemName = element?.article?.title.orEmpty(),
                itemImage = element?.article?.imageBanner.orEmpty(),
                itemURL = element?.article?.url.orEmpty(),
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
}
