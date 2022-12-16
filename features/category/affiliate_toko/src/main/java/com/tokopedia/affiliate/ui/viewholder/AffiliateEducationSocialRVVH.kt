package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliateEducationSocialCTAClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialRVUiModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialUiModel
import com.tokopedia.affiliate_toko.R

class AffiliateEducationSocialRVVH(
    itemView: View,
    socialCTAClickInterface: AffiliateEducationSocialCTAClickInterface?
) : AbstractViewHolder<AffiliateEducationSocialRVUiModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_social_list
    }

    private var socialAdapter: AffiliateAdapter =
        AffiliateAdapter(AffiliateAdapterFactory(educationSocialCTAClickInterface = socialCTAClickInterface))
    private val rvSocial = itemView.findViewById<RecyclerView>(R.id.rv_education_social)
    private val rvLayoutManager =
        LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)

    override fun bind(element: AffiliateEducationSocialRVUiModel?) {
        rvSocial?.apply {
            layoutManager = rvLayoutManager
            adapter = socialAdapter
        }
        socialAdapter.setVisitables(
            element?.socialList?.map { AffiliateEducationSocialUiModel(it) }
        )
    }
}
