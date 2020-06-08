package com.tokopedia.fakeresponse.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.data.models.SearchType
import com.tokopedia.fakeresponse.presentation.viewholder.ResponseVH

class GqlRvAdapter(val dataList: ArrayList<ResponseListData>, val itemClickCallback: (SearchType, Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(ResponseVH.getLayout(), parent, false)
        return ResponseVH(v,itemClickCallback)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val responseVH = holder as ResponseVH
        responseVH.setData(dataList[position])
    }

}