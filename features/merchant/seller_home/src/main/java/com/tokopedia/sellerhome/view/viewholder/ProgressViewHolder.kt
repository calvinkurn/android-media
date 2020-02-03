package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ProgressWidgetUiModel
import com.tokopedia.sellerhome.SellerHomeWidgetTooltipClickListener
import com.tokopedia.sellerhome.view.model.TooltipUiModel
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import kotlinx.android.synthetic.main.sah_partial_progress_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget_error.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_progress_widget.view.*

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressViewHolder(view: View?, private val tooltipClickListener: SellerHomeWidgetTooltipClickListener, private val listener: Listener) : AbstractViewHolder<ProgressWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_progress_card_widget
    }

    private var state = State.LOADING

    override fun bind(element: ProgressWidgetUiModel) {
        listener.getProgressData()
        createListeners(element.tooltip ?: return)
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
                itemView.tv_card_title.text = title
                itemView.tv_description.text = data?.subtitle
                setupProgressBar(barTitle, valueTxt, maxValueTxt, value, maxValue, colorState)
            }
        }

        showProgressLayout()
    }

    private fun createListeners(tooltip: TooltipUiModel) {
        itemView.iv_info.setOnClickListener { showBottomSheet(tooltip) }
        itemView.tv_see_details.setOnClickListener { goToDetails() }
    }

    private fun showBottomSheet(tooltip: TooltipUiModel) {
        Toast.makeText(itemView.context, "Hi Bambang!", Toast.LENGTH_SHORT).show()
        tooltipClickListener.onInfoTooltipClicked(tooltip)
    }

    private fun goToDetails() {
        Toast.makeText(itemView.context, "Hi Bambang!", Toast.LENGTH_SHORT).show()
    }

    private fun showErrorState(element: ProgressWidgetUiModel) {
        hideProgressLayout()
        hideShimmeringLayout()
        itemView.tv_error_card_title.text = element.title
        showErrorLayout()
    }

    private fun setupProgressBar(
            progressTitle: String,
            currentProgressText: String,
            maxProgressText: String,
            currentProgress: Int,
            maxProgress: Int,
            state: ShopScorePMWidget.State
    ) {
        itemView.shop_score_widget.setProgressTitle(progressTitle)
        itemView.shop_score_widget.setCurrentProgressText(currentProgressText)
        itemView.shop_score_widget.setMaxProgressText(maxProgressText)
        itemView.shop_score_widget.setProgressValue(currentProgress)
        itemView.shop_score_widget.setMaxProgressValue(maxProgress)
        itemView.shop_score_widget.setProgressColor(state)
    }

    private fun showShimmeringLayout() {
        itemView.sah_progress_layout_shimmering.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.sah_progress_layout_shimmering.gone()
    }

    private fun showProgressLayout() {
        itemView.sah_progress_layout.visible()
    }

    private fun hideProgressLayout() {
        itemView.sah_progress_layout.gone()
    }

    private fun showErrorLayout() {
        itemView.sah_error_layout.visible()
    }

    private fun hideErrorLayout() {
        itemView.sah_error_layout.gone()
    }

    enum class State {
        LOADING,
        SUCESS,
        ERROR
    }

    interface Listener {
        fun getProgressData()
    }
}