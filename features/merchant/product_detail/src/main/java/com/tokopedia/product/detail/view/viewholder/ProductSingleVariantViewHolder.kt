package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.common.view.AtcVariantOptionAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.databinding.ItemLocalLoadUnifyBinding
import com.tokopedia.product.detail.databinding.ItemSingleVariantViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ThumbnailSmoothScroller

/**
 * Created by Yehezkiel on 02/06/21
 */
class ProductSingleVariantViewHolder(val view: View,
                                     val variantListener: AtcVariantListener,
                                     val pdpListener: DynamicProductDetailListener) : AbstractViewHolder<ProductSingleVariantDataModel>(view), AtcVariantListener by variantListener {


    private var containerAdapter: AtcVariantOptionAdapter? = null
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
    private val emptyVariantData = VariantOptionWithAttribute()

    companion object {
        val LAYOUT = R.layout.item_single_variant_view_holder
    }

    private val binding = ItemSingleVariantViewHolderBinding.bind(view)
    private val itemLocalLoadUnifyBinding = ItemLocalLoadUnifyBinding.bind(binding.root)
    private val smoothScroller by lazyThreadSafetyNone {
        ThumbnailSmoothScroller(binding.root.context, binding.rvSingleVariant)
    }

    init {
        containerAdapter = AtcVariantOptionAdapter(this)
    }

    fun scrollToPosition(position: Int) {
        if (position != -1) {
            smoothScroller.scrollThumbnail(position)
        }
    }

    override fun bind(element: ProductSingleVariantDataModel) {
        if (element.isVariantError) {
            showError(element)
        } else {
            element.variantLevelOne?.let {
                binding.txtVariantIdentifierTitle.text = pdpListener.getVariantString()
                binding.rvSingleVariant.adapter = containerAdapter
                binding.rvSingleVariant.itemAnimator = null
                binding.rvSingleVariant.layoutManager = layoutManager
                binding.rvSingleVariant.setRecycledViewPool(pdpListener.getParentRecyclerViewPool())
                containerAdapter?.setData(it.variantOptions)

                itemView.setOnClickListener {
                    //pass dummy object since we need to redirect to variant bottomsheet
                    variantListener.onVariantClicked(emptyVariantData)
                }
                hideError()
            }
        }
        view.addOnImpressionListener(element.impressHolder) {
            pdpListener.onImpressComponent(getComponentTrackData(element))
        }
    }

    override fun bind(element: ProductSingleVariantDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element.variantLevelOne?.let {
            containerAdapter?.setData(it.variantOptions)
        }
    }

    override fun onSelectionChanged(view: View, position: Int) {
    }

    private fun showError(element: ProductSingleVariantDataModel) = with(binding) {
        itemLocalLoadUnifyBinding.variantLocalLoad.progressState = false
        itemLocalLoadUnifyBinding.variantLocalLoad.show()
        renderError(element)
        rvSingleVariant.hide()
        txtVariantIdentifierTitle.hide()
        txtChooseVariantLabel.hide()
    }

    private fun renderError(element: ProductSingleVariantDataModel) = with(itemLocalLoadUnifyBinding) {
        variantLocalLoad.refreshBtn?.setOnClickListener {
            if (!element.isRefreshing) {
                element.isRefreshing = true
                variantLocalLoad.progressState = true
                pdpListener.refreshPage()
            }
        }
    }

    private fun hideError() = with(binding) {
        rvSingleVariant.show()
        txtVariantIdentifierTitle.show()
        txtChooseVariantLabel.show()
        itemLocalLoadUnifyBinding.variantLocalLoad.hide()
    }

    private fun getComponentTrackData(
        element: ProductSingleVariantDataModel
    ) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}
