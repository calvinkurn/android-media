package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_progress_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_progress_widget_error.view.*
import kotlinx.android.synthetic.main.shc_partial_shimmering_progress_widget.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class ProgressViewHolder(view: View?, private val listener: Listener) : AbstractViewHolder<ProgressWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_progress_card_widget
    }

    override fun bind(element: ProgressWidgetUiModel) {
        observeState(element)
    }

    private fun observeState(element: ProgressWidgetUiModel) {
        val data = element.data
        when {
            null == data -> onLoading()
            data.error.isNotBlank() -> {
                onError(element)
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> onSuccessLoadData(element)
        }
    }

    private fun onLoading() {
        showLoadingState()
    }

    private fun onError(element: ProgressWidgetUiModel) {
        showErrorState(element)
    }

    private fun onSuccessLoadData(element: ProgressWidgetUiModel) {
        showSuccessState(element)
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideProgressLayout()
        showShimmeringLayout()
    }

    private fun showSuccessState(element: ProgressWidgetUiModel) {
        hideErrorLayout()
        hideShimmeringLayout()

        element.data?.run {
            with(element) {
                itemView.tvProgressTitle.text = title
                itemView.tvProgressDescription.text = data?.subtitle?.parseAsHtml()
                setupProgressBar(subtitle, valueTxt, maxValueTxt, value, maxValue, colorState)
                setupDetails(this)
                addImpressionTracker(this)
            }
        }

        showProgressLayout()
    }

    private fun addImpressionTracker(progressWidgetUiModel: ProgressWidgetUiModel) {
        with(progressWidgetUiModel) {
            itemView.addOnImpressionListener(impressHolder) {
                listener.sendProgressImpressionEvent(dataKey, data?.colorState.toString(), data?.value.orZero())
            }
        }
    }

    private fun goToDetails(element: ProgressWidgetUiModel) {
        with(element) {
            if (RouteManager.route(itemView.context, appLink)) {
                listener.sendProgressCtaClickEvent(dataKey, data?.colorState.toString(), data?.value.orZero())
            }
        }
    }

    private fun showErrorState(element: ProgressWidgetUiModel) {
        hideProgressLayout()
        hideShimmeringLayout()
        itemView.tvProgressTitleOnError.text = element.title
        itemView.imgWidgetOnError.loadImageDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        showErrorLayout()
    }

    private fun setupProgressBar(
            progressTitle: String,
            currentProgressText: String,
            maxProgressText: String,
            currentProgress: Int,
            maxProgress: Int,
            state: ShopScorePMWidget.State
    ) = with(itemView.shopScoreProgress) {
        setProgressTitle(progressTitle)
        setCurrentProgressText(currentProgressText)
        setMaxProgressText(maxProgressText)
        setProgressValue(currentProgress)
        setMaxProgressValue(maxProgress)
        setProgressColor(state)
    }

    private fun setupDetails(element: ProgressWidgetUiModel) {
        with(itemView) {
            if (element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()) {
                tvProgressSeeDetails.text = element.ctaText
                tvProgressSeeDetails.visibility = View.VISIBLE
                icProgressArrow.visibility = View.VISIBLE
                tvProgressSeeDetails.setOnClickListener {
                    goToDetails(element)
                }
                icProgressArrow.setOnClickListener {
                    goToDetails(element)
                }
            } else {
                tvProgressSeeDetails.visible()
                icProgressArrow.gone()
            }
        }
    }

    private fun showShimmeringLayout() {
        itemView.shcProgressOnLoadingStateLayout.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.shcProgressOnLoadingStateLayout.gone()
    }

    private fun showProgressLayout() {
        itemView.sahProgressOnSuccessLayout.visible()
    }

    private fun hideProgressLayout() {
        itemView.sahProgressOnSuccessLayout.gone()
    }

    private fun showErrorLayout() {
        itemView.sahProgressOnErrorLayout.visible()
    }

    private fun hideErrorLayout() {
        itemView.sahProgressOnErrorLayout.gone()
    }

    interface Listener : BaseViewHolderListener {

        fun sendProgressImpressionEvent(dataKey: String, stateColor: String, valueScore: Int) {}

        fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Int) {}
    }
}