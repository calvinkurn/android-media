package com.tokopedia.imagepicker_insta.menu

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.activity.MainActivity
import com.tokopedia.imagepicker_insta.toPx
import com.tokopedia.unifyprinciples.Typography

object MenuManager {
    const val MENU_ITEM_ID = 1

    fun addCustomMenu(activity: FragmentActivity?, menu: Menu){
        val menuTitle = (activity as? MainActivity)?.menuTitle ?: activity?.getString(R.string.imagepicker_insta_lanjut)
        menu.add(Menu.NONE, 1, Menu.NONE, menuTitle)
        menu.findItem(MenuManager.MENU_ITEM_ID).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            activity?.let {
                val tv = Typography(it)
                tv.setType(Typography.BODY_2)
                tv.setWeight(Typography.BOLD)

                val spanText = SpannableString(title)
                val color = MethodChecker.getColor(activity, R.color.imagepicker_insta_menu)
                spanText.setSpan(ForegroundColorSpan(color),0,spanText.length,0)
                tv.text = spanText

                tv.setPadding(0,0,16.toPx().toInt(),0)
                actionView = tv
            }

        }
    }
}