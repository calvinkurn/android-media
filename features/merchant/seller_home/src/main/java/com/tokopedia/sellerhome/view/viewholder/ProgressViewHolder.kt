package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ProgressUiModel
import kotlinx.android.synthetic.main.sah_partial_error_load_data.view.*
import kotlinx.android.synthetic.main.sah_partial_progress_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_progress_widget.view.*

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressViewHolder(view: View?) : AbstractViewHolder<ProgressUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_progress_card_widget
    }

    private var state = State.SUCESS

    override fun bind(element: ProgressUiModel) {
        when (state) {
            State.LOADING -> {
                hideErrorLayout()
                hideProgressLayout()
                showShimmeringLayout()
            }
            State.SUCESS -> {
                hideErrorLayout()
                hideShimmeringLayout()

                with(element) {
                    itemView.tv_card_title.text = title
                    itemView.tv_description.text = description
                    setupCurrentProgress(currentProgress, state)
                }

                showProgressLayout()
            }
            State.ERROR -> {
                hideProgressLayout()
                hideShimmeringLayout()
                itemView.tv_error_card_title.text = element.title
                showErrorLayout()
            }
        }
    }

    private fun setupCurrentProgress(currentProgress: Float, state: ProgressUiModel.State) {
        itemView.shop_score_widget.setProgress(currentProgress)

        when (state) {
            ProgressUiModel.State.GREEN -> itemView.shop_score_widget.setProgressColor(intArrayOf(R.color.Green_G400, R.color.Green_G600))
            ProgressUiModel.State.ORANGE -> itemView.shop_score_widget.setProgressColor(intArrayOf(R.color.Yellow_Y300, R.color.Yellow_Y400))
            ProgressUiModel.State.RED -> itemView.shop_score_widget.setProgressColor(intArrayOf(R.color.Red_R400, R.color.Red_R500))
        }
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