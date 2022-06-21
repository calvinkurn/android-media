package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcBottomsheetMenuBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MenuViewHolder(
        itemView: View?,
        private val callback: (MoreMenuUiModel) -> Unit
) : AbstractViewHolder<MoreMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_bottomsheet_menu
    }

    private var binding: ItemMvcBottomsheetMenuBinding? by viewBinding()

    override fun bind(element: MoreMenuUiModel) {
        binding?.apply {
            tvTitle.text = element.title.orEmpty()
            element.icon?.let {
                icMenu.loadImageDrawable(it)
            }

            root.setOnClickListener {
                callback(element)
            }
        }
    }

    interface Listener {
        fun onMoreMenuItemClickListener(menu: MoreMenuUiModel)
    }
}