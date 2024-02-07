package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.View
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.home.databinding.ItemBalanceWidgetDividerBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class BalanceDividerViewHolder(
    v: View,
    private val homeThematicUtil: HomeThematicUtil,
): BaseBalanceViewHolder<BalanceDividerModel>(v) {
    companion object {
        val LAYOUT = homeR.layout.item_balance_widget_divider
    }
    private val binding: ItemBalanceWidgetDividerBinding? by viewBinding()

    override fun bind(model: BalanceDividerModel?, listener: HomeCategoryListener?) {
        if ((itemView.context.isDarkMode() && homeThematicUtil.isDefault()) || homeThematicUtil.isDarkMode()) {
            binding?.dividerBalance?.setBackgroundColor(homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN100, itemView.context))
        } else {
            binding?.dividerBalance?.setBackgroundColor(homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN50, itemView.context))
        }
    }

    override fun bind(
        model: BalanceDividerModel?,
        listener: HomeCategoryListener?,
        payloads: MutableList<Any>
    ) {
        bind(model, listener)
    }
}
