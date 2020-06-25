package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageBannerEmptyModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import kotlinx.android.synthetic.main.view_recharge_home_banner_empty.view.*

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageBannerEmptyViewHolder(itemView: View)
    : AbstractViewHolder<RechargeHomepageBannerEmptyModel>(itemView) {

    override fun bind(element: RechargeHomepageBannerEmptyModel) {
        val section = element.section
        with (itemView) {
            if (section.title.isNotEmpty()) tv_recharge_home_banner_empty_title.text = section.title
            if (section.subtitle.isNotEmpty()) tv_recharge_home_banner_empty_desc.text = section.subtitle
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_banner_empty
    }
}
