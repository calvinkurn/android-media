package com.tokopedia.manageaddress.ui.shoplocation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.domain.model.shoplocation.Warehouse
import kotlinx.android.synthetic.main.bottomsheet_action_shop_address.view.*
import kotlinx.android.synthetic.main.card_shop_location_address.view.*

class ShopLocationItemAdapter(private val listener: ShopLocationItemAdapterListener) : RecyclerView.Adapter<ShopLocationItemAdapter.ShopLocationViewHolder>() {

    var shopLocationList = mutableListOf<Warehouse>()

    interface ShopLocationItemAdapterListener {
        fun onShopLocationStateStatusClicked(data: Warehouse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopLocationViewHolder {
        return ShopLocationViewHolder(parent.inflateLayout(R.layout.card_shop_location_address), listener)
    }

    override fun getItemCount(): Int {
        return shopLocationList.size
    }

    override fun onBindViewHolder(holder: ShopLocationViewHolder, position: Int) {
        holder.bindData(shopLocationList[position])
    }

    fun addList(data: List<Warehouse>) {
        shopLocationList.addAll(data)
    }

    inner class ShopLocationViewHolder(itemView: View, private val listener: ShopLocationItemAdapterListener) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Warehouse) {
            with(itemView) {
                setPinpointStatus(data)
                setHeadquarter(data)
                tv_shop_name.text = data.warehouseName
                tv_address_detail.text = data.addressDetail
                tv_address_city.text = "${data.cityName}, ${data.districtName}"
                tv_address_zipcode.text = data.postalCode
                setListener(data)

            }
        }

        private fun setHeadquarter(shopLocation: Warehouse) {
            if (shopLocation.warehouseType == 1) {
                itemView.lbl_main_shop.visibility = View.VISIBLE
            } else {
                itemView.lbl_main_shop.visibility = View.GONE
            }
        }

        private fun setPinpointStatus(shopLocation: Warehouse) {
            if (shopLocation.latLon.isNullOrEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pinpoint)
                itemView.img_location_state.setImageDrawable(icon)
                itemView.tv_pinpoint_state.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pinpoint_green)
                itemView.img_location_state.setImageDrawable(icon)
                itemView.tv_pinpoint_state.text = itemView.context.getString(R.string.pinpoint)
            }
        }

        private fun setListener(shopLocation: Warehouse) {
            itemView.btn_set_location_status.setOnClickListener {
                listener.onShopLocationStateStatusClicked(shopLocation)
            }
        }
    }
}