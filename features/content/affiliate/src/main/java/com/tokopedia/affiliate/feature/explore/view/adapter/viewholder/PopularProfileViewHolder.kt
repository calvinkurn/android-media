package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.PopularProfileViewModel

/**
 * @author by milhamj on 12/03/19.
 */
class PopularProfileViewHolder(v: View) : AbstractViewHolder<PopularProfileViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_af_popular_profile
    }

    override fun bind(element: PopularProfileViewModel?) {

    }


}