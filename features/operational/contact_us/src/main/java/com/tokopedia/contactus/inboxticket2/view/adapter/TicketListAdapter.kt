package com.tokopedia.contactus.inboxticket2.view.adapter

import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem
import com.tokopedia.contactus.inboxticket2.view.adapter.TicketListAdapter.TicketItemHolder
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListPresenter
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet.CloseServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

const val TICKET_TITLE_NORMAL = 2
const val CLOSED = 2
const val NEED_RATING = 1
const val IN_PROCESS = 1

class TicketListAdapter(private val itemList: MutableList<TicketsItem>,
                        private val mPresenter: InboxListPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val footerItem = TicketsItem()
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

    private fun getItem(position: Int): TicketsItem {
        return itemList[position]
    }

    fun add(item: TicketsItem) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    fun addAll(items: List<TicketsItem>) {
        for (item in items) {
            add(item)
        }
    }

    private fun remove(item: TicketsItem) {
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
        private var checkboxDelete: AppCompatCheckBox? = null
        private var tvTicketStatus: TextView? = null
        private var tvTicketTitle: TextView? = null
        private var tvTicketDesc: TextView? = null
        private var tvTicketDate: TextView? = null
        private var tvPrioritylabel: TextView? = null
        private var layoutItemTicket: ConstraintLayout? = null
        private var isOfficialStore = false
        private var servicePrioritiesBottomSheet: CloseableBottomSheetDialog? = null
        private fun findingViewsId(view: View) {
            checkboxDelete = view.findViewById(R.id.checkbox_delete)
            tvTicketStatus = view.findViewById(R.id.tv_ticket_status)
            tvTicketTitle = view.findViewById(R.id.tv_ticket_title)
            tvTicketDesc = view.findViewById(R.id.tv_ticket_desc)
            tvTicketDate = view.findViewById(R.id.tv_ticket_date)
            tvPrioritylabel = view.findViewById(R.id.tv_priority_label)
            layoutItemTicket = view.findViewById(R.id.layout_item_ticket)
        }

        fun bindViewHolder(item: TicketsItem) {
            val mContext = itemView.context
            val utils = Utils()
            if (item.readStatusId == TICKET_TITLE_NORMAL) {
                tvTicketDesc?.setTextColor(MethodChecker.getColor(mContext, com.tokopedia.design.R.color.black_38))
                tvTicketTitle?.setTypeface(null, Typeface.NORMAL)
            } else {
                tvTicketDesc?.setTextColor(MethodChecker.getColor(mContext, com.tokopedia.design.R.color.black_70))
                tvTicketTitle?.setTypeface(null, Typeface.BOLD)
            }
            tvTicketTitle?.text = item.subject
            tvTicketDesc?.text = item.lastMessagePlaintext
            tvTicketDate?.text = utils.getDateTimeYear(item.lastUpdate ?: "")
            if (item.statusId == IN_PROCESS) {
                MethodChecker.setBackground(tvTicketStatus, MethodChecker.getDrawable(mContext, R.drawable.rounded_rect_yellow))
                tvTicketStatus?.setText(R.string.on_going)
                tvTicketStatus?.setTextColor(MethodChecker.getColor(mContext, com.tokopedia.design.R.color.black_38))
            } else if (item.statusId == CLOSED && item.needRating != NEED_RATING) {
                MethodChecker.setBackground(tvTicketStatus, MethodChecker.getDrawable(mContext, R.drawable.rounded_rect_grey))
                tvTicketStatus?.setTextColor(MethodChecker.getColor(mContext, com.tokopedia.design.R.color.black_38))
                tvTicketStatus?.setText(R.string.closed)
            } else if (item.needRating == NEED_RATING) {
                MethodChecker.setBackground(tvTicketStatus, MethodChecker.getDrawable(mContext, R.drawable.rounded_rect_orange))
                tvTicketStatus?.setTextColor(MethodChecker.getColor(mContext, com.tokopedia.design.R.color.red_150))
                tvTicketStatus?.setText(R.string.need_rating)
            }
            if (item.isSelectableMode) checkboxDelete?.show() else checkboxDelete?.hide()
            if (!TextUtils.isEmpty(item.isOfficialStore) && item.isOfficialStore.equals("yes", ignoreCase = true)) {
                isOfficialStore = true
                tvPrioritylabel?.show()
                tvPrioritylabel?.setOnClickListener {
                    servicePrioritiesBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(mContext)
                    servicePrioritiesBottomSheet?.setCustomContentView(ServicePrioritiesBottomSheet(mContext, this@TicketItemHolder), "", false)
                    servicePrioritiesBottomSheet?.show()
                }
            } else {
                tvPrioritylabel?.hide()
                isOfficialStore = false
            }
            layoutItemTicket?.setOnClickListener { clickItem() }
            layoutItemTicket?.setOnLongClickListener { false }
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
    }

    private class FooterViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    companion object {
        private const val ITEM = 1
        private const val FOOTER = 2
    }

}