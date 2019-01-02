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
                     model: BaseKolViewModel,
                     listener: PostMenuListener?): Menus {
    val menus = Menus(context)
    val menuList = ArrayList<Menus.ItemMenus>()
    if (model.isDeletable) {
        menuList.add(
                Menus.ItemMenus(
                        context.getString(R.string.kol_delete_post),
                        -1
                )
        )
    }
    if (model.isReportable) {
        menuList.add(
                Menus.ItemMenus(
                        context.getString(R.string.kol_report),
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
        }
        menus.dismiss()
    }
    return menus
}

interface PostMenuListener {
    fun onDeleteClicked()

    fun onReportClick()
}