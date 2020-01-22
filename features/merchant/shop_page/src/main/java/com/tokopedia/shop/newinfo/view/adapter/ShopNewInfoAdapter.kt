package com.tokopedia.shop.newinfo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.newinfo.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.shop.newinfo.view.adapter.viewholder.ProfileViewHolder
import com.tokopedia.shop.newinfo.view.adapter.viewholder.ShippingViewHolder
import com.tokopedia.shop.newinfo.view.adapter.viewholder.SupportViewHolder
import com.tokopedia.shop.newinfo.view.model.*

class ShopNewInfoAdapter(private val profileItemClickListener: ProfileItemClickListener,
                         private val productItemClickListener: ProductItemClickListener,
                         private val supportItemClickListener: SupportItemClickListener,
                         private val shippingItemClickListener: ShippingItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PROFILE = 1
        private const val TYPE_PRODUCT = 2
        private const val TYPE_SUPPORT = 3
        private const val TYPE_SHIPPING = 4
    }

    interface ProfileItemClickListener {
        fun onChangeProfileClicked()
        fun onChangeShopNoteClicked()
        fun onEditShopScheduleClicked()
    }

    interface ProductItemClickListener {
        fun onDisplayProductsClicked()
        fun onEditEtalaseClicked()
    }

    interface SupportItemClickListener {
        fun onGetSupportClicked()
        fun onGetTipsClicked()
    }

    interface ShippingItemClickListener {
        fun onEditLocationClicked()
        fun onManageShippingServiceClicked()
    }

    private var newInfoList = listOf<NewInfo>()

    override fun getItemViewType(position: Int): Int {
        return when (newInfoList[position]) {
            is Profile -> TYPE_PROFILE
            is Product -> TYPE_PRODUCT
            is Support -> TYPE_SUPPORT
            is Shipping -> TYPE_SHIPPING
            else -> throw IllegalArgumentException("Invalid type of info")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PROFILE -> ProfileViewHolder(inflater.inflate(R.layout.item_shop_new_info_profile, parent, false))
            TYPE_PRODUCT -> ProductViewHolder(inflater.inflate(R.layout.item_shop_new_info_product, parent, false))
            TYPE_SUPPORT -> SupportViewHolder(inflater.inflate(R.layout.item_shop_new_info_support, parent, false))
            TYPE_SHIPPING -> ShippingViewHolder(inflater.inflate(R.layout.item_shop_new_info_shipping, parent, false))
            else -> throw IllegalArgumentException("Invalid type of viewType")
        }
    }

    override fun getItemCount(): Int {
        return newInfoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileViewHolder -> {
                holder.bind(profileItemClickListener)
            }
            is ProductViewHolder -> {
                holder.bind(productItemClickListener)
            }
            is SupportViewHolder -> {
                holder.bind(supportItemClickListener)
            }
            is ShippingViewHolder -> {
                holder.bind(shippingItemClickListener)
            }
        }
    }

    fun setNewInfoList(newNewInfoList: List<NewInfo>) {
        this.newInfoList = newNewInfoList
    }
}