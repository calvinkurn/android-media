package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ResetOnBoardingUiModel
import com.tokopedia.unifycomponents.UnifyButton

class SkipOnBoardingViewHolder(
    itemView: View,
    private val listener: SkipOnBoardingListener
): AbstractViewHolder<ResetOnBoardingUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_skip_onboarding
    }

    override fun bind(element: ResetOnBoardingUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.skip_onboarding_btn)
        btn.setOnClickListener {
            listener.onClickSkipOnBoardingBtn()
        }
    }

    interface SkipOnBoardingListener {
        fun onClickSkipOnBoardingBtn()
    }
}
