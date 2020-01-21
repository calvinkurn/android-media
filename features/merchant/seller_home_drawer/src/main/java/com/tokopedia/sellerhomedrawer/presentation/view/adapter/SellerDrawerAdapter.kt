package com.tokopedia.sellerhomedrawer.presentation.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerSeparator

class SellerDrawerAdapter(adapterTypeFactory: SellerDrawerAdapterTypeFactory,
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

//    override fun getItemViewType(position: Int): Int =
//        when {
//            position == 0 -> SellerDrawerHeaderViewHolder.LAYOUT_RES
//            isDrawerSeparator(position) -> SellerDrawerSeparatorViewHolder.LAYOUT_RES
//            isDrawerGroup(position) -> SellerDrawerGroupViewHolder.LAYOUT_RES
//            else -> SellerDrawerItemViewHolder.LAYOUT_RES
//        }

    private fun isDrawerSeparator(position: Int) : Boolean =
            visitables[position - itemCount] is SellerDrawerSeparator

    private fun isDrawerGroup(position: Int) : Boolean =
            visitables[position - itemCount] is SellerDrawerItem

}