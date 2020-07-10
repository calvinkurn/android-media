package com.tokopedia.contactus.inboxticket2.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListPresenter
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet.CloseServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

private const val CLOSED = "closed"
private const val NEED_RATING = 1
private const val NEW = "new"
private const val OPEN = "open"
private const val SOLVED = "solved"
private const val RATING_CLOSED = 0
private const val READ = "read"
private const val UNREAD = "unread"

class TicketListAdapter(private val itemList: MutableList<InboxTicketListResponse.Ticket.Data.TicketItem>,
                        private val mPresenter: InboxListPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val footerItem = InboxTicketListResponse.Ticket.Data.TicketItem()
    private var isFooterAdded = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v: View
        return when (viewType) {
            FOOTER -> {
                v = inflater.inflate(R.layout.inbox_footer_layout, parent, false)
                FooterViewHolder(v)
            }
            else -> {
                v = inflater.inflate(R.layout.layout_item_ticket, parent, false)
                TicketItemHolder(v)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> (holder as TicketItemHolder).bindViewHolder(itemList[position])
            FOOTER -> {
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position) && isFooterAdded) FOOTER else ITEM
    }

    fun addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true
            add(footerItem)
        }
    }

    private fun getItem(position: Int): InboxTicketListResponse.Ticket.Data.TicketItem {
        return itemList[position]
    }

    fun add(item: InboxTicketListResponse.Ticket.Data.TicketItem) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    private fun remove(item: InboxTicketListResponse.Ticket.Data.TicketItem) {
        val position = itemList.indexOf(item)
        if (position > -1) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isFooterAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemList.isEmpty()

    private fun isLastPosition(position: Int): Boolean {
        return position == itemList.size - 1
    }

    fun removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false
            val position = itemList.size - 1
            val item = getItem(position)
            if (item === footerItem) {
                itemList.removeAt(position)
                notifyItemRemoved(position)
            }
            notifyDataSetChanged()
            mPresenter.scrollList()
        }
    }

    internal inner class TicketItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), CloseServicePrioritiesBottomSheet {
        private var tvTicketStatus: Typography? = null
        private var tvTicketTitle: Typography? = null
        private var tvTicketDate: Typography? = null
        private var ticketId: Typography? = null
        private var group: Group? = null
        private var layoutItemTicket: ConstraintLayout? = null
        private var isOfficialStore = false
        private var servicePrioritiesBottomSheet: CloseableBottomSheetDialog? = null
        private fun findingViewsId(view: View) {
            tvTicketStatus = view.findViewById(R.id.tv_ticket_status)
            tvTicketTitle = view.findViewById(R.id.tv_ticket_title)
            tvTicketDate = view.findViewById(R.id.tv_ticket_date)
            ticketId = view.findViewById(R.id.ticketId)
            layoutItemTicket = view.findViewById(R.id.layout_item_ticket)
            group = view.findViewById(R.id.group)
        }

        fun bindViewHolder(item: InboxTicketListResponse.Ticket.Data.TicketItem) {
            val mContext = itemView.context
            val utils = Utils()
            if (UNREAD.equals(item.readStatus, true)) {
                layoutItemTicket?.setBackgroundColor(MethodChecker.getColor(mContext, R.color.contact_us_unread_ticket_background))
                tvTicketTitle?.setWeight(Typography.BOLD)
            } else if (READ.equals(item.readStatus, true)) {
                layoutItemTicket?.setBackgroundColor(MethodChecker.getColor(mContext, com.tokopedia.abstraction.R.color.white))
                tvTicketTitle?.setWeight(Typography.REGULAR)
            }
            tvTicketTitle?.text = item.subject
            ticketId?.text = String.format("ID: %s", item.caseNumber)
            tvTicketDate?.text = utils.getDateTimeYear(item.lastUpdate ?: "")
            if (NEW.equals(item.status, true) || OPEN.equals(item.status, true) || SOLVED.equals(item.status, true)) {
                MethodChecker.setBackground(tvTicketStatus, MethodChecker.getDrawable(mContext, R.drawable.rounded_rect_yellow))
                tvTicketStatus?.setText(R.string.on_going)
                tvTicketStatus?.setTextColor(MethodChecker.getColor(mContext, R.color.contact_us_in_process))
            } else if (CLOSED.equals(item.status, true) && item.needRating == RATING_CLOSED) {
                MethodChecker.setBackground(tvTicketStatus, MethodChecker.getDrawable(mContext, R.drawable.rounded_rect_grey))
                tvTicketStatus?.setTextColor(MethodChecker.getColor(mContext, R.color.contact_us_closed))
                tvTicketStatus?.setText(R.string.closed)
            } else if (CLOSED.equals(item.status, true) && item.needRating == NEED_RATING) {
                MethodChecker.setBackground(tvTicketStatus, MethodChecker.getDrawable(mContext, R.drawable.rounded_rect_pink))
                tvTicketStatus?.setTextColor(MethodChecker.getColor(mContext, R.color.contact_us_need_rating))
                tvTicketStatus?.setText(R.string.need_rating)
            }
            if (item.isOfficialStore) {
                isOfficialStore = true
                group?.show()
                itemView.post {
                    group?.setAllOnClickListener{
                        servicePrioritiesBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(mContext)
                        servicePrioritiesBottomSheet?.setCustomContentView(ServicePrioritiesBottomSheet(mContext, this@TicketItemHolder), "", false)
                        servicePrioritiesBottomSheet?.show()
                    }
                }
            } else {
                group?.hide()
                isOfficialStore = false
            }
            layoutItemTicket?.setOnClickListener { clickItem() }
        }

        private fun clickItem() {
            mPresenter.onClickTicket(adapterPosition, isOfficialStore)
        }

        override fun onClickClose() {
            servicePrioritiesBottomSheet?.dismiss()
        }

        init {
            findingViewsId(itemView)
        }

        private fun Group.setAllOnClickListener(listener: (View) -> Unit) {
            referencedIds.forEach { id ->
                itemView.findViewById<View>(id)?.setOnClickListener(listener)
            }
        }
    }

    private class FooterViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    companion object {
        private const val ITEM = 1
        private const val FOOTER = 2
    }

}