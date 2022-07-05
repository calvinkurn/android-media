package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.databinding.ItemBalanceWidgetDividerBinding
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceDividerViewHolder(v: View): RecyclerView.ViewHolder(v) {
    companion object {
        const val FIRST_POSITION = 0
    }
    private val binding: ItemBalanceWidgetDividerBinding? by viewBinding()

    fun bind(position: Int) {
        if (position == FIRST_POSITION) {
            binding?.dividerBalance?.invisible()
        } else {
            binding?.dividerBalance?.show()
        }
    }
}