package com.tokopedia.chatbot.view.customview


import android.app.Activity
import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.databinding.BottomSheetReasonBinding
import com.tokopedia.chatbot.view.adapter.ReasonAdapter
import java.util.*

/**
 * @author by nisie on 6/11/18.
 */
class ReasonBottomSheet(private val activity: Activity,
                        private val reasonList: ArrayList<String>,
                        private val onClickReasonRating: (String) -> Unit)
    : BottomSheetDialog(activity) {
    private var reasonRecyclerView: RecyclerView? = null
    private var closeIcon: ImageView? = null

    init {
        init()
    }

    private fun init() {
        val bottomSheetView = BottomSheetReasonBinding.inflate(LayoutInflater.from(activity.baseContext))
        setContentView(bottomSheetView.root)

        reasonRecyclerView = bottomSheetView.reasonRv
        closeIcon = bottomSheetView.closeIcon

        reasonRecyclerView!!.isFocusable = false
        closeIcon!!.setOnClickListener { this@ReasonBottomSheet.dismiss() }

        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager
                .VERTICAL, false)
        reasonRecyclerView!!.layoutManager = mLayoutManager

        val adapter = ReasonAdapter(onClickReasonRating)
        adapter.addList(reasonList)
        reasonRecyclerView!!.adapter = adapter

    }

    companion object {

        fun createInstance(activity: Activity,
                           reasons: ArrayList<String>,
                           onClickReasonRating: (String) -> Unit): ReasonBottomSheet {
            return ReasonBottomSheet(activity, reasons, onClickReasonRating)
        }
    }
}
