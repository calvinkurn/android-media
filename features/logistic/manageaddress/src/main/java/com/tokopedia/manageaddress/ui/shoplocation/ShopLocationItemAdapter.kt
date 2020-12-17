package com.tokopedia.manageaddress.ui.shoplocation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ShopLocationItemAdapter(private val listener: ShopLocationItemAdapterListener) : RecyclerView.Adapter<ShopLocationItemAdapter.ShopLocationViewHolder>() {

    var shopLocationList = mutableListOf<Warehouse>()

    interface ShopLocationItemAdapterListener {
        fun onShopLocationStateStatusClicked(data: Warehouse)
        fun onShopEditAddress(data: Warehouse)
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
        notifyDataSetChanged()
    }

    inner class ShopLocationViewHolder(itemView: View, private val listener: ShopLocationItemAdapterListener) : RecyclerView.ViewHolder(itemView) {

        private val tvShopName = itemView.findViewById<Typography>(R.id.tv_shop_name)
        private val tvAddressDetail = itemView.findViewById<Typography>(R.id.tv_address_detail)
        private val tvAddressCity = itemView.findViewById<Typography>(R.id.tv_address_city)
        private val tvAddressZipCode = itemView.findViewById<Typography>(R.id.tv_address_zipcode)
        private val labelMainShop = itemView.findViewById<Label>(R.id.lbl_main_shop)
        private val imgPinpointState = itemView.findViewById<ImageView>(R.id.img_location_state)
        private val tvPinpointState = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        private val btnSetLocation = itemView.findViewById<IconUnify>(R.id.icon_kebab)
        private val btnEditocation = itemView.findViewById<Typography>(R.id.action_edit)

        @SuppressLint("SetTextI18n")
        fun bindData(data: Warehouse) {
            setPinpointStatus(data)
            setHeadquarter(data)
            tvShopName.text = data.warehouseName
            tvAddressDetail.text = data.addressDetail
            tvAddressCity.text = "${data.cityName}, ${data.districtName}"
            tvAddressZipCode.text = data.postalCode
            setListener(data)
        }

        private fun setHeadquarter(shopLocation: Warehouse) {
            if (shopLocation.warehouseType == 1) {
                labelMainShop.visibility = View.VISIBLE
            } else {
                labelMainShop.visibility = View.GONE
            }
        }

        private fun setPinpointStatus(shopLocation: Warehouse) {
            if (shopLocation.latLon.isNullOrEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pinpoint)
                imgPinpointState.setImageDrawable(icon)
                tvPinpointState.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pinpoint_green)
                imgPinpointState.setImageDrawable(icon)
                tvPinpointState.text = itemView.context.getString(R.string.pinpoint)
            }
        }

        private fun setListener(shopLocation: Warehouse) {
            btnSetLocation.setOnClickListener {
                listener.onShopLocationStateStatusClicked(shopLocation)
            }
            btnEditocation.setOnClickListener {
                listener.onShopEditAddress(shopLocation)
            }
        }
    }
}