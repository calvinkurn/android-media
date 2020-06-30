package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import kotlinx.android.synthetic.main.bottomsheet_cancel_item.view.*

/**
 * Created by fwidjaja on 11/06/20.
 */

class GetCancelSubReasonBottomSheetAdapter(private var listener: ActionListener): RecyclerView.Adapter<GetCancelSubReasonBottomSheetAdapter.ViewHolder>() {
    var listSubReason = listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem.SubReasonsItem>()

    interface ActionListener {
        fun onSubReasonClicked(rCode: Int, reason: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_cancel_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listSubReason.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_cancel.text = listSubReason[position].reason

        holder.itemView.setOnClickListener {
            listener.onSubReasonClicked(listSubReason[position].rCode, listSubReason[position].reason)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}