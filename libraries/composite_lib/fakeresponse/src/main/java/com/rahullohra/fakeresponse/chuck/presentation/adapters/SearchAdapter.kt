package com.rahullohra.fakeresponse.chuck.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.chuck.presentation.viewholder.SearchViewHolder

class SearchAdapter(val dataList: ArrayList<TransactionEntity>, val itemClickCallback: (TransactionEntity) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(SearchViewHolder.getLayout(), parent, false)
        return SearchViewHolder(v, itemClickCallback)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val searchVH = holder as SearchViewHolder
        searchVH.setData(dataList[position])
    }

}