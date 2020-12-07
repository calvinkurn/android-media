package com.tokopedia.manageaddress.ui.shoplocation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.domain.model.shoplocation.Warehouse
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_shop_location_address.view.*

class ShopLocationItemAdapter(private val listener: ShopLocationItemAdapterListener) : RecyclerView.Adapter<ShopLocationItemAdapter.ShopLocationViewHolder>() {

    var shopLocationList = mutableListOf<Warehouse>()

    interface ShopLocationItemAdapterListener {
        fun onShopLocationStateStatusClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopLocationViewHolder {
        return ShopLocationViewHolder(parent.inflateLayout(R.layout.item_shop_location_address), listener)
    }

    override fun getItemCount(): Int {
        return shopLocationList.size
    }

    override fun onBindViewHolder(holder: ShopLocationViewHolder, position: Int) {
        holder.bindData(shopLocationList[position])
    }

    inner class ShopLocationViewHolder(itemView: View, private val listener: ShopLocationItemAdapterListener) : RecyclerView.ViewHolder(itemView) {

        private val pinPointImage = itemView.findViewById<ImageView>(R.id.img_location_state)
        private val pinPointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        private val setLocationStateStatus = itemView.findViewById<Typography>(R.id.btn_set_location_status)

        @SuppressLint("SetTextI18n")
        fun bindData(data: Warehouse) {
            with(itemView) {
                setPinpointStatus(data)
                tv_shop_name.text = data.warehouseName
                tv_address_detail.text = data.addressDetail
                tv_address_city.text = "${data.cityName}, ${data.districtName}"
                tv_address_zipcode.text = data.postalCode
                setListener(data)

            }
        }

        private fun setHeadquarter(shopLocation: Warehouse) {

        }

        private fun setPinpointStatus(shopLocation: Warehouse) {
            if (shopLocation.latLon.isNullOrEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pinpoint)
                pinPointImage.setImageDrawable(icon)
                pinPointText.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pinpoint_green)
                pinPointImage.setImageDrawable(icon)
                pinPointText.text = itemView.context.getString(R.string.pinpoint)
            }
        }

        private fun setListener(shopLoaction: Warehouse) {
            setLocationStateStatus.setOnClickListener {
                listener.onShopLocationStateStatusClicked()
            }
        }
    }
}