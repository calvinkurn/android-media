package com.tokopedia.logisticaddaddress.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.databinding.ChipsUnifyItemBinding
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by fwidjaja on 2019-06-21.
 */
class LabelAlamatChipsAdapter(private var actionListener: ActionListener) :
    RecyclerView.Adapter<LabelAlamatChipsAdapter.ViewHolder>() {

    private val labelAlamatList = mutableListOf<Pair<String, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChipsUnifyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return labelAlamatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        labelAlamatList.getOrNull(position)?.apply {
            holder.bind(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(addressLabels: List<Pair<String, Boolean>>) {
        labelAlamatList.clear()
        labelAlamatList.addAll(addressLabels)
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onLabelAlamatChipClicked(labelAlamat: String)
    }

    inner class ViewHolder(
        private val binding: ChipsUnifyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(label: Pair<String, Boolean>) {
            binding.chipsItem.apply {
                chipText = label.first
                chipType = if (!label.second) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
                chipSize = ChipsUnify.SIZE_MEDIUM
                setOnClickListener {
                    labelAlamatList.getOrNull(position)?.let {
                        actionListener.onLabelAlamatChipClicked(it.first)
                    }
                }
            }
        }
    }
}
