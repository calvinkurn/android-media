package com.tokopedia.product_bundle.single.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem

class SingleProductBundleAdapter : RecyclerView.Adapter<SingleProductBundleViewHolder>() {

    private var data: List<SingleProductBundleItem> = listOf()
    private var checkedStatus: MutableList<Boolean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleProductBundleViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_single_product_bundle, parent, false)
        val viewHolder = SingleProductBundleViewHolder(rootView)
        setupViewHolderOnClick(viewHolder)
        return viewHolder
    }

    private fun setupViewHolderOnClick(viewHolder: SingleProductBundleViewHolder) {
        viewHolder.layoutItem.setOnClickListener {
            viewHolder.radioItem.isChecked = true
            checkedStatus = MutableList(data.size) { false }
            checkedStatus[viewHolder.adapterPosition] = true
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SingleProductBundleViewHolder, position: Int) {
        val data = data[position]
        val isChecked = checkedStatus[position]
        holder.bindData(data, isChecked)
    }

    fun setData(data: List<SingleProductBundleItem>) {
        this.data = data
        this.checkedStatus = MutableList(data.size) { false }
        notifyDataSetChanged()
    }
}