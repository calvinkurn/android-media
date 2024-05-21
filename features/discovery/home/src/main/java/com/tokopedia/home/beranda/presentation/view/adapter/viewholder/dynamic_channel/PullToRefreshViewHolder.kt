package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R as homeR
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PullToRefreshDataModel
import com.tokopedia.home.databinding.PullToRefreshViewBinding
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class PullToRefreshViewHolder(
    itemView: View,
    private val listener: HomeCategoryListener,
) : AbstractViewHolder<PullToRefreshDataModel>(itemView) {

    private var binding: PullToRefreshViewBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.pull_to_refresh_view
    }

    override fun bind(element: PullToRefreshDataModel) {
        renderEmptySpace()
        renderHeader()
    }

    private fun renderHeader() {
        binding?.viewPullRefresh?.let {
            it.setColorPullRefresh(getPullRefreshLoaderColor())
            listener.pullRefreshIconCaptured(it)
        }
    }

    private fun renderEmptySpace() {
        val layoutParams = binding?.viewEmpty?.layoutParams
        layoutParams?.height = NavToolbarExt.getFullToolbarHeight(itemView.context)
        binding?.viewEmpty?.layoutParams = layoutParams
        binding?.viewEmpty?.invalidate()
    }

    private fun getPullRefreshLoaderColor(): Int {
        return LayoutIconPullRefreshView.TYPE_GREEN
    }
}
