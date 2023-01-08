package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.databinding.ItemBalanceWidgetDividerBinding
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceDividerViewHolder(v: View): RecyclerView.ViewHolder(v) {
    companion object {
        private const val FIRST_POSITION = 0
    }
    private val binding: ItemBalanceWidgetDividerBinding? by viewBinding()
    var coachMarkView: View? = null

    fun bind(position: Int) {
        coachMarkView = binding?.dummyContainerBalanceWidget
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
        if (position == FIRST_POSITION) {
            binding?.dividerBalance?.invisible()
        } else {
            binding?.dividerBalance?.show()
        }
    }
}