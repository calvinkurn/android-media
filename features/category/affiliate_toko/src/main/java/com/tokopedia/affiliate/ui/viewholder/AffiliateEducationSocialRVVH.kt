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
    private val socialCTAClickInterface: AffiliateEducationSocialCTAClickInterface?
) : AbstractViewHolder<AffiliateEducationSocialRVUiModel>(itemView) {

    private var socialAdapter: AffiliateAdapter? = null

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_social_list
    }

    override fun bind(element: AffiliateEducationSocialRVUiModel?) {
        socialAdapter =
            AffiliateAdapter(AffiliateAdapterFactory(educationSocialCTAClickInterface = socialCTAClickInterface))
        val rvSocial = itemView.findViewById<RecyclerView>(R.id.rv_education_social)
        val rvLayoutManager =
            LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        rvSocial?.apply {
            layoutManager = rvLayoutManager
            adapter = socialAdapter
        }
        socialAdapter?.addMoreData(
            element?.socialList?.map { AffiliateEducationSocialUiModel(it) }
        )
    }
}
