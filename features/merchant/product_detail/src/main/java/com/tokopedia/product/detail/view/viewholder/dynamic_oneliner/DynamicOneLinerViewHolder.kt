package com.tokopedia.product.detail.view.viewholder.dynamic_oneliner

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.ItemDynamicOneLinerBinding
import com.tokopedia.product.detail.view.fragment.delegate.BasicComponentEvent
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.delegate.DynamicOneLinerCallback
import com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.event.DynamicOneLinerEvent

class DynamicOneLinerViewHolder(
    view: View,
    private val callback: DynamicOneLinerCallback
) : ProductDetailPageViewHolder<DynamicOneLinerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_one_liner
        private const val STATUS_SHOW = "show"
        private const val CHEVRON_POS_FOLLOW = "follow_text"
        private const val CHEVRON_POS_END = "end"
    }

    val binding = ItemDynamicOneLinerBinding.bind(view)

    override fun bind(element: DynamicOneLinerUiModel) = with(element.data) {
        when (status) {
            STATUS_SHOW -> {
                itemView.setLayoutHeight(LayoutParams.WRAP_CONTENT)
                renderContent(this, element.name, getComponentTrackData(element))
                impressComponent(element)
            }

            else -> {
                itemView.setLayoutHeight(0)
            }
        }
    }

    private fun renderContent(
        data: DynamicOneLinerUiModel.Data,
        name: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) = with(binding) {
        configPadding(binding, data)

        val title = data.text
        dynamicOneLinerTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title.parseAsHtmlLink(context = context, replaceNewLine = false)
        }

        val iconUrl = data.icon
        dynamicOneLinerIconLeft.showIfWithBlock(iconUrl.isNotEmpty()) {
            setImageWithMaxSize(iconUrl)
        }

        val url = data.applink
        val showClickArea = url.isNotEmpty() ||
            name == ProductDetailConstant.PRODUCT_DYNAMIC_ONELINER_PROMO

        setupChevronRight(
            showClickArea = showClickArea,
            chevronPos = data.chevronPos
        )

        setupClick(
            url = url,
            showClickArea = showClickArea,
            text = data.text,
            componentTrackDataModel = componentTrackDataModel
        )
        dynamicOneLinerSeparatorTop.showWithCondition(data.shouldShowSeparatorTop)
        dynamicOneLinerSeparatorBottom.showWithCondition(data.shouldShowSeparatorBottom)
    }

    private fun setupChevronRight(
        showClickArea: Boolean,
        chevronPos: String
    ) = with(binding) {
        dynamicOneLinerIconRight.showIfWithBlock(
            showClickArea
        ) {
            val chainStyle = when (chevronPos) {
                CHEVRON_POS_FOLLOW -> ConstraintSet.CHAIN_PACKED
                CHEVRON_POS_END -> ConstraintSet.CHAIN_SPREAD_INSIDE
                else -> null
            }

            chainStyle?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(dynamicOneLinerContent)

                constraintSet.setHorizontalChainStyle(id, chainStyle)
                constraintSet.applyTo(dynamicOneLinerContent)
            }
        }
    }

    private fun setImageWithMaxSize(url: String) = with(binding) {
        Glide.with(itemView.context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val height = resource.height
                    val width = resource.width
                    val masSizeInDp = 48
                    val masSizeInPx = masSizeInDp.dpToPx(itemView.context.resources.displayMetrics)

                    dynamicOneLinerIconLeft.layoutParams.height =
                        if (height > masSizeInPx) masSizeInPx else height
                    dynamicOneLinerIconLeft.layoutParams.width =
                        if (width > masSizeInPx) masSizeInPx else height

                    dynamicOneLinerIconLeft.setImageBitmap(resource)
                    dynamicOneLinerIconLeft.requestLayout()
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun setupClick(
        url: String,
        text: String,
        showClickArea: Boolean,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (showClickArea) {
            itemView.setOnClickListener {
                callback.event(
                    DynamicOneLinerEvent.OnDynamicOneLinerClicked(
                        text,
                        url,
                        componentTrackDataModel
                    )
                )
            }
        } else {
            itemView.setOnClickListener(null)
        }
    }

    private fun configPadding(
        binding: ItemDynamicOneLinerBinding,
        data: DynamicOneLinerUiModel.Data
    ) = with(binding.dynamicOneLinerContent) {
        setPadding(paddingLeft, data.paddingTopPx, paddingRight, data.paddingBottomPx)
    }

    private fun impressComponent(element: DynamicOneLinerUiModel) {
        itemView.addOnImpressionListener(
            holder = element.impressHolder,
            holders = callback.impressionHolders,
            name = element.name,
            useHolders = callback.isRemoteCacheableActive
        ) {
            val trackerData = getComponentTrackData(element = element)
            callback.event(BasicComponentEvent.OnImpressComponent(trackData = trackerData))
        }
    }
}
