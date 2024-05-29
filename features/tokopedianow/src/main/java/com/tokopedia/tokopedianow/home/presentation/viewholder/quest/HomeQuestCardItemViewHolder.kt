package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.imageassets.TokopediaImageUrl.TOKOPEDIANOW_IC_QUEST_LOCKED_DARK
import com.tokopedia.imageassets.TokopediaImageUrl.TOKOPEDIANOW_IC_QUEST_LOCKED_LIGHT
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantUrl
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.unifycomponents.utils.setImageVectorDrawable
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.resources.isDarkMode

class HomeQuestCardItemViewHolder(
    private val binding: ItemTokopedianowQuestCardBinding,
    private val listener: HomeQuestCardItemListener? = null
): RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val QUEST_CARD_DISPLAYED_PERCENTAGE = 0.95f
    }

    init {
        binding.root.initQuestCardWidth()
    }

    private fun View.initQuestCardWidth() {
        val padding = getDpFromDimen(
            context = itemView.context,
            id = R.dimen.tokopedianow_quest_card_horizontal_padding
        )
        val width = ((getScreenWidth() - (padding + padding)) * QUEST_CARD_DISPLAYED_PERCENTAGE)
        layoutParams.width = width.toIntSafely()
    }

    private fun ItemTokopedianowQuestCardBinding.adjustTitleStartPadding() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(layoutQuestCard)
        constraintSet.setMargin(
            tpTitle.id,
            ConstraintSet.START,
            getDpFromDimen(
                context = itemView.context,
                id = R.dimen.tokopedianow_quest_card_title_start_padding
            ).toIntSafely()
        )
        constraintSet.applyTo(layoutQuestCard)
    }

    private fun openQuestChannelPage() {
        val appLink = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) ConstantUrl.QUEST_CHANNEL_STAGING_APPLINK else ConstantUrl.QUEST_CHANNEL_PRODUCTION_APPLINK
        RouteManager.route(itemView.context, appLink)
    }

    fun bind(element: HomeQuestCardItemUiModel) {
        binding.apply {
            tpTitle.text = element.title
            tpDescription.text = element.description
            aivBackground.showIfWithBlock(element.isLockedShown) {
                setImageVectorDrawable(R.drawable.tokopedianow_bg_quest_locked)
                adjustTitleStartPadding()
            }
            aivIcon.showIfWithBlock(element.isLockedShown) {
                loadImage(
                    if (context.isDarkMode())
                        TOKOPEDIANOW_IC_QUEST_LOCKED_DARK
                    else
                        TOKOPEDIANOW_IC_QUEST_LOCKED_LIGHT
                )
            }
            root.setOnClickListener {
                openQuestChannelPage()
                listener?.onClickQuestCard()
            }
            setupStartButton(element)
        }
    }

    private fun setupStartButton(element: HomeQuestCardItemUiModel) {
        binding.apply {
            if(element.showStartBtn) {
                setStartBtnClickListener(element)
                btnStart.isLoading = element.isLoading
                btnStart.show()
            } else {
                btnStart.hide()
            }
        }
    }

    private fun ItemTokopedianowQuestCardBinding.setStartBtnClickListener(
        element: HomeQuestCardItemUiModel
    ) {
        btnStart.apply {
            if (!element.isLoading) {
                setOnClickListener {
                    listener?.onClickStartButton(
                        channelId = element.channelId,
                        questId = element.id.toIntOrZero()
                    )
                    setOnClickListener(null)
                }
            } else {
                setOnClickListener(null)
            }
        }
    }

    interface HomeQuestCardItemListener {
        fun onClickQuestCard()

        fun onClickStartButton(channelId: String, questId: Int)
    }
}
