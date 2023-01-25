package com.tokopedia.kol.feature.report.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.model.ReportReasonUiModel

/**
 * @author by milhamj on 12/11/18.
 */
class ReportReasonAdapter(val view: ContentReportContract.View) :
    RecyclerView.Adapter<ReportReasonAdapter.ViewHolder>() {

    private val list: MutableList<ReportReasonUiModel> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        if (model.isSelected && model.type != getCustomTypeString()) {
            view.enableSendBtn()
        }

        MethodChecker.getDrawable(
            holder.radio.getContext(),
            if (model.isSelected) {
                com.tokopedia.design.R.drawable.ic_radiobutton_selected
            } else {
                com.tokopedia.design.R.drawable.ic_radiobutton_normal
            }
        )?.apply {
            holder.radio.setImageDrawable(this)
        }
        holder.reason.text = model.description

        val itemClicked = { itemPosition: Int ->
            list.forEachIndexed { index, item ->
                item.isSelected = index == itemPosition
                if (item.isSelected && item.type != getCustomTypeString()) {
                    view.hideKeyboard()
                }
            }
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener { itemClicked(holder.adapterPosition) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report_reason, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    fun addAll(list: MutableList<ReportReasonUiModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setCustomTypeSelected() {
        list.forEach {
            it.isSelected = it.type == getCustomTypeString()
        }
        notifyDataSetChanged()
    }

    fun getSelectedItem(): ReportReasonUiModel = list.first { it.isSelected }

    fun getCustomTypeString(): String {
        return view.getContext()?.getString(R.string.kol_reason_type_others) ?: ""
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radio: ImageView = itemView.findViewById(R.id.radio)
        val reason: TextView = itemView.findViewById(R.id.reason)
    }
}
