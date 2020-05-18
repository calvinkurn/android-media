package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.fragment.partialview.PartialSnapshotView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class ProductSnapshotViewHolder(private val view: View,
                                private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductSnapshotDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    private var header: PartialSnapshotView? = null

    init {
        header = PartialSnapshotView(view, listener)
        setupFabDetail()
        measureScreenHeight()
    }

    override fun bind(element: ProductSnapshotDataModel) {
        with(view) {
            view_picture_search_bar.shouldRenderViewPager = element.shouldRefreshViewPager
            initializeClickListener(element)
            element.dynamicProductInfoP1?.let {
                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
                header?.renderData(it, element.nearestWarehouseDataModel?.nearestWarehouseStockWording
                        ?: "")
                header?.showOfficialStore(it.data.isPowerMerchant, it.data.isOS)
                view_picture_search_bar.renderShopStatusDynamicPdp(element.shopStatus, element.statusTitle, element.statusMessage,
                        it.basic.status)
            }
            renderWishlist(element.isAllowManage, element.isWishlisted)
            renderTradein(element.showTradeIn())
            renderCod(element.showCod())

            element.media?.let {
                view_picture_search_bar.renderData(it, listener::onImageClicked, listener::onSwipePicture, listener.getProductFragmentManager(),
                        getComponentTrackData(element), listener::onImageClickedTrack, listener.getLifecycleFragment())
                if (element.shouldRenderImageVariant) {
                    view.view_picture_search_bar.updateImage(element.media)
                }
                element.shouldRefreshViewPager = false
                element.shouldRenderImageVariant = false
            }
        }
    }

    override fun bind(element: ProductSnapshotDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> renderWishlist(element.isAllowManage, element.isWishlisted)
            ProductDetailConstant.PAYLOAD_P3 -> {
                view.label_cod.visibility = if (element.shouldShowCod) View.VISIBLE else View.GONE
                renderCod(element.showCod())
            }
            ProductDetailConstant.PAYLOAD_VARIANT_SELECTED -> {
                view.view_picture_search_bar.updateImage(element.media)
            }
        }
    }

    private fun initializeClickListener(element: ProductSnapshotDataModel?) = with(view) {
        tv_trade_in_promo.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }
        fab_detail.setOnClickListener {
            listener.onEditProductClick(element!!, element.dynamicProductInfoP1, getComponentTrackData(element))
            listener.onFabWishlistClicked(it.isActivated, getComponentTrackData(element))
        }
    }

    private fun renderCod(shouldShowCod: Boolean) = with(view){
        label_cod.showWithCondition(shouldShowCod)
        header?.renderCod(shouldShowCod)
    }

    private fun renderTradein(shouldShowTradein: Boolean) = with(view) {
        tv_trade_in_promo.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.tradein_white), null, null, null)
        header?.renderTradein(shouldShowTradein)
    }

    private fun renderWishlist(isAllowManage: Int, wishlisted: Boolean) {
        view.context?.let {
            view.fab_detail.hide()
            if (isAllowManage == 1) {
                view.fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
                view.fab_detail.show()
            } else {
                updateWishlist(wishlisted)
            }
        }
    }

    private fun updateWishlist(wishlisted: Boolean) = with(view) {
        if (wishlisted) {
            fab_detail.hide()
            fab_detail.isActivated = true
            fab_detail.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_wishlist_selected_pdp))
            fab_detail.show()
        } else {
            fab_detail.hide()
            fab_detail.isActivated = false
            fab_detail.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_wishlist_unselected_pdp))
            fab_detail.show()
        }
    }

    private fun measureScreenHeight() = with(view) {
        val screenWidth = view.resources.displayMetrics.widthPixels
        view_picture_search_bar.layoutParams.height = screenWidth
    }

    private fun setupFabDetail() = with(view) {
        view_picture_search_bar.isVisibleOnTheScreen({
            fab_detail.show()
        }, {
            fab_detail.hide()
            view_picture_search_bar.stopVideo()
        })
    }

    private fun getComponentTrackData(element: ProductSnapshotDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}