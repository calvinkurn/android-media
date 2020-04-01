package com.tokopedia.sellerhomedrawer.presentation.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem

class SellerDrawerAdapter(val context: Context,
                          adapterTypeFactory: SellerDrawerAdapterTypeFactory,
                          visitables: List<Visitable<*>>,
                          val drawerCache: LocalCacheHandler)
    : BaseAdapter<SellerDrawerAdapterTypeFactory>(adapterTypeFactory, visitables){

    companion object {
        const val VIEW_HEADER = 100
        const val VIEW_GROUP = 101
        const val VIEW_ITEM = 102
        const val VIEW_SEPARATOR = 103

        const val IS_INBOX_OPENED = "IS_INBOX_OPENED"
        const val IS_SHOP_OPENED = "IS_SHOP_OPENED"
        const val IS_PEOPLE_OPENED = "IS_PEOPLE_OPENED"
        const val IS_RESO_OPENED = "IS_RESO_OPENED"

        const val IS_PRODUCT_OPENED = "IS_PRODUCT_OPENED"
        const val IS_PRODUCT_DIGITAL_OPENED = "IS_PRODUCT_OPENED"
        const val IS_GM_OPENED = "IS_GM_OPENED"
    }

    var drawerItemData : MutableList<SellerDrawerItem> = arrayListOf()
    var isOfficialStore: Boolean = false
    var isGoldMerchant: Boolean = false
    var isFlashSaleVisible: Boolean = false

    fun renderFlashSaleDrawer() {
        var flashSaleIndexPosition = -1
        if (isFlashSaleVisible) {
            visitables.forEachIndexed{ index, visitable ->
                if (visitable is SellerDrawerItem) {
                    if (visitable.id == SellerHomeState.DrawerPosition.SELLER_TOP_ADS &&
                            (visitables.getOrNull(index + 1) as? SellerDrawerItem)?.id != SellerHomeState.DrawerPosition.SELLER_FLASH_SALE) {
                        flashSaleIndexPosition = index + 1
                    }
                    return@forEachIndexed
                }
            }
        }
        if (flashSaleIndexPosition >= 0) {
            val flashSaleDrawerItem = SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_flash_sale),
                    iconId = R.drawable.sh_ic_flash_sale_grey,
                    id = SellerHomeState.DrawerPosition.SELLER_FLASH_SALE,
                    isExpanded = true)
            visitables.add(flashSaleDrawerItem)
        }
    }

}