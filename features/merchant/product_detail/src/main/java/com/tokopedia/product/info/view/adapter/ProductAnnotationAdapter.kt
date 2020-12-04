package com.tokopedia.product.info.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.info.model.productdetail.uidata.AnnotationValueDataModel
import com.tokopedia.product.info.view.viewholder.ProductAnnotationViewHolder

/**
 * Created by Yehezkiel on 18/11/20
 */
class ProductAnnotationAdapter : RecyclerView.Adapter<ProductAnnotationViewHolder>() {

    private var data: List<AnnotationValueDataModel> = listOf()

    fun setData(data: List<AnnotationValueDataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAnnotationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(ProductAnnotationViewHolder.LAYOUT, parent, false)
        return ProductAnnotationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductAnnotationViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}