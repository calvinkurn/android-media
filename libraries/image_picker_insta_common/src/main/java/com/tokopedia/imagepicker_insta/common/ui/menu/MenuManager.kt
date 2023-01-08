package com.tokopedia.imagepicker_insta.common.ui.menu

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity.CENTER_VERTICAL
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

object MenuManager {
    const val MENU_ITEM_ID = 1

    fun addCustomMenu(activity: FragmentActivity?, menuTitle:String, isActive: Boolean, menu: Menu, onClick: View.OnClickListener) {
        menu.add(Menu.NONE, 1, Menu.NONE, menuTitle)
        menu.findItem(MENU_ITEM_ID).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            activity?.let {
                val backgroundColor = MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
                val tv = Typography(it)
                tv.setType(Typography.BODY_2)
                tv.setWeight(Typography.BOLD)
                tv.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
                tv.gravity = CENTER_VERTICAL
                tv.setBackgroundColor(backgroundColor)

                val spanText = SpannableString(title)
                var color = MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_NN400)
                if (isActive) {
                    color = MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                    tv.setOnClickListener(onClick)
                } else {
                    tv.setOnClickListener(null)
                }
                spanText.setSpan(ForegroundColorSpan(color), 0, spanText.length, 0)
                tv.text = spanText

                tv.setPadding(16.toPx(), 0, 16.toPx(), 0)
                actionView = tv
            }
        }
    }
}