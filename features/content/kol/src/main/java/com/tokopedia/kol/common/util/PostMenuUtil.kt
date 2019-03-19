package com.tokopedia.kol.common.util

import android.content.Context
import com.tokopedia.design.component.Menus
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel
import java.util.*

/**
 * @author by milhamj on 13/11/18.
 */
fun createBottomMenu(context: Context,
                     isDeletable: Boolean,
                     isReportable: Boolean,
                     isEditable: Boolean,
                     listener: PostMenuListener?): Menus {
    val menus = Menus(context)
    val menuList = ArrayList<Menus.ItemMenus>()
    if (isDeletable) {
        menuList.add(
                Menus.ItemMenus(
                        context.getString(R.string.kol_delete_post),
                        -1
                )
        )
    }
    if (isReportable) {
        menuList.add(
                Menus.ItemMenus(
                        context.getString(R.string.kol_report),
                        -1
                )
        )
    }
    if (isEditable) {
        menuList.add(
                Menus.ItemMenus(
                        context.getString(R.string.kol_edit_post),
                        -1
                )
        )
    }
    menus.itemMenuList = menuList
    menus.setActionText(context.getString(R.string.button_cancel))
    menus.setOnActionClickListener { menus.dismiss() }
    menus.setOnItemMenuClickListener { itemMenus, _ ->
        when (itemMenus.title) {
            context.getString(R.string.kol_delete_post) -> listener?.onDeleteClicked()
            context.getString(R.string.kol_report) -> listener?.onReportClick()
            context.getString(R.string.kol_edit_post) -> listener?.onEditClick()
        }
        menus.dismiss()
    }
    return menus
}

fun createBottomMenu(context: Context,
                     model: BaseKolViewModel,
                     listener: PostMenuListener?): Menus {
    createBottomMenu(
            context,
            model.isDeletable,
            model.isReportable,
            model.isEditable, listener)
}

interface PostMenuListener {
    fun onDeleteClicked()

    fun onReportClick()

    fun onEditClick()
}