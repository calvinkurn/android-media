package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.buyercancellationorder.R
import com.tokopedia.ordermanagement.buyercancellationorder.databinding.BottomsheetCancelItemBinding

/**
 * Created by fwidjaja on 11/06/20.
 */

class GetCancelReasonBottomSheetAdapter(private var listener: ActionListener): RecyclerView.Adapter<GetCancelReasonBottomSheetAdapter.ViewHolder>() {
    var listReason = listOf<String>()
    var currReason = ""

    interface ActionListener {
        fun onReasonClicked(reason: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BottomsheetCancelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listReason.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listReason.isNotEmpty()) {
            holder.bind(listReason[position])
        }
    }

    inner class ViewHolder(private val binding: BottomsheetCancelItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            with(binding) {
                labelCancel.text = data
                if (data.equals(currReason, true)) {
                    icGreenCheck.visible()
                } else {
                    icGreenCheck.gone()
                }
                root.setOnClickListener {
                    listener.onReasonClicked(listReason[position])
                }
            }
        }
    }
}