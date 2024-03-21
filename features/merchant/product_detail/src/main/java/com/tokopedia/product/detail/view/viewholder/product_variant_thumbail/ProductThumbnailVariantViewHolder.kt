package com.tokopedia.product.detail.view.viewholder.product_variant_thumbail

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.databinding.ItemThumbnailVariantViewHolderBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.util.ThumbnailSmoothScroller
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.adapter.ProductThumbnailVariantAdapter

/**
 * Created by Yovi.Putra on 10/01/23
 */
class ProductThumbnailVariantViewHolder(
    val view: View,
    private val atcListener: AtcVariantListener,
    private val pdpListener: ProductDetailListener
) : ProductDetailPageViewHolder<ProductSingleVariantDataModel>(view),
    AtcVariantListener by atcListener {

    companion object {
        val LAYOUT = R.layout.item_thumbnail_variant_view_holder
    }

    private val binding by lazyThreadSafetyNone {
        ItemThumbnailVariantViewHolderBinding.bind(view)
    }

    private val containerAdapter by lazyThreadSafetyNone {
        ProductThumbnailVariantAdapter(atcListener = atcListener, pdpListener = pdpListener)
    }

    private val smoothScroller by lazyThreadSafetyNone {
        ThumbnailSmoothScroller(binding.root.context, binding.thumbVariantList)
    }
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private val emptyVariantData = VariantOptionWithAttribute.EMPTY

    init {
        with(binding) {
            thumbVariantList.adapter = containerAdapter
            thumbVariantList.itemAnimator = null
            thumbVariantList.layoutManager = layoutManager
        }
    }

    fun scrollToPosition(position: Int) {
        if (position >= 0) {
            binding.thumbVariantList.post {
                smoothScroller.scrollThumbnail(position)
            }
        }
    }

    override fun bind(element: ProductSingleVariantDataModel) {
        binding.thumbVariantTitle.text = element.title
        setThumbnailItems(element = element)
        setOnClick()
        setImpression(element = element)
    }

    override fun bind(element: ProductSingleVariantDataModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            binding.thumbVariantTitle.text = element.title
            setThumbnailItems(element = element)
        }
    }

    private fun setOnClick() {
        itemView.setOnClickListener {
            // pass dummy object since we need to redirect to variant bottomsheet
            atcListener.onVariantClicked(emptyVariantData)
        }
    }

    private fun setImpression(element: ProductSingleVariantDataModel) {
        view.addOnImpressionListener(
            holder = element.impressHolder,
            holders = pdpListener.getImpressionHolders(),
            name = element.name,
            useHolders = pdpListener.isRemoteCacheableActive()
        ) {
            val trackData = getComponentTrackData(element)
            pdpListener.onImpressComponent(
                trackData.copy(componentName = element.getComponentNameAsThumbnail())
            )
        }
    }

    private fun setThumbnailItems(
        element: ProductSingleVariantDataModel
    ) {
        containerAdapter.submitList(element.variantLevelOne?.variantOptions)

        if (element.mapOfSelectedVariant.isEmpty()) {
            scrollToPosition(0)
        }
    }

    override fun onSelectionChanged(view: View, position: Int) {
        if (!layoutManager.isViewPartiallyVisible(view, true, true)) {
            scrollToPosition(position = position)
        }
    }
}
