package com.tokopedia.feed_shop.shop.view.util

import android.content.Context
import com.tokopedia.design.component.Menus
import com.tokopedia.feed_shop.R
import java.util.*

/**
 * @author by milhamj on 13/11/18.
 */
fun createBottomMenu(
    context: Context,
    isDeletable: Boolean,
    isReportable: Boolean,
    isEditable: Boolean,
    listener: PostMenuListener?
): Menus {
    val menus = Menus(context)
    val menuList = ArrayList<Menus.ItemMenus>()
    if (isDeletable) {
        menuList.add(
            Menus.ItemMenus(
                context.getString(R.string.feed_shop_delete_post),
                -1
            )
        )
    }
    if (isReportable) {
        menuList.add(
            Menus.ItemMenus(
                context.getString(R.string.feed_shop_report),
                -1
            )
        )
    }
    if (isEditable) {
        menuList.add(
            Menus.ItemMenus(
                context.getString(R.string.feed_shop_edit_post),
                -1
            )
        )
    }
    menus.itemMenuList = menuList
    menus.setActionText(context.getString(com.tokopedia.design.R.string.button_cancel))
    menus.setOnActionClickListener { menus.dismiss() }
    menus.setOnItemMenuClickListener { itemMenus, _ ->
        when (itemMenus.title) {
            context.getString(R.string.feed_shop_delete_post) -> listener?.onDeleteClicked()
            context.getString(R.string.feed_shop_report) -> listener?.onReportClick()
            context.getString(R.string.feed_shop_edit_post) -> listener?.onEditClick()
        }
        menus.dismiss()
    }
    return menus
}

interface PostMenuListener {
    fun onDeleteClicked()

    fun onReportClick()

    fun onEditClick()
}
