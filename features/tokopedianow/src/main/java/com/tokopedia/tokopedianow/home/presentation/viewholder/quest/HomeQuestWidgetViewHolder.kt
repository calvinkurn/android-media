package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_CHANNEL_PRODUCTION_APPLINK
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_CHANNEL_STAGING_APPLINK
import com.tokopedia.tokopedianow.common.listener.SnapPositionChangeListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestBinding
import com.tokopedia.tokopedianow.common.util.SnapHelperUtil.attachSnapHelperWithListener
import com.tokopedia.tokopedianow.home.presentation.decoration.QuestCardItemDecoration
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.quest.adapter.HomeQuestCardAdapter
import com.tokopedia.tokopedianow.home.presentation.viewholder.quest.HomeQuestCardItemViewHolder.HomeQuestCardItemListener
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestWidgetViewHolder(
    itemView: View,
    private val listener: HomeQuestWidgetListener? = null
): AbstractViewHolder<HomeQuestWidgetUiModel>(itemView),
    SnapPositionChangeListener,
    HomeQuestCardItemListener
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest
    }

    private val mAdapter: HomeQuestCardAdapter by lazy {
        HomeQuestCardAdapter(
            questCardItemListener = this
        )
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
            addItemDecoration(QuestCardItemDecoration())

            attachSnapHelperWithListener(
                snapHelper = PagerSnapHelper(),
                listener = this@HomeQuestWidgetViewHolder
            )
        }
    }

    override fun bind(element: HomeQuestWidgetUiModel) {
        mAdapter.submitList(element.questList)
        binding?.apply {
            tpTitle.text = element.title
            sivCircleSeeAll.setOnClickListener {
                listener?.onClickSeeDetailsQuestWidget()
                openQuestChannelPage()
            }
            root.addOnImpressionListener(element) {
                listener?.onImpressQuestWidget()
            }
            questProgressBar.bind(element)
        }
    }

    private fun openQuestChannelPage() {
        val appLink = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) QUEST_CHANNEL_STAGING_APPLINK else QUEST_CHANNEL_PRODUCTION_APPLINK
        RouteManager.route(itemView.context, appLink)
    }

    override fun onSnapPositionChange(position: Int) {
        listener?.onImpressQuestCardSwiped()
    }

    override fun onClickQuestCard() {
        listener?.onClickQuestCard()
    }

    interface HomeQuestWidgetListener {
        fun onImpressQuestWidget()
        fun onClickSeeDetailsQuestWidget()
        fun onClickQuestCard()
        fun onClickProgressiveBar()
        fun onImpressQuestCardSwiped()
    }
}
