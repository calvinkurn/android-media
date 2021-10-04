package com.tokopedia.home.account.presentation.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home.account.R
import kotlinx.android.synthetic.main.view_item_list.view.*

class ItemListView(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    init {
        inflate(getContext(), R.layout.view_item_list, this)
    }

    fun setTitle(text: String) {
        text_view_title.text = text
    }

    fun setBetaLabel(isBeta: Boolean) {
        if (isBeta) {
            beta_label_view.visibility = View.VISIBLE
        } else {
            beta_label_view.visibility = View.INVISIBLE
        }
        invalidate()
        requestLayout()
    }

    fun setBadgeCounter(counter: Int) {
        text_view_badge.setText(badgeCounter(counter))
        text_view_badge.setVisibility(if (counter > 0) VISIBLE else GONE)
        invalidate()
        requestLayout()
    }

    private fun badgeCounter(badge: Int): String {
        var counter = badge.toString()
        if (badge > 99) {
            counter = "99+"
        }
        return counter
    }

    fun setSubTitle(text: String) {
        text_view_sub_title.setText(text)
        text_view_sub_title.setVisibility(if (TextUtils.isEmpty(text)) GONE else VISIBLE)
        invalidate()
        requestLayout()
    }
}