package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topchat.R

class TopchatErrorViewHolder(
        itemView: View?
) : AbstractViewHolder<ErrorNetworkModel>(itemView) {

    private val globalError: GlobalError? = itemView?.findViewById(
            R.id.global_error_topchat
    )

    override fun bind(element: ErrorNetworkModel) {
        bindClickPrimaryButton(element)
        bindSecondaryButton()
    }

    private fun bindClickPrimaryButton(element: ErrorNetworkModel) {
        globalError?.setActionClickListener {
            element.onRetryListener.onRetryClicked()
        }
    }

    private fun bindSecondaryButton() {
        globalError?.errorSecondaryAction?.hide()
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_error_view
    }
}