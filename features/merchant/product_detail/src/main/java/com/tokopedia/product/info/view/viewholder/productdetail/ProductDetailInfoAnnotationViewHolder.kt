package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.databinding.BsItemProductDetailAnnotationBinding
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.models.ProductDetailInfoAnnotationDataModel
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by yovi.putra on 20/09/22"
 * Project name: android-tokopedia-core
 **/

class ProductDetailInfoAnnotationViewHolder(
    view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoAnnotationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_annotation
    }

    private val binding = BsItemProductDetailAnnotationBinding.bind(view)

    override fun bind(element: ProductDetailInfoAnnotationDataModel) {
        setupItem(data = element.data)
    }

    private fun setupItem(data: ProductDetailInfoContent) =
        with(binding.layoutItemInfoProductDetail) {
            val bindingPosition = bindingAdapterPosition + Int.ONE
            infoDetailTitle.text = data.title
            infoDetailValue.text = data.subtitle

            infoDetailValue.run {
                if (data.applink.isNotEmpty()) {
                    setAppLink(data = data)
                }

                if (data.infoLink.isNotEmpty()) {
                    setInfoLink(data = data, position = bindingPosition)
                }

                listener.onImpressInfo(data.title, data.subtitle, bindingPosition)
            }
        }

    private fun setAppLink(
        data: ProductDetailInfoContent
    ) = with(binding.layoutItemInfoProductDetail) {

        infoDetailValue.apply {
            setTextColor(context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            setWeight(Typography.BOLD)
        }

        infoDetailClickArea.setOnClickListener {
            when (data.title.lowercase(Locale.getDefault())) {
                ProductDetailCommonConstant.KEY_CATEGORY -> {
                    listener.goToCategory(data.applink)
                }
                ProductDetailCommonConstant.KEY_ETALASE -> {
                    listener.goToEtalase(data.applink)
                }
                ProductDetailCommonConstant.KEY_CATALOG -> {
                    listener.goToCatalog(data.applink, data.subtitle)
                }
                else -> {
                    listener.goToApplink(data.applink)
                }
            }
        }
    }

    private fun setInfoLink(
        data: ProductDetailInfoContent,
        position: Int
    ) = with(binding.layoutItemInfoProductDetail) {

        infoDetailIcon.show()
        infoDetailClickArea.setOnClickListener {
            listener.goToEducational(
                data.infoLink,
                data.title,
                data.subtitle,
                position
            )
        }

        data.icon.toIntOrNull()?.let { icon ->
            infoDetailIcon.setImage(icon)
        }
    }
}
