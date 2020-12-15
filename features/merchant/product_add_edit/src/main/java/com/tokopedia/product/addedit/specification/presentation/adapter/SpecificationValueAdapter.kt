package com.tokopedia.product.addedit.specification.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.specification.presentation.adapter.viewholder.SpecificationValueViewHolder
import com.tokopedia.product.addedit.specification.presentation.dialog.SpecificationDataBottomSheet

class SpecificationValueAdapter(
        private val onSpecificationValueAdapterClickListener: OnSpecificationValueAdapterClickListener,
        private val fragmentManager: FragmentManager?
) :
        RecyclerView.Adapter<SpecificationValueViewHolder>(),
        SpecificationValueViewHolder.OnSpecificationValueViewHolderClickListener,
        SpecificationDataBottomSheet.SpecificationDataListener {

    interface OnSpecificationValueAdapterClickListener {
        fun onSpecificationValueTextClicked(position: Int)
    }

    private var items: MutableList<String> = mutableListOf()
    private var editedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationValueViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_specification_value, parent, false)
        return SpecificationValueViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpecificationValueViewHolder, position: Int) {
        val item = getData(position)
        holder.bindData(item, item.reversed())
    }

    fun setData(items: List<String>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun setData(position: Int, item: String) {
        if (position in 0 until itemCount) {
            this.items[position] = item
            notifyItemChanged(position)
        }
    }

    fun addData(item: String) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun getData(position: Int): String {
        return items.getOrNull(position).orEmpty()
    }

    override fun onSpecificationValueTextClicked(position: Int) {
        val dummy = mutableListOf("nomo 1", "nomo 2", "sa", "asas", "asasa", "asasas", "asasas", "asasd", "nomo 1", "nomo 2", "sa", "asas", "asasa", "asasas", "asasas", "asasd", "nomo 1", "nomo 2", "sa", "asas", "asasa", "asasas", "asasas", "asasd")
        val bottomSheet = SpecificationDataBottomSheet("Jenis", dummy, this)

        editedPosition = position
        bottomSheet.show(fragmentManager)
        onSpecificationValueAdapterClickListener.onSpecificationValueTextClicked(position)
    }

    override fun onSpecificationDataSelected(position: Int, specificationData: String) {
        editedPosition?.let {
            setData(it, specificationData)
        }
    }
}