package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.adapter.ShopProductEtalaseAdapter
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseChipItemViewModel

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductEtalaseChipItemViewHolder(
        itemView: View,
        private val shopProductEtalaseListViewHolderListener: ShopProductEtalaseListViewHolder.ShopProductEtalaseChipListViewHolderListener?,
        private var shopProductEtalaseAdapter: ShopProductEtalaseAdapter
) : AbstractViewHolder<ShopProductEtalaseChipItemViewModel>(itemView) {

    init {
        initlayout(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_etalase_chip
    }

    lateinit var textViewEtalaseChip: TextView

    override fun bind(element: ShopProductEtalaseChipItemViewModel) {
        val selectedEtalaseId = shopProductEtalaseAdapter.selectedEtalaseId
        textViewEtalaseChip.text = (MethodChecker.fromHtml(element.etalaseName))
        textViewEtalaseChip.isSelected = selectedEtalaseId.equals(element.etalaseId, ignoreCase = true)
        textViewEtalaseChip.setOnClickListener {
            if(selectedEtalaseId != element.etalaseId)
            shopProductEtalaseListViewHolderListener?.onEtalaseChipClicked(element)
        }
    }

    private fun initlayout(view: View) {
        textViewEtalaseChip = view.findViewById(R.id.text)
    }


}