package com.rahullohra.fakeresponse.presentaiton.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.presentaiton.viewholder.ResponseVH

class GqlRvAdapter(val dataList: ArrayList<ResponseListData>, val itemClickCallback: (Int,Boolean) -> Unit) :
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