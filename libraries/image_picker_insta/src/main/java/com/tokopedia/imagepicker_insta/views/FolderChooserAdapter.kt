package com.tokopedia.imagepicker_insta.views

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FolderChooserAdapter(val dataList:List<String>): RecyclerView.Adapter<FolderChooserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderChooserViewHolder {
        return FolderChooserViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: FolderChooserViewHolder, position: Int) {
        holder.setData(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}