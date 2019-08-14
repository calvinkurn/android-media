package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CommissionDetailHeaderViewModel

/**
 * @author by milhamj on 2019-08-14.
 */
class CommissionDetailHeaderViewHolder(v: View) : AbstractViewHolder<CommissionDetailHeaderViewModel>(v){

    companion object {
        @LayoutRes
        const val LAYOUT = 1
    }

    override fun bind(element: CommissionDetailHeaderViewModel?) {

    }
}