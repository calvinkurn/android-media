package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.ProductOther
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import kotlinx.android.synthetic.main.item_other_product.view.*

class OtherProductAdapter(private val products: List<ProductOther>): RecyclerView.Adapter<OtherProductAdapter.OtherProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherProductViewHolder {
        return OtherProductViewHolder(parent.inflateLayout(R.layout.item_other_product))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: OtherProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    class OtherProductViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(product: ProductOther){
            with(itemView){
                tv_name.text = MethodChecker.fromHtml(product.name)
                tv_price.text = product.price.getCurrencyFormatted()
                ImageHandler.loadImage(context, iv_pic, product.imageUrl300, -1)
                setOnClickListener {
                    context.startActivity(ProductDetailActivity.createIntent(context, product.id))
                }
            }
        }
    }
}