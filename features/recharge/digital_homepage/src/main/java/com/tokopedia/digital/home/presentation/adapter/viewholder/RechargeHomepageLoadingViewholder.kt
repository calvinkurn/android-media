package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R

/**
 * @author Kulomady on 1/25/17.
 */
class RechargeHomepageLoadingViewholder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {
    override fun bind(element: LoadingModel) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    companion object {
        @LayoutRes
        val LAYOUT: Int = R.layout.view_recharge_home_loading
    }
}