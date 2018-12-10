package com.tokopedia.chatbot.view.customview

import android.app.Activity
import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView


import com.tokopedia.chatbot.view.adapter.ReasonAdapter

import java.util.ArrayList

/**
 * @author by nisie on 6/11/18.
 */
class ReasonBottomSheet(private val context: Context, private val reasonList: ArrayList<String>,
                        private val listener: ReasonAdapter.OnReasonClickListener) : BottomSheetDialog(context) {
    private var reasonRecyclerView: RecyclerView? = null
    private var closeIcon: ImageView? = null

    init {
        init()
    }

    private fun init() {
        val bottomSheetView = (context as Activity).layoutInflater.inflate(R.layout
                .bottom_sheet_reason, null)
        setContentView(bottomSheetView)

        reasonRecyclerView = bottomSheetView.findViewById(R.id.reason_rv)
        closeIcon = bottomSheetView.findViewById(R.id.close_icon)

        reasonRecyclerView!!.isFocusable = false
        closeIcon!!.setOnClickListener { this@ReasonBottomSheet.dismiss() }

        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager
                .VERTICAL, false)
        reasonRecyclerView!!.layoutManager = mLayoutManager

        val adapter = ReasonAdapter(listener)
        adapter.addList(reasonList)
        reasonRecyclerView!!.adapter = adapter

    }

    companion object {

        fun createInstance(activity: Activity,
                           reasons: ArrayList<String>,
                           listener: ReasonAdapter.OnReasonClickListener): ReasonBottomSheet {
            return ReasonBottomSheet(activity, reasons, listener)
        }
    }
}
