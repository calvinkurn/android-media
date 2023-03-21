package com.tokopedia.tokopedianow.home.presentation.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_DETAIL_PRODUCTION_URL
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_DETAIL_STAGING_URL
import com.tokopedia.tokopedianow.common.util.ImageUtil.setBackgroundImage
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestTitleWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestTitleUiModel
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestTitleViewHolder(
    itemView: View,
    private val listener: HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener? = null
): AbstractViewHolder<HomeQuestTitleUiModel>(itemView) {

    companion object {
        private const val BG_QUEST_TITLE = TokopediaImageUrl.BG_QUEST_TITLE
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_title_widget
    }

    private var binding: ItemTokopedianowQuestTitleWidgetBinding? by viewBinding()

    override fun bind(element: HomeQuestTitleUiModel) {
        hideShimmering()
        if (element.isErrorState) {
            setErrorState()
        } else {
            setLoadState(element)
        }
    }

    private fun hideShimmering() {
        binding?.container?.apply {
            setBackgroundImage(context, BG_QUEST_TITLE, binding?.container)
        }
        binding?.questTitleWidgetShimmering?.root?.hide()
    }

    private fun showShimmering() {
        binding?.apply {
            binding?.container?.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.tokopedianow_cardview_background_dms_color))
            questTitleWidgetShimmering.root.show()
            questTitleWidgetErrorState.root.hide()
            questTitleWidget.hide()
        }
    }

    private fun setLoadState(element: HomeQuestTitleUiModel) {
        binding?.apply {
            questTitleWidgetErrorState.root.hide()
            questTitleWidget.show()
            iuGift.setImage(newIconId = IconUnify.GIFT)
            iuGift.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White), BlendModeCompat.SRC_ATOP)
            tpCounter.text = String.format("${element.currentQuestFinished}/${element.totalQuestTarget}")
            root.setOnClickListener {
                listener?.onClickQuestWidgetTitle()
                goToQuestDetailWebView()
            }
        }
    }

    private fun setErrorState() {
        binding?.apply {
            questTitleWidget.hide()
            questTitleWidgetErrorState.let {
                it.root.show()
                it.sivRefresh.setOnClickListener {
                    listener?.onClickRefreshQuestWidget()
                    showShimmering()
                }
            }
        }
    }
    private fun goToQuestDetailWebView() {
        val url = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            QUEST_DETAIL_STAGING_URL
        } else {
            QUEST_DETAIL_PRODUCTION_URL
        }
        RouteManager.route(itemView.context, url)
    }
}