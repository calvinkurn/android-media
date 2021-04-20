package com.tokopedia.product.detail.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_informasi_product.view.*

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

        private val categoryApplink: String = view.context.getString(R.string.pdp_category_applink)

        fun bind(data: Content) {
            view.title_info.contentDescription = view.context.getString(R.string.content_desc_title_info, data.title)
            view.desc_info.contentDescription = view.context.getString(R.string.content_desc_desc_info, data.subtitle)
            view.title_info.text = data.title
            view.desc_info.text = data.subtitle

            if (data.applink.isNotEmpty()) {
                view.desc_info.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                view.desc_info.setOnClickListener {
                    when {
                        data.applink.startsWith(categoryApplink) -> {
                            listener.onCategoryClicked(data.applink, componentTrackDataModel)
                        }
                        else -> {
                            val uriLink = Uri.parse(data.applink).pathSegments

                            if (uriLink.size >= 2 && uriLink[1] == "etalase") {
                                listener.onEtalaseClicked(data.applink, componentTrackDataModel)
                            } else {
                                listener.goToApplink(data.applink)
                            }
                        }
                    }
                }
            }
        }
    }
}