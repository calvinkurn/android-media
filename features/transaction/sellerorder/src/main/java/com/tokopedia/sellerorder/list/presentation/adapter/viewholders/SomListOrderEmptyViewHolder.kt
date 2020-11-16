package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import kotlinx.android.synthetic.main.item_list_empty.view.*

class SomListOrderEmptyViewHolder(
        itemView: View?,
        private val listener: SomListEmptyStateListener
) : AbstractViewHolder<SomListEmptyStateUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_list_empty
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: SomListEmptyStateUiModel?) {
        element?.let { element ->
            with(itemView) {
                ivSomListEmptyStateIllustration.loadImage(element.imageUrl)
                tvEmptyStateTitle.text = element.title
                tvEmptyStateDescription.text = element.description
                btnEmptyState.apply {
                    text = element.buttonText
                    setOnClickListener { context?.let { openAppLink(it, element.buttonAppLink) } }
                    showWithCondition(element.showButton)
                }
            }
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