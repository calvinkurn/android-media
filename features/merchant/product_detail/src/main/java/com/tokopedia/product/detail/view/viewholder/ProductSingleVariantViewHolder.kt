package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.common.view.AtcVariantOptionAdapter
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_local_load_unify.view.*

/**
 * Created by Yehezkiel on 02/06/21
 */
class ProductSingleVariantViewHolder(val view: View,
                                     variantListener: AtcVariantListener,
                                     val pdpListener: DynamicProductDetailListener) : AbstractViewHolder<ProductSingleVariantDataModel>(view), AtcVariantListener by variantListener {


    private var containerAdapter: AtcVariantOptionAdapter? = null
    private val rvSingleVariant = view.findViewById<RecyclerView>(R.id.rv_single_variant)
    private val txtVariantIdentifier = view.findViewById<Typography>(R.id.txt_variant_identifier_title)
    private val txtVariantIdentifierStatic = view.findViewById<Typography>(R.id.txt_choose_variant_label)
    private val variantLocalLoad = view.findViewById<LocalLoad>(R.id.variant_local_load)

    companion object {
        val LAYOUT = R.layout.item_single_variant_view_holder
    }

    init {
        containerAdapter = AtcVariantOptionAdapter(this)
    }

    override fun bind(element: ProductSingleVariantDataModel) {
        if (element.isVariantError) {
            showError(element)
        } else {
            element.variantLevelOne?.let {
                txtVariantIdentifier.text = pdpListener.getVariantString()
                rvSingleVariant.adapter = containerAdapter
                rvSingleVariant.itemAnimator = null
                containerAdapter?.setData(it.variantOptions)
                hideError()
            }
        }
    }

    override fun onSelectionChanged(view: View, position: Int) {
        super.onSelectionChanged(view, position)
    }

    private fun showError(element: ProductSingleVariantDataModel) = with(view) {
        variantLocalLoad.progressState = false
        variantLocalLoad.show()
        renderError(element)
        rvSingleVariant.hide()
        txtVariantIdentifier.hide()
        txtVariantIdentifierStatic.hide()
    }

    private fun renderError(element: ProductSingleVariantDataModel) = with(view) {
        variant_local_load.refreshBtn?.setOnClickListener {
            if (!element.isRefreshing) {
                element.isRefreshing = true
                variantLocalLoad.progressState = true
                pdpListener.refreshPage()
            }
        }
    }

    private fun hideError() = with(view) {
        rvSingleVariant.show()
        txtVariantIdentifier.show()
        txtVariantIdentifierStatic.show()
        variantLocalLoad.hide()
    }
}