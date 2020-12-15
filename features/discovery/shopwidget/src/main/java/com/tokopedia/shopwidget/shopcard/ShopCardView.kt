package com.tokopedia.shopwidget.shopcard

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shopwidget.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.shopwidget_shop_card_layout.view.*
import com.tokopedia.unifyprinciples.Typography

class ShopCardView: BaseCustomView {

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

        initCardViewShopCard(shopCardListener)
        initImageShopAvatar(shopCardModel, shopCardListener)
        initImageShopBadge(shopCardModel)
        initShopName(shopCardModel)
        initImageShopReputation(shopCardModel)
        initShopLocation(shopCardModel)
        initButtonSeeShop(shopCardListener)
        initProductPreview(shopCardModel, shopCardListener)
        initShopVoucherLabel(shopCardModel)
        initShopStatus(shopCardModel)

        finishBindShopItem()
    }

    private fun initCardViewShopCard(shopCardListener: ShopCardListener) {
        shopWidgetCardViewShopCard?.setOnClickListener {
            shopCardListener.onItemClicked()
        }
    }

    private fun initImageShopAvatar(shopCardModel: ShopCardModel, shopCardListener: ShopCardListener) {
        shopWidgetImageViewShopAvatar?.let {
            ImageHandler.loadImageCircle2(context, it, shopCardModel.image)
        }

        shopCardModel.impressHolder?.let { impressHolder ->
            shopWidgetImageViewShopAvatar?.addOnImpressionListener(impressHolder) {
                shopCardListener.onItemImpressed()
            }
        }
    }

    private fun initImageShopBadge(shopCardModel: ShopCardModel) {
        shopWidgetImageViewShopBadge?.let { imageViewShopBadge ->
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopCardModel)

            imageViewShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    shopCardModel.isOfficial -> imageViewShopBadge.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.shopwidget_ic_official_store))
                    shopCardModel.isGoldShop -> imageViewShopBadge.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_power_merchant))
                }
            }
        }
    }

    private fun getIsImageShopBadgeVisible(shopCardModel: ShopCardModel): Boolean {
        return shopCardModel.isOfficial
                || shopCardModel.isGoldShop
    }

    private fun initShopName(shopCardModel: ShopCardModel) {
        shopWidgetTextViewShopName?.text = MethodChecker.fromHtml(shopCardModel.name)
    }

    private fun initImageShopReputation(shopCardModel: ShopCardModel) {
        shopWidgetImageViewShopReputation?.let { imageViewShopReputation ->
            ImageHandler.loadImageThumbs(context, imageViewShopReputation, shopCardModel.reputationImageUri)
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
        }

        if (shopCardModel.productList.size > 1) {
            showProductItemPreviewPerItem(
                    shopCardModel.productList[1],
                    shopWidgetImageViewShopItemProductImage2,
                    shopWidgetTextViewShopItemProductPrice2,
                    1,
                    shopCardListener
            )
        }

        if (shopCardModel.productList.size > 2) {
            showProductItemPreviewPerItem(
                    shopCardModel.productList[2],
                    shopWidgetImageViewShopItemProductImage3,
                    shopWidgetTextViewShopItemProductPrice3,
                    2,
                    shopCardListener
            )
        }
    }

    private fun showProductItemPreviewPerItem(
            productPreviewItem: ShopCardModel.ShopItemProduct,
            imageViewShopItemProductImage: AppCompatImageView?,
            textViewShopItemProductPrice: Typography?,
            productPreviewIndex: Int,
            shopCardListener: ShopCardListener
    ) {
        imageViewShopItemProductImage?.let {
            ImageHandler.loadImageFitCenter(context, imageViewShopItemProductImage, productPreviewItem.imageUrl)
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

    private fun finishBindShopItem() {
        setTextViewShopNameMargin()
        setLabelVoucherCashbackMargin()
        setLabelVoucherConstraints()
    }

    private fun setTextViewShopNameMargin() {
        shopWidgetTextViewShopName?.let { textViewShopName ->
            if(textViewShopName.isVisible) {
                setViewMargins(textViewShopName.id, ConstraintSet.START, getTextViewShopNameMarginLeft())
            }
        }
    }

    @DimenRes
    private fun getTextViewShopNameMarginLeft(): Int {
        return shopWidgetImageViewShopBadge?.let { if (it.isVisible) R.dimen.unify_space_2 else R.dimen.unify_space_8 }
                ?: R.dimen.unify_space_8
    }

    private fun setLabelVoucherCashbackMargin() {
        shopWidgetLabelVoucherCashback?.doIfVisible {
            if(shopWidgetLabelVoucherFreeShipping.isNullOrNotVisible) {
                setViewMargins(it.id, ConstraintSet.START, R.dimen.unify_space_0)
            }
        }
    }

    private fun setLabelVoucherConstraints() {
        val topConstraintViewForLabelVoucher = getTopConstraintViewForLabelVoucher()

        topConstraintViewForLabelVoucher?.let {
            setLabelVoucherConstraintTop(it)
        }
    }

    private fun getTopConstraintViewForLabelVoucher(): View? {
        return if (shopWidgetTextViewShopItemProductPrice1?.isVisible == true) {
            shopWidgetTextViewShopItemProductPrice1
        }
        else {
            shopWidgetTextViewShopHasNoProduct
        }
    }

    private fun setLabelVoucherConstraintTop(topConstraintViewForLabelVoucher: View) {
        shopWidgetLabelVoucherFreeShipping?.doIfVisible { labelVoucherFreeShipping ->
            setViewConstraint(
                    labelVoucherFreeShipping.id, ConstraintSet.TOP,
                    topConstraintViewForLabelVoucher.id, ConstraintSet.BOTTOM,
                    R.dimen.unify_space_4
            )
        }

        shopWidgetLabelVoucherCashback?.doIfVisible { labelVoucherCashback ->
            setViewConstraint(
                    labelVoucherCashback.id, ConstraintSet.TOP,
                    topConstraintViewForLabelVoucher.id, ConstraintSet.BOTTOM,
                    R.dimen.unify_space_4
            )
        }
    }

    private fun setViewMargins(@IdRes viewId: Int, anchor: Int, marginDp: Int) {
        applyConstraintSetToConstraintLayoutShopCard { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.setMargin(viewId, anchor, marginPixel)
        }
    }

    private fun setViewConstraint(
            @IdRes startLayoutId: Int,
            startSide: Int,
            @IdRes endLayoutId: Int,
            endSide: Int,
            @DimenRes marginDp: Int
    ) {
        applyConstraintSetToConstraintLayoutShopCard { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.connect(startLayoutId, startSide, endLayoutId, endSide, marginPixel)
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

    private fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return context.resources.getDimensionPixelSize(id)
    }

    private val View?.isNullOrNotVisible: Boolean
        get() = this == null || !this.isVisible

    private fun View.doIfVisible(action: (View) -> Unit) {
        if(this.isVisible) {
            action(this)
        }
    }

    fun getMaxCardElevation() = shopWidgetCardViewShopCard?.maxCardElevation ?: 0f

    fun getRadius() = shopWidgetCardViewShopCard?.radius ?: 0f
}