package com.tokopedia.shop.common.view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.widget.FrameLayout
import androidx.annotation.AttrRes

/**
 * Created by nathan on 6/2/17.
 */
open class ShopPageBaseCustomView : FrameLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = ShopPageSavedState(superState)
        ss.initChildrenStates()
        for (i in 0 until childCount) {
            getChildAt(i).saveHierarchyState(ss.childrenStates)
        }
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is ShopPageSavedState) {
            val ss = state
            super.onRestoreInstanceState(ss.superState)
            for (i in 0 until childCount) {
                getChildAt(i).restoreHierarchyState(ss.childrenStates)
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}