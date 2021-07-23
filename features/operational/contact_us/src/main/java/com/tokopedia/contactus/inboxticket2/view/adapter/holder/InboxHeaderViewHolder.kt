package com.tokopedia.contactus.inboxticket2.view.adapter.holder

import android.text.SpannableString
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem
import com.tokopedia.contactus.inboxticket2.view.activity.TICKET_STATUS_CLOSED
import com.tokopedia.contactus.inboxticket2.view.activity.TICKET_STATUS_IN_PROCESS
import com.tokopedia.contactus.inboxticket2.view.activity.TICKET_STATUS_NEED_RATING
import com.tokopedia.contactus.inboxticket2.view.listeners.InboxDetailListener
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class InboxHeaderViewHolder(val view: View) : InboxDetailViewHolder(view) {

    private val ticketTitle = view.findViewById<Typography>(R.id.message_ticket_title)
    private val ticketLabel = view.findViewById<TextView>(R.id.tv_priority_label)
    private val ticketId = view.findViewById<Typography>(R.id.tv_id_num)
    private val ticketTransactionDetails = view.findViewById<Typography>(R.id.tv_view_transaction)

    fun bind(header: CommentsItem, inboxDetailListener: InboxDetailListener, utils: Utils) {
        ticketTitle.text = setTitle(header.ticketTitle ?: "", header.ticketStatus ?: "", utils)
                ?: header.ticketTitle ?: ""
        setPriorityLabel(header.priorityLabel, inboxDetailListener)
        setTicketId(header.ticketId ?: "")
        ticketTransactionDetails.setOnClickListener { inboxDetailListener.onTransactionDetailsClick() }

    }

    private fun setPriorityLabel(priorityLabel: Boolean, inboxDetailListener: InboxDetailListener) {
        if (priorityLabel) {
            ticketLabel.show()
            ticketLabel.setOnClickListener { inboxDetailListener.onPriorityLabelClick() }
        } else {
            ticketLabel.hide()
        }
    }

    private fun setTicketId(id: String) {
        if (id.isEmpty()) {
            ticketId.hide()
        } else {
            ticketId.text = String.format(view.context.getString(R.string.invoice_id), id)
        }
    }

    private fun setTitle(ticketTitle: String, ticketStatus: String, utils: Utils): SpannableString? {
        val context = view.context
        return when (ticketStatus) {
            TICKET_STATUS_IN_PROCESS -> utils.getStatusTitle(ticketTitle + ".   " + context.getString(R.string.on_going),
                    ContextCompat.getColor(context, R.color.contact_us_y_200),
                    ContextCompat.getColor(context, R.color.contact_us_orange_500), context.resources.getDimensionPixelSize(R.dimen.sp_11), context)
            TICKET_STATUS_NEED_RATING ->
                utils.getStatusTitle(ticketTitle + ".   " + context.getString(R.string.need_rating),
                        ContextCompat.getColor(context, R.color.contact_us_r_100),
                        ContextCompat.getColor(context, R.color.contact_us_r_400), context.resources.getDimensionPixelSize(R.dimen.sp_11), context)
            TICKET_STATUS_CLOSED ->
                utils.getStatusTitle(ticketTitle + ".   " + context.getString(R.string.closed),
                        ContextCompat.getColor(context, R.color.contact_us_grey_200),
                        ContextCompat.getColor(context, R.color.contact_us_black_38), context.resources.getDimensionPixelSize(R.dimen.sp_11), context)
            else -> null
        }

    }

}