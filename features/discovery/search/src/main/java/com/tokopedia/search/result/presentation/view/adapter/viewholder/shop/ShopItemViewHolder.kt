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
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
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

        private const val KEY_SHOP_IS_GOLD = 1
        private const val KEY_SHOP_IS_INACTIVE = "4"
        private const val KEY_SHOP_IS_CLOSED = "2"
        private const val SHOP_PRODUCT_ITEM_COUNT = 3
    }

    private val context = itemView.context

    override fun bind(shopViewItem: ShopViewModel.ShopItem?) {
        if(shopViewItem == null) return

        initCardViewShopCard(shopViewItem)
        initImageShopAvatar(shopViewItem)
        initImageShopBadge(shopViewItem)
        initShopName(shopViewItem)
        initShopLocation(shopViewItem)
        initImageShopReputation(shopViewItem)
        initTextShopCredibilityInfo(shopViewItem)
        initProductPreview(shopViewItem)

        finishBindShopItem()
    }

    private fun initCardViewShopCard(shopViewItem: ShopViewModel.ShopItem) {
        itemView.cardViewShopCard?.setOnClickListener {
            shopListener.onItemClicked(shopViewItem)
        }
    }

    private fun initImageShopAvatar(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopAvatar?.let {
            ImageHandler.loadImageCircle2(context, itemView.imageViewShopAvatar, shopViewItem.shopImage)
        }
    }

    private fun initImageShopBadge(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopBadge?.let { imageViewShopBadge ->
            when {
                shopViewItem.isOfficial -> imageViewShopBadge.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.search_ic_official_store))
                shopViewItem.shopGoldShop == KEY_SHOP_IS_GOLD -> imageViewShopBadge.setImageDrawable(GMConstant.getGMDrawable(context))
                else -> imageViewShopBadge.visibility = View.GONE
            }
        }
    }

    private fun initShopName(shopViewItem: ShopViewModel.ShopItem) {
        itemView.textViewShopName?.text = MethodChecker.fromHtml(shopViewItem.shopName)
    }

    private fun initShopLocation(shopViewItem: ShopViewModel.ShopItem) {
        itemView.textViewShopLocation?.text = getShopLocation(shopViewItem)
    }

    private fun getShopLocation(shopViewItem: ShopViewModel.ShopItem): Spanned {
        return MethodChecker.fromHtml(shopViewItem.shopLocation + " |")
    }

    private fun initImageShopReputation(shopViewItem: ShopViewModel.ShopItem) {
        itemView.imageViewShopReputation?.let { imageViewShopReputation ->
            ImageHandler.loadImageFitCenter(context, imageViewShopReputation, shopViewItem.reputationImageUri)
        }
    }

    private fun initTextShopCredibilityInfo(shopViewItem: ShopViewModel.ShopItem) {
        itemView.textViewShopCredibilityInfo?.text = getShopCredibilityInfo(shopViewItem)
    }

    private fun getShopCredibilityInfo(shopViewItem: ShopViewModel.ShopItem): String {
        return when {
            shopViewItem.shopTotalTransaction != "" -> {
                "| " + context.getString(R.string.shop_total_transaction, shopViewItem.shopTotalTransaction)
            }
            shopViewItem.shopTotalFavorite != "" -> {
                "| " + context.getString(R.string.shop_total_favorite, shopViewItem.shopTotalFavorite)
            }
            else -> {
                ""
            }
        }
    }

    private fun initProductPreview(shopViewItem: ShopViewModel.ShopItem) {
        if (shopViewItem.shopItemProductList.isNotEmpty()) {
            showShopProductItemPreview(shopViewItem)
        } else {
            showTextShopHasNoProduct()
        }
    }

    private fun showShopProductItemPreview(shopViewItem: ShopViewModel.ShopItem) {
        itemView.recyclerViewShopProductItem?.let { recyclerViewShopProductItem ->
            initShopProductItemRecyclerView(recyclerViewShopProductItem, shopViewItem)
        }
    }

    private fun initShopProductItemRecyclerView(recyclerViewShopProductItem: RecyclerView, shopViewItem: ShopViewModel.ShopItem) {
        recyclerViewShopProductItem.visibility = View.VISIBLE

        recyclerViewShopProductItem.adapter = ShopProductItemAdapter(
                context, shopViewItem.shopItemProductList, SHOP_PRODUCT_ITEM_COUNT, shopListener
        )

        recyclerViewShopProductItem.layoutManager = createRecyclerViewShopProductItemLayoutManager()

        recyclerViewShopProductItem.addItemDecoration(createRecyclerViewShopProductItemDecoration())
    }

    private fun createRecyclerViewShopProductItemLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, SHOP_PRODUCT_ITEM_COUNT, GridLayoutManager.VERTICAL, false)
    }

    private fun createRecyclerViewShopProductItemDecoration(): RecyclerView.ItemDecoration {
        return ShopProductItemDecoration(getDimensionPixelSize(R.dimen.dp_8), SHOP_PRODUCT_ITEM_COUNT)
    }

    private fun showTextShopHasNoProduct() {
        itemView.textViewShopHasNoProduct?.visible()
    }

    private fun finishBindShopItem() {
        setViewMargins(itemView.textViewShopName.id, ConstraintSet.START, getTextViewShopNameMarginLeft())
    }

    @DimenRes
    private fun getTextViewShopNameMarginLeft(): Int {
        return itemView.imageViewShopBadge?.let { if (it.isVisible) R.dimen.dp_2 else R.dimen.dp_8 }
                ?: R.dimen.dp_8
    }

    private fun setViewMargins(@IdRes viewId: Int, anchor: Int, marginDp: Int) {
        applyConstraintSetToConstraintLayoutProductCard { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.setMargin(viewId, anchor, marginPixel)
        }
    }

    private fun applyConstraintSetToConstraintLayoutProductCard(
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