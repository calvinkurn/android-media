package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcProgressCardWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class ProgressViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<ProgressWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_progress_card_widget
    }

    private val binding by lazy {
        ShcProgressCardWidgetBinding.bind(itemView)
    }
    private val errorStateBinding by lazy { binding.shcProgressErrorState }
    private val commonErrorStateBinding by lazy {
        errorStateBinding.shcProgressCommonErrorState
    }
    private val loadingStateBinding by lazy { binding.shcProgressLoadingState }

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
                setTagNotification(element.tag)
                binding.shcProgressSuccessState.tvProgressTitle.text = title
                binding.shcProgressSuccessState.tvProgressDescription.text =
                    data?.subtitle?.parseAsHtml()
                setupProgressBar(subtitle, valueTxt, maxValueTxt, value, maxValue, colorState)
                setupDetails(this)
                addImpressionTracker(this)
            }
        }

        showProgressLayout()
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding.shcProgressSuccessState) {
            notifTagProgress.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagProgress.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun addImpressionTracker(progressWidgetUiModel: ProgressWidgetUiModel) {
        with(progressWidgetUiModel) {
            itemView.addOnImpressionListener(impressHolder) {
                listener.sendProgressImpressionEvent(
                    dataKey,
                    data?.colorState?.name.orEmpty(),
                    data?.value.orZero()
                )
            }
        }
    }

    private fun goToDetails(element: ProgressWidgetUiModel) {
        with(element) {
            if (RouteManager.route(itemView.context, appLink)) {
                listener.sendProgressCtaClickEvent(
                    dataKey,
                    data?.colorState?.name.orEmpty(),
                    data?.value.orZero()
                )
            }
        }
    }

    private fun showErrorState(element: ProgressWidgetUiModel) {
        hideProgressLayout()
        hideShimmeringLayout()
        errorStateBinding.tvProgressTitleOnError.text = element.title
        commonErrorStateBinding.imgWidgetOnError.loadImage(
            com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
        )
        showErrorLayout()
    }

    private fun setupProgressBar(
        progressTitle: String,
        currentProgressText: String,
        maxProgressText: String,
        currentProgress: Int,
        maxProgress: Int,
        state: ShopScorePMWidget.State
    ) = with(binding.shcProgressSuccessState.shopScoreProgress) {
        setProgressTitle(progressTitle)
        setCurrentProgressText(currentProgressText)
        setMaxProgressText(maxProgressText)
        setProgressValue(currentProgress)
        setMaxProgressValue(maxProgress)
        setProgressColor(state)
    }

    private fun setupDetails(element: ProgressWidgetUiModel) {
        with(binding.shcProgressSuccessState) {
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
        loadingStateBinding.shcProgressOnLoadingStateLayout.visible()
    }

    private fun hideShimmeringLayout() {
        loadingStateBinding.shcProgressOnLoadingStateLayout.gone()
    }

    private fun showProgressLayout() {
        binding.shcProgressSuccessState.sahProgressOnSuccessLayout.visible()
    }

    private fun hideProgressLayout() {
        binding.shcProgressSuccessState.sahProgressOnSuccessLayout.gone()
    }

    private fun showErrorLayout() {
        errorStateBinding.sahProgressOnErrorLayout.visible()
    }

    private fun hideErrorLayout() {
        errorStateBinding.sahProgressOnErrorLayout.gone()
    }

    interface Listener : BaseViewHolderListener {

        fun sendProgressImpressionEvent(dataKey: String, stateColor: String, valueScore: Int) {}

        fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Int) {}
    }
}