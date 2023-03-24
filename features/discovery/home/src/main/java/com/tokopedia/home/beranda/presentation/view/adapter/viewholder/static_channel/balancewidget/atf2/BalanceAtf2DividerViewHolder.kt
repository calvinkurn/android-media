package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceAtf2DividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BaseBalanceViewHolder
import com.tokopedia.home.databinding.ItemBalanceWidgetAtf2DividerBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceAtf2DividerViewHolder(v: View): BaseBalanceViewHolder<BalanceAtf2DividerModel>(v) {
    companion object {
        val LAYOUT = com.tokopedia.home.R.layout.item_balance_widget_atf2_divider
    }
    private val binding: ItemBalanceWidgetAtf2DividerBinding? by viewBinding()

    override fun bind(model: BalanceAtf2DividerModel?, listener: HomeCategoryListener?) {
        if (itemView.context.isDarkMode()) {
            binding?.dividerBalance?.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN100
                )
            )
        } else {
            binding?.dividerBalance?.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN50
                )
            )
        }
    }
}
