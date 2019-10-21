package com.tokopedia.search.result.shop.presentation.viewholder

import android.graphics.Bitmap
import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.support.constraint.ConstraintSet
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.AppCompatImageView
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.utils.doIfVisible
import com.tokopedia.productcard.utils.isNullOrNotVisible
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.listener.ShopListener
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.search_result_shop_card.view.*

internal class ShopItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener
) : AbstractViewHolder<ShopViewModel.ShopItem>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_shop_card
    }

    private var context = itemView.context

    override fun bind(shopViewItem: ShopViewModel.ShopItem?) {
        if(shopViewItem == null) return

        initCardViewShopCard(shopViewItem)
        initImageShopAvatar(shopViewItem)
        initImageShopBadge(shopViewItem)
        initShopName(shopViewItem)
        initImageShopReputation(shopViewItem)
        initShopLocation(shopViewItem)
        initButtonSeeShop(shopViewItem)
        initProductPreview(shopViewItem)
        initShopVoucherLabel(shopViewItem)
        initShopStatus(shopViewItem)

        finishBindShopItem()
    }

    private fun initCardViewShopCard(shopViewItem: ShopViewModel.ShopItem) {
        itemView.cardViewShopCard?.setOnClickListener {
            shopListener.onItemClicked(shopViewItem)
        }
    }

    private fun initImageShopAvatar(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopAvatar?.let {
            val imageViewShopAvatarTarget = createCircleImageViewTarget(it)

            Glide.with(context)
                    .load(shopViewItem.image)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .into(imageViewShopAvatarTarget)
        }
    }

    private fun createCircleImageViewTarget(imageView: ImageView): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                circularBitmapDrawable.isCircular = true
                imageView.setImageDrawable(circularBitmapDrawable)
            }
        }
    }

    private fun initImageShopBadge(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopBadge?.let { imageViewShopBadge ->
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopViewItem)

            imageViewShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    shopViewItem.isOfficial -> imageViewShopBadge.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.search_ic_official_store))
                    shopViewItem.isGoldShop -> imageViewShopBadge.setImageDrawable(GMConstant.getGMDrawable(context))
                }
            }
        }
    }

    private fun getIsImageShopBadgeVisible(shopViewItem: ShopViewModel.ShopItem): Boolean {
        return shopViewItem.isOfficial
                || shopViewItem.isGoldShop
    }

    private fun initShopName(shopViewItem: ShopViewModel.ShopItem) {
        itemView.textViewShopName?.text = MethodChecker.fromHtml(shopViewItem.name)
    }

    private fun initShopLocation(shopViewItem: ShopViewModel.ShopItem) {
        itemView.textViewShopLocation?.text = getShopLocation(shopViewItem)
    }

    private fun getShopLocation(shopViewItem: ShopViewModel.ShopItem): Spanned {
        return MethodChecker.fromHtml(shopViewItem.location + " ")
    }

    private fun initButtonSeeShop(shopViewItem: ShopViewModel.ShopItem) {
        itemView.buttonSeeShop?.setOnClickListener {
            shopListener.onItemClicked(shopViewItem)
        }
    }

    private fun initImageShopReputation(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopReputation?.let { imageViewShopReputation ->
            val imageViewShopReputationTarget = createImageViewShopReputationTarget(imageViewShopReputation)

            Glide.with(context)
                    .load(shopViewItem.reputationImageUri)
                    .dontAnimate()
                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageViewShopReputationTarget)
        }
    }

    private fun createImageViewShopReputationTarget(imageViewShopReputation: ImageView): SimpleTarget<GlideDrawable> {
        return object : SimpleTarget<GlideDrawable>() {
            override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                imageViewShopReputation.setImageDrawable(resource)
            }
        }
    }

    private fun initProductPreview(shopViewItem: ShopViewModel.ShopItem) {
        clearProductPreviewOnClickListener()

        val productPreviewItemSize = shopViewItem.productList.size

        setProductPreviewComponentVisibility(productPreviewItemSize)

        if (productPreviewItemSize > 0) {
            showShopProductItemPreview(shopViewItem)
        }
    }

    private fun clearProductPreviewOnClickListener() {
        itemView.imageViewShopItemProductImage1?.setOnClickListener(null)
        itemView.imageViewShopItemProductImage2?.setOnClickListener(null)
        itemView.imageViewShopItemProductImage3?.setOnClickListener(null)
    }

    private fun setProductPreviewComponentVisibility(productPreviewItemSize: Int) {
        setProductPreviewImagesVisibility(productPreviewItemSize)
        setProductPreviewPriceVisibility(productPreviewItemSize)
        setTextViewNoProductVisibility(productPreviewItemSize)
    }

    private fun setProductPreviewImagesVisibility(productPreviewItemSize: Int) {
        itemView.imageViewShopItemProductImage1?.showWithCondition(productPreviewItemSize > 0)
        itemView.imageViewShopItemProductImage2?.showWithCondition(productPreviewItemSize > 0)
        itemView.imageViewShopItemProductImage3?.showWithCondition(productPreviewItemSize > 0)
    }

    private fun setProductPreviewPriceVisibility(productPreviewItemSize: Int) {
        itemView.textViewShopItemProductPrice1?.showWithCondition(productPreviewItemSize > 0)
        itemView.textViewShopItemProductPrice2?.showWithCondition(productPreviewItemSize > 1)
        itemView.textViewShopItemProductPrice3?.showWithCondition(productPreviewItemSize > 2)
    }

    private fun setTextViewNoProductVisibility(productPreviewItemSize: Int) {
        itemView.textViewShopHasNoProduct?.showWithCondition(productPreviewItemSize == 0)
    }

    private fun showShopProductItemPreview(shopViewItem: ShopViewModel.ShopItem) {
        if (shopViewItem.productList.isNotEmpty()) {
            showProductItemPreviewPerItem(
                    shopViewItem.productList[0],
                    itemView.imageViewShopItemProductImage1,
                    itemView.textViewShopItemProductPrice1
            )
        }

        if (shopViewItem.productList.size > 1) {
            showProductItemPreviewPerItem(
                    shopViewItem.productList[1],
                    itemView.imageViewShopItemProductImage2,
                    itemView.textViewShopItemProductPrice2
            )
        }

        if (shopViewItem.productList.size > 2) {
            showProductItemPreviewPerItem(
                    shopViewItem.productList[2],
                    itemView.imageViewShopItemProductImage3,
                    itemView.textViewShopItemProductPrice3
            )
        }
    }

    private fun showProductItemPreviewPerItem(
            productPreviewItem: ShopViewModel.ShopItem.ShopItemProduct,
            imageViewShopItemProductImage: AppCompatImageView?,
            textViewShopItemProductPrice: Typography?
    ) {
        val imageUrl = productPreviewItem.imageUrl
        val priceFormat = productPreviewItem.priceFormat

        imageViewShopItemProductImage?.let {
            Glide.with(itemView.context)
                    .load(imageUrl)
                    .asBitmap()
                    .centerCrop()
                    .dontAnimate()
                    .into(createRoundedImageViewTarget(it))
        }

        imageViewShopItemProductImage?.setOnClickListener {
            shopListener.onProductItemClicked(productPreviewItem)
        }

        textViewShopItemProductPrice?.text = MethodChecker.fromHtml(priceFormat)
    }

    private fun createRoundedImageViewTarget(imageView: ImageView): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap) {
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                roundedBitmapDrawable.cornerRadius = 6f
                imageView.setImageDrawable(roundedBitmapDrawable)
            }
        }
    }

    private fun initShopVoucherLabel(shopViewItem: ShopViewModel.ShopItem) {
        initShopVoucherFreeShipping(shopViewItem)
        initShopVoucherCashback(shopViewItem)
    }

    private fun initShopVoucherFreeShipping(shopViewItem: ShopViewModel.ShopItem) {
        itemView.labelVoucherFreeShipping?.showWithCondition(shopViewItem.voucher.freeShipping)
    }

    private fun initShopVoucherCashback(shopViewItem: ShopViewModel.ShopItem) {
        itemView.labelVoucherCashback?.shouldShowWithAction(shopViewItem.voucher.cashback.cashbackValue > 0) {
            itemView.labelVoucherCashback?.text = getShopVoucherCashbackText(shopViewItem.voucher.cashback)
        }
    }

    private fun getShopVoucherCashbackText(voucherCashback: ShopViewModel.ShopItem.ShopItemVoucher.ShopItemVoucherCashback): String {
        return if (voucherCashback.isPercentage) {
            getShopVoucherCashbackValuePercentage(voucherCashback.cashbackValue)
        }
        else {
            getShopVoucherCashbackValueNonPercentage(voucherCashback.cashbackValue)
        }
    }

    private fun getShopVoucherCashbackValuePercentage(cashbackValue: Int): String {
        return getString(R.string.shop_voucher_cashback_percentage, cashbackValue.toString())
    }

    private fun getShopVoucherCashbackValueNonPercentage(cashbackValue: Int): String {
        val cashbackValueString =
                if (cashbackValue % 1000 == 0) getString(R.string.shop_voucher_cashback_rb, (cashbackValue / 1000).toString())
                else cashbackValue.toString()

        return getString(R.string.shop_voucher_cashback_not_percentage, cashbackValueString)
    }

    private fun initShopStatus(shopViewItem: ShopViewModel.ShopItem) {
        when {
            shopViewItem.isClosed -> showShopStatus(getString(R.string.shop_status_closed))
            shopViewItem.isModerated -> showShopStatus(getString(R.string.shop_status_moderated))
            shopViewItem.isInactive -> showShopStatus(getString(R.string.shop_status_inactive))
            else -> hideShopStatus()
        }
    }

    private fun showShopStatus(shopStatus: String) {
        itemView.constraintLayoutShopStatus?.visible()
        itemView.textViewShopStatus?.text = shopStatus
    }

    private fun hideShopStatus() {
        itemView.constraintLayoutShopStatus?.gone()
    }

    private fun finishBindShopItem() {
        setTextViewShopNameMargin()
        setLabelVoucherCashbackMargin()
        setLabelVoucherConstraints()
    }

    private fun setTextViewShopNameMargin() {
        itemView.textViewShopName?.let { textViewShopName ->
            if(textViewShopName.isVisible) {
                setViewMargins(textViewShopName.id, ConstraintSet.START, getTextViewShopNameMarginLeft())
            }
        }
    }

    @DimenRes
    private fun getTextViewShopNameMarginLeft(): Int {
        return itemView.imageViewShopBadge?.let { if (it.isVisible) com.tokopedia.design.R.dimen.dp_2 else com.tokopedia.design.R.dimen.dp_8 }
                ?: com.tokopedia.design.R.dimen.dp_8
    }

    private fun setLabelVoucherCashbackMargin() {
        itemView.labelVoucherCashback?.doIfVisible {
            if(itemView.labelVoucherFreeShipping.isNullOrNotVisible) {
                setViewMargins(it.id, ConstraintSet.START, com.tokopedia.design.R.dimen.dp_0)
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
        return if (itemView.textViewShopItemProductPrice1?.isVisible == true) {
            itemView.textViewShopItemProductPrice1
        }
        else {
            itemView.textViewShopHasNoProduct
        }
    }

    private fun setLabelVoucherConstraintTop(topConstraintViewForLabelVoucher: View) {
        itemView.labelVoucherFreeShipping?.doIfVisible { labelVoucherFreeShipping ->
            setViewConstraint(
                    labelVoucherFreeShipping.id, ConstraintSet.TOP,
                    topConstraintViewForLabelVoucher.id, ConstraintSet.BOTTOM,
                    com.tokopedia.design.R.dimen.dp_4
            )
        }

        itemView.labelVoucherCashback?.doIfVisible { labelVoucherCashback ->
            setViewConstraint(
                    labelVoucherCashback.id, ConstraintSet.TOP,
                    topConstraintViewForLabelVoucher.id, ConstraintSet.BOTTOM,
                    com.tokopedia.design.R.dimen.dp_4
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
        itemView.constraintLayoutShopCard?.let {
            val constraintSet = ConstraintSet()

            constraintSet.clone(it)
            configureConstraintSet(constraintSet)
            constraintSet.applyTo(it)
        }
    }

    private fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return context.resources.getDimensionPixelSize(id)
    }
}