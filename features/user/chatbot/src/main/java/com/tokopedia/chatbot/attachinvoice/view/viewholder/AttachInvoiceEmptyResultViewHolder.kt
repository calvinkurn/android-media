package com.tokopedia.chatbot.attachinvoice.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder

import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder

/**
 * Created by Hendri on 08/08/18.
 */
class AttachInvoiceEmptyResultViewHolder : EmptyResultViewHolder {
    constructor(itemView: View) : super(itemView) {}

    constructor(itemView: View, callback: BaseEmptyViewHolder.Callback) : super(itemView, callback) {}
}