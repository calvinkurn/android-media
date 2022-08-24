package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifyprinciples.Typography
import java.util.*

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

        private val detailTitle: Typography? = itemView.findViewById(R.id.info_detail_title)
        private val detailDesc: Typography? = itemView.findViewById(R.id.info_detail_value)
        private val detailIcon: IconUnify? = itemView.findViewById(R.id.info_detail_icon)
        private val detailClickArea: View? = itemView.findViewById(R.id.info_detail_click_area)

        fun bind(data: ProductDetailInfoContent) = with(itemView) {
            detailTitle?.text = data.title
            detailDesc?.text = data.subtitle

            if (data.infoLink.isNotEmpty()) {
                detailIcon?.show()
                detailClickArea?.setOnClickListener {
                    listener.goToEducational(data.applink)
                }

                data.icon.toIntOrNull()?.let { icon ->
                    detailIcon?.setImage(icon)
                }
            }

            if (data.applink.isNotEmpty()) {
                detailDesc?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                detailDesc?.setWeight(Typography.BOLD)
                detailDesc?.setOnClickListener {
                    when (data.title.lowercase(Locale.getDefault())) {
                        ProductDetailCommonConstant.KEY_CATEGORY -> {
                            listener.onCategoryClicked(data.applink, componentTrackDataModel
                                    ?: ComponentTrackDataModel())
                        }
                        ProductDetailCommonConstant.KEY_ETALASE -> {
                            listener.onEtalaseClicked(data.applink, componentTrackDataModel
                                    ?: ComponentTrackDataModel())
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