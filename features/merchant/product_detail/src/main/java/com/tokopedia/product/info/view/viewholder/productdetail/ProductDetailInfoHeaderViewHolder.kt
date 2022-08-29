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
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.databinding.BsItemProductDetailHeaderBinding
import com.tokopedia.product.detail.databinding.ItemInfoProductDetailBinding
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoHeaderViewHolder(private val view: View,
                                        private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_header

        private const val HEADER_IMAGE_ROUND_SIZE = 8f
    }

    private val binding = BsItemProductDetailHeaderBinding.bind(view)

    override fun bind(element: ProductDetailInfoHeaderDataModel) {
        binding.pdpHeaderProductTitle.text = element.productTitle
        binding.pdpHeaderImg.loadImageRounded(element.img, HEADER_IMAGE_ROUND_SIZE)
        setupItemList(element.listOfInfo)
        setupSpecification(element.listOfAnnotation, element.needToShowSpecification())
    }

    private fun setupSpecification(annotation: List<ProductDetailInfoContent>, showSpecification: Boolean) = with(binding) {
        if (showSpecification) {
            pdpHeaderProductSeeMore.show()
            pdpHeaderProductSeeMore.setOnClickListener {
                listener.goToSpecification(annotation)
            }
        } else {
            pdpHeaderProductSeeMore.hide()
            pdpHeaderProductSeeMore.setOnClickListener {}
        }
    }

    private fun setupItemList(listOfInfo: List<ProductDetailInfoContent>) = with(view) {
        val rootView = findViewById<ViewGroup>(R.id.pdp_header_list_container)
        val inflater: LayoutInflater = context.layoutInflater
        rootView.removeAllViews()

        listOfInfo.forEach { data ->
            val socProofView: View = inflater.inflate(R.layout.item_info_product_detail, null)
            val socProofBinding = ItemInfoProductDetailBinding.bind(socProofView)
            setupItem(socProofBinding, data)
            rootView.addView(socProofView)
        }
    }

    private fun setupItem(socProofBinding: ItemInfoProductDetailBinding,
                          data: ProductDetailInfoContent) = with(socProofBinding) {
        infoDetailTitle.text = data.title
        infoDetailValue.text = data.subtitle

        infoDetailValue.run {
            if (data.applink.isNotEmpty()) {
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                setWeight(Typography.BOLD)

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

            if (data.infoLink.isNotEmpty()) {
                infoDetailIcon.show()
                infoDetailClickArea.setOnClickListener {
                    listener.goToEducational(data.infoLink, data.title, data.subtitle, adapterPosition + 1)
                }

                data.icon.toIntOrNull()?.let { icon ->
                    infoDetailIcon.setImage(icon)
                }
            }

            listener.onImpressInfo(data.title, data.subtitle, adapterPosition + 1)
        }
    }
}