package com.tokopedia.shopwidget.shopcard

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.shopwidget.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_SHADOW
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlin.LazyThreadSafetyMode.NONE
import com.tokopedia.gm.common.R as gmcommonR
import com.tokopedia.media.loader.R as medialoaderR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopCardView : BaseCustomView {
    private val shopWidgetCardViewShopCard: CardUnify2? by lazy(NONE) {
        findViewById(R.id.shopWidgetCardViewShopCard)
    }
    private val shopWidgetConstraintLayoutShopCard: ConstraintLayout? by lazy(NONE) {
        findViewById(R.id.shopWidgetConstraintLayoutShopCard)
    }
    private val shopWidgetImageViewShopAvatar: ImageView? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewShopAvatar)
    }
    private val shopWidgetButtonSeeShop: UnifyButton by lazy(NONE) {
        findViewById(R.id.shopWidgetButtonSeeShop)
    }
    private val shopWidgetImageViewShopBadge: ImageView? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewShopBadge)
    }
    private val shopWidgetTextViewShopName: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopName)
    }
    private val shopWidgetTextViewShopLocation: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopLocation)
    }
    private val shopWidgetImageViewShopReputation: ImageView? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewShopReputation)
    }
    private val shopWidgetLabelVoucherFreeShipping: Label? by lazy(NONE) {
        findViewById(R.id.shopWidgetLabelVoucherFreeShipping)
    }
    private val shopWidgetLabelVoucherCashback: Label? by lazy(NONE) {
        findViewById(R.id.shopWidgetLabelVoucherCashback)
    }
    private val shopWidgetImageViewShopItemProductImage1: AppCompatImageView? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewShopItemProductImage1)
    }
    private val shopWidgetTextViewShopItemProductPrice1: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopItemProductPrice1)
    }
    private val shopWidgetImageViewShopItemProductImage2: AppCompatImageView? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewShopItemProductImage2)
    }
    private val shopWidgetTextViewShopItemProductPrice2: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopItemProductPrice2)
    }
    private val shopWidgetImageViewShopItemProductImage3: AppCompatImageView? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewShopItemProductImage3)
    }
    private val shopWidgetTextViewShopItemProductPrice3: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopItemProductPrice3)
    }
    private val shopWidgetTextViewShopHasNoProduct: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopHasNoProduct)
    }
    private val shopWidgetConstraintLayoutShopStatus: ConstraintLayout? by lazy(NONE) {
        findViewById(R.id.shopWidgetConstraintLayoutShopStatus)
    }
    private val shopWidgetTextViewShopStatus: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetTextViewShopStatus)
    }

    private val shopWidgetImageViewAdsText: Typography? by lazy(NONE) {
        findViewById(R.id.shopWidgetImageViewAdsText)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.shopwidget_shop_card_layout, this)
    }

    fun setShopCardModel(shopCardModel: ShopCardModel?, shopCardListener: ShopCardListener) {
        shopCardModel ?: return
        val isReimagine = shopCardModel.isReimagine
        initCardStyle(isReimagine)
        initCardViewShopCard(shopCardListener)
        initImageShopAvatar(shopCardModel, shopCardListener)
        initImageShopBadge(shopCardModel, isReimagine)
        initShopName(shopCardModel)
        initImageShopReputation(shopCardModel)
        initShopLocation(shopCardModel)
        initButtonSeeShop(shopCardListener)
        initProductPreview(shopCardModel, shopCardListener)
        initShopVoucherLabel(shopCardModel)
        initShopStatus(shopCardModel)
        initShopAdsText(isReimagine)
    }

    private fun initCardStyle(isReimagine: Boolean) {
        if (isReimagine) {
            renderShopCardReimagine()
        } else {
            renderShopCardControl()
        }
    }
    private fun initCardViewShopCard(shopCardListener: ShopCardListener) {
        shopWidgetCardViewShopCard?.setOnClickListener {
            shopCardListener.onItemClicked()
        }
    }

    private fun initImageShopAvatar(shopCardModel: ShopCardModel, shopCardListener: ShopCardListener) {
        shopWidgetImageViewShopAvatar?.loadImageCircle(shopCardModel.image)

        shopCardModel.impressHolder?.let { impressHolder ->
            shopWidgetImageViewShopAvatar?.addOnImpressionListener(impressHolder) {
                shopCardListener.onItemImpressed()
            }
        }
    }

    private fun initImageShopBadge(shopCardModel: ShopCardModel, isReimagine: Boolean= false) {
        shopWidgetImageViewShopBadge?.let { imageViewShopBadge ->
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopCardModel)

            imageViewShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    shopCardModel.isOfficial -> imageViewShopBadge.renderImageShopBadgeOfficialStore(isReimagine)
                    shopCardModel.isPMPro -> imageViewShopBadge.loadImage(R.drawable.shopwidget_ic_pm_pro)
                    shopCardModel.isGoldShop -> imageViewShopBadge.loadImage(gmcommonR.drawable.ic_power_merchant)
                }
            }
        }
    }

    private fun ImageView.renderImageShopBadgeOfficialStore(isReimagine: Boolean){
        if (isReimagine) {
            this.loadImage(gmcommonR.drawable.ic_official_store_product)
        } else {
            this.loadImage(R.drawable.shopwidget_ic_official_store)
        }
    }

    private fun getIsImageShopBadgeVisible(shopCardModel: ShopCardModel): Boolean {
        return shopCardModel.isOfficial
                || shopCardModel.isPMPro
                || shopCardModel.isGoldShop
    }

    private fun initShopName(shopCardModel: ShopCardModel) {
        shopWidgetTextViewShopName?.text = MethodChecker.fromHtml(shopCardModel.name)

        setShopNameTopMargin(shopCardModel)
    }

    private fun setShopNameTopMargin(shopCardModel: ShopCardModel) {
        val isShopReputationVisible = shopCardModel.reputationImageUri.isNotEmpty()
        val topMarginDp = if (isShopReputationVisible) 0 else 5
        setViewMargins(R.id.shopWidgetTextViewShopName, ConstraintSet.TOP, topMarginDp)
    }

    private fun initImageShopReputation(shopCardModel: ShopCardModel) {
        shopWidgetImageViewShopReputation?.shouldShowWithAction(shopCardModel.reputationImageUri.isNotEmpty()) {
            shopWidgetImageViewShopReputation?.loadIcon(shopCardModel.reputationImageUri)
        }
    }

    private fun initShopLocation(shopCardModel: ShopCardModel) {
        shopWidgetTextViewShopLocation?.text = getShopLocation(shopCardModel)
    }

    private fun getShopLocation(shopCardModel: ShopCardModel): Spanned {
        return MethodChecker.fromHtml(shopCardModel.location + " ")
    }

    private fun initButtonSeeShop(shopCardListener: ShopCardListener) {
        shopWidgetButtonSeeShop?.setOnClickListener {
            shopCardListener.onItemClicked()
        }
    }

    private fun initProductPreview(shopCardModel: ShopCardModel, shopCardListener: ShopCardListener) {
        clearProductPreviewOnClickListener()

        val productPreviewItemSize = shopCardModel.productList.size

        setProductPreviewComponentVisibility(productPreviewItemSize)

        if (productPreviewItemSize > 0) {
            showShopProductItemPreview(shopCardModel, shopCardListener)
        }
    }

    private fun clearProductPreviewOnClickListener() {
        shopWidgetImageViewShopItemProductImage1?.setOnClickListener(null)
        shopWidgetImageViewShopItemProductImage2?.setOnClickListener(null)
        shopWidgetImageViewShopItemProductImage3?.setOnClickListener(null)
    }

    private fun setProductPreviewComponentVisibility(productPreviewItemSize: Int) {
        setProductPreviewImagesVisibility(productPreviewItemSize)
        setProductPreviewPriceVisibility(productPreviewItemSize)
        setTextViewNoProductVisibility(productPreviewItemSize)
    }

    private fun setProductPreviewImagesVisibility(productPreviewItemSize: Int) {
        shopWidgetImageViewShopItemProductImage1?.showWithCondition(productPreviewItemSize > 0)
        shopWidgetImageViewShopItemProductImage2?.showWithCondition(productPreviewItemSize > 0)
        shopWidgetImageViewShopItemProductImage3?.showWithCondition(productPreviewItemSize > 0)
    }

    private fun setProductPreviewPriceVisibility(productPreviewItemSize: Int) {
        shopWidgetTextViewShopItemProductPrice1?.showWithCondition(productPreviewItemSize > 0)
        shopWidgetTextViewShopItemProductPrice2?.showWithCondition(productPreviewItemSize > 1)
        shopWidgetTextViewShopItemProductPrice3?.showWithCondition(productPreviewItemSize > 2)
    }

    private fun setTextViewNoProductVisibility(productPreviewItemSize: Int) {
        shopWidgetTextViewShopHasNoProduct?.showWithCondition(productPreviewItemSize == 0)
    }

    private fun showShopProductItemPreview(shopCardModel: ShopCardModel, shopCardListener: ShopCardListener) {
        if (shopCardModel.productList.isNotEmpty()) {
            showProductItemPreviewPerItem(
                    shopCardModel.productList[0],
                    shopWidgetImageViewShopItemProductImage1,
                    shopWidgetTextViewShopItemProductPrice1,
                    0,
                    shopCardListener
            )
        } else {
            clearProductPreviewItem(shopWidgetImageViewShopItemProductImage1, shopWidgetTextViewShopItemProductPrice1)
        }

        if (shopCardModel.productList.size > 1) {
            showProductItemPreviewPerItem(
                    shopCardModel.productList[1],
                    shopWidgetImageViewShopItemProductImage2,
                    shopWidgetTextViewShopItemProductPrice2,
                    1,
                    shopCardListener
            )
        } else {
            clearProductPreviewItem(shopWidgetImageViewShopItemProductImage2, shopWidgetTextViewShopItemProductPrice2)
        }

        if (shopCardModel.productList.size > 2) {
            showProductItemPreviewPerItem(
                    shopCardModel.productList[2],
                    shopWidgetImageViewShopItemProductImage3,
                    shopWidgetTextViewShopItemProductPrice3,
                    2,
                    shopCardListener
            )
        } else {
            clearProductPreviewItem(shopWidgetImageViewShopItemProductImage3, shopWidgetTextViewShopItemProductPrice3)
        }
    }

    private fun showProductItemPreviewPerItem(
            productPreviewItem: ShopCardModel.ShopItemProduct,
            imageViewShopItemProductImage: AppCompatImageView?,
            textViewShopItemProductPrice: Typography?,
            productPreviewIndex: Int,
            shopCardListener: ShopCardListener
    ) {
        imageViewShopItemProductImage?.loadImageRounded(productPreviewItem.imageUrl, 6.toPx().toFloat()) {
            setPlaceHolder(medialoaderR.drawable.media_placeholder_grey)
        }

        productPreviewItem.impressHolder?.let { impressHolder ->
            imageViewShopItemProductImage?.addOnImpressionListener(impressHolder) {
                shopCardListener.onProductItemImpressed(productPreviewIndex)
            }
        }

        imageViewShopItemProductImage?.setOnClickListener {
            shopCardListener.onProductItemClicked(productPreviewIndex)
        }

        textViewShopItemProductPrice?.text = MethodChecker.fromHtml(productPreviewItem.priceFormat)
    }

    private fun clearProductPreviewItem(
            imageViewShopItemProductImage: AppCompatImageView?,
            textViewShopItemProductPrice: Typography?,
    ) {
        imageViewShopItemProductImage?.setImageResource(unifyprinciplesR.color.Unify_NN50)
        textViewShopItemProductPrice?.text = ""
    }

    private fun initShopVoucherLabel(shopCardModel: ShopCardModel) {
        initShopVoucherFreeShipping(shopCardModel)
        initShopVoucherCashback(shopCardModel)
    }

    private fun initShopVoucherFreeShipping(shopCardModel: ShopCardModel) {
        shopWidgetLabelVoucherFreeShipping?.showWithCondition(shopCardModel.voucher.freeShipping)
    }

    private fun initShopVoucherCashback(shopCardModel: ShopCardModel) {
        shopWidgetLabelVoucherCashback?.shouldShowWithAction(shopCardModel.voucher.cashback.cashbackValue > 0) {
            shopWidgetLabelVoucherCashback?.text = getShopVoucherCashbackText(shopCardModel.voucher.cashback)
        }
    }

    private fun getShopVoucherCashbackText(voucherCashback: ShopCardModel.ShopItemVoucherCashback): String {
        return if (voucherCashback.isPercentage) {
            getShopVoucherCashbackValuePercentage(voucherCashback.cashbackValue)
        }
        else {
            getShopVoucherCashbackValueNonPercentage(voucherCashback.cashbackValue)
        }
    }

    private fun getShopVoucherCashbackValuePercentage(cashbackValue: Int): String {
        return context.getString(R.string.shopwidget_shop_voucher_cashback_percentage, cashbackValue.toString())
    }

    private fun getShopVoucherCashbackValueNonPercentage(cashbackValue: Int): String {
        val cashbackValueString =
                if (cashbackValue % 1000 == 0) context.getString(R.string.shopwidget_shop_voucher_cashback_rb, (cashbackValue / 1000).toString())
                else cashbackValue.toString()

        return context.getString(R.string.shopwidget_shop_voucher_cashback_not_percentage, cashbackValueString)
    }

    private fun initShopStatus(shopCardModel: ShopCardModel) {
        when {
            shopCardModel.isClosed -> showShopStatus(context.getString(R.string.shopwidget_shop_status_closed))
            shopCardModel.isModerated -> showShopStatus(context.getString(R.string.shopwidget_shop_status_moderated))
            shopCardModel.isInactive -> showShopStatus(context.getString(R.string.shopwidget_shop_status_inactive))
            else -> hideShopStatus()
        }
    }

    private fun showShopStatus(shopStatus: String) {
        shopWidgetConstraintLayoutShopStatus?.visible()
        shopWidgetTextViewShopStatus?.text = shopStatus
    }

    private fun hideShopStatus() {
        shopWidgetConstraintLayoutShopStatus?.gone()
    }

    private fun setViewMargins(@IdRes viewId: Int, anchor: Int, marginDp: Int) {
        applyConstraintSetToConstraintLayoutShopCard { constraintSet ->
            val marginPixel = marginDp.toPx()
            constraintSet.setMargin(viewId, anchor, marginPixel)
        }
    }

    private fun applyConstraintSetToConstraintLayoutShopCard(
            configureConstraintSet: (constraintSet: ConstraintSet) -> Unit
    ) {
        shopWidgetConstraintLayoutShopCard?.let {
            val constraintSet = ConstraintSet()

            constraintSet.clone(it)
            configureConstraintSet(constraintSet)
            constraintSet.applyTo(it)
        }
    }

    fun getMaxCardElevation() = shopWidgetCardViewShopCard?.maxCardElevation ?: 0f

    fun getRadius() = shopWidgetCardViewShopCard?.radius ?: 0f

    private fun renderShopCardReimagine() {
        shopWidgetCardViewShopCard?.apply {
            cardType = TYPE_BORDER
            val rootView = this as CardView
            rootView.preventCornerOverlap = true
            rootView.useCompatPadding = false
        }
    }

    private fun renderShopCardControl() {
        shopWidgetCardViewShopCard?.apply {
            cardType = TYPE_SHADOW
            val rootView = this as CardView
            rootView.preventCornerOverlap = false
            rootView.useCompatPadding = true
        }
    }

    private fun initShopAdsText(isReimagine: Boolean) {
        if (isReimagine) {
            shopWidgetImageViewAdsText?.visible()
            shopWidgetTextViewShopLocation?.gone()
        } else {
            shopWidgetImageViewAdsText?.gone()
            shopWidgetTextViewShopLocation?.visible()
        }
    }
}
