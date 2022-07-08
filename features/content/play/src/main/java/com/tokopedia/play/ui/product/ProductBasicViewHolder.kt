package com.tokopedia.play.ui.product

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 23/02/21
 */
open class ProductBasicViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    interface Listener {
        fun onClickProductCard(product: PlayProductUiModel.Product, position: Int)
    }
}