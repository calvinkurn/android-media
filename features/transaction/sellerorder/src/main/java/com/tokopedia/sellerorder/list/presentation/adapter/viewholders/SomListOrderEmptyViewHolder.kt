package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.animation.LayoutTransition
import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemListEmptyBinding
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomListOrderEmptyViewHolder(
    itemView: View,
    private val listener: SomListEmptyStateListener
) : AbstractViewHolder<SomListEmptyStateUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_list_empty

        const val PAYLOAD_TITLE_CHANGES = "payload_title_changes"
        const val PAYLOAD_DESCRIPTION_CHANGES = "payload_description_changes"
        const val PAYLOAD_ILLUSTRATION_CHANGES = "payload_illustration_changes"
        const val PAYLOAD_BUTTON_TEXT_CHANGES = "payload_button_text_changes"
        const val PAYLOAD_BUTTON_APPLINK_CHANGES = "payload_button_applink_changes"
        const val PAYLOAD_SHOW_BUTTON_CHANGES = "payload_show_button_changes"
    }

    private val binding by viewBinding<ItemListEmptyBinding>()

    @Suppress("NAME_SHADOWING")
    override fun bind(element: SomListEmptyStateUiModel?) {
        element?.let { element ->
            setIllustration(element.imageUrl)
            setTitle(element.title)
            setDescription(element.description)
            if (element.buttonText.isNotBlank()) {
                setButtonText(element.buttonText)
                setButtonAppLink(element.buttonAppLink)
                binding?.btnEmptyState?.show()
            } else {
                binding?.btnEmptyState?.gone()
            }
        }
    }

    override fun bind(element: SomListEmptyStateUiModel?, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val changes = payloads.firstOrNull()
            if (changes is Bundle) {
                binding?.somEmptyStateContainer?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                if (changes.containsKey(PAYLOAD_TITLE_CHANGES)) setTitle(changes.getString(PAYLOAD_TITLE_CHANGES, ""))
                if (changes.containsKey(PAYLOAD_DESCRIPTION_CHANGES)) setDescription(changes.getString(PAYLOAD_DESCRIPTION_CHANGES, ""))
                if (changes.containsKey(PAYLOAD_ILLUSTRATION_CHANGES)) setIllustration(changes.getString(PAYLOAD_ILLUSTRATION_CHANGES, ""))
                if ((changes.containsKey(PAYLOAD_SHOW_BUTTON_CHANGES) && changes.getBoolean(PAYLOAD_SHOW_BUTTON_CHANGES, false)) ||
                        (!changes.containsKey(PAYLOAD_SHOW_BUTTON_CHANGES) && binding?.btnEmptyState?.isVisible.orFalse())) {
                    binding?.btnEmptyState?.show()
                    if (changes.containsKey(PAYLOAD_BUTTON_TEXT_CHANGES)) setButtonText(changes.getString(PAYLOAD_BUTTON_TEXT_CHANGES, ""))
                    if (changes.containsKey(PAYLOAD_BUTTON_APPLINK_CHANGES)) setButtonAppLink(changes.getString(PAYLOAD_BUTTON_APPLINK_CHANGES, ""))
                } else {
                    binding?.btnEmptyState?.gone()
                }
                binding?.somEmptyStateContainer?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                return
            }
        }
        super.bind(element, payloads)
    }

    private fun setIllustration(imageUrl: String) {
        binding?.ivSomListEmptyStateIllustration?.loadImage(imageUrl)
    }

    private fun setTitle(title: String) {
        binding?.tvEmptyStateTitle?.text = title
    }

    private fun setDescription(description: String) {
        binding?.tvEmptyStateDescription?.text = description
    }

    private fun setButtonText(text: String) {
        binding?.btnEmptyState?.text = text
    }

    private fun setButtonAppLink(appLink: String) {
        binding?.btnEmptyState?.setOnClickListener { it?.context?.let { openAppLink(it, appLink) } }
    }

    private fun openAppLink(context: Context, appLink: String) {
        if (RouteManager.route(context, appLink)) {
            listener.onStartAdvertiseButtonClicked()
        }
    }

    interface SomListEmptyStateListener {
        fun onStartAdvertiseButtonClicked()
    }
}
