package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestWidgetViewHolder(
    itemView: View,
    private val listener: HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener? = null
): AbstractViewHolder<HomeQuestWidgetUiModel>(itemView) {

    companion object {
        private const val PROGRESS_WIDTH = 15F
        private const val PERCENT_MULTIPLIER = 100

        const val STATUS_IDLE = "Idle"
        const val STATUS_ON_PROGRESS = "On Progress"
        const val STATUS_CLAIMED = "Claimed"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_widget
    }

    private var binding: ItemTokopedianowQuestWidgetBinding? by viewBinding()

    override fun bind(element: HomeQuestWidgetUiModel) {
        hideShimmering()
        if (element.isErrorState) {
            setTitle(getString(R.string.tokopedianow_quest_error_title))
            setDesc(getString(R.string.tokopedianow_quest_error_desc))
            setCircularRefreshButton()
        } else {
            setCircularProgressBar(element)
            setTitle(element.title)
            setDesc(element.desc)
        }
    }

    private fun setCircularProgressBar(element: HomeQuestWidgetUiModel) {
        binding?.circularProgressBar?.apply {
            when(element.status) {
                STATUS_IDLE -> {
                    setImageResId(getIconUnifyDrawable(context, IconUnify.LOCK, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)), true)
                    binding?.let {
                        it.container.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.tokopedianow_cardview_inactive_background_dms_color))
                        it.title.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
                        it.desc.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
                    }
                }
                STATUS_ON_PROGRESS -> {
                    setContainer(element.appLink)
                    val result = (element.currentProgress / element.totalProgress) * PERCENT_MULTIPLIER
                    if (!result.isNaN()) {
                        setImageUrl(element.iconUrl, false, AppCompatResources.getDrawable(context, R.drawable.tokopedianow_ic_quest_default))
                        setProgress(result)
                        setProgressWidth(PROGRESS_WIDTH)
                        setRounded(true)
                        setProgressColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                        setProgressBackgroundColor(ContextCompat.getColor(context, R.color.tokopedianow_circle_progressbar_background_dms_color))
                    }
                }
            }
        }
    }

    private fun setCircularRefreshButton() {
        binding?.container?.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.tokopedianow_cardview_background_dms_color))
        binding?.circularProgressBar?.apply {
            setImageResId(getIconUnifyDrawable(context, IconUnify.RELOAD, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)), true)
            setOnClickListener {
                listener?.onClickRefreshQuestWidget()
                showShimmering()
            }
        }
    }

    private fun setTitle(title: String) {
        binding?.title?.text = title
    }

    private fun setDesc(desc: String) {
        binding?.desc?.text = desc
    }

    private fun setContainer(appLink: String) {
        binding?.container?.apply {
            setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.tokopedianow_cardview_background_dms_color))
            setOnClickListener {
                RouteManager.route(itemView.context, appLink)
            }
        }
    }

    private fun hideShimmering() {
        binding?.questWidgetShimmering?.root?.hide()
        binding?.questWidget?.show()
    }

    private fun showShimmering() {
        binding?.questWidgetShimmering?.root?.show()
        binding?.questWidget?.hide()
    }
}