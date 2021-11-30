package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomsheetRejectItemBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-08.
 */
class SomBottomSheetRejectOrderAdapter(
    private var listener: ActionListener,
    private var hasRadioBtn: Boolean
) : RecyclerView.Adapter<SomBottomSheetRejectOrderAdapter.ViewHolder>() {
    var mapKey = HashMap<String, String>()

    interface ActionListener {
        fun onBottomSheetItemClick(key: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_reject_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mapKey.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.run {
            val arrayValues = mapKey.values.toMutableList()
            val arrayKeys = mapKey.keys.toMutableList()
            labelReject.text = arrayValues[position]

            if (!hasRadioBtn) {
                rbReject.visibility = View.GONE
            } else {
                rbReject.visibility = View.VISIBLE
            }

            root.setOnClickListener {
                listener.onBottomSheetItemClick(arrayKeys[position])
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding by viewBinding<BottomsheetRejectItemBinding>()
    }
}