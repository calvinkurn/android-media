package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.buyercancellationorder.R
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.databinding.BottomsheetCancelItemBinding

/**
 * Created by fwidjaja on 11/06/20.
 */

class GetCancelSubReasonBottomSheetAdapter(private var listener: ActionListener) :
    RecyclerView.Adapter<GetCancelSubReasonBottomSheetAdapter.ViewHolder>() {
    var listSubReason =
        listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem.SubReasonsItem>()
    var currReasonCode = -1

    interface ActionListener {
        fun onSubReasonClicked(rCode: Int, reason: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BottomsheetCancelItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return listSubReason.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listSubReason.isNotEmpty()) {
            holder.bind(listSubReason[position])
        }
    }

    inner class ViewHolder(private val binding: BottomsheetCancelItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem.SubReasonsItem) {
            with(binding) {
                labelCancel.text = data.reason
                if (data.rCode == currReasonCode) {
                    icGreenCheck.visible()
                } else {
                    icGreenCheck.gone()
                }

                root.setOnClickListener {
                    listener.onSubReasonClicked(
                        data.rCode,
                        data.reason
                    )
                }
            }
        }
    }
}