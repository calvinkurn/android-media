package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.bs_item_product_detail_header.view.*
import kotlinx.android.synthetic.main.item_info_product_detail.view.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoHeaderViewHolder(private val view: View,
                                        private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_header
    }

    override fun bind(element: ProductDetailInfoHeaderDataModel) {
        view.pdp_header_product_title?.text = element.productTitle
        view.pdp_header_img.loadImageRounded(element.img, 8f)
        setupItemList(element.listOfInfo)
        setupSpecification(element.listOfAnnotation, element.needToShowSpecification())
    }

    private fun setupSpecification(annotation: List<ProductDetailInfoContent>, showSpecification: Boolean) = with(view) {
        if (showSpecification) {
            pdp_header_product_see_more.show()
            pdp_header_product_see_more.setOnClickListener {
                listener.goToSpecification(annotation)
            }
        } else {
            pdp_header_product_see_more.hide()
            pdp_header_product_see_more.setOnClickListener {}
        }
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
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                setWeight(Typography.BOLD)
                setOnClickListener {
                    listener.goToApplink(data.applink)
                }
            } else {
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                setWeight(Typography.REGULAR)
                setOnClickListener { }
            }
        }
    }
}