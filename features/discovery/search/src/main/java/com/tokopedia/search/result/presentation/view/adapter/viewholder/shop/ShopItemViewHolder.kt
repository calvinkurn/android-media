package com.tokopedia.search.result.presentation.view.adapter.viewholder.shop

import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.support.constraint.ConstraintSet
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.common.constants.SearchConstant.SearchShop.SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.utils.doIfVisible
import com.tokopedia.productcard.utils.isNullOrNotVisible
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.adapter.ShopProductItemAdapter
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.ShopProductItemDecoration
import com.tokopedia.search.result.presentation.view.listener.ShopListener
import kotlinx.android.synthetic.main.search_result_shop_card.view.*

class ShopItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener
) : AbstractViewHolder<ShopViewModel.ShopItem>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.search_result_shop_card
    }

    private val context = itemView.context

    override fun bind(shopViewItem: ShopViewModel.ShopItem?) {
        if(shopViewItem == null) return

        initCardViewShopCard(shopViewItem)
        initImageShopAvatar(shopViewItem)
        initImageShopBadge(shopViewItem)
        initShopName(shopViewItem)
        initImageShopReputation(shopViewItem)
        initShopLocation(shopViewItem)
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
            ImageHandler.loadImageCircle2(context, itemView.imageViewShopAvatar, shopViewItem.image)
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

    private fun initImageShopReputation(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopReputation?.let { imageViewShopReputation ->
//            ImageHandler.loadImageFitCenter(imageViewShopReputation.context, imageViewShopReputation, shopViewItem.reputationImageUri)
            ImageHandler.loadImageThumbs(context, imageViewShopReputation, shopViewItem.reputationImageUri)
        }
    }

    private fun initProductPreview(shopViewItem: ShopViewModel.ShopItem) {
        val isProductListVisible = shopViewItem.productList.isNotEmpty()

        itemView.recyclerViewShopProductItem?.showWithCondition(isProductListVisible)
        itemView.textViewShopHasNoProduct?.showWithCondition(!isProductListVisible)

        if (shopViewItem.productList.isNotEmpty()) {
            showShopProductItemPreview(shopViewItem)
        }
    }

    private fun showShopProductItemPreview(shopViewItem: ShopViewModel.ShopItem) {
        itemView.recyclerViewShopProductItem?.let { recyclerViewShopProductItem ->
            initShopProductItemRecyclerView(recyclerViewShopProductItem, shopViewItem)
        }
    }

    private fun initShopProductItemRecyclerView(recyclerViewShopProductItem: RecyclerView, shopViewItem: ShopViewModel.ShopItem) {
        recyclerViewShopProductItem.adapter = createRecyclerViewShopProductItemAdapter(shopViewItem.productList)

        recyclerViewShopProductItem.layoutManager = createRecyclerViewShopProductItemLayoutManager()

        if (!hasItemDecoration(recyclerViewShopProductItem)) {
            recyclerViewShopProductItem.addItemDecoration(createRecyclerViewShopProductItemDecoration())
        }
    }

    private fun createRecyclerViewShopProductItemAdapter(
            productList: List<ShopViewModel.ShopItem.ShopItemProduct>
    ): RecyclerView.Adapter<ShopProductItemViewHolder> {
        return ShopProductItemAdapter(context, createShopItemProductPreviewList(productList), shopListener)
    }

    private fun createShopItemProductPreviewList(
            productList: List<ShopViewModel.ShopItem.ShopItemProduct>
    ): List<ShopViewModel.ShopItem.ShopItemProduct> {
        val productPreviewList = productList.take(SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT).toMutableList()

        while(productPreviewList.size < SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT) {
            productPreviewList += ShopViewModel.ShopItem.ShopItemProduct()
        }

        return productPreviewList
    }

    private fun createRecyclerViewShopProductItemLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, SHOP_PRODUCT_PREVIEW_ITEM_MAX_COUNT, GridLayoutManager.VERTICAL, false)
    }

    private fun hasItemDecoration(recyclerViewShopProductItem: RecyclerView): Boolean {
        return recyclerViewShopProductItem.itemDecorationCount != 0
    }

    private fun createRecyclerViewShopProductItemDecoration(): RecyclerView.ItemDecoration {
        return ShopProductItemDecoration(getDimensionPixelSize(R.dimen.dp_8))
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
        return itemView.imageViewShopBadge?.let { if (it.isVisible) R.dimen.dp_2 else R.dimen.dp_8 }
                ?: R.dimen.dp_8
    }

    private fun setLabelVoucherCashbackMargin() {
        itemView.labelVoucherCashback?.doIfVisible {
            if(itemView.labelVoucherFreeShipping.isNullOrNotVisible) {
                setViewMargins(it.id, ConstraintSet.LEFT, R.dimen.dp_0)
                setViewMargins(it.id, ConstraintSet.START, R.dimen.dp_0)
            }
        }
    }

    private fun setLabelVoucherConstraints() {
        val topConstraintViewForLabelVoucher = getTopConstraintViewForLabelVoucher()

        topConstraintViewForLabelVoucher?.let {
            setLabelVoucherConstraintTop(it)
        }
    }

    private fun setLabelVoucherConstraintTop(topConstraintViewForLabelVoucher: View) {
        itemView.labelVoucherFreeShipping?.doIfVisible { labelVoucherFreeShipping ->
            setViewConstraint(
                    labelVoucherFreeShipping.id, ConstraintSet.TOP,
                    topConstraintViewForLabelVoucher.id, ConstraintSet.BOTTOM,
                    R.dimen.dp_4
            )
        }

        itemView.labelVoucherCashback?.doIfVisible { labelVoucherCashback ->
            setViewConstraint(
                    labelVoucherCashback.id, ConstraintSet.TOP,
                    topConstraintViewForLabelVoucher.id, ConstraintSet.BOTTOM,
                    R.dimen.dp_4
            )
        }
    }

    private fun getTopConstraintViewForLabelVoucher(): View? {
        return if (itemView.recyclerViewShopProductItem?.isVisible == true) {
            itemView.recyclerViewShopProductItem
        }
        else {
            itemView.textViewShopHasNoProduct
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