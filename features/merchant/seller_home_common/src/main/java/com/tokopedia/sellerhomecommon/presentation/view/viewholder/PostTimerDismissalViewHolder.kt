package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemPostTimerDismissalBinding
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostListPagerAdapter
import com.tokopedia.sellerhomecommon.presentation.view.customview.DismissalTimerView

/**
 * Created by @ilhamsuaib on 08/09/22.
 */

class PostTimerDismissalViewHolder(
    view: View?,
    private val listener: PostListPagerAdapter.Listener,
) : AbstractViewHolder<PostItemUiModel.PostTimerDismissalUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_post_timer_dismissal
    }

    private val binding by lazy {
        ShcItemPostTimerDismissalBinding.bind(itemView)
    }

    override fun bind(element: PostItemUiModel.PostTimerDismissalUiModel) {
        with(binding) {
            val title = root.context.getString(
                R.string.shc_n_info_deleted, element.totalDeletedItems
            )
            setBackgroundRes()
            val duration = if (element.runningTimeInMillis.isZero()) {
                DismissalTimerView.DEFAULT_DURATION
            } else {
                element.runningTimeInMillis
            }

            shcPostListTimerView.startTimer(title = title,
                duration = duration,
                listener = object : DismissalTimerView.Listener {
                    override fun onFinished() {
                        listener.onTimerFinished()
                        element.runningTimeInMillis = Int.ZERO.toLong()
                    }

                    override fun onCancelTimer() {
                        listener.onCancelDismissalClicked()
                        element.runningTimeInMillis = Int.ZERO.toLong()
                    }

                    override fun onTicked(millisUntilFinished: Long) {
                        element.runningTimeInMillis = millisUntilFinished
                    }
                })
        }
    }

    private fun setBackgroundRes() {
        binding.shcPostListTimerView.setBackgroundResource(R.drawable.shc_dashed_background)
    }
}