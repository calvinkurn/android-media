package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetRejectItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-11-05.
 */
class SomBottomSheetRejectReasonsAdapter(private var listener: ActionListener) :
    RecyclerView.Adapter<SomBottomSheetRejectReasonsAdapter.ViewHolder>() {

    companion object {
        const val REJECT_REASON_PRODUCT_EMPTY = 1
        const val REJECT_REASON_SHOP_CLOSED = 4
        const val REJECT_REASON_COURIER_PROBLEMS = 7
        const val REJECT_REASON_OTHER_REASON = 14
        const val REJECT_REASON_BUYER_NO_RESPONSE = 15
    }

    var listRejectReasons = mutableListOf<SomReasonRejectData.Data.SomRejectReason>()

    interface ActionListener {
        fun onRejectReasonItemClick(rejectReason: SomReasonRejectData.Data.SomRejectReason)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_reject_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listRejectReasons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRejectReasons.getOrNull(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding<BottomsheetRejectItemBinding>()

        fun bind(element: SomReasonRejectData.Data.SomRejectReason?) {
            element?.run {
                binding?.run {
                    rbReject.visibility = View.GONE
                    labelReject.text = reasonText
                    root.setOnClickListener {
                        listener.onRejectReasonItemClick(element)
                    }
                }
            }
        }
    }
}