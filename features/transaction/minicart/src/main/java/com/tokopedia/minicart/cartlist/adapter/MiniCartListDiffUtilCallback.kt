package com.tokopedia.minicart.cartlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartGwpGiftUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProgressiveInfoUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartSeparatorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel

class MiniCartListDiffUtilCallback(
    private val oldList: List<Any>,
    private val newList: List<Any>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is MiniCartAccordionUiModel && newItem is MiniCartAccordionUiModel -> oldItem == newItem
            oldItem is MiniCartProductUiModel && newItem is MiniCartProductUiModel -> oldItem == newItem
            oldItem is MiniCartSeparatorUiModel && newItem is MiniCartSeparatorUiModel -> oldItem == newItem
            oldItem is MiniCartShopUiModel && newItem is MiniCartShopUiModel -> oldItem == newItem
            oldItem is MiniCartTickerErrorUiModel && newItem is MiniCartTickerErrorUiModel -> oldItem == newItem
            oldItem is MiniCartTickerWarningUiModel && newItem is MiniCartTickerWarningUiModel -> oldItem == newItem
            oldItem is MiniCartUnavailableHeaderUiModel && newItem is MiniCartUnavailableHeaderUiModel -> oldItem == newItem
            oldItem is MiniCartUnavailableReasonUiModel && newItem is MiniCartUnavailableReasonUiModel -> oldItem == newItem
            oldItem is MiniCartProgressiveInfoUiModel && newItem is MiniCartProgressiveInfoUiModel -> oldItem == newItem
            oldItem is MiniCartGwpGiftUiModel && newItem is MiniCartGwpGiftUiModel -> oldItem == newItem
            else -> false
        }
    }
}
