package com.tokopedia.shop.showcase.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageBaseViewHolder

/**
 * Created by Rafli Syam on 30/03/2021
 */
class ShopShowcaseListImageViewHolder(
        itemView: View,
        private val listener: ShopShowcaseListImageListener
) : ShopShowcaseListImageBaseViewHolder(itemView) {

    override fun bind(element: Any) {

        // cast to actual ui model
        val showcaseItem = element as ShopEtalaseUiModel

        // render showcase info
        renderShowcaseMainInfo(showcaseItem)

        // showcase item impressed listener
        ivShowcaseImage?.addOnImpressionListener(
                holder = showcaseItem,
                listener = object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onShowcaseListItemImpressed(element, adapterPosition)
                    }
                }
        )

        // handle showcase item click listener
        setItemShowcaseClickListener {
            listener.onShowcaseListItemSelected(element, adapterPosition)
        }
    }
}

interface ShopShowcaseListImageListener {
    fun onShowcaseListItemSelected(element: ShopEtalaseUiModel, position: Int)
    fun onShowcaseListItemImpressed(element: ShopEtalaseUiModel, position: Int)
}