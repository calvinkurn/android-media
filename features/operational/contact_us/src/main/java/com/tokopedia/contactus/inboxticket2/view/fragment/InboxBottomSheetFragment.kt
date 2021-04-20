package com.tokopedia.contactus.inboxticket2.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter

abstract class InboxBottomSheetFragment : BottomSheetDialogFragment() {
    private var title: TextView? = null
    private var layoutID = 0
    abstract fun setAdapter(adapter: RecyclerView.Adapter<*>?)
    abstract fun setPresenter(presenter: InboxBasePresenter)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutID = arguments?.getInt(RESID, R.layout.layout_bottom_sheet_fragment) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(layoutID, container, false)
        title = contentView.findViewById(R.id.tv_bottom_sheet_title)
        title?.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(activity, R.drawable.contact_us_ic_close), null, null, null)
        dialog?.setOnShowListener { dialog: DialogInterface ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.let {
                BottomSheetBehavior.from(it).setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
        title?.setOnClickListener { closeBottomSheet() }
        return contentView
    }

    private fun closeBottomSheet() {
        if (activity is InboxDetailActivity) {
            ContactUsTracking.sendGTMInboxTicket(context, "",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickReason,
                    "Closing Reason Pop Up")
        } else {
            ContactUsTracking.sendGTMInboxTicket(context, "",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickFilter,
                    "Closing Status Pop Up")
        }
        dismiss()
    }

    companion object {
        private const val RESID = "RES_ID"
        fun getBottomSheetFragment(resID: Int): InboxBottomSheetFragment? {
            val fragment: InboxBottomSheetFragment = when (resID) {
                R.layout.layout_bottom_sheet_fragment -> {
                    BottomSheetListFragment()
                }
                else -> {
                    return null
                }
            }
            val bundle = Bundle()
            bundle.putInt(RESID, resID)
            fragment.arguments = bundle
            return fragment
        }
    }
}