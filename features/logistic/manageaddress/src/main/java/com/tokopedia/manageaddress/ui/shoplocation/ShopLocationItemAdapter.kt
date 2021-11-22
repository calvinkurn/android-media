package com.tokopedia.manageaddress.ui.shoplocation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.CardShopLocationAddressBinding
import com.tokopedia.manageaddress.util.ShopLocationConstant
import com.tokopedia.manageaddress.util.ShopLocationConstant.TICKER_LABEL
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

class ShopLocationItemAdapter(private val listener: ShopLocationItemAdapterListener) : RecyclerView.Adapter<ShopLocationItemAdapter.ShopLocationViewHolder>() {

    var shopLocationList = mutableListOf<Warehouse>()

    interface ShopLocationItemAdapterListener {
        fun onShopLocationStateStatusClicked(data: Warehouse)
        fun onShopEditAddress(data: Warehouse)
        fun onImageMainInfoIconClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopLocationViewHolder {
        val binding = CardShopLocationAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopLocationViewHolder(binding, listener)
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

    fun clearData() {
        shopLocationList.clear()
        notifyDataSetChanged()
    }

    inner class ShopLocationViewHolder(private val binding: CardShopLocationAddressBinding, private val listener: ShopLocationItemAdapterListener) : RecyclerView.ViewHolder(binding.root) {

        //private val tvShopName = itemView.findViewById<Typography>(R.id.tv_shop_name)
        //private val tvShopLabel = itemView.findViewById<Typography>(R.id.tv_shop_label)
        //private val iconShopLabel = itemView.findViewById<ImageView>(R.id.img_mark_icon)
        //private val tvAddressDetail = itemView.findViewById<Typography>(R.id.tv_address_detail)
        //private val tvAddressCity = itemView.findViewById<Typography>(R.id.tv_address_city)
        //private val tvAddressZipCode = itemView.findViewById<Typography>(R.id.tv_address_zipcode)
        //private val labelMainShop = itemView.findViewById<Label>(R.id.lbl_main_shop)
        private val imgInfoIcon = itemView.findViewById<ImageView>(R.id.img_info_icon)
        private val imgPinpointState = itemView.findViewById<ImageView>(R.id.img_location_state)
        private val tvPinpointState = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        private val btnSetLocation = itemView.findViewById<IconUnify>(R.id.icon_kebab)
        private val btnEditocation = itemView.findViewById<Typography>(R.id.action_edit)
        private val tickerAddressInfo = itemView.findViewById<Ticker>(R.id.ticker_address_info)

        @SuppressLint("SetTextI18n")
        fun bindData(data: Warehouse) {
            setPinpointStatus(data)
            setHeadquarter(data)
            setItemData(data)
            setListener(data)
        }

        private fun setHeadquarter(shopLocation: Warehouse) {
            if (shopLocation.warehouseType == ShopLocationConstant.WAREHOUSE_STATE_UTAMA) {
                binding.lblMainShop.visibility = View.VISIBLE
                imgInfoIcon.visibility = View.VISIBLE
                btnSetLocation.visibility = View.GONE
            } else {
                binding.lblMainShop.visibility = View.GONE
                imgInfoIcon.visibility = View.GONE
                btnSetLocation.visibility = View.VISIBLE
            }

            if (shopLocation.status == ShopLocationConstant.SHOP_LOCATION_STATE_ACTIVE) {
                binding.tvShopLabel.text = itemView.context.getString(R.string.shop_active)
                binding.tvShopLabel.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G600))
                binding.imgMarkIcon.setImageDrawable(itemView.context.getResDrawable(R.drawable.ic_mark_ico))
            } else if (shopLocation.status == ShopLocationConstant.SHOP_LOCATION_STATE_INACTIVE) {
                binding.tvShopLabel.text = itemView.context.getString(R.string.shop_inactive)
                binding.tvShopLabel.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                binding.imgMarkIcon.setImageDrawable(itemView.context.getResDrawable(R.drawable.ic_mark_ico_inactive))
            }
        }

        private fun setItemData(shopLocation: Warehouse) {
            binding.tvShopName.text = shopLocation.warehouseName
            binding.tvAddressDetail.text = shopLocation.addressDetail
            binding.tvAddressCity.text = "${shopLocation.cityName}, ${shopLocation.districtName}"
            binding.tvAddressZipcode.text = shopLocation.postalCode

            if (shopLocation.ticker.textInactive.isNotEmpty()) {
                tickerAddressInfo.visibility = View.VISIBLE
                tickerAddressInfo.setHtmlDescription("${shopLocation.ticker.textInactive} ${itemView.context.getString(R.string.ticker_info_selengkapnya)
                        .replace(TICKER_LABEL, shopLocation.ticker.textCourierSetting)}")
                tickerAddressInfo?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        val intent = RouteManager.getIntent(itemView.context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING)
                        itemView.context.startActivity(intent)
                    }

                    override fun onDismiss() {
                        //no-op
                    }
                })
            } else {
                tickerAddressInfo.visibility = View.GONE
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
            imgInfoIcon.setOnClickListener {
                listener.onImageMainInfoIconClicked()
            }
        }
    }
}