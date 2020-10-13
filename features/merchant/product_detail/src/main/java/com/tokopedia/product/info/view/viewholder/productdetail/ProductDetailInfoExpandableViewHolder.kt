package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import kotlinx.android.synthetic.main.bs_item_product_detail_expandable.view.*

/**
 * Created by Yehezkiel on 13/10/20
 */
class ProductDetailInfoExpandableViewHolder(private val view: View, private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel) {
        view.product_detail_value?.text = view.context.getString(R.string.lorem)
        if (element.isShowable) {
            view.product_detail_title_chevron?.isExpand = true
            view.product_detail_value?.show()
        } else {
            view.product_detail_title_chevron?.isExpand = false
            view.product_detail_value?.hide()
        }

        view.product_detail_title_chevron?.titleText = "Deskripsi Produk"
        view.product_detail_title_chevron?.setOnClickListener {
            if (view.product_detail_title_chevron?.isExpand == true) {
                view.product_detail_title_chevron?.isExpand = false
                listener.closeAllExpand(element.uniqueIdentifier(), false)
            } else {
                listener.closeAllExpand(element.uniqueIdentifier(), true)
                view.product_detail_title_chevron?.isExpand = true
            }
        }
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey("toggle")) {
                val toggle = bundle.getBoolean("toggle")
                view.product_detail_value?.showWithCondition(toggle)
                view.product_detail_title_chevron?.isExpand = toggle
            }
        }
    }
}