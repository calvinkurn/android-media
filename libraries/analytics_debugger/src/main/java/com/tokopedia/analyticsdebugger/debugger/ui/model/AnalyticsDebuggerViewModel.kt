package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.AnalyticsDebuggerTypeFactory

/**
 * @author okasurya on 5/16/18.
 */
class AnalyticsDebuggerViewModel : Visitable<AnalyticsDebuggerTypeFactory>, Parcelable {
    var id: Long = 0
    var name: String? = null
    var category: String? = null
    var data: String? = null
    var dataExcerpt: String? = null
    var timestamp: String? = null

    constructor() {

    }

    override fun type(typeFactory: AnalyticsDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.name)
        dest.writeString(this.category)
        dest.writeString(this.data)
        dest.writeString(this.dataExcerpt)
        dest.writeString(this.timestamp)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readLong()
        this.name = `in`.readString()
        this.category = `in`.readString()
        this.data = `in`.readString()
        this.dataExcerpt = `in`.readString()
        this.timestamp = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<AnalyticsDebuggerViewModel> = object : Parcelable.Creator<AnalyticsDebuggerViewModel> {
            override fun createFromParcel(source: Parcel): AnalyticsDebuggerViewModel {
                return AnalyticsDebuggerViewModel(source)
            }

            override fun newArray(size: Int): Array<AnalyticsDebuggerViewModel> {
                return arrayOfNulls(size)
            }
        }
    }
}
