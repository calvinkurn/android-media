package com.tokopedia.product.detail.view.viewholder.product_detail_info.nested_adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.databinding.ItemInfoProductDetailBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import java.util.Locale

class ItemProductDetailInfoViewHolder(
    private val binding: ItemInfoProductDetailBinding,
    private val listener: DynamicProductDetailListener,
    private val isProductCatalog: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        data: ProductDetailInfoContent,
        trackData: ComponentTrackDataModel?,
        itemCount: Int
    ) {
        renderLineText(data = data, trackData)
        renderIcon(data = data)
        renderDivider(itemCount = itemCount)
    }

    private fun renderLineText(
        data: ProductDetailInfoContent,
        trackData: ComponentTrackDataModel?
    ) = with(binding) {
        infoDetailTitle.text = data.title

        infoDetailValue.apply {
            text = data.subtitle.parseAsHtmlLink(root.context)

            if (data.applink.isNotEmpty()) {
                setTextColor(root.context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                setWeight(com.tokopedia.unifyprinciples.Typography.BOLD)
            }

            setOnClickListener {
                setOnDetailValueClicked(data = data, trackData = trackData)
            }
        }
    }

    private fun renderIcon(data: ProductDetailInfoContent) = with(binding) {
        if (data.infoLink.isNotEmpty()) {
            infoDetailIcon.show()
            infoDetailClickArea.setOnClickListener {
                listener.goToEducational(data.applink)
            }

            data.icon.toIntOrNull()?.let { icon ->
                infoDetailIcon.setImage(icon)
            }
        }
    }

    private fun renderDivider(itemCount: Int) = with(binding) {
        val showDivider = adapterPosition < itemCount - 1 || !isProductCatalog
        divider.showWithCondition(shouldShow = showDivider)
    }

    private fun setOnDetailValueClicked(
        data: ProductDetailInfoContent,
        trackData: ComponentTrackDataModel?
    ) {
        when (data.title.lowercase(Locale.getDefault())) {
            ProductDetailCommonConstant.KEY_CATEGORY -> {
                listener.onCategoryClicked(
                    url = data.applink,
                    componentTrackDataModel = trackData ?: ComponentTrackDataModel()
                )
            }
            ProductDetailCommonConstant.KEY_ETALASE -> {
                listener.onEtalaseClicked(
                    url = data.applink,
                    componentTrackDataModel = trackData ?: ComponentTrackDataModel()
                )
            }
            else -> {
                listener.goToApplink(data.applink)
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: DynamicProductDetailListener,
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