package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

/**
 * @author anggaprasetiyo on 5/8/17.
 */
class ClientNumber() : Parcelable {

    var name: String? = null
    var type: String? = null
    var text: String? = null
    var placeholder: String? = null
    var _default: String? = null
    var validation: List<Validation> = ArrayList()
    var additionalButton: AdditionalButton? = null
    var isEmoney: Boolean? = false

    constructor(name: String, type: String, text: String, placeholder: String, _default: String,
                validation: List<Validation>): this() {
        this.name = name
        this.type = type
        this.text = text
        this.placeholder = placeholder
        this._default = _default
        this.validation = validation
    }

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        type = parcel.readString()
        text = parcel.readString()
        placeholder = parcel.readString()
        _default = parcel.readString()
        validation = parcel.createTypedArrayList(Validation)
        isEmoney = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(text)
        parcel.writeString(placeholder)
        parcel.writeString(_default)
        parcel.writeTypedList(validation)
        parcel.writeValue(isEmoney)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClientNumber> {
        override fun createFromParcel(parcel: Parcel): ClientNumber {
            return ClientNumber(parcel)
        }

        override fun newArray(size: Int): Array<ClientNumber?> {
            return arrayOfNulls(size)
        }
    }


}
