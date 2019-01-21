package com.tokopedia.broadcast.message.view.viewholder

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.extensions.toISO8601Date
import com.tokopedia.broadcast.message.common.extensions.toStringDayMonth
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.broadcast.message.view.adapter.ItemMessageProductAdapter
import kotlinx.android.synthetic.main.item_broadcast_message.view.*

class BroadcastMessageItemViewHolder(val view: View): AbstractViewHolder<TopChatBlastSeller>(view) {

    init {
        itemView.message_product.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun bind(element: TopChatBlastSeller) {
        val spannableBuilder = SpannableStringBuilder()

        itemView.run {
            if (element.state?.status == STATUS_SENT){
                loading.visibility = View.GONE
                spannableBuilder.append(boldSpannableString(getString(R.string.sent)))
                spannableBuilder.append(" ")
                spannableBuilder.append(getString(R.string.sent_on_template, element.executionTime.toISO8601Date().toStringDayMonth()))
                progress_container.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
            } else {
                loading.visibility = View.VISIBLE
                spannableBuilder.append(boldSpannableString("${context.getString(R.string.sending)}..."))
                /** temp hide this text
                spannableBuilder.append(" ")
                spannableBuilder.append(context.getString(R.string.send_progress_template, element.state?.totalSent, element.state?.totalTarget))*/
                progress_container.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow_sending))
            }
            message_progress.setText(spannableBuilder, TextView.BufferType.SPANNABLE)
            spannableBuilder.clear()
            spannableBuilder.append("${context.getString(R.string.message_read)} ")
            spannableBuilder.append(boldSpannableString (context.getString(R.string.read_progress_template, element.state?.totalRead, element.state?.totalTarget)))
            message_status.setText(spannableBuilder, TextView.BufferType.SPANNABLE)
            message.text = getFormatedMessage(element.message)
            message.setOnClickListener { if (message.text.endsWith(MORE_DESCRIPTION))
                message.text = element.message }
            itemView.message_product.adapter = ItemMessageProductAdapter(getListImageUrls(element))
        }

    }

    private fun getListImageUrls(element: TopChatBlastSeller): List<String> {
        val list = mutableListOf(element.marketingThumbnail?.attributes?.imageUrl)
        list.addAll(element.products?.map { it.attributes?.productProfile?.imageUrl } ?: listOf())

        return list.filterNotNull()
    }

    private fun getFormatedMessage(message: String): Spanned {
        if (message.length > MAX_CHAR) {
            val subDescription = message.substring(0, (MAX_CHAR - (4+ MORE_DESCRIPTION.length)))
            return MethodChecker
                    .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                            + HTML_MORE_DESCRIPTION)
        } else {
            return SpannableString(message)
        }
    }

    private fun boldSpannableString(text: String): SpannableString {
        val boldString = SpannableString(text)
        boldString.setSpan(StyleSpan(Typeface.BOLD), 0, boldString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return boldString
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message

        private const val STATUS_SENT = 1
        private const val MAX_CHAR= 75
        private const val MORE_DESCRIPTION = "Selengkapnya"
        private const val HTML_MORE_DESCRIPTION = "<font color='#42b549'>$MORE_DESCRIPTION</font>"
    }

}