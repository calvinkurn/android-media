package com.tokopedia.order_management_common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.databinding.ItemDetailAddOnBinding
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.widget.AddOnDescriptionWidget
import com.tokopedia.order_management_common.util.stripLastDot
import com.tokopedia.utils.view.binding.viewBinding

class AddOnViewHolder(
    itemView: View?,
    private val listener: Listener
) : AbstractViewHolder<AddOnSummaryUiModel.AddonItemUiModel>(itemView),
    AddOnDescriptionWidget.Listener {

    companion object {
        val RES_LAYOUT = R.layout.item_detail_add_on
        const val MAX_RECYCLED_VIEWS = 10
    }

    private val binding by viewBinding<ItemDetailAddOnBinding>()
    private var element: AddOnSummaryUiModel.AddonItemUiModel? = null

    override fun bind(element: AddOnSummaryUiModel.AddonItemUiModel) {
        this.element = element
        setupProductAddOn(element)
        setupListener(element)
    }

    private fun setupListener(element: AddOnSummaryUiModel.AddonItemUiModel) {
        binding?.root?.setOnClickListener { listener.onAddOnClicked(element) }
    }

    override fun onDescriptionSeeLessClicked() {
        element?.run {
            descriptionExpanded = false
            binding?.layoutAddOnDescription?.collapse()
        }
    }

    override fun onDescriptionSeeMoreClicked() {
        element?.run {
            descriptionExpanded = true
            binding?.layoutAddOnDescription?.expand()
        }
    }

    override fun onCopyDescriptionClicked(label: String, description: CharSequence) {
        listener.onCopyAddOnDescriptionClicked(label, description)
    }

    private fun setupProductAddOn(addOnUiModel: AddOnSummaryUiModel.AddonItemUiModel) {
        binding?.run {
            setupAddOnImage(addOnUiModel.addOnsThumbnailUrl)
            setupAddOnName(addOnUiModel.addOnsName)
            setupAddOnPrice(addOnUiModel.quantity, addOnUiModel.priceText)
            setupAddOnDescriptions(addOnUiModel)
            setupInfoLink(addOnUiModel.infoLink, addOnUiModel.type)
        }
    }

    private fun ItemDetailAddOnBinding.setupInfoLink(infoLink: String, type: String) {
        icBomDetailAddonsInfo.showIfWithBlock(infoLink.isNotEmpty()) {
            setOnClickListener {
                listener.onAddOnsInfoLinkClicked(infoLink, type)
            }
        }
    }

    private fun ItemDetailAddOnBinding.setupAddOnImage(thumbnailUrl: String) {
        if (thumbnailUrl.isNotBlank()) {
            ivAddOn.show()
            ivAddOn.loadImage(thumbnailUrl)
        } else {
            ivAddOn.hide()
        }
    }

    private fun ItemDetailAddOnBinding.setupAddOnName(name: String) {
        tvAddOnName.text = name
    }

    private fun ItemDetailAddOnBinding.setupAddOnPrice(quantity: Int, priceText: String) {
        tvAddOnPrice.text = StringBuilder("$quantity x $priceText")
    }

    private fun ItemDetailAddOnBinding.setupAddOnDescriptions(
        addonItemUiModel: AddOnSummaryUiModel.AddonItemUiModel
    ) {
        val description =
            if (addonItemUiModel.message.isNotEmpty()) addonItemUiModel.message.stripLastDot()
            else if (addonItemUiModel.tips.isNotEmpty()) addonItemUiModel.tips
            else ""

        val disableTruncate = addonItemUiModel.tips.isNotEmpty()

        layoutAddOnDescription.run {
            setReceiverName(addonItemUiModel.toStr)
            setSenderName(addonItemUiModel.fromStr)
            setDescription(
                description,
                addonItemUiModel.descriptionExpanded,
                disableTruncate
            )
            setIsCopyable(copyable = addonItemUiModel.noteCopyable)
            setMarginBottomAddonDescWidget()
            listener = this@AddOnViewHolder
            show()
        }
    }

    interface Listener {
        fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence)
        fun onAddOnsExpand(isExpand:Boolean, addOnsIdentifier: String)
        fun onAddOnsInfoLinkClicked(infoLink: String, type: String)
        fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel)
    }
}
