package com.tokopedia.manageaddress.ui.shoplocation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.CardShopLocationAddressBinding
import com.tokopedia.manageaddress.util.ShopLocationConstant
import com.tokopedia.manageaddress.util.ShopLocationConstant.TICKER_LABEL
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopLocationItemAdapter(private val listener: ShopLocationItemAdapterListener) :
    RecyclerView.Adapter<ShopLocationItemAdapter.ShopLocationViewHolder>() {

    var shopLocationList = mutableListOf<Warehouse>()

    interface ShopLocationItemAdapterListener {
        fun onShopLocationStateStatusClicked(data: Warehouse)
        fun onShopEditAddress(data: Warehouse)
        fun onImageMainInfoIconClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopLocationViewHolder {
        val binding = CardShopLocationAddressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
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

    inner class ShopLocationViewHolder(
        private val binding: CardShopLocationAddressBinding,
        private val listener: ShopLocationItemAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Warehouse) {
            setPinpointStatus(data)
            setHeadquarter(data)
            setWarehouseStatus(data)
            setItemData(data)
            setListener(data)
        }

        private fun setWarehouseStatus(data: Warehouse) {
            if (data.status == ShopLocationConstant.SHOP_LOCATION_STATE_ACTIVE) {
                binding.tvShopLabel.text = itemView.context.getString(R.string.shop_active)
                binding.tvShopLabel.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_GN600
                    )
                )
                binding.imgMarkIcon.setImageDrawable(itemView.context.getResDrawable(R.drawable.ic_mark_ico))
            } else if (data.status == ShopLocationConstant.SHOP_LOCATION_STATE_INACTIVE) {
                binding.tvShopLabel.text = itemView.context.getString(R.string.shop_inactive)
                binding.tvShopLabel.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_N700_68
                    )
                )
                binding.imgMarkIcon.setImageDrawable(itemView.context.getResDrawable(R.drawable.ic_mark_ico_inactive))
            }
        }

        private fun setHeadquarter(shopLocation: Warehouse) {
            if (shopLocation.warehouseType == ShopLocationConstant.WAREHOUSE_STATE_UTAMA) {
                binding.lblMainShop.visibility = View.VISIBLE
                binding.imgInfoIcon.visibility = View.VISIBLE
                binding.iconKebab.visibility = View.GONE
            } else {
                binding.lblMainShop.visibility = View.GONE
                binding.imgInfoIcon.visibility = View.GONE
                binding.iconKebab.visibility = View.VISIBLE
            }
        }

        private fun setItemData(shopLocation: Warehouse) {
            binding.tvShopName.text = shopLocation.warehouseName
            binding.tvAddressDetail.text = shopLocation.addressDetail
            binding.tvAddressCity.text = "${shopLocation.cityName}, ${shopLocation.districtName}"
            binding.tvAddressZipcode.text = shopLocation.postalCode

            if (shopLocation.ticker.textInactive.isNotEmpty()) {
                binding.tickerAddressInfo.visibility = View.VISIBLE
                binding.tickerAddressInfo.setHtmlDescription(
                    "${shopLocation.ticker.textInactive} ${
                    itemView.context.getString(R.string.ticker_info_selengkapnya)
                        .replace(TICKER_LABEL, shopLocation.ticker.textCourierSetting)
                    }"
                )
                binding.tickerAddressInfo.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        val intent = RouteManager.getIntent(
                            itemView.context,
                            ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
                        )
                        itemView.context.startActivity(intent)
                    }

                    override fun onDismiss() {
                        // no-op
                    }
                })
            } else {
                binding.tickerAddressInfo.visibility = View.GONE
            }
        }

        private fun setPinpointStatus(shopLocation: Warehouse) {
            if (shopLocation.latLon.isNullOrEmpty()) {
                binding.imgLocationState.setImage(newIconId = IconUnify.LOCATION_OFF, newLightEnable = MethodChecker.getColor(binding.root.context, unifyprinciplesR.color.Unify_NN500))
                binding.tvPinpointState.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                binding.imgLocationState.setImage(newIconId = IconUnify.LOCATION_FILLED, newLightEnable = MethodChecker.getColor(binding.root.context, unifyprinciplesR.color.Unify_GN500))
                binding.tvPinpointState.text = itemView.context.getString(R.string.pinpoint)
            }
        }

        private fun setListener(shopLocation: Warehouse) {
            binding.iconKebab.setOnClickListener {
                listener.onShopLocationStateStatusClicked(shopLocation)
            }
            binding.actionEdit.setOnClickListener {
                listener.onShopEditAddress(shopLocation)
            }
            binding.imgInfoIcon.setOnClickListener {
                listener.onImageMainInfoIconClicked()
            }
        }
    }
}
