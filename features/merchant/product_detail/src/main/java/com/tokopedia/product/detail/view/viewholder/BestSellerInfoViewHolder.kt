package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.BestSellerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.BestSellerInfoDataModel.Companion.SEPARATOR_BOTH
import com.tokopedia.product.detail.data.model.datamodel.BestSellerInfoDataModel.Companion.SEPARATOR_BOTTOM
import com.tokopedia.product.detail.data.model.datamodel.BestSellerInfoDataModel.Companion.SEPARATOR_TOP
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_best_seller_info.view.bottom_separator
import kotlinx.android.synthetic.main.item_best_seller_info.view.top_separator

class BestSellerInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) :
    AbstractViewHolder<BestSellerInfoDataModel>(view) {

    private var bestSellerInfoTitleText: Typography? = view.findViewById(R.id.best_seller_info_title)
    private var bestSellerInfoDescriptionText: Typography? =
        view.findViewById(R.id.best_seller_info_description)

    companion object {
        val LAYOUT = R.layout.item_best_seller_info
    }

    override fun bind(element: BestSellerInfoDataModel) {
        if (element.bestSellerInfoContent != null && element.bestSellerInfoContent?.isVisible == true) {
            itemView.setVisible(true)
            renderContent(element)
            renderSeparator(element.bestSellerInfoContent?.separator ?: "")
        } else {
            itemView.setVisible(false)
        }
    }

    private fun renderContent(element: BestSellerInfoDataModel) {
        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }

        bestSellerInfoTitleText?.text = element.bestSellerInfoContent?.linkText
        bestSellerInfoDescriptionText?.text = element.bestSellerInfoContent?.content

        view.setOnClickListener {
            element.bestSellerInfoContent?.applink?.let {
                listener.onClickBestSeller(getComponentTrackData(element), it)
            }
        }
    }

    private fun renderSeparator(separator: String) = with(view) {
        top_separator.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_TOP)
        bottom_separator.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_BOTTOM)
    }

    private fun getComponentTrackData(element: BestSellerInfoDataModel?) = ComponentTrackDataModel(
        element?.type
            ?: "",
        element?.name ?: "", adapterPosition + 1
    )

    private fun View.setVisible(isVisible: Boolean) {
        if (isVisible) {
            val params: ViewGroup.LayoutParams = this.layoutParams
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            this.layoutParams = params
        } else {
            val params: ViewGroup.LayoutParams = this.layoutParams
            params.height = 0
            this.layoutParams = params
        }
    }
}