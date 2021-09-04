package com.tokopedia.createpost.view.plist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.createpost.R


class ShopProductSortAdapter(private val dataSet: List<ShopPagePListSortItem>) :
    RecyclerView.Adapter<ShopProductSortAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rbSort: RadioButton = view.findViewById(R.id.rb_sort)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cp_row_shop_page_sort_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.rbSort.text = dataSet[position].name

        viewHolder.rbSort.setOnCheckedChangeListener { compoundButton, b ->
            dataSet[position].isSelected = b;
            notifyDataSetChanged()
            //TODO this result to back
        }


    }

    override fun getItemCount() = dataSet.size
}

