package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.listener.SnapPositionChangeListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestBinding
import com.tokopedia.tokopedianow.common.util.SnapHelperUtil.attachSnapHelperWithListener
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.quest.adapter.HomeQuestCardAdapter
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestWidgetViewHolder(
    itemView: View
): AbstractViewHolder<HomeQuestWidgetUiModel>(itemView), SnapPositionChangeListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest
    }

    private val mAdapter: HomeQuestCardAdapter by lazy {
        HomeQuestCardAdapter()
    }

    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            itemView.context,
            RecyclerView.HORIZONTAL,
            false
        )
    }

    private val binding: ItemTokopedianowQuestBinding? by viewBinding()

    init {
        binding?.rvQuestCards?.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager

            attachSnapHelperWithListener(
                snapHelper = PagerSnapHelper(),
                listener = this@HomeQuestWidgetViewHolder
            )
        }
    }

    override fun bind(element: HomeQuestWidgetUiModel) {
        mAdapter.submitList(
            listOf(
                HomeQuestCardItemUiModel(
                    id = "1212",
                    title = "Belanja min. Rp80rb untuk dapat",
                    description = "Kupon Cashback 10rb",
                    isLockedShown = false
                ),
                HomeQuestCardItemUiModel(
                    id = "1333",
                    title = "Belanja min. Rp90rb untuk dapat",
                    description = "Kupon Cashback 20rb",
                    isLockedShown = true
                ),
                HomeQuestCardItemUiModel(
                    id = "1212",
                    title = "Belanja min. Rp70rb untuk dapat",
                    description = "Kupon Cashback 30rb",
                    isLockedShown = true
                )
            )
        )
        mLayoutManager.scrollToPosition(2)
    }

    override fun onSnapPositionChange(position: Int) {
        Log.d("POSITION CHANGED QUEST", position.toString())
    }
}
