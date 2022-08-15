package com.tokopedia.play.ui.product

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 23/02/21
 */
open class ProductBasicViewHolder(
    itemView: View,
) : BaseViewHolder(itemView) {

    interface Listener {
        fun onClickProductCard(product: PlayProductUiModel.Product, position: Int)
    }
}