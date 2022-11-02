package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceShimmerModel
import com.tokopedia.home.databinding.LayoutBalanceWidgetShimmerBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceWidgetShimmerViewHolder (itemView: View, val listener: HomeCategoryListener?) :
    AbstractViewHolder<BalanceShimmerModel>(itemView) {
    private val binding: LayoutBalanceWidgetShimmerBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.layout_balance_widget_shimmer
    }

    override fun bind(element: BalanceShimmerModel) {
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