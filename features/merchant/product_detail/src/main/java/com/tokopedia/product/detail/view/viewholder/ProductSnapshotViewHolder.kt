package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewVisibilityListener
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialHeaderView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class ProductSnapshotViewHolder(itemView: View,
                                private val childFragmentManager: FragmentManager,
                                private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductSnapshotDataModel>(itemView) {
    private lateinit var header: PartialHeaderView

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun bind(element: ProductSnapshotDataModel) {
        val screenWidth = itemView.resources.displayMetrics.widthPixels
        itemView.view_picture_search_bar.layoutParams.height = screenWidth

        if (!::header.isInitialized) {
            header = PartialHeaderView.build(itemView.base_header, null)
        }

        element.productInfoP1?.let {
            header.renderData(it)

            element.nearestWarehouse?.let { nearestWarehouse ->
                if (nearestWarehouse.warehouseInfo.id.isNotBlank())
                    header.updateStockAndPriceWarehouse(nearestWarehouse, it.campaign)
            }
        }

        element.shopInfo?.let {
            header.showOfficialStore(it.goldOS)
            renderWishlist(it, element.isWishlisted)
        }

        header.renderCod(element.shouldShowCod)
        header.renderTradein(element.shouldShowTradein)
        itemView.view_picture_search_bar.isVisibleOnTheScreen(object : ViewVisibilityListener {
            override fun onViewNotVisible() {
                itemView.fab_detail.hide()
            }

            override fun onViewVisible() {
                itemView.fab_detail.show()
            }

        })

        itemView.fab_detail.setOnClickListener { listener.onFabWishlistClicked(it.isActivated) }
        itemView.view_picture_search_bar.renderData(element.media, listener::onImageClicked, childFragmentManager)
    }

    override fun bind(element: ProductSnapshotDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            1 -> renderWishlist(element.shopInfo ?: ShopInfo(), element.isWishlisted)
            2 -> renderCod(element.shouldShowCod)
        }
    }

    private fun renderCod(shouldShowCod: Boolean) {
        if (::header.isInitialized) {
            header.renderCod(shouldShowCod)
        }
    }

    private fun renderWishlist(shopInfo: ShopInfo, wishlisted: Boolean) {
        itemView.context?.let {
            itemView.fab_detail.show()
            if (shopInfo.isAllowManage == 1) {
                itemView.fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
            } else {
                updateWishlist(wishlisted)
            }
        }
    }

    private fun updateWishlist(wishlisted: Boolean) {
        itemView.context?.let {
            if (wishlisted) {
                itemView.fab_detail.hide()
                itemView.fab_detail.isActivated = true
                itemView.fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_checked))
                itemView.fab_detail.show()
            } else {
                itemView.fab_detail.hide()
                itemView.fab_detail.isActivated = false
                itemView.fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_unchecked))
                itemView.fab_detail.show()
            }
        }
    }

}