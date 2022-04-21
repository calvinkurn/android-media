package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel
import kotlinx.android.synthetic.main.item_mvc_bottomsheet_menu.view.*

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

    override fun bind(element: MoreMenuUiModel) {
        with(itemView) {
            tvTitle.text = element.title.orEmpty()
            element.icon?.let {
                icMenu.loadImageDrawable(it)
            }

            setOnClickListener {
                callback(element)
            }
        }
    }

    interface Listener {
        fun onMoreMenuItemClickListener(menu: MoreMenuUiModel)
    }
}