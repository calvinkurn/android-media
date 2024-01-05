package com.tokopedia.shop.home.view.customview.directpurchase

import android.view.View
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.customview.MultipleContentSwitcher

class FrameBannerWrapperView(
    val view: View,
    val switcher: MultipleContentSwitcher,
    val listener: MultipleContentSwitcher.MultipleContentSwitcherListener
) {
    var titleList: List<Title>? = null

    init {
        switcher.setListener(listener)
    }

    fun setData(titleListInput: List<Title>?) {
        val prevTitle = this.titleList
        this.titleList = titleListInput
        if (titleListHasChanged(prevTitle, titleListInput)) {
            if (titleListInput.isNullOrEmpty() || titleListInput[0].title.isNullOrEmpty()) {
                view.visibility = View.GONE
            } else {
                switcher.setData(titleListInput.map { it.title ?: "" })
                switcher.post {
                    switcher.selectItem(
                        switcher.getSelectedIndex().coerceIn(0, titleListInput.size - 1)
                    )
                }
                switcher.visibility = View.VISIBLE
                view.visibility = View.VISIBLE
            }
        }
    }

    fun setColor(colorPallete: ColorPallete) {
        switcher.setColor(colorPallete)
    }

    private fun titleListHasChanged(curTl: List<Title>?, titleListInput: List<Title>?): Boolean {
        if (curTl == null) return true
        val titleInput = titleListInput ?: return true
        if (titleInput.size != curTl.size) {
            return true
        } else {
            for ((i, titleString) in titleInput.withIndex()) {
                if (titleString.title != curTl[i].title) {
                    return true
                }
                if (titleString.imageUrl != curTl[i].imageUrl) {
                    return true
                }
            }
            return false
        }
    }

}
