package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.ProgressState
import com.tokopedia.sellerhome.view.model.ProgressUiModel
import kotlinx.android.synthetic.main.partial_sah_progress_widget.view.*
import kotlinx.android.synthetic.main.partial_sah_shimmering_progress_widget.view.*

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressViewHolder(view: View?) : AbstractViewHolder<ProgressUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_progress_card_widget
    }

    private var visibility = true

    override fun bind(element: ProgressUiModel) {
        with(element) {
            itemView.tv_progress_title.text = title
            itemView.tv_description.text = description
            setupCurrentProgress(currentProgress, state)
            itemView.setOnClickListener {
                if (visibility) {
                    itemView.sah_progress_layout.visibility = View.GONE
                    itemView.sah_progress_layout_shimmering.visibility = View.VISIBLE
                } else {
                    itemView.sah_progress_layout.visibility = View.VISIBLE
                    itemView.sah_progress_layout_shimmering.visibility = View.GONE
                }
                visibility = !visibility
            }
        }
    }

    private fun setupCurrentProgress(currentProgress: Float, state: ProgressState) {
        itemView.shop_score_widget.setProgress(currentProgress)

        when (state) {
            ProgressState.GREEN -> itemView.shop_score_widget.setProgressColor(intArrayOf(R.color.Green_G400, R.color.Green_G600))
            ProgressState.ORANGE -> itemView.shop_score_widget.setProgressColor(intArrayOf(R.color.Yellow_Y300, R.color.Yellow_Y400))
            ProgressState.RED -> itemView.shop_score_widget.setProgressColor(intArrayOf(R.color.Red_R400, R.color.Red_R500))
        }
    }
}