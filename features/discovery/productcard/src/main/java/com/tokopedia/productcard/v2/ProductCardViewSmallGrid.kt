package com.tokopedia.productcard.v2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * ProductCardView with Small Grid layout.
 */
class ProductCardViewSmallGrid: ProductCardView {

    private var imageShop: ImageView? = null
    private var textViewAddToCart: Typography? = null
    private var labelSoldOut: Label? = null
    private var labelPreOrder: Label? = null
    private var labelWholesale: Label? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_small_grid
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        imageShop = inflatedView.findViewById(R.id.imageShop)
        textViewAddToCart = inflatedView.findViewById(R.id.textViewAddToCart)
        labelSoldOut = inflatedView.findViewById(R.id.labelEmptyStock)
        labelPreOrder= inflatedView.findViewById(R.id.label_pre_order)
        labelWholesale= inflatedView.findViewById(R.id.label_wholesale)
    }

    override fun setProductModel(productCardModel: ProductCardModel, blankSpaceConfig: BlankSpaceConfig) {
        super.setProductModel(productCardModel, blankSpaceConfig)
        initLabelSoldOut(productCardModel.isProductSoldOut)
        initLabelPreOrder(productCardModel.isProductPreOrder)
        initLabelWholesale(productCardModel.isProductWholesale)
    }

    private fun initLabelWholesale(productWholesale: Boolean) {
        labelWholesale?.visibility = if (productWholesale) View.VISIBLE else View.GONE
    }

    private fun initLabelPreOrder(productPreOrder: Boolean) {
        labelPreOrder?.visibility = if (productPreOrder) View.VISIBLE else View.GONE
    }

    private fun initLabelSoldOut(productSoldOut: Boolean) {
        val drawable =  MethodChecker.getDrawable(context, R.drawable.sold_out_label_bg)
        drawable?.let{
            labelSoldOut?.background = it
        }
        labelSoldOut?.visibility = if (productSoldOut) View.VISIBLE else View.GONE
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let { imageShop ->
            ImageHandler.loadImageCircle2(context, imageShop, imageUrl)
        }
    }

    fun setAddToCartVisible(isVisible: Boolean) {
        textViewAddToCart?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setAddToCartOnClickListener(onClickListener: (view: View) -> Unit) {
        textViewAddToCart?.setOnClickListener(onClickListener)
    }
}