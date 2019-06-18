package  com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author anggaprasetiyo on 5/8/17.
 */
class DigitalFavNumber() : Parcelable {

    var name: String? = null
    var type: String? = null
    var text: String? = null
    var placeholder: String? = null
    var _default: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        type = parcel.readString()
        text = parcel.readString()
        placeholder = parcel.readString()
        _default = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(text)
        parcel.writeString(placeholder)
        parcel.writeString(_default)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalFavNumber> {
        override fun createFromParcel(parcel: Parcel): DigitalFavNumber {
            return DigitalFavNumber(parcel)
        }

        override fun newArray(size: Int): Array<DigitalFavNumber?> {
            return arrayOfNulls(size)
        }
    }
}
