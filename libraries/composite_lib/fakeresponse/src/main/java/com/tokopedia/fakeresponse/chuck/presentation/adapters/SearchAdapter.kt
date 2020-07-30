package com.tokopedia.fakeresponse.chuck.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.presentation.viewholder.SearchViewHolder
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.data.models.SearchType
import com.tokopedia.fakeresponse.presentation.viewholder.ResponseVH

class SearchAdapter(val dataList: ArrayList<SearchType>,
                    val chuckClickCallback: (SearchType) -> Unit,
                    val recordClickCallback: (SearchType, Boolean) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        when (viewType) {
            SearchViewHolder.getLayout() -> return SearchViewHolder(v, chuckClickCallback)
            else -> return ResponseVH(v, recordClickCallback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList[position] is TransactionEntity) return SearchViewHolder.getLayout()
        else return ResponseVH.getLayout()
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SearchViewHolder-> holder.setData(dataList[position] as TransactionEntity)
            is ResponseVH-> holder.setData(dataList[position] as ResponseListData)
            else->{}
        }
    }

}