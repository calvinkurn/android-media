package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewVisibilityListener
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialSnapshotView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*

class ProductSnapshotViewHolder(private val view: View,
                                private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductSnapshotDataModel>(view) {
    private lateinit var header: PartialSnapshotView

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun bind(element: ProductSnapshotDataModel) {
        val screenWidth = view.resources.displayMetrics.widthPixels
        view.view_picture_search_bar.layoutParams.height = screenWidth

        if (!::header.isInitialized) {
            header = PartialSnapshotView(view, listener)
        }

        element.dynamicProductInfoP1?.let {
            header.renderData(it)

            element.nearestWarehouse?.let { nearestWarehouse ->
                if (nearestWarehouse.warehouseInfo.id.isNotBlank())
                    header.updateStockAndPriceWarehouse(nearestWarehouse, it.data.campaign)
            }

            it.data.media.let {
//                view.view_picture_search_bar.renderData(it, listener::onImageClicked, listener.getProductFragmentManager(), element.shouldReinitVideoPicture)
                element.shouldReinitVideoPicture = false
            }
        }

        element.shopInfo?.let {
            header.showOfficialStore(it.goldOS)
            renderWishlist(it, element.isWishlisted)
        }

        header.renderCod(element.shouldShowCod)
        header.renderTradein(element.shouldShowTradein)
        view.view_picture_search_bar.isVisibleOnTheScreen(object : ViewVisibilityListener {
            override fun onViewNotVisible() {
                view.fab_detail.hide()
            }

            override fun onViewVisible() {
                view.fab_detail.show()
            }

        })

        view.fab_detail.setOnClickListener { listener.onFabWishlistClicked(it.isActivated) }
        element.media?.let {
            view.view_picture_search_bar.renderData(it, listener::onImageClicked, listener.getProductFragmentManager(), element.shouldReinitVideoPicture)
            element.shouldReinitVideoPicture = false
        }
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
        view.context?.let {
            view.fab_detail.show()
            if (shopInfo.isAllowManage == 1) {
                view.fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
            } else {
                updateWishlist(wishlisted)
            }
        }
    }

    private fun updateWishlist(wishlisted: Boolean) {
        view.context?.let {
            if (wishlisted) {
                view.fab_detail.hide()
                view.fab_detail.isActivated = true
                view.fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_checked))
                view.fab_detail.show()
            } else {
                view.fab_detail.hide()
                view.fab_detail.isActivated = false
                view.fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_unchecked))
                view.fab_detail.show()
            }
        }
    }

}