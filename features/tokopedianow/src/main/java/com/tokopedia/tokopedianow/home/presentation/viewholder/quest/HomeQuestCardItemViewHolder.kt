package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_DETAIL_PRODUCTION_APPLINK
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_DETAIL_STAGING_APPLINK
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.unifycomponents.utils.setImageVectorDrawable
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.resources.isDarkMode

class HomeQuestCardItemViewHolder(
    private val binding: ItemTokopedianowQuestCardBinding
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

    private fun goToQuestDetailPage(id: String) {
        val appLink = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) QUEST_DETAIL_STAGING_APPLINK + id else QUEST_DETAIL_PRODUCTION_APPLINK + id
        RouteManager.route(itemView.context, appLink)
    }

    fun bind(element: HomeQuestCardItemUiModel) {
        binding.apply {
            tpTitle.text = element.title
            tpDescription.text = element.description
            aivIcon.showIfWithBlock(element.isLockedShown) {
                setBackgroundResource(R.drawable.tokopedianow_bg_quest_locked)
                setImageVectorDrawable(if (context.isDarkMode()) R.drawable.tokopedianow_ic_quest_locked_dark else R.drawable.tokopedianow_ic_quest_locked_light)
                adjustTitleStartPadding()
            }
            root.setOnClickListener {
                goToQuestDetailPage(element.id)
            }
        }
    }
}
