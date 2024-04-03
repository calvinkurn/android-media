package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicProductContentBinding
import com.tokopedia.product.detail.view.fragment.partialview.PartialContentView
import com.tokopedia.product.detail.view.listener.ProductDetailListener

/**
 * Created by Yehezkiel on 06/05/20
 */
class ProductContentViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<ProductContentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_content
    }

    private val binding = ItemDynamicProductContentBinding.bind(view)
    private val header = PartialContentView(binding)

    override fun bind(element: ProductContentDataModel) {
        initializeClickListener(element)

        element.data?.let {
            view.addOnImpressionListener(
                holder = element.impressHolder,
                holders = listener.getImpressionHolders(),
                name = element.name,
                useHolders = listener.isRemoteCacheableActive()
            ) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            header.renderData(it, element.isNpl, element.freeOngkirImgUrl, listener)
        }

        header.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
    }

    override fun bind(element: ProductContentDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductContentDataModel.PAYLOAD_WISHLIST -> header.updateWishlist(
                element.isWishlisted,
                listener.shouldShowWishlist()
            )

            ProductContentDataModel.PAYLOAD_BOE_SHARE -> {
                header.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
                // only triggered when get data from p2, will update with boe/bo imageurl from Restriction Engine p2
                header.renderFreeOngkir(element.freeOngkirImgUrl, element.data?.isShowPrice == true)
            }
        }
        view.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun initializeClickListener(element: ProductContentDataModel?) = with(binding) {
        val content = element ?: return@with
        fabDetailPdp.apply {
            setOnClickListener {
                listener.onFabWishlistClicked(activeState, getComponentTrackData(content))
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        header.onViewRecycled()
    }
}
