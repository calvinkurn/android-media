package com.tokopedia.topads.view.adapter.adstat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.view.datamodel.AdStatModel
import com.tokopedia.topads.create.databinding.AdGroupStatItemBinding

class AdStatAdapter : RecyclerView.Adapter<AdStatItemViewHolder>() {
    private var list:List<AdStatModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdStatItemViewHolder {
        val binding = AdGroupStatItemBinding.inflate(LayoutInflater.from(parent.context))
        return AdStatItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdStatItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(adStatList:List<AdStatModel>){
        list = adStatList
        notifyDataSetChanged()
    }

    fun isViewholderInLoadingState(index:Int) : Boolean{
        if(index < itemCount){
            return list[index].loading
        }
        return false
    }
}
