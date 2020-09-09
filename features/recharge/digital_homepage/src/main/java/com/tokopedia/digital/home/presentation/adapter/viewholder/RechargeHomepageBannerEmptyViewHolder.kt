package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageBannerEmptyModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_banner_empty.view.*

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageBannerEmptyViewHolder(itemView: View, val listener: RechargeHomepageItemListener)
    : AbstractViewHolder<RechargeHomepageBannerEmptyModel>(itemView) {

    override fun bind(element: RechargeHomepageBannerEmptyModel) {
        val section = element.section
        if (section.title.isEmpty() && section.subtitle.isEmpty()) {
            listener.loadRechargeSectionData(section.id)
        } else {
            with(itemView) {
                if (section.title.isNotEmpty()) {
                    tv_recharge_home_banner_empty_title.show()
                    tv_recharge_home_banner_empty_title.text = MethodChecker.fromHtml(section.title)
                } else tv_recharge_home_banner_empty_title.hide()
                if (section.subtitle.isNotEmpty()) {
                    tv_recharge_home_banner_empty_desc.show()
                    tv_recharge_home_banner_empty_desc.text = MethodChecker.fromHtml(section.subtitle)
                } else tv_recharge_home_banner_empty_desc.hide()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_banner_empty
    }
}
