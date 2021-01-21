package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.animation.LayoutTransition
import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import kotlinx.android.synthetic.main.item_list_empty.view.*

class SomListOrderEmptyViewHolder(
        itemView: View?,
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

    init {
        itemView?.somEmptyStateContainer?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: SomListEmptyStateUiModel?) {
        element?.let { element ->
            setIllustration(element.imageUrl)
            setTitle(element.title)
            setDescription(element.description)
            if (element.showButton) {
                setButtonText(element.buttonText)
                setButtonAppLink(element.buttonAppLink)
                itemView.btnEmptyState.show()
            } else {
                itemView.btnEmptyState.gone()
            }
        }
    }

    override fun bind(element: SomListEmptyStateUiModel?, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val changes = payloads.firstOrNull()
            if (changes is Bundle) {
                if (changes.containsKey(PAYLOAD_TITLE_CHANGES)) setTitle(changes.getString(PAYLOAD_TITLE_CHANGES, ""))
                if (changes.containsKey(PAYLOAD_DESCRIPTION_CHANGES)) setDescription(changes.getString(PAYLOAD_DESCRIPTION_CHANGES, ""))
                if (changes.containsKey(PAYLOAD_ILLUSTRATION_CHANGES)) setIllustration(changes.getString(PAYLOAD_ILLUSTRATION_CHANGES, ""))
                if ((changes.containsKey(PAYLOAD_SHOW_BUTTON_CHANGES) && changes.getBoolean(PAYLOAD_SHOW_BUTTON_CHANGES, false)) ||
                        (!changes.containsKey(PAYLOAD_SHOW_BUTTON_CHANGES) && itemView.btnEmptyState.isVisible)) {
                    itemView.btnEmptyState.show()
                    if (changes.containsKey(PAYLOAD_BUTTON_TEXT_CHANGES)) setButtonText(changes.getString(PAYLOAD_BUTTON_TEXT_CHANGES, ""))
                    if (changes.containsKey(PAYLOAD_BUTTON_APPLINK_CHANGES)) setButtonAppLink(changes.getString(PAYLOAD_BUTTON_APPLINK_CHANGES, ""))
                } else {
                    itemView.btnEmptyState.gone()
                }
                return
            }
        }
        super.bind(element, payloads)
    }

    private fun setIllustration(imageUrl: String) {
        itemView.ivSomListEmptyStateIllustration.loadImage(imageUrl)
    }

    private fun setTitle(title: String) {
        itemView.tvEmptyStateTitle.text = title
    }

    private fun setDescription(description: String) {
        itemView.tvEmptyStateDescription.text = description
    }

    private fun setButtonText(text: String) {
        itemView.btnEmptyState.text = text
    }

    private fun setButtonAppLink(appLink: String) {
        with(itemView) {
            btnEmptyState.setOnClickListener { context?.let { openAppLink(it, appLink) } }
        }
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