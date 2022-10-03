package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.databinding.BsItemProductDetailAnnotationBinding
import com.tokopedia.product.detail.databinding.ItemInfoProductDetailBinding
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.models.ProductDetailInfoAnnotationDataModel
import com.tokopedia.product.share.ekstensions.layoutInflater
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
        setupProductInfo(productInfo = element.productInfo)
        setupReadMoreInfo(element = element)
    }

    private fun setupProductInfo(productInfo: List<ProductDetailInfoContent>) = with(binding) {
        val inflater = root.context.layoutInflater

        pdpHeaderListContainer.removeAllViews()

        productInfo.forEach {
            val child = onInfoCreateView(data = it, layoutInflater = inflater)
            pdpHeaderListContainer.addView(child)
        }
    }

    private fun onInfoCreateView(
        data: ProductDetailInfoContent,
        layoutInflater: LayoutInflater
    ) : View {
        val infoBinding = ItemInfoProductDetailBinding.inflate(
            layoutInflater,
            binding.root,
            false
        ).also {
            it.onInfoBinding(data)
        }

        return infoBinding.root
    }

    private fun ItemInfoProductDetailBinding.onInfoBinding(data: ProductDetailInfoContent) {
        val bindingPosition = bindingAdapterPosition + Int.ONE
        infoDetailTitle.text = data.title
        infoDetailValue.text = data.subtitle

        if (data.applink.isNotEmpty()) {
            setAppLink(data = data)
        }

        infoDetailIcon.shouldShowWithAction(data.infoLink.isNotEmpty()) {
            setInfoLink(data = data, position = bindingPosition)
        }

        listener.onImpressInfo(data.title, data.subtitle, bindingPosition)
    }

    private fun ItemInfoProductDetailBinding.setAppLink(data: ProductDetailInfoContent) {
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

    private fun ItemInfoProductDetailBinding.setInfoLink(
        data: ProductDetailInfoContent,
        position: Int
    ) {
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

    private fun setupReadMoreInfo(element: ProductDetailInfoAnnotationDataModel) = with(binding) {
        pdpHeaderProductSeeMore.shouldShowWithAction(element.annotation.isNotEmpty()) {
            pdpHeaderProductSeeMore.setOnClickListener {
                listener.goToSpecification(annotation = element.annotation)
            }
        }
    }
}
