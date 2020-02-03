package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ProgressWidgetUiModel
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import kotlinx.android.synthetic.main.sah_partial_error_load_data.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_progress_widget.view.*

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressViewHolder(
        view: View?,
        private val listener: Listener
) : AbstractViewHolder<ProgressWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_progress_card_widget
    }

    private var state = State.LOADING

    override fun bind(element: ProgressWidgetUiModel) {
        listener.getProgressData()
//        showLoadingState()
//        itemView.postDelayed({
//            showErrorState(element)
//        }, 5000)
//        itemView.postDelayed({
//            showSuccessState(element)
//        }, 10000)
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
                itemView.tv_description.text = subtitle
                setupProgressBar(title, value, maxValue, state)
            }
        }

        showProgressLayout()
    }

    private fun showErrorState(element: ProgressWidgetUiModel) {
        hideProgressLayout()
        hideShimmeringLayout()
        itemView.tv_error_card_title.text = element.title
        showErrorLayout()
    }

    private fun setupProgressBar(progressTitle: String, currentProgress: Int, maxProgress: Int, state: ShopScorePMWidget.State) {
        itemView.shop_score_widget.setProgressTitle(progressTitle)
        itemView.shop_score_widget.setProgress(currentProgress)
        itemView.shop_score_widget.setMaxProgress(maxProgress)
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