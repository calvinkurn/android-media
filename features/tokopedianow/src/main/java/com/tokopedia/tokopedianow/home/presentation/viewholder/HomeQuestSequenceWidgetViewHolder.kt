package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestSequenceWidgetBinding
import com.tokopedia.tokopedianow.databinding.PartialTokopedianowViewStubDcTitleBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestTitleUiModel
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

    init {
        binding?.vsTitle?.apply {
            setOnInflateListener { _, inflated ->
                stubBinding = PartialTokopedianowViewStubDcTitleBinding.bind(inflated)
            }
            inflate()
        }
    }

    override fun bind(element: HomeQuestSequenceWidgetUiModel) {
        setAdapter()
        when(element.state) {
            HomeLayoutItemState.LOADING -> {}
            HomeLayoutItemState.LOADED -> questLoaded(element)
            HomeLayoutItemState.NOT_LOADED -> questNotLoaded()
        }
    }

    private fun questLoaded(element: HomeQuestSequenceWidgetUiModel) {
        setTitle(element.title)
        setSeeAll(element.seeAll, element.appLink)
        addWidgets(element.questList)
    }

    private fun questNotLoaded() {
        setTitle(getString(R.string.tokopedianow_quest_widget_error_title))
        addErrorWidgets()
    }

    private fun setAdapter() {
        binding?.rvQuest?.apply {
            adapter = this@HomeQuestSequenceWidgetViewHolder.adapter
            layoutManager = LinearLayoutManager(itemView.context,  LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun addWidgets(questList: List<HomeQuestWidgetUiModel>) {
        val widgets = mutableListOf<Visitable<*>>()
        widgets.add(
            HomeQuestTitleUiModel(
                currentQuestFinished = questList.filter { it.currentProgress == it.totalProgress }.size,
                totalQuestTarget = questList.size
            )
        )
        widgets.addAll(questList)
        adapter.submitList(widgets)
    }

    private fun addErrorWidgets() {
        val widgets = mutableListOf(
            HomeQuestTitleUiModel(isErrorState = true),
            HomeQuestWidgetUiModel(isErrorState = true)
        )
        adapter.submitList(widgets)
    }

    private fun setTitle(title: String) {
        stubBinding?.channelTitle?.text = title
    }

    private fun setSeeAll(seeAll: String, appLink: String) {
        if (seeAll.isNotBlank()) {
            binding?.tvSeeAll?.apply {
                show()
                text = seeAll
                setOnClickListener {
                    RouteManager.route(itemView.context, appLink)
                }
            }
        }
    }
}