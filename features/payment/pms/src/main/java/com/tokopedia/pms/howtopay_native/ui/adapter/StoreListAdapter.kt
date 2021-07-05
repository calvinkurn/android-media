package com.tokopedia.pms.howtopay_native.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.data.model.StoreData
import com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder.StoreDataViewHolder

class StoreListAdapter(private val storeDataList: ArrayList<StoreData>) : RecyclerView.Adapter<StoreDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pms_hwp_item_store_info,
                parent, false)
        return StoreDataViewHolder(view)
    }

    override fun getItemCount(): Int {
        return storeDataList.size
    }

    override fun onBindViewHolder(holder: StoreDataViewHolder, position: Int) {
        holder.bind(storeDataList[position])
    }
}