package com.tokopedia.chatbot.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.R
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 6/11/18.
 */
class ReasonAdapter @Inject
constructor(private val onClickReasonRating: (String) -> Unit)
    : RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder>() {

    private val reasonList: MutableList<String>

    init {
        reasonList = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReasonViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_reason, viewGroup, false)

        return ReasonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        val reason = reasonList[position]
        if (!isNullOrEmpty(reason)) {
            holder.reason.text = MethodChecker.fromHtml(reason)
        }
    }


    private fun isNullOrEmpty(string: String?): Boolean {
        return string == null || string.equals(other = "null", ignoreCase = true)
                || string.isEmpty()
    }

    override fun getItemCount(): Int {
        return reasonList.size
    }

    fun addList(reasonList: ArrayList<String>) {
        this.reasonList.clear()
        this.reasonList.addAll(reasonList)
    }

    inner class ReasonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var reason: TextView = itemView.findViewById(R.id.reason)

        init {
            reason.setOnClickListener { onClickReasonRating(reasonList[adapterPosition]) }
        }
    }

}
