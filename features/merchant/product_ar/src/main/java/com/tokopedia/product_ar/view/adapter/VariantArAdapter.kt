package com.tokopedia.product_ar.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.view.ProductRoundedBorderImageView
import com.tokopedia.unifyprinciples.Typography

class VariantArAdapter : RecyclerView.Adapter<VariantArAdapter.ItemArImage>() {

    private var arImageDatas: List<String> = listOf()

    fun setInitialData(arImageDatas: List<String>) {
        this.arImageDatas = arImageDatas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemArImage {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_variant_ar_view, parent, false)

        return ItemArImage(view)
    }

    override fun onBindViewHolder(holder: ItemArImage, position: Int) {
        holder.bind(arImageDatas[position])
    }

    override fun getItemCount(): Int = arImageDatas.size

    inner class ItemArImage(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgVariant = itemView.findViewById<ProductRoundedBorderImageView>(R.id.img_item_variant_ar)
        private val txtVariantName = itemView.findViewById<Typography>(R.id.txt_variant_ar)

        fun bind(url: String) {
            imgVariant.loadImage(url)
            txtVariantName.text = "Normal"

            itemView.setOnClickListener {
                imgVariant.setSelected = !imgVariant.setSelected
            }
        }
    }
}

