package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewStub
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneProgressbarUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.unifycomponents.ProgressBarIndicatorItemUnify
import kotlinx.android.synthetic.main.shc_milestone_widget_error.view.*
import kotlinx.android.synthetic.main.shc_milestone_widget_loading.view.*
import kotlinx.android.synthetic.main.shc_milestone_widget_success.view.*

class MilestoneViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MilestoneWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_milestone_widget
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

            setupMilestoneProgress(data.milestoneProgress)
        }
    }

    private fun setupMilestoneProgress(milestoneProgress: MilestoneProgressbarUiModel) {
        with(onSuccessView) {
            val progressBarIndicator = (0..milestoneProgress.totalTask).map {
                ProgressBarIndicatorItemUnify(it, it.toString())
            }
            progressBarShcMilestone.progressBarIndicator = progressBarIndicator.toTypedArray()
            progressBarShcMilestone.setValue(milestoneProgress.taskCompleted)
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
    }
}