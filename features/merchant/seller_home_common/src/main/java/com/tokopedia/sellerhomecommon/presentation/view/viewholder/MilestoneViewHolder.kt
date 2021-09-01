package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.presentation.adapter.MilestoneMissionAdapter
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneProgressbarUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.ProgressBarUnify
import kotlinx.android.synthetic.main.shc_milestone_widget_error.view.*
import kotlinx.android.synthetic.main.shc_milestone_widget_loading.view.*
import kotlinx.android.synthetic.main.shc_milestone_widget_success.view.*

class MilestoneViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MilestoneWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_milestone_widget

        private const val PROGRESS_BAR_MAX_VALUE = 100
    }

    private val onLoadingView: View by itemView.viewStubInflater(R.id.stubShcMilestoneLoading)
    private val onErrorView: View by itemView.viewStubInflater(R.id.stubShcMilestoneError)
    private val onSuccessView: View by itemView.viewStubInflater(R.id.stubShcMilestoneSuccess)

    override fun bind(element: MilestoneWidgetUiModel) {
        val data = element.data
        when {
            data == null -> showLoadingState(element)
            data.error.isNotBlank() -> showErrorState(element)
            else -> setOnSuccess(element)
        }
    }

    private fun setOnSuccess(element: MilestoneWidgetUiModel) {
        onErrorView.containerShcMilestoneError.gone()
        onLoadingView.containerShcMilestoneLoading.gone()
        with(onSuccessView) {
            val data = element.data ?: return@with

            containerShcMilestoneSuccess.visible()
            tvTitleMilestoneWidget.text = data.title
            tvDescMilestoneWidget.text = data.subTitle
            tvProgressTitleMilestoneWidget.text = data.milestoneProgress.description

            val milestoneValueFmt = data.milestoneProgress.percentageFormatted.parseAsHtml()
            tvProgressValueMilestoneWidget.text = milestoneValueFmt

            iconShcToggleMission.setOnClickListener {
                if (rvShcMissionMilestone.isVisible) {
                    rvShcMissionMilestone.gone()
                    iconShcToggleMission.setImage(IconUnify.CHEVRON_UP)
                } else {
                    rvShcMissionMilestone.visible()
                    iconShcToggleMission.setImage(IconUnify.CHEVRON_DOWN)
                }
            }

            setupMilestoneProgress(data.milestoneProgress)
            setupMilestoneList(element)
            setupTooltip(tvTitleMilestoneWidget, element)
            setupSeeMoreCta(element)
        }
    }

    private fun setupSeeMoreCta(element: MilestoneWidgetUiModel) {
        with(onSuccessView) {
            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
            tvShcMilestoneCta.isVisible = isCtaVisible

            if (isCtaVisible) {
                tvShcMilestoneCta.text = element.ctaText
                tvShcMilestoneCta.setOnClickListener {
                    openApplink(element)
                }
                val iconColor = context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                )
                val iconWidth = context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                val iconHeight = context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                tvShcMilestoneCta.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
            }
        }
    }

    private fun openApplink(element: MilestoneWidgetUiModel) {
        RouteManager.route(itemView.context, element.appLink)
    }

    private fun setupMilestoneList(element: MilestoneWidgetUiModel) {
        with(onSuccessView) {
            val mission = element.data ?: return
            rvShcMissionMilestone.layoutManager = object : LinearLayoutManager(
                context, HORIZONTAL, false
            ) {
                override fun canScrollVertically(): Boolean = false
            }

            rvShcMissionMilestone.adapter = MilestoneMissionAdapter(mission) {
                listener.onMilestoneMissionActionClickedListener(element, it)
            }
        }
    }

    private fun setupMilestoneProgress(milestoneProgress: MilestoneProgressbarUiModel) {
        with(onSuccessView) {
            val valuePerIndicator = PROGRESS_BAR_MAX_VALUE / milestoneProgress.totalTask
            progressBarShcMilestone.progressBarHeight = ProgressBarUnify.SIZE_MEDIUM
            progressBarShcMilestone.setValue(milestoneProgress.taskCompleted.times(valuePerIndicator))
        }
    }

    private fun showErrorState(element: MilestoneWidgetUiModel) {
        onSuccessView.containerShcMilestoneSuccess.gone()
        onLoadingView.containerShcMilestoneLoading.gone()
        onErrorView.run {
            containerShcMilestoneError.visible()
            tvShcMilestoneErrorStateTitle.text = element.title
            btnMilestoneError.setOnClickListener {
                listener.reloadMilestoneWidget(element)
            }

            imgMilestoneOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)

            setupTooltip(tvShcMilestoneErrorStateTitle, element)
        }
    }

    private fun showLoadingState(element: MilestoneWidgetUiModel) {
        onSuccessView.containerShcMilestoneSuccess.gone()
        onErrorView.containerShcMilestoneError.gone()
        onLoadingView.run {
            containerShcMilestoneLoading.visible()
            tvShcMilestoneErrorTitle.text = element.title
            setupTooltip(tvShcMilestoneErrorTitle, element)
        }
    }

    private fun setupTooltip(textView: TextView, element: MilestoneWidgetUiModel) {
        SellerHomeCommonUtils.setupWidgetTooltip(textView, element) {
            listener.onTooltipClicked(it)
        }
    }

    private fun View.viewStubInflater(viewStubId: Int): Lazy<View> {
        return lazy {
            val viewStub: ViewStub = findViewById(viewStubId)
            viewStub.inflate()
        }
    }

    interface Listener : BaseViewHolderListener {

        fun reloadMilestoneWidget(model: MilestoneWidgetUiModel) {}
        fun onMilestoneMissionActionClickedListener(
            element: MilestoneWidgetUiModel,
            mission: BaseMilestoneMissionUiModel
        ) {
        }
    }
}