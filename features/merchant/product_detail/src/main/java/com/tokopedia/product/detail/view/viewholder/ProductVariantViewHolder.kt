package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.views.VariantItemDecorator
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.ItemLocalLoadUnifyBinding
import com.tokopedia.product.detail.databinding.ItemProductVariantViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.variant_common.view.adapter.VariantContainerAdapter

/**
 * Created by Yehezkiel on 2020-02-26
 */
class ProductVariantViewHolder(val view: View,
                               val variantListener: AtcVariantListener,
                               val pdpListener: DynamicProductDetailListener) : AbstractViewHolder<VariantDataModel>(view) {

    private var containerAdapter: VariantContainerAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_product_variant_view_holder
    }

    private val binding = ItemProductVariantViewHolderBinding.bind(view)
    private val itemLocalLoadUnifyBinding = ItemLocalLoadUnifyBinding.bind(binding.root)

    override fun bind(element: VariantDataModel) {
        with(binding) {
            if (element.isVariantError) {
                showError(element)
            } else {
                hideError()
                element.listOfVariantCategory?.let {
                    containerAdapter = VariantContainerAdapter(variantListener)
                    rvContainerVariant.adapter = containerAdapter
                    if (rvContainerVariant.itemDecorationCount == 0) {
                        rvContainerVariant.addItemDecoration(VariantItemDecorator(MethodChecker.getDrawable(view.context, com.tokopedia.variant_common.R.drawable.bg_separator_variant)))
                    }
                    rvContainerVariant.itemAnimator = null
                    containerAdapter?.setData(it)
                }
            }
            view.addOnImpressionListener(element.impressHolder) {
                pdpListener.onImpressComponent(getComponentTrackData(element))
            }
        }
    }

    override fun bind(element: VariantDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element.listOfVariantCategory?.let {
            containerAdapter?.variantContainerData = it
            containerAdapter?.notifyItemRangeChanged(0, it.size, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
        }
    }

    private fun showError(element: VariantDataModel) = with(binding) {
        val variantLocalLoad = itemLocalLoadUnifyBinding.variantLocalLoad
        variantLocalLoad.progressState = false
        variantLocalLoad.show()
        renderError(element)
        rvContainerVariant.hide()
    }

    private fun renderError(element: VariantDataModel) = with(binding) {
        val variantLocalLoad = itemLocalLoadUnifyBinding.variantLocalLoad
        variantLocalLoad.refreshBtn?.setOnClickListener {
            if (!element.isRefreshing) {
                element.isRefreshing = true
                variantLocalLoad.progressState = true
                pdpListener.refreshPage()
            }
        }
    }

    private fun hideError() = with(binding) {
        rvContainerVariant.show()
        itemLocalLoadUnifyBinding.variantLocalLoad.hide()
    }

    private fun getComponentTrackData(
        element: VariantDataModel
    ) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}