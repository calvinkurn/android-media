package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowQuestUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestSequenceWidgetBinding
import com.tokopedia.tokopedianow.databinding.PartialTokopedianowViewStubDcTitleBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestSequenceWidgetViewHolder(
    itemView: View
): AbstractViewHolder<HomeQuestSequenceWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_sequence_widget
    }

    private var binding: ItemTokopedianowQuestSequenceWidgetBinding? by viewBinding()
    private var stubBinding: PartialTokopedianowViewStubDcTitleBinding? by viewBinding()

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(),
            differ = HomeListDiffer()
        )
    }

    override fun bind(element: HomeQuestSequenceWidgetUiModel) {
        setTitle(element.title)
        setSeeAll(element.seeAll)
        setQuestWidget()
    }

    private fun setQuestWidget() {
        binding?.rvQuest?.apply {
            adapter = this@HomeQuestSequenceWidgetViewHolder.adapter
            layoutManager = LinearLayoutManager(itemView.context)
        }
        adapter.submitList(listOf(HomeQuestWidgetUiModel(id = "da", 1, 3, "daf", "ad")))
    }

    private fun setTitle(title: String) {
        binding?.vsTitle?.setOnInflateListener { _, inflated ->
            stubBinding = PartialTokopedianowViewStubDcTitleBinding.bind(inflated)
        }
        binding?.vsTitle?.inflate()
        stubBinding?.channelTitle?.text = title
    }

    private fun setSeeAll(seeAll: String) {
        binding?.tvSeeAll?.text = seeAll
    }
}