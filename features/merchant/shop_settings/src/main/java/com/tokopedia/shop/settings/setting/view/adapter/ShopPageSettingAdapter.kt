package com.tokopedia.shop.settings.setting.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.setting.data.Product
import com.tokopedia.shop.settings.setting.data.Profile
import com.tokopedia.shop.settings.setting.data.Shipping
import com.tokopedia.shop.settings.setting.data.ShopPageSetting
import com.tokopedia.shop.settings.setting.data.Support
import com.tokopedia.shop.settings.setting.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.shop.settings.setting.view.adapter.viewholder.ProfileViewHolder
import com.tokopedia.shop.settings.setting.view.adapter.viewholder.ShippingViewHolder
import com.tokopedia.shop.settings.setting.view.adapter.viewholder.SupportViewHolder

class ShopPageSettingAdapter(
    private val profileItemClickListener: ProfileItemClickListener,
    private val productItemClickListener: ProductItemClickListener,
    private val supportItemClickListener: SupportItemClickListener,
    private val shippingItemClickListener: ShippingItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    private var shopPageSettingList = listOf<ShopPageSetting>()
    private var shippingViewHolder: ShippingViewHolder? = null

    override fun getItemViewType(position: Int): Int {
        return when (shopPageSettingList[position]) {
            is Profile -> TYPE_PROFILE
            is Product -> TYPE_PRODUCT
            is Support -> TYPE_SUPPORT
            is Shipping -> TYPE_SHIPPING
            else -> throw IllegalArgumentException("Invalid type of setting")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PROFILE -> ProfileViewHolder(inflater.inflate(R.layout.item_shop_page_setting_profile, parent, false))
            TYPE_PRODUCT -> ProductViewHolder(inflater.inflate(R.layout.item_shop_page_setting_product, parent, false))
            TYPE_SUPPORT -> SupportViewHolder(inflater.inflate(R.layout.item_shop_page_setting_support, parent, false))
            TYPE_SHIPPING -> ShippingViewHolder(inflater.inflate(R.layout.item_shop_page_setting_shipping, parent, false))
            else -> throw IllegalArgumentException("Invalid type of viewType")
        }
    }

    override fun getItemCount(): Int {
        return shopPageSettingList.size
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
                holder.bind(true, shippingItemClickListener)
                shippingViewHolder = holder
            }
        }
    }

    fun setShopPageSettingList(newShopPageSettingList: List<ShopPageSetting>) {
        this.shopPageSettingList = newShopPageSettingList
    }

    fun setMultiLocationEligibility() {
        notifyItemChanged(shippingViewHolder?.adapterPosition.orZero())
    }
}
