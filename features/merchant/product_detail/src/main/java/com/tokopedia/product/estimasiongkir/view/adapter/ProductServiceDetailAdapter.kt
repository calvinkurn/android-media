package com.tokopedia.product.estimasiongkir.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product.detail.R
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductServiceDetailDataModel

/**
 * Created by Yehezkiel on 26/01/21
 */
class ProductServiceDetailAdapter : RecyclerView.Adapter<ProductServiceDetailAdapter.ProductServiceDetailViewHolder>() {
    private val products: MutableList<ProductServiceDetailDataModel> = mutableListOf()

    fun updateServices(products: List<ProductServiceDetailDataModel>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductServiceDetailViewHolder {
        return ProductServiceDetailViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_shipping_service_detail, parent, false))
    }

    override fun onBindViewHolder(holder: ProductServiceDetailViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ProductServiceDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceDetailName: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.pdp_service_detail_name)
        private val serviceDetailEstimation: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.pdp_service_detail_estimation)
        private val serviceDetailPrice: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.pdp_service_detail_price)
        private val codLabel: com.tokopedia.unifycomponents.Label? = itemView.findViewById(R.id.pdp_service_detail_cod)
        private val dynamicPricingLabel: com.tokopedia.unifycomponents.Label? = itemView.findViewById(R.id.pdp_service_detail_dynamic_pricing)

        fun bind(product: ProductServiceDetailDataModel) = with(itemView) {
            serviceDetailName?.text = context.getString(R.string.location_dot_builder, product.serviceProductName)
            serviceDetailPrice?.text = product.serviceProductPrice
            serviceDetailEstimation?.shouldShowWithAction(product.serviceProductEstimation.isNotEmpty()) {
                serviceDetailEstimation.text = product.serviceProductEstimation
            }

            dynamicPricingLabel?.shouldShowWithAction(product.dynamicPricingText.isNotEmpty()) {
                dynamicPricingLabel.setLabel(product.dynamicPricingText)
            }

            codLabel?.shouldShowWithAction(product.isCod) {
                if (product.codText.isEmpty()) {
                    codLabel.setLabel(context.getString(R.string.pdp_shipping_available_cod_label))
                } else {
                    codLabel.text = product.codText
                }
            }
        }
    }
}
