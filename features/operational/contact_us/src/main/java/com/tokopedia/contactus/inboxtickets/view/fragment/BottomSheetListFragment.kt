package com.tokopedia.contactus.inboxtickets.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.contactus.R

class BottomSheetListFragment : InboxBottomSheetFragment() {
    private var rvBottomSheet: VerticalRecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        mAdapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        rvBottomSheet = rootView?.findViewById(R.id.rv_filter)
        rvBottomSheet?.adapter = mAdapter
        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialog)
        return super.onCreateDialog(savedInstanceState)
    }
}
