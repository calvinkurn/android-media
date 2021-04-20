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
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 01/02/21
 */
class ProductDetailInfoAdapter(private val listener: DynamicProductDetailListener) : RecyclerView.Adapter<ProductDetailInfoAdapter.ItemProductDetailInfoViewHolder>() {

    private var listOfContent: List<ProductDetailInfoContent> = listOf()
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    fun updateData(data: List<ProductDetailInfoContent>, componentTrackDataModel: ComponentTrackDataModel?) {
        listOfContent = data
        this.componentTrackDataModel = componentTrackDataModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductDetailInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_product_detail, parent, false)
        return ItemProductDetailInfoViewHolder(view)
    }

    override fun getItemCount(): Int = listOfContent.size

    override fun onBindViewHolder(holder: ItemProductDetailInfoViewHolder, position: Int) {
        holder.bind(listOfContent[position])
    }

    inner class ItemProductDetailInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val categoryApplink: String = view.context.getString(R.string.pdp_category_applink)
        private val detailTitle: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.info_detail_title)
        private val detailDesc: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.info_detail_value)

        fun bind(data: ProductDetailInfoContent) = with(itemView) {
            detailTitle?.text = data.title
            detailDesc?.text = data.subtitle

            //remove soon
            if (data.applink.isNotEmpty()) {
                detailDesc?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                detailDesc?.setWeight(Typography.BOLD)
                detailDesc?.setOnClickListener {
                    when {
                        data.applink.startsWith(categoryApplink) -> {
                            listener.onCategoryClicked(data.applink, componentTrackDataModel
                                    ?: ComponentTrackDataModel())
                        }
                        else -> {
                            val uriLink = Uri.parse(data.applink).pathSegments

                            if (uriLink.size >= 2 && uriLink[1] == "etalase") {
                                listener.onEtalaseClicked(data.applink, componentTrackDataModel
                                        ?: ComponentTrackDataModel())
                            } else {
                                listener.goToApplink(data.applink)
                            }
                        }
                    }
                }
            } else {
                detailDesc?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                detailDesc?.setWeight(Typography.REGULAR)
                detailDesc?.setOnClickListener(null)
            }
        }
    }
}