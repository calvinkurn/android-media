package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.views.VariantItemDecorator
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.adapter.VariantContainerAdapter
import kotlinx.android.synthetic.main.item_local_load_unify.view.*
import kotlinx.android.synthetic.main.item_product_variant_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-26
 */
class ProductVariantViewHolder(val view: View,
                               val variantListener: ProductVariantListener,
                               val pdpListener: DynamicProductDetailListener) : AbstractViewHolder<VariantDataModel>(view) {

    private var containerAdapter: VariantContainerAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_product_variant_view_holder
    }

    override fun bind(element: VariantDataModel) {
        with(view) {
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

        }
    }

    override fun bind(element: VariantDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element.listOfVariantCategory?.let {
            containerAdapter?.variantContainerData = it
            containerAdapter?.notifyItemRangeChanged(0, it.size, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
        }
    }

    private fun showError(element: VariantDataModel) = with(view) {
        variant_local_load.progressState = false
        variant_local_load.show()
        renderError(element)
        rvContainerVariant.hide()
    }

    private fun renderError(element: VariantDataModel) = with(view) {
        variant_local_load.refreshBtn?.setOnClickListener {
            if (!element.isRefreshing) {
                element.isRefreshing = true
                variant_local_load.progressState = true
                pdpListener.refreshPage()
            }
        }
    }

    private fun hideError() = with(view) {
        rvContainerVariant.show()
        variant_local_load.hide()
    }
}