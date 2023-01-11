package com.tokopedia.product.detail.view.viewholder.product_variant_thumbail

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.databinding.ItemThumbnailVariantViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.adapter.ProductThumbnailVariantAdapter

/**
 * Created by Yovi.Putra on 10/01/23
 */
class ProductThumbnailVariantViewHolder(
    val view: View,
    val variantListener: AtcVariantListener,
    val pdpListener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductSingleVariantDataModel>(view),
    AtcVariantListener by variantListener {

    companion object {
        val LAYOUT = R.layout.item_thumbnail_variant_view_holder
    }

    private val binding by lazyThreadSafetyNone {
        ItemThumbnailVariantViewHolderBinding.bind(view)
    }

    private val containerAdapter by lazyThreadSafetyNone {
        ProductThumbnailVariantAdapter(variantListener)
    }

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private val emptyVariantData = VariantOptionWithAttribute()

    init {
        with(binding) {
            thumbVariantList.adapter = containerAdapter
            thumbVariantList.itemAnimator = null
            thumbVariantList.layoutManager = layoutManager
            thumbVariantList.setRecycledViewPool(pdpListener.getParentRecyclerViewPool())
        }
    }

    fun scrollToPosition(position: Int) {
        if (position != -1) {
            binding.thumbVariantList.scrollToPosition(position)
        }
    }

    override fun bind(element: ProductSingleVariantDataModel) = with(binding) {
        element.variantLevelOne?.let {
            thumbVariantTitle.text = pdpListener.getVariantString()
            thumbVariantList.post {
                containerAdapter.firstLoad = element.firstLoad
                containerAdapter.submitList(it.variantOptions)
            }

            itemView.setOnClickListener {
                // pass dummy object since we need to redirect to variant bottomsheet
                variantListener.onVariantClicked(emptyVariantData)
            }
        }
        view.addOnImpressionListener(element.impressHolder) {
            pdpListener.onImpressComponent(getComponentTrackData(element))
        }
    }

    override fun bind(element: ProductSingleVariantDataModel, payloads: MutableList<Any>): Unit =
        with(binding) {
            element.variantLevelOne?.let {
                thumbVariantList.post {
                    containerAdapter.firstLoad = element.firstLoad
                    containerAdapter.submitList(it.variantOptions)
                }
            }
        }

    override fun onSelectionChanged(view: View, position: Int) {
        if (!layoutManager.isViewPartiallyVisible(view, true, true)) {
            view.post { binding.thumbVariantList.smoothScrollToPosition(position) }
        }
    }
}
