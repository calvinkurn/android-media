package com.tokopedia.analyticsdebugger.debugger.ui.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.ApplinkDebuggerTypeFactory

class ApplinkDebuggerViewModel : Visitable<ApplinkDebuggerTypeFactory>, Parcelable {
    var id: Long = 0
    var applink: String? = null
    var trace: String? = null
    var previewTrace: String? = null
    var timestamp: String? = null

    constructor() {

    }

    override fun type(typeFactory: ApplinkDebuggerTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.applink)
        dest.writeString(this.trace)
        dest.writeString(this.previewTrace)
        dest.writeString(this.timestamp)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readLong()
        this.applink = `in`.readString()
        this.trace = `in`.readString()
        this.previewTrace = `in`.readString()
        this.timestamp = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<ApplinkDebuggerViewModel> = object : Parcelable.Creator<ApplinkDebuggerViewModel> {
            override fun createFromParcel(source: Parcel): ApplinkDebuggerViewModel {
                return ApplinkDebuggerViewModel(source)
            }

            override fun newArray(size: Int): Array<ApplinkDebuggerViewModel> {
                return arrayOfNulls(size)
            }
        }
    }
}
