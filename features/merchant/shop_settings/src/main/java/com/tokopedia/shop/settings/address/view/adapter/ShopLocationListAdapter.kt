package com.tokopedia.shop.settings.address.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel

class ShopLocationListAdapter : RecyclerView.Adapter<ShopLocationListAdapter.ShopLocationViewHolder>() {

    private val locationList: MutableList<ShopLocationModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopLocationViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount() = locationList.size

    override fun onBindViewHolder(holder: ShopLocationViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ShopLocationViewHolder(private val view: View): RecyclerView.ViewHolder(view){

    }
}