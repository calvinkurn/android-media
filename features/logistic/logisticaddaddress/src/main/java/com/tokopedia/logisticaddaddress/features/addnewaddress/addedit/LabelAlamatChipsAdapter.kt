package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.chips_item.view.*

/**
 * Created by fwidjaja on 2019-06-21.
 */
class LabelAlamatChipsAdapter(private var actionListener: ActionListener)
    : RecyclerView.Adapter<LabelAlamatChipsAdapter.ViewHolder>() {

    private val labelAlamatList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chips_item, parent, false))
    }

    override fun getItemCount(): Int {
        return labelAlamatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ctx = holder.itemView.context
        holder.itemView.tv_chips_item.run {
            text = labelAlamatList[position]
            setTextColor(ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            setOnClickListener {
                setTextColor(ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_G300))
                labelAlamatList.getOrNull(position)?.let {
                    actionListener.onLabelAlamatChipClicked(it)
                }
            }
        }
    }

    fun submitList(addressLabels: List<String>) {
        labelAlamatList.clear()
        labelAlamatList.addAll(addressLabels)
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onLabelAlamatChipClicked(labelAlamat: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}