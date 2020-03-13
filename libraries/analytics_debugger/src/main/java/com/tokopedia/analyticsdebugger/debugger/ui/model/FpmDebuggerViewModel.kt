package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.FpmDebuggerTypeFactory

class FpmDebuggerViewModel : Visitable<FpmDebuggerTypeFactory>, Parcelable {
    var id: Long = 0
    var name: String? = null
    var duration: Long = 0
    var metrics: String? = null
    var attributes: String? = null
    var previewMetrics: String? = null
    var previewAttributes: String? = null
    var timestamp: String? = null

    constructor() {

    }

    override fun type(typeFactory: FpmDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.name)
        dest.writeLong(this.duration)
        dest.writeString(this.metrics)
        dest.writeString(this.attributes)
        dest.writeString(this.timestamp)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readLong()
        this.name = `in`.readString()
        this.duration = `in`.readLong()
        this.metrics = `in`.readString()
        this.attributes = `in`.readString()
        this.timestamp = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<FpmDebuggerViewModel> = object : Parcelable.Creator<FpmDebuggerViewModel> {
            override fun createFromParcel(source: Parcel): FpmDebuggerViewModel {
                return FpmDebuggerViewModel(source)
            }

            override fun newArray(size: Int): Array<FpmDebuggerViewModel> {
                return arrayOfNulls(size)
            }
        }
    }
}
