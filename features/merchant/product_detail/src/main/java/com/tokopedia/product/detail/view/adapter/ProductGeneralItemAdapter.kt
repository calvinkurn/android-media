package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PRODUCT_PROTECTION
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_info_detail.view.*
import java.lang.IllegalStateException

class ProductGeneralItemAdapter(private var listOfData: List<Content>, val name: String, val listener: DynamicProductDetailListener,
                                private val componentType: String, private val componentName: String, private val componentPosition: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return if (name != PRODUCT_PROTECTION)
            ProductGeneralItemViewHolder(view)
        else PurchaseProtectionItemViewHolder(view)

    }

    override fun getItemCount(): Int = listOfData.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductGeneralItemViewHolder -> holder.bind(listOfData[position])
            is PurchaseProtectionItemViewHolder -> holder.bind(listOfData[position])
            else -> throw IllegalStateException("Unsupported type: $holder")
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (name == PRODUCT_PROTECTION) return R.layout.item_protection_partner_info_detail
        else return R.layout.item_dynamic_info_detail
    }

    inner class ProductGeneralItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Content) {
            view.setOnClickListener {
                listener.onInfoClicked(name, ComponentTrackDataModel(componentType, componentName, componentPosition))
            }
            if (data.icon.isNotEmpty()) {
                view.ic_info_item.loadImage(data.icon)
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

    inner class PurchaseProtectionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

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