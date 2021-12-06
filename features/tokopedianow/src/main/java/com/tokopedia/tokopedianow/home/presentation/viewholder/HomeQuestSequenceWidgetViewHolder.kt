package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
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
    itemView: View,
    private val listener: HomeQuestSequenceWidgetListener? = null
): AbstractViewHolder<HomeQuestSequenceWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_sequence_widget
    }

    private var binding: ItemTokopedianowQuestSequenceWidgetBinding? by viewBinding()
    private var stubBinding: PartialTokopedianowViewStubDcTitleBinding? by viewBinding()

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(homeQuestSequenceWidgetListener = listener),
            differ = HomeListDiffer(),
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
        setTitle(element.title)
        setSeeAll(element.seeAll, element.appLink)
        when(element.state) {
            HomeLayoutItemState.LOADED -> questLoaded(element)
            else -> questNotLoaded()
        }
    }

    private fun questLoaded(element: HomeQuestSequenceWidgetUiModel) {
        addWidgets(element.questList)
    }

    private fun questNotLoaded() {
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
        stubBinding?.channelTitle?.apply {
            if (title.isNotBlank()) {
                show()
                text = title
            } else {
                hide()
            }
        }
    }

    private fun setSeeAll(seeAll: String, appLink: String) {
        binding?.tvSeeAll?.apply {
            if (seeAll.isNotBlank() && appLink.isNotBlank()) {
                show()
                text = seeAll
                setOnClickListener {
                    RouteManager.route(itemView.context, appLink)
                }
            } else {
                visibility = View.INVISIBLE
            }
        }
    }

    interface HomeQuestSequenceWidgetListener {
        fun onClickRefreshQuestWidget()
    }
}