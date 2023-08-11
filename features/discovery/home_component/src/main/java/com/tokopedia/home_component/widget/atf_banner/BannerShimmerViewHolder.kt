package com.tokopedia.home_component.widget.atf_banner

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by frenzel
 */
class BannerShimmerViewHolder(itemView: View) : AbstractViewHolder<BannerShimmerModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.layout_banner_revamp_channel_shimmer
    }

    override fun bind(element: BannerShimmerModel?) {

    }
}
