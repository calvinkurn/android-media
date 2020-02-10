package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ProgressWidgetUiModel
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import kotlinx.android.synthetic.main.sah_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget_error.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_progress_widget.view.*

/**
 * Created By @yusufhendrawan on 2020-01-22
 */

class ProgressViewHolder(view: View?, private val listener: Listener) : AbstractViewHolder<ProgressWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_progress_card_widget
    }

    override fun bind(element: ProgressWidgetUiModel) {
        observeState(element)
        listener.getProgressData()
    }

    private fun observeState(element: ProgressWidgetUiModel) {
        val data = element.data
        when {
            null == data -> showLoadingState()
            data.error.isNotBlank() -> showErrorState(element)
            else -> showSuccessState(element)
        }
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
                itemView.tvProgressDescription.text = data?.subtitle
                setupProgressBar(subtitle, valueTxt, maxValueTxt, value, maxValue, colorState)
                setupDetails(this)
            }
        }

        showProgressLayout()
    }

    private fun goToDetails(element: ProgressWidgetUiModel) {
        RouteManager.route(itemView.context, element.appLink)
    }

    private fun showErrorState(element: ProgressWidgetUiModel) {
        hideProgressLayout()
        hideShimmeringLayout()
        itemView.tvProgressTitleOnError.text = element.title
        ImageHandler.loadImageWithId(itemView.imgWidgetOnError, R.drawable.unify_globalerrors_connection)
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
        itemView.sahProgressOnLoadingStateLayout.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.sahProgressOnLoadingStateLayout.gone()
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

    interface Listener {
        fun getProgressData()
    }
}