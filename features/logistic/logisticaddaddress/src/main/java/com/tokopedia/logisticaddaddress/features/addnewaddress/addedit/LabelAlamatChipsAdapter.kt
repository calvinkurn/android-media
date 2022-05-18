package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.chips_item.view.*
import kotlinx.android.synthetic.main.chips_unify_item.view.*

/**
 * Created by fwidjaja on 2019-06-21.
 */
class LabelAlamatChipsAdapter(private var actionListener: ActionListener)
    : RecyclerView.Adapter<LabelAlamatChipsAdapter.ViewHolder>() {

    private val labelAlamatList = mutableListOf<Pair<String, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chips_unify_item, parent, false))
    }

    override fun getItemCount(): Int {
        return labelAlamatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = labelAlamatList[position]
        holder.itemView.chips_item.run {
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

    fun submitList(addressLabels: List<Pair<String, Boolean>>) {
        labelAlamatList.clear()
        labelAlamatList.addAll(addressLabels)
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onLabelAlamatChipClicked(labelAlamat: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}