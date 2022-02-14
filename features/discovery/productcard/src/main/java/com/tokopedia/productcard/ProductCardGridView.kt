package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_footer_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*

class ProductCardGridView: BaseCustomView, IProductCardView {

    private val cartExtension = ProductCardCartExtension(this)

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

    override fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImage(productCardModel.productImageUrl)

        val isShowCampaign = productCardModel.isShowLabelCampaign()
        renderLabelCampaign(
                isShowCampaign,
                labelCampaignBackground,
                textViewLabelCampaign,
                productCardModel
        )

        val isShowBestSeller = productCardModel.isShowLabelBestSeller()
        renderLabelBestSeller(
            isShowBestSeller,
            labelBestSeller,
            productCardModel
        )

        val isShowCategorySide = productCardModel.isShowLabelCategorySide()
        renderLabelBestSellerCategorySide(
            isShowCategorySide,
            textCategorySide,
            productCardModel
        )

        val isShowCategoryBottom = productCardModel.isShowLabelCategoryBottom()
        renderLabelBestSellerCategoryBottom(
            isShowCategoryBottom,
            textCategoryBottom,
            productCardModel
        )

        outOfStockOverlay?.showWithCondition(productCardModel.isOutOfStock)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel, productCardModel.isWideContent)

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        renderProductCardFooter(productCardModel, isProductCardList = false)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        cartExtension.setProductModel(productCardModel)

        constraintLayoutProductCard?.post {
            imageThreeDots?.expandTouchArea(
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            )
        }
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        imageThreeDots?.setOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        cartExtension.addToCartClickListener = addToCartClickListener
    }

    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }

    fun setSimilarProductClickListener(similarProductClickListener: (View) -> Unit) {
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        buttonSimilarProduct?.setOnClickListener(similarProductClickListener)
    }

    fun setAddVariantClickListener(addVariantClickListener: (View) -> Unit) {
        buttonAddVariant?.setOnClickListener(addVariantClickListener)
    }

    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        buttonNotify?.setOnClickListener(notifyMeClickListener)
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear()
        imageFreeOngkirPromo?.glideClear()
        labelCampaignBackground?.glideClear()
        cartExtension.clear()
    }

    override fun getThreeDotsButton(): View? = imageThreeDots

    override fun getNotifyMeButton(): UnifyButton? = buttonNotify

    override fun getShopBadgeView(): View? = imageShopBadge

}