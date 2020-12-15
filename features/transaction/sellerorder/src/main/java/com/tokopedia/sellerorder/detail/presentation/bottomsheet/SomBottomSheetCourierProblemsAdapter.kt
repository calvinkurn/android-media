package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import kotlinx.android.synthetic.main.bottomsheet_reject_item.view.*



/**
 * Created by fwidjaja on 2019-11-07.
 */
class SomBottomSheetCourierProblemsAdapter(private var listener: ActionListener): RecyclerView.Adapter<SomBottomSheetCourierProblemsAdapter.ViewHolder>() {
    var listChildCourierProblems = mutableListOf<SomReasonRejectData.Data.SomRejectReason.Child>()
    var reasonCode = ""
    var selectedRadio = -1

    interface ActionListener {
        fun onChooseOptionCourierProblem(optionCourierProblem: SomReasonRejectData.Data.SomRejectReason.Child)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_reject_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listChildCourierProblems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.rb_reject.visibility = View.VISIBLE
        holder.itemView.label_reject.text = listChildCourierProblems[position].reasonText
        holder.itemView.rb_reject.setOnCheckedChangeListener(null)
        holder.itemView.rb_reject.isChecked = position == selectedRadio
        holder.itemView.rb_reject.setOnCheckedChangeListener { buttonView, isChecked ->
            selectItem(position)
        }

        holder.itemView.rl_reject_item.setOnClickListener {
            selectItem(position)
        }
    }

    private fun selectItem(position: Int) {
        selectedRadio = position
        listener.onChooseOptionCourierProblem(listChildCourierProblems[position])
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}