package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.fragment.partialview.PartialSnapshotView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

/**
 * Created by Yehezkiel on 06/05/20
 */
class ProductContentViewHolder(private val view: View,
                               private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductContentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_content
    }

    private var header: PartialSnapshotView? = null

    init {
        header = PartialSnapshotView(view, listener)
    }

    init {
    }

    override fun bind(element: ProductContentDataModel) {
        initializeClickListener(element)

        element.data?.let {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            header?.renderData(it, element.nearestWarehouseDataModel?.nearestWarehouseStockWording
                    ?: "")
            header?.showOfficialStore(it.data.isPowerMerchant, it.data.isOS)
        }

        renderWishlist(element.isAllowManage, element.isWishlisted)
        renderTradein(element.showTradeIn())
        renderCod(element.showCod())
    }

    override fun bind(element: ProductContentDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> renderWishlist(element.isAllowManage, element.isWishlisted)
            ProductDetailConstant.PAYLOAD_P3 -> {
                renderCod(element.showCod())
            }
        }
    }

    private fun initializeClickListener(element: ProductContentDataModel?) = with(view) {
        tradein_header_container.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }
        fab_detail_pdp.setOnClickListener {
            listener.onFabWishlistClicked(it.isActivated, getComponentTrackData(element))
        }
    }

    private fun renderCod(shouldShowCod: Boolean) = with(view) {
        header?.renderCod(shouldShowCod)
    }

    private fun renderTradein(shouldShowTradein: Boolean) = with(view) {
        tradein_header_container.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.tradein_white), null, null, null)
        header?.renderTradein(shouldShowTradein)
    }

    private fun renderWishlist(isAllowManage: Int, wishlisted: Boolean) {
        view.context?.let {
            view.fab_detail_pdp.hide()
            if (isAllowManage == 1) {
                view.fab_detail_pdp.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
                view.fab_detail_pdp.show()
            } else {
                updateWishlist(wishlisted)
            }
        }
    }

    private fun updateWishlist(wishlisted: Boolean) = with(view) {
        if (wishlisted) {
            fab_detail_pdp.hide()
            fab_detail_pdp.isActivated = true
            fab_detail_pdp.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_wishlist_selected_pdp))
            fab_detail_pdp.show()
        } else {
            fab_detail_pdp.hide()
            fab_detail_pdp.isActivated = false
            fab_detail_pdp.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_wishlist_unselected_pdp))
            fab_detail_pdp.show()
        }
    }

    private fun getComponentTrackData(element: ProductContentDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}