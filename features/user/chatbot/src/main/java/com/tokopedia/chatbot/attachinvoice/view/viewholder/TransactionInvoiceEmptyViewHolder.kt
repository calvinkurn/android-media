package com.tokopedia.chatbot.attachinvoice.view.viewholder

import android.view.View
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.view.model.EmptyTransactionInvoiceUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TransactionInvoiceEmptyViewHolder(itemView: View, private var emptyViewHolderListener: EmptyViewHolderListener)
    : AbstractViewHolder<EmptyTransactionInvoiceUiModel>(itemView) {
    private val ivIcon: DeferredImageView = itemView.findViewById(R.id.icon)
    private val tvTitle: Typography = itemView.findViewById(R.id.title)
    private val tvDescription: Typography = itemView.findViewById(R.id.description)
    private val button: UnifyButton = itemView.findViewById(R.id.button)

    override fun bind(element: EmptyTransactionInvoiceUiModel) {
        ivIcon.loadRemoteImageDrawable("", element.remoteUrl)
        tvTitle.text = getString(element.title)
        tvDescription.text = getString(element.description)
        button.setOnClickListener {
            emptyViewHolderListener.onTryAgain()
        }
        button.showWithCondition(element.showButton)
    }

    companion object {
        var LAYOUT = R.layout.invoice_transaction_empty_view
    }
}

interface EmptyViewHolderListener {
    fun onTryAgain()
}