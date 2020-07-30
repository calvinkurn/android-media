package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_info_detail.view.*

class ProductGeneralItemAdapter(var listOfData: List<Content>, val name: String, val listener: DynamicProductDetailListener,
                                private val componentType: String, private val componentName: String, private val componentPosition: Int) : RecyclerView.Adapter<ProductGeneralItemAdapter.ProductGeneralItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGeneralItemViewHolder {
        return ProductGeneralItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dynamic_info_detail, parent, false))
    }

    override fun getItemCount(): Int = listOfData.size

    override fun onBindViewHolder(holder: ProductGeneralItemViewHolder, position: Int) {
        holder.bind(listOfData[position])
    }

    inner class ProductGeneralItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Content) {
            view.setOnClickListener {
                listener.onInfoClicked(name, ComponentTrackDataModel(componentType, componentName, componentPosition))
            }
            if (data.icon.isNotEmpty()) {
                ImageHandler.loadImage(view.context, view.ic_info_item, data.icon, R.drawable.ic_loading_image)
                view.ic_info_item.show()
            } else {
                view.ic_info_item.hide()
            }

            if (data.subtitle.isNotEmpty()) {
                view.desc_info_item.show()
                view.desc_info_item.text = MethodChecker.fromHtml(data.subtitle)
            } else {
                view.desc_info_item.hide()
            }
        }

    }
}