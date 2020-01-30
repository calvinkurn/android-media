package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ProgressUiModel
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import kotlinx.android.synthetic.main.sah_partial_progress_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget_error.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_progress_widget.view.*

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressViewHolder(view: View?) : AbstractViewHolder<ProgressUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_progress_card_widget
    }

    private var state = State.LOADING

    override fun bind(element: ProgressUiModel) {
        showLoadingState()
        itemView.postDelayed({
            showErrorState(element)
        }, 5000)
        itemView.postDelayed({
            showSuccessState(element)
        }, 10000)

        createListeners()
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideProgressLayout()
        showShimmeringLayout()
    }

    private fun showSuccessState(element: ProgressUiModel) {
        hideErrorLayout()
        hideShimmeringLayout()

        element.data?.run {
            with(element) {
                itemView.tv_card_title.text = title
                itemView.tv_description.text = subtitle
                setupProgressBar(barTitle, valueTxt, maxValueTxt, value, maxValue, colorState)
            }
        }

        showProgressLayout()
    }

    private fun createListeners() {
        itemView.iv_info.setOnClickListener { showBottomSheet() }
        itemView.tv_see_details.setOnClickListener { goToDetails() }
    }

    private fun showBottomSheet() {
        Toast.makeText(itemView.context, "Hi Bambang!", Toast.LENGTH_SHORT).show()
    }

    private fun goToDetails() {
        Toast.makeText(itemView.context, "Hi Bambang!", Toast.LENGTH_SHORT).show()
    }

    private fun showErrorState(element: ProgressUiModel) {
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
}