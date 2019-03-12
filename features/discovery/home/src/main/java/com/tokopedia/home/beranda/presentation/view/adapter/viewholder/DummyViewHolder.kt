package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.viewmodel.DummyModel
import kotlinx.android.synthetic.main.dummy_ovo.view.*
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan


class DummyViewHolder(itemView: View, listener: HomeCategoryListener) : AbstractViewHolder<DummyModel>(itemView) {

    override fun bind(element: DummyModel) {
        val messages = "Akan ada perubahan batas waktu respon pesanan."
        val messagesSpan = SpannableString(messages+" ")
        messagesSpan.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.home_ticker_text_color)),
                0, messages.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val detailsSpan = SpannableStringBuilder(getString(R.string.ticker_details_text))
        detailsSpan.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, detailsSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        detailsSpan.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.home_ticker_info_lengkap_color)),
                0, detailsSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        itemView.tv_ticker_text.text = TextUtils.concat(messagesSpan, detailsSpan)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.dummy_ovo
    }
}
