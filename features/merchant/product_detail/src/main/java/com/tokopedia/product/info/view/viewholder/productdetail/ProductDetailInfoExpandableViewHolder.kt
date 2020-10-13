package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.bs_item_product_detail_header.view.*

/**
 * Created by Yehezkiel on 13/10/20
 */
class ProductDetailInfoExpandableViewHolder(private val view: View) : AbstractViewHolder<ProductDetailInfoExpandableDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_header
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel) {
        view.pdp_header_product_title?.text = "Coba dulu"
        ImageUtils.loadImageRounded2(view.context, view.pdp_header_img, "https://ecs7.tokopedia.net/android/others/ic_waiting_payment_order_tips_empty_stock.png", 8F)
    }
}