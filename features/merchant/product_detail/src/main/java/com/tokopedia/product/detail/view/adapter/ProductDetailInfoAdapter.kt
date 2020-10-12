package com.tokopedia.product.detail.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_info_product_detail.view.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoAdapter(private val listener: DynamicProductDetailListener,
                               private val componentTrackDataModel: ComponentTrackDataModel) : RecyclerView.Adapter<ProductDetailInfoAdapter.ItemProductDetailInfoViewHolder>() {

    private var listOfData: List<ProductDetailInfoContent> = listOf()

    fun updateData(data: List<ProductDetailInfoContent>) {
        listOfData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailInfoAdapter.ItemProductDetailInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info_product_detail, parent, false)
        return ItemProductDetailInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemProductDetailInfoViewHolder, position: Int) {
        holder.bind(listOfData[position])
    }

    override fun getItemCount(): Int = listOfData.size

    inner class ItemProductDetailInfoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val categoryApplink: String = view.context.getString(R.string.pdp_category_applink)

        fun bind(data: ProductDetailInfoContent) = with(view) {
            info_detail_title.text = data.title
            info_detail_value.text = data.subtitle

            if (data.applink.isNotEmpty()) {
                info_detail_value.setTextColor(MethodChecker.getColor(view.context, R.color.tkpd_main_green))
                info_detail_value.setOnClickListener {
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

