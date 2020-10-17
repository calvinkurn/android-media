package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.bs_item_product_detail_header.view.*
import kotlinx.android.synthetic.main.item_info_product_detail.view.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoHeaderViewHolder(private val view: View) : AbstractViewHolder<ProductDetailInfoHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_header
    }

    override fun bind(element: ProductDetailInfoHeaderDataModel) {
        view.pdp_header_product_title?.text = "Coba dulu"
        ImageUtils.loadImageRounded2(view.context, view.pdp_header_img, "https://ecs7.tokopedia.net/android/others/ic_waiting_payment_order_tips_empty_stock.png", 8F)
        setupItemList(element.listOfInfo)
    }

    private fun setupItemList(listOfInfo: List<ProductDetailInfoContent>) = with(view) {
        val rootView = findViewById<ViewGroup>(R.id.pdp_header_list_container)
        val inflater: LayoutInflater = context.layoutInflater
        rootView.removeAllViews()

        listOfInfo.forEach { data ->
            val socProofView: View = inflater.inflate(R.layout.item_info_product_detail, null)
            setupItem(socProofView, data)
            rootView.addView(socProofView)
        }
    }

    private fun setupItem(itemView: View, data: ProductDetailInfoContent) = with(view) {
        itemView.info_detail_title?.text = data.title
        itemView.info_detail_value?.text = data.subtitle

        itemView.info_detail_value?.run {
            if (data.applink.isNotEmpty()) {
                setTextColor(MethodChecker.getColor(context, R.color.light_G500))
                setWeight(Typography.BOLD)
                setOnClickListener {
                    //todo here
                }
            } else {
                setTextColor(MethodChecker.getColor(context, R.color.light_N700_68))
                setWeight(Typography.REGULAR)
                setOnClickListener {  }
            }
        }
    }

}