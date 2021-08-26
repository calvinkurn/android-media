package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewholder

import android.view.View
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tkpd.remoteresourcerequest.view.ImageDensityType

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.unifyprinciples.Typography

class SaldoListEmptyViewHolder(itemView: View, private val isSalesTab: Boolean = false) :
    AbstractViewHolder<EmptyModel>(itemView) {

    private val emptyViewTitle: Typography = itemView.findViewById(R.id.text_view_empty_title_text)
    private val emptyViewDescription: Typography = itemView.findViewById(R.id.text_view_empty_content_text)
    private val deferredImageView: DeferredImageView = itemView.findViewById(R.id.no_result_image)

    override fun bind(element: EmptyModel) {
        deferredImageView.loadRemoteImageDrawable(EMPTY_IMAGE,  ImageDensityType.SUPPORT_SINGLE_DPI)

        if (isSalesTab) {
            emptyViewTitle.text = getString(R.string.saldo_empty_sales_tab_title)
            emptyViewDescription.text = getString(R.string.saldo_empty_sales_tab_desc)
            emptyViewDescription.visible()
        } else {
            emptyViewTitle.text = getString(R.string.saldo_empty_tab_title)
            emptyViewDescription.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.saldo_transaction_list_empty
        const val EMPTY_IMAGE = "saldo_img_empty_list"
    }
}
