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
 * Created by fwidjaja on 2019-11-07.
 */
class SomBottomSheetCourierProblemsAdapter(private var listener: ActionListener) :
    RecyclerView.Adapter<SomBottomSheetCourierProblemsAdapter.ViewHolder>() {
    var listChildCourierProblems = mutableListOf<SomReasonRejectData.Data.SomRejectReason.Child>()
    var selectedRadio = RecyclerView.NO_POSITION

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
        holder.bind(listChildCourierProblems.getOrNull(position))
    }

    private fun selectItem(position: Int) {
        selectedRadio = position
        listener.onChooseOptionCourierProblem(listChildCourierProblems[position])
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding<BottomsheetRejectItemBinding>()

        fun bind(element: SomReasonRejectData.Data.SomRejectReason.Child?) {
            element?.run {
                binding?.run {
                    rbReject.visibility = View.VISIBLE
                    labelReject.text = reasonText
                    rbReject.setOnCheckedChangeListener(null)
                    rbReject.isChecked = adapterPosition == selectedRadio
                    rbReject.setOnCheckedChangeListener { buttonView, isChecked ->
                        selectItem(adapterPosition)
                    }

                    rlRejectItem.setOnClickListener {
                        selectItem(adapterPosition)
                    }
                }
            }
        }
    }
}