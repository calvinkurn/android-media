package com.tokopedia.shop.settings.etalase.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleDataModel

/**
 * Created by hendry on 16/08/18.
 */
class ShopEtalaseTitleViewHolder(itemView: View) : AbstractViewHolder<ShopEtalaseTitleDataModel>(itemView) {

    private val tvEtalaseTitle: TextView

    init {
        tvEtalaseTitle = itemView.findViewById(R.id.tvEtalaseTitle)
    }

    override fun bind(shopEtalaseTitleViewModel: ShopEtalaseTitleDataModel) {
        tvEtalaseTitle.text = shopEtalaseTitleViewModel.name
    }

    companion object {

        val LAYOUT = R.layout.item_shop_etalase_title
    }

}
