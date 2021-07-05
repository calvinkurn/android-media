package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailProductBundlingAdapter
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel
import kotlinx.android.synthetic.main.item_som_product_bundling.view.*

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailProductBundleCardViewHolder(
        private val actionListener: SomDetailAdapter.ActionListener?,
        itemView: View?
) : AbstractViewHolder<ProductBundleUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_som_product_bundling
    }

    private val productAdapter by lazy {
        SomDetailProductBundlingAdapter(actionListener)
    }

    override fun bind(element: ProductBundleUiModel) {
        with(itemView) {
            tvSomBundleName.text = element.bundleName
            tvSomTotalPrice.text = element.totalPriceFmt

            setupProductList(element.products)
        }
    }

    private fun setupProductList(products: List<SomDetailOrder.Data.GetSomDetail.Products>) {
        with(itemView.rvSomProductBundling) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = productAdapter

            productAdapter.products = products
            post {
                productAdapter.notifyDataSetChanged()
            }
        }
    }
}