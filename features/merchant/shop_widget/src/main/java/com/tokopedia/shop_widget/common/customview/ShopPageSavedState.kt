package com.tokopedia.shop_widget.common.customview

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.ClassLoaderCreator
import android.util.SparseArray
import android.view.View

/**
 * Created by nathan on 5/4/17.
 */
class ShopPageSavedState : View.BaseSavedState {
    var childrenStates: SparseArray<Parcelable>? = null
        private set

    constructor(superState: Parcelable?) : super(superState) {}
    private constructor(`in`: Parcel, classLoader: ClassLoader) : super(`in`) {
        childrenStates = `in`.readSparseArray(classLoader)
    }

    fun initChildrenStates() {
        childrenStates = SparseArray()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeSparseArray(childrenStates)
    }

}
