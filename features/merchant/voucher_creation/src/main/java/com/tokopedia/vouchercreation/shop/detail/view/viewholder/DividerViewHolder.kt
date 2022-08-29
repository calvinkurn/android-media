package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcDividerBinding
import com.tokopedia.vouchercreation.shop.detail.model.DividerUiModel

/**
 * Created By @ilhamsuaib on 04/05/20
 */

class DividerViewHolder(itemView: View?) : AbstractViewHolder<DividerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_divider
    }

    private var binding: ItemMvcDividerBinding? by viewBinding()

    override fun bind(element: DividerUiModel) {
        binding?.apply {
            viewMvcDivider.layoutParams.height = viewMvcDivider.context.dpToPx(element.dividerHeight).toInt()
            viewMvcDivider.requestLayout()
        }
    }
}