package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_informasi_product.view.*
import java.util.*

class ProductInfoAdapter(private val listener: DynamicProductDetailListener,
                         private val listOfData: List<Content>,
                         private val componentTrackDataModel: ComponentTrackDataModel) : RecyclerView.Adapter<ProductInfoAdapter.ItemProductInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_informasi_product, parent, false)
        return ItemProductInfoViewHolder(view)
    }

    override fun getItemCount(): Int = listOfData.size

    override fun onBindViewHolder(holder: ItemProductInfoViewHolder, position: Int) {
        holder.bind(listOfData[position])
    }

    inner class ItemProductInfoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Content) {
            view.title_info.contentDescription = view.context.getString(R.string.content_desc_title_info, data.title)
            view.desc_info.contentDescription = view.context.getString(R.string.content_desc_desc_info, data.subtitle)
            view.title_info.text = data.title
            view.desc_info.text = data.subtitle

            if (data.applink.isNotEmpty()) {
                view.desc_info.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                view.desc_info.setOnClickListener {
                    when (data.title.toLowerCase(Locale.getDefault())) {
                        ProductDetailCommonConstant.KEY_CATEGORY -> {
                            listener.onCategoryClicked(data.applink, componentTrackDataModel)
                        }
                        ProductDetailCommonConstant.KEY_ETALASE -> {
                            listener.onEtalaseClicked(data.applink, componentTrackDataModel)
                        }
                        else -> {
                            listener.goToApplink(data.applink)
                        }
                    }
                }
            }
        }
    }
}