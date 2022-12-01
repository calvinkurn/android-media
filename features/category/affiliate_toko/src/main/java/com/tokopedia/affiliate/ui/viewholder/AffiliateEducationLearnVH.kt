package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.interfaces.AffiliateEducationLearnClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationLearnUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.user.session.UserSession

class AffiliateEducationLearnVH(
    itemView: View,
    private val affiliateEducationLearnClickInterface: AffiliateEducationLearnClickInterface?
) : AbstractViewHolder<AffiliateEducationLearnUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_learn_item
    }

    private val bantuanContainer = itemView.findViewById<View>(R.id.bantuan_container)
    private val kamusContainer = itemView.findViewById<View>(R.id.kamus_container)

    override fun bind(element: AffiliateEducationLearnUiModel?) {
        bantuanContainer.setOnClickListener {
            affiliateEducationLearnClickInterface?.onBantuanClick()
            sendEducationClickEvent(AffiliateAnalytics.ActionKeys.CLICK_BANTUAN)
        }
        kamusContainer.setOnClickListener {
            affiliateEducationLearnClickInterface?.onKamusClick()
            sendEducationClickEvent(AffiliateAnalytics.ActionKeys.CLICK_KAMUS_AFFILIATE)
        }
    }

    private fun sendEducationClickEvent(actionKeys: String) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            actionKeys,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE,
            userId = UserSession(itemView.context).userId,
            eventLabel = ""
        )
    }
}
