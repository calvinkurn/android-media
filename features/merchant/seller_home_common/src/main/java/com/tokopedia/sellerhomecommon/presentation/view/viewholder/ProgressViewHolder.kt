package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcProgressCardWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class ProgressViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<ProgressWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_progress_card_widget
        private const val MAX_VALUE = 100
    }

    private val binding by lazy {
        ShcProgressCardWidgetBinding.bind(itemView)
    }
    private val errorStateBinding by lazy { binding.shcProgressErrorState }
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
        listener.showProgressBarCoachMark(element.dataKey, itemView)
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
                binding.shcProgressSuccessState.tvProgressTitle.text = title.parseAsHtml()
                binding.shcProgressSuccessState.tvProgressDescription.text =
                    data?.subtitle?.parseAsHtml()
                setupLastUpdated(element)
                val mValue = getPercentValue(value, maxValue)
                setupProgressBar(subtitle, valueTxt, maxValueTxt, mValue, colorState)
                setupDetails(this)
                addImpressionTracker(this)
            }
        }

        showProgressLayout()
    }

    private fun getPercentValue(value: Long, maxValue: Long): Int {
        return try {
            value.times(MAX_VALUE).div(maxValue).toInt()
        } catch (e: ArithmeticException) {
            Timber.e(e)
            Int.ZERO
        }
    }

    private fun setupLastUpdated(element: ProgressWidgetUiModel) {
        with(binding.shcProgressSuccessState.tvShcProgressLastUpdated) {
            element.data?.lastUpdated?.let { lastUpdated ->
                isVisible = lastUpdated.isEnabled
                setLastUpdated(getLastUpdateFmt(lastUpdated.lastUpdatedInMillis))
                setRefreshButtonVisibility(lastUpdated.needToUpdated)
                setRefreshButtonClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun getLastUpdateFmt(lastUpdatedInMillis: Long): String {
        val lastUpdateStr = DateTimeUtil.format(
            lastUpdatedInMillis, DateTimeUtil.FORMAT_DD_MMM_YYYY
        )
        return itemView.context.getString(R.string.shc_last_updated, lastUpdateStr)
    }

    private fun refreshWidget(element: ProgressWidgetUiModel) {
        listener.onReloadWidget(element)
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
        errorStateBinding.imgShcProgressWidgetOnError.loadImage(
            com.tokopedia.globalerror.R.drawable.unify_globalerrors_404
        )
        showErrorLayout()
    }

    private fun setupProgressBar(
        progressTitle: String,
        currentProgressText: String,
        maxProgressText: String,
        currentProgress: Int,
        state: ShopScorePMWidget.State
    ) = with(binding.shcProgressSuccessState.shopScoreProgress) {
        setProgressTitle(progressTitle)
        setCurrentProgressText(currentProgressText)
        setMaxProgressText(maxProgressText)
        setProgressValue(currentProgress)
        setMaxProgressValue(MAX_VALUE)
        setProgressColor(state)
    }

    private fun setupDetails(element: ProgressWidgetUiModel) {
        with(binding.shcProgressSuccessState) {
            if (element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()) {
                tvProgressSeeDetails.text = element.ctaText
                tvProgressSeeDetails.visible()
                tvProgressSeeDetails.setOnClickListener {
                    goToDetails(element)
                }
                val dp24 = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                tvProgressSeeDetails.setUnifyDrawableEnd(
                    iconId = IconUnify.CHEVRON_RIGHT,
                    colorIcon = root.context.getResColor(
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    ),
                    width = dp24,
                    height = dp24
                )
            } else {
                tvProgressSeeDetails.invisible()
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

        fun sendProgressImpressionEvent(dataKey: String, stateColor: String, valueScore: Long) {}

        fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Long) {}

        fun showProgressBarCoachMark(dataKey: String, view: View) {}
    }
}