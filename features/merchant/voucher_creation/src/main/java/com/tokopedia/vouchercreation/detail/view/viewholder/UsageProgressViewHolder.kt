package com.tokopedia.vouchercreation.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.UsageProgressUiModel
import kotlinx.android.synthetic.main.item_mvc_usage_progress.view.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class UsageProgressViewHolder(itemView: View?) : AbstractViewHolder<UsageProgressUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_usage_progress
    }

    override fun bind(element: UsageProgressUiModel) {
        with(itemView) {
            progressMvcUsage.setValue(element.voucherUsed, true)
            progressMvcUsage.progressBarHeight = context.pxToDp(6).toInt()
        }
    }
}