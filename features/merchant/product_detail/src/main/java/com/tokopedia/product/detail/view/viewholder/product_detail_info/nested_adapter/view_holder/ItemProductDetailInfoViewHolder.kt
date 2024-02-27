package com.tokopedia.product.detail.view.viewholder.product_detail_info.nested_adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoAnnotationTrackData
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoNavigator
import com.tokopedia.product.detail.databinding.ItemInfoProductDetailBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener

class ItemProductDetailInfoViewHolder(
    private val binding: ItemInfoProductDetailBinding,
    private val listener: ProductDetailListener,
    private val isProductCatalog: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    private val annotationTrackData by lazy {
        ProductDetailInfoAnnotationTrackData()
    }

    fun bind(
        data: ProductDetailInfoContent,
        trackData: ComponentTrackDataModel?,
        itemCount: Int
    ) {
        renderLineText(data = data, trackData = trackData)
        renderIcon(data = data)
        renderDivider(itemCount = itemCount)
        setImpression(data = data, trackData = trackData)
    }

    private fun renderLineText(
        data: ProductDetailInfoContent,
        trackData: ComponentTrackDataModel?
    ) = with(binding) {
        infoDetailTitle.text = data.title

        infoDetailValue.apply {
            text = data.subtitle.parseAsHtmlLink(root.context)

            if (data.isClickable) {
                setTextColor(root.context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                setWeight(com.tokopedia.unifyprinciples.Typography.BOLD)

                setOnClickListener {
                    setOnDetailValueClicked(data = data, trackData = trackData)
                }
            }
        }
    }

    private fun renderIcon(data: ProductDetailInfoContent) = with(binding) {
        if (data.infoLink.isNotEmpty()) {
            infoDetailIcon.show()
            infoDetailClickArea.setOnClickListener {
                listener.goToEducational(data.infoLink)
            }

            data.icon.toIntOrNull()?.let { icon ->
                infoDetailIcon.setImage(icon)
            }
        }
    }

    private fun renderDivider(itemCount: Int) = with(binding) {
        val showDivider = bindingAdapterPosition < itemCount - 1 || !isProductCatalog
        divider.showWithCondition(shouldShow = showDivider)
    }

    private fun setOnDetailValueClicked(
        data: ProductDetailInfoContent,
        trackData: ComponentTrackDataModel?
    ) {
        data.routeOnClick(object : ProductDetailInfoNavigator {
            override fun toCategory(appLink: String) {
                listener.onCategoryClicked(
                    url = appLink,
                    componentTrackDataModel = trackData ?: ComponentTrackDataModel()
                )
            }

            override fun toEtalase(appLink: String) {
                listener.onEtalaseClicked(
                    url = appLink,
                    componentTrackDataModel = trackData ?: ComponentTrackDataModel()
                )
            }

            override fun toCatalog(appLink: String, subTitle: String) {
                // no-ops, on bottom sheet only
            }

            override fun toAppLink(appLink: String) {
                listener.goToApplink(url = appLink)
            }

            override fun toWebView(infoLink: String) {
                listener.goToWebView(url = infoLink)
            }

            override fun toProductDetailInfo(key: String, extParam: String) {
                listener.onAnnotationOpenProductInfoSheet(
                    extParam = extParam,
                    trackData = annotationTrackData.copy(
                        parentTrackData = trackData,
                        key = key,
                        value = data.subtitle
                    )
                )
            }
        })
    }

    private fun setImpression(data: ProductDetailInfoContent, trackData: ComponentTrackDataModel?) {
        if (data.key == Content.KEY_PANDUAN_UKURAN) {
            binding.root.addOnImpressionListener(
                holder = data.impressionHolder,
                holders = listener.getImpressionHolders(),
                name = data.hashCode().toString(),
                useHolders = listener.isRemoteCacheableActive()
            ) {
                listener.onAnnotationGenericImpression(
                    trackData = annotationTrackData.copy(
                        parentTrackData = trackData,
                        key = data.key,
                        value = data.subtitle
                    )
                )
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: ProductDetailListener,
            isProductCatalog: Boolean
        ): ItemProductDetailInfoViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = ItemInfoProductDetailBinding.inflate(inflate, parent, false)
            return ItemProductDetailInfoViewHolder(
                binding = binding,
                listener = listener,
                isProductCatalog = isProductCatalog
            )
        }
    }
}
