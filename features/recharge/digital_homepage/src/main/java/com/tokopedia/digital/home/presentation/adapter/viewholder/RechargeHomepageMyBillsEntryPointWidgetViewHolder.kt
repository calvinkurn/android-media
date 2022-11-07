package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.RechargeHomepageMyBillsEntryPointWidget
import com.tokopedia.digital.home.R

/**
 * created by @bayazidnasir on 7/11/2022
 */

class RechargeHomepageMyBillsEntryPointWidgetViewHolder(
    itemView: View
): AbstractViewHolder<RechargeHomepageMyBillsEntryPointWidget>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_my_bills_entrypoint
    }

    override fun bind(element: RechargeHomepageMyBillsEntryPointWidget?) {

    }
}
