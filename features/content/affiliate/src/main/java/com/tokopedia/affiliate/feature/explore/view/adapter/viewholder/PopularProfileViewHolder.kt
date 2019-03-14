package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.adapter.PopularProfileAdapter
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel
import kotlinx.android.synthetic.main.item_af_popular_profile.view.*

/**
 * @author by milhamj on 12/03/19.
 */
class PopularProfileViewHolder(v: View) : AbstractViewHolder<PopularProfileViewModel>(v) {

    val adapter: PopularProfileAdapter by lazy {
        PopularProfileAdapter()
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_popular_profile
    }

    override fun bind(element: PopularProfileViewModel?) {
        if (element == null) {
            return
        }

        adapter.list.clear()
        adapter.list.addAll(element.popularProfiles)
        adapter.notifyDataSetChanged()
        itemView.profileRv.adapter = adapter

        itemView.titleView.bind(element.titleViewModel)
    }
}