package com.tokopedia.manageaddress.ui.shoplocation

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopLocationItemAdapter(private val listener: ShopLocationItemAdapterListener) : RecyclerView.Adapter<ShopLocationItemAdapter.ShopLocationViewHolder>() {


    interface ShopLocationItemAdapterListener {
        fun onShopLocationAddressTypeCllicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopLocationViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ShopLocationViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ShopLocationViewHolder(itemView: View, private val listener: ShopLocationItemAdapterListener) : RecyclerView.ViewHolder(itemView) {

    }
}