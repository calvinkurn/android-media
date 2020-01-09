package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import kotlinx.android.synthetic.main.item_informasi_product.view.*

class ProductInfoAdapter(private val listOfData: List<Content>) : RecyclerView.Adapter<ProductInfoAdapter.ItemProductInfoViewHolder>() {

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

        private val categoryApplink: String = view.context.getString(R.string.staging_pdp_category_applink)

        fun bind(data: Content) {
            view.title_info.text = data.title
            view.desc_info.text = data.subtitle

            if (data.applink.isNotEmpty()) {
                view.desc_info.setTextColor(MethodChecker.getColor(view.context, R.color.tkpd_main_green))
                view.desc_info.setOnClickListener {
                    when {
                        data.applink.startsWith(categoryApplink) -> {
                            if (GlobalConfig.isCustomerApp()) {
                                RouteManager.route(view.context, data.applink)
                            }
                        }
                        else -> {
                            RouteManager.route(view.context, data.applink)
                        }
                    }
                }
            }
        }

    }
}