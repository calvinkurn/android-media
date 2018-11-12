package com.tokopedia.kol.feature.report.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.tokopedia.design.component.EditTextCompat
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.report.view.model.ReportReasonViewModel

/**
 * @author by milhamj on 12/11/18.
 */
class ReportReasonAdapter : RecyclerView.Adapter<ReportReasonAdapter.ViewHolder>() {

    val list: MutableList<ReportReasonViewModel> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = list[position]

        holder.radio.setImageResource(
                if (model.isSelected) R.drawable.ic_radiobutton_normal
                else R.drawable.ic_radiobutton_selected
        )
        holder.reason.text = model.description

        if (model.type == context.getString(R.string.kol_reason_type_others)) {
            holder.reasonInput.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_reason, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    fun addAll(list: MutableList<ReportReasonViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radio: ImageView = itemView.findViewById(R.id.radio)
        val reason: TextView = itemView.findViewById(R.id.reason)
        val reasonInput: EditTextCompat = itemView.findViewById(R.id.reasonInput)
    }
}