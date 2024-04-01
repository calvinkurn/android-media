package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceShimmerModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.home.databinding.LayoutBalanceWidgetShimmerBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by frenzel
 */
class BalanceWidgetShimmerViewHolder (
    itemView: View,
    val listener: HomeCategoryListener?,
    private val homeThematicUtil: HomeThematicUtil,
) :
    AbstractViewHolder<BalanceShimmerModel>(itemView) {
    private val binding: LayoutBalanceWidgetShimmerBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.layout_balance_widget_shimmer
    }

    override fun bind(element: BalanceShimmerModel) {
        if ((itemView.context.isDarkMode() && homeThematicUtil.isDefault()) || homeThematicUtil.isDarkMode()) {
            binding?.dividerBalance?.setBackgroundColor(
                homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN100, itemView.context)
            )
        } else {
            binding?.dividerBalance?.setBackgroundColor(
                homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN50, itemView.context)
            )
        }
    }
}
