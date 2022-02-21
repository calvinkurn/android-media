package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeThreeIconsBinding
import com.tokopedia.digital.home.model.RechargeHomepageThreeIconsModel
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageThreeIconsItemAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener

class RechargeHomepageThreeIconsViewHolder(
    itemView: View,
    val listener: RechargeHomepageItemListener
): AbstractViewHolder<RechargeHomepageThreeIconsModel>(itemView) {
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_three_icons

        private const val SPAN_COUNT = 3
    }

    private val rvPool = RecyclerView.RecycledViewPool()

    override fun bind(element: RechargeHomepageThreeIconsModel) {
        listener.loadRechargeSectionData(element.visitableId())

        val bind = ViewRechargeHomeThreeIconsBinding.bind(itemView)
        val itemThreeIcons = element.section.items
        with(bind.rvThreeIcons){
            setRecycledViewPool(rvPool)
            adapter = RechargeHomepageThreeIconsItemAdapter(itemThreeIcons, listener)
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }
}