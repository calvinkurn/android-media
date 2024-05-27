package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.callback.SnapOnScrollCallback.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_CHANNEL_PRODUCTION_APPLINK
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_CHANNEL_STAGING_APPLINK
import com.tokopedia.tokopedianow.common.listener.SnapPositionChangeListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestBinding
import com.tokopedia.tokopedianow.common.util.SnapHelperUtil.attachSnapHelperWithListener
import com.tokopedia.tokopedianow.home.presentation.decoration.QuestCardItemDecoration
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.view.HomeQuestProgressBarView.HomeQuestProgressBarListener
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

        private const val QUEST_CARD_FIRST_INDEX = 0
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

    private var shouldTrackSwipe = false

    private val binding: ItemTokopedianowQuestBinding? by viewBinding()

    init {
        binding?.rvQuestCards?.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
            addItemDecoration(QuestCardItemDecoration())

            attachSnapHelperWithListener(
                snapHelper = PagerSnapHelper(),
                behavior = NOTIFY_ON_SCROLL_STATE_IDLE,
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
            rvQuestCards.scrollToPosition(QUEST_CARD_FIRST_INDEX)
            setupProgressBar(element)
        }
    }

    override fun bind(element: HomeQuestWidgetUiModel?, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && element != null) {
            mAdapter.submitList(element.questList)
            setupProgressBar(element)
        }
    }

    private fun setupProgressBar(element: HomeQuestWidgetUiModel) {
        binding?.apply {
            val listener = createProgressBarListener(element)
            questProgressBar.bind(element, listener)
        }
    }

    private fun openQuestChannelPage() {
        val appLink = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) QUEST_CHANNEL_STAGING_APPLINK else QUEST_CHANNEL_PRODUCTION_APPLINK
        RouteManager.route(itemView.context, appLink)
    }

    private fun createProgressBarListener(element: HomeQuestWidgetUiModel): HomeQuestProgressBarListener {
        return object : HomeQuestProgressBarListener {
            override fun onClickProgressItem(index: Int, isSelected: Boolean) {
                if(!isSelected) {
                    shouldTrackSwipe = false
                    smoothSnapToPosition(index)
                    listener?.onClickProgressiveBar()
                }
            }

            override fun onAnimationFinished() {
                val progressPosition = element.currentProgressPosition
                smoothSnapToPosition(progressPosition)
            }
        }
    }

    private fun smoothSnapToPosition(index: Int) {
        binding?.rvQuestCards?.smoothSnapToPosition(index)
    }

    private fun switchProgressItemColor(position: Int) {
        binding?.questProgressBar?.switchItemColor(position)
    }

    override fun onSnapPositionChange(position: Int) {
        if(shouldTrackSwipe) {
            listener?.onImpressQuestCardSwiped()
        }
        switchProgressItemColor(position)
        shouldTrackSwipe = true
    }

    override fun onClickQuestCard() {
        listener?.onClickQuestCard()
    }

    override fun onClickStartButton(channelId: String, questId: Int) {
        listener?.onClickStartButton(channelId, questId)
    }

    interface HomeQuestWidgetListener {
        fun onImpressQuestWidget()
        fun onClickSeeDetailsQuestWidget()
        fun onClickQuestCard()
        fun onClickProgressiveBar()
        fun onImpressQuestCardSwiped()
        fun onClickStartButton(channelId: String, questId: Int)
    }
}
