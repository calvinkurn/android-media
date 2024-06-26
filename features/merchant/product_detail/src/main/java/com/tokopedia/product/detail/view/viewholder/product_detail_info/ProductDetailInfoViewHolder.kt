package com.tokopedia.product.detail.view.viewholder.product_detail_info

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicProductDetailInfoBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.product_detail_info.nested_adapter.ProductDetailInfoAdapter

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<ProductDetailInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_detail_info

        private const val MARGIN_TOP_DESC_TITLE_WHEN_CATALOG = 12
        private const val MARGIN_TOP_DESC_TITLE_WHEN_NON_CATALOG = 20
    }

    private val binding = ItemDynamicProductDetailInfoBinding.bind(view)

    private var adapter = ProductDetailInfoAdapter(listener)

    override fun bind(element: ProductDetailInfoDataModel) {
        renderWidget(element = element)
        setImpression(element = element)
    }

    private fun renderWidget(element: ProductDetailInfoDataModel) {
        renderTitle(element = element)
        renderListInfo(element = element)
        renderDescription(element = element)
        renderSeeMoreCatalog(element = element)
        renderSeeMoreDescription(element = element)
    }

    private fun renderSeeMoreCatalog(element: ProductDetailInfoDataModel) = with(binding) {
        val isCatalog = element.isCatalog

        groupCatalog.showWithCondition(shouldShow = isCatalog)

        productDetailInfoSeemoreSpecification.apply {
            text = element.catalogBottomSheet?.actionTitle

            shouldShowWithAction(shouldShow = element.isCatalog) {
                setOnClickListener {
                    listener.onSeeMoreSpecificationClicked(
                        infoData = element,
                        componentTrackDataModel = getComponentTrackData(element)
                    )
                }
            }
        }
    }

    private fun renderSeeMoreDescription(element: ProductDetailInfoDataModel) = with(binding) {
        val descFormatted = element.getDescription()
        val resources = root.resources

        productDetailInfoSeemore.apply {
            text = element.bottomSheet.actionTitle

            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                this?.topMargin = if (descFormatted.isNotEmpty()) {
                    resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
                } else {
                    resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
                }
            }

            setOnClickListener {
                listener.onSeeMoreDescriptionClicked(
                    infoData = element,
                    componentTrackDataModel = getComponentTrackData(element)
                )
            }
        }
    }

    private fun renderTitle(element: ProductDetailInfoDataModel) = with(binding) {
        productDetailInfoTitle.text = element.title
    }

    private fun renderListInfo(element: ProductDetailInfoDataModel) = with(binding) {
        adapter.setAnnotationData(
            data = element.getShowableData(),
            isProductCatalog = element.isCatalog,
            componentTrackDataModel = getComponentTrackData(element)
        )
        rvProductDetailInfo.adapter = adapter
    }

    private fun renderDescription(element: ProductDetailInfoDataModel) = with(binding) {
        val descFormatted = element.getDescription()

        productDetailInfoDescription.apply {
            if (descFormatted.isNotEmpty()) {
                text = descFormatted.parseAsHtmlLink(root.context, replaceNewLine = false)

                setOnClickListener {
                    listener.onSeeMoreDescriptionClicked(
                        infoData = element,
                        componentTrackDataModel = getComponentTrackData(element)
                    )
                }

                show()
            } else {
                hide()
            }
        }
    }

    private fun setImpression(element: ProductDetailInfoDataModel) {
        view.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }
}
