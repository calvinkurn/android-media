package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingTemporaryFinishBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TemporaryFinishUiModel

class TemporaryFinishViewHolder(view: View) : AbstractViewHolder<TemporaryFinishUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_temporary_finish
    }

    private val binding = ItemTokofoodOrderTrackingTemporaryFinishBinding.bind(itemView)

    override fun bind(element: TemporaryFinishUiModel) {
        with(binding) {
            setTemporaryFinishImage(element.temporaryFinishUrl)
        }
    }

    private fun ItemTokofoodOrderTrackingTemporaryFinishBinding.setTemporaryFinishImage(imageUrl: String) {
        ivTemporaryFinish.setImageUrl(imageUrl)
    }
}