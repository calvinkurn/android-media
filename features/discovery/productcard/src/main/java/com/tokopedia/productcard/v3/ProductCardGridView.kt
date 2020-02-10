package com.tokopedia.productcard.v3

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.productcard.utils.toLabelType
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*

class ProductCardGridView: BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_grid_layout, this)
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct.loadImage(productCardModel.productImageUrl)

        initLabelProductStatus(productCardModel)

        textTopAds.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        imageThreeDots.showWithCondition(productCardModel.hasOptions)
    }

    private fun initLabelProductStatus(productCardModel: ProductCardModel) {
        val labelProductStatusModel = productCardModel.getLabelProductStatus()

        if (labelProductStatusModel == null) hideLabelStatus()
        else showLabelStatus(labelProductStatusModel)
    }

    private fun hideLabelStatus() {
        labelProductStatus.hide()
    }

    private fun showLabelStatus(labelProductStatusModel: ProductCardModel.Label) {
        labelProductStatus.shouldShowWithAction(labelProductStatusModel.title.isNotEmpty()) {
            it.text = labelProductStatusModel.title
            it.setLabelType(labelProductStatusModel.type.toLabelType())
        }
    }
}