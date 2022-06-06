package com.tokopedia.core.common.category.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.core.common.category.view.model.CategoryViewModel

/**
 * @author sebastianuskh on 4/4/17.
 */
class CategoryViewModel(var name:String = "", var id:Long=0,
                        var isHasChild: Boolean=false) : Parcelable {




    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeLong(id)
        dest.writeByte(if (isHasChild) 1.toByte() else 0.toByte())
    }

    constructor(`in`: Parcel) : this() {
        name = `in`.readString().toString()
        id = `in`.readLong()
        isHasChild = `in`.readByte().toInt() != 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CategoryViewModel> =
            object : Parcelable.Creator<CategoryViewModel> {
                override fun createFromParcel(source: Parcel): CategoryViewModel {
                    return CategoryViewModel(source)
                }

                override fun newArray(size: Int): Array<CategoryViewModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}