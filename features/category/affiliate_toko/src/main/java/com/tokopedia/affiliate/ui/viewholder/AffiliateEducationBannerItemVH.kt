package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateEducationBannerClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationBannerUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.carousel.CarouselUnify

class AffiliateEducationBannerItemVH(
    itemView: View,
    private val affiliateEducationBannerClickInterface: AffiliateEducationBannerClickInterface?
) :
    AbstractViewHolder<AffiliateEducationBannerUiModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_banner_view_holder
    }

    override fun bind(element: AffiliateEducationBannerUiModel?) {
        setData(element)
    }

    private fun setData(element: AffiliateEducationBannerUiModel?) {
        itemView.findViewById<CarouselUnify>(R.id.education_carousel).apply {
            element?.bannerList?.mapNotNull { it?.media?.mobile }?.let {
                addImages(ArrayList(it))
                bannerItemMargin = 0
                centerMode = false
            }
            onItemClick = {
                affiliateEducationBannerClickInterface?.onBannerClick(
                    element?.bannerList?.get(it)?.text?.primary?.redirectUrl.orEmpty()
                )
            }
        }
    }
}
