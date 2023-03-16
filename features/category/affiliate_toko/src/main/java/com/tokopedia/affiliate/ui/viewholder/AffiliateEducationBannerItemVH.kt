package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.interfaces.AffiliateEducationBannerClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationBannerUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.user.session.UserSession

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

    private val carousel = itemView.findViewById<CarouselUnify>(R.id.education_carousel)

    override fun bind(element: AffiliateEducationBannerUiModel?) {
        setData(element)
    }

    private fun setData(element: AffiliateEducationBannerUiModel?) {
        carousel.apply {
            stage.removeAllViews()
            element?.bannerList?.mapNotNull { it?.media?.mobile }?.let {
                addImages(ArrayList(it))
                bannerItemMargin = 0
                centerMode = false
            }
            onItemClick = {
                affiliateEducationBannerClickInterface?.onBannerClick(
                    element?.bannerList?.get(it)?.text?.primary?.redirectUrl.orEmpty()
                )
                sendEducationClickEvent(
                    it,
                    element?.bannerList?.get(it)?.title,
                    element?.bannerList?.get(it)?.bannerId.toString()
                )
            }
        }
    }

    private fun sendEducationClickEvent(position: Int?, creativeName: String?, bannerId: String?) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_MAIN_BANNER,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE,
            bannerId,
            position,
            bannerId,
            UserSession(itemView.context).userId,
            creativeName
        )
    }
}
