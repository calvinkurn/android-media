package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcBottomsheetMoreMenuBinding
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel

class MoreMenuViewHolder(
        itemView: View?,
        private val callback: (MoreMenuUiModel) -> Unit
) : AbstractViewHolder<MoreMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_bottomsheet_more_menu
    }

    private var binding: ItemMvcBottomsheetMoreMenuBinding? by viewBinding()

    override fun bind(element: MoreMenuUiModel) {
        setTitle(element)
        setIcon(element)
        setOnClickListener(element)
    }

    private fun setTitle(element: MoreMenuUiModel) {
        binding?.apply {
            tvTitle.text = element.title.orEmpty()
        }
    }

    private fun setIcon(element: MoreMenuUiModel) {
        binding?.apply {
            val unifyColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            val iconDrawable = getIconUnifyDrawable(root.context, element.icon.orZero(), unifyColor)
            iuMenu.loadImage(iconDrawable)
        }
    }

    private fun setOnClickListener(element: MoreMenuUiModel) {
        binding?.root?.setOnClickListener {
            callback(element)
        }
    }
}