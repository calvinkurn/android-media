package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyCommissionViewModel

/**
 * @author by yoasfs on 2019-08-15
 */
class EmptyCommissionViewHolder (v: View) : AbstractViewHolder<EmptyCommissionViewModel>(v) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_commission_empty
    }
    override fun bind(element: EmptyCommissionViewModel) {

    }
}