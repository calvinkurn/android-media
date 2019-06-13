package  com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author anggaprasetiyo on 5/8/17.
 */
class DigitalFavNumber : Parcelable {

    var name: String? = null
    var type: String? = null
    var text: String? = null
    var placeholder: String? = null
    var _default: String? = null
    var validation: List<DigitalValidation> = ArrayList()
    var additionalButton: DigitalAdditionalButton? = null

    constructor(name: String, type: String, text: String, placeholder: String, _default: String,
                validation: List<DigitalValidation>) {
        this.name = name
        this.type = type
        this.text = text
        this.placeholder = placeholder
        this._default = _default
        this.validation = validation
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.name)
        dest.writeString(this.type)
        dest.writeString(this.text)
        dest.writeString(this.placeholder)
        dest.writeString(this._default)
        dest.writeList(this.validation)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.name = `in`.readString()
        this.type = `in`.readString()
        this.text = `in`.readString()
        this.placeholder = `in`.readString()
        this._default = `in`.readString()
        this.validation = ArrayList()
        `in`.readList(this.validation, DigitalValidation::class.java.classLoader)
    }

    companion object CREATOR : Parcelable.Creator<DigitalFavNumber> {
        override fun createFromParcel(source: Parcel): DigitalFavNumber {
            return DigitalFavNumber(source)
        }

        override fun newArray(size: Int): Array<DigitalFavNumber?> {
            return arrayOfNulls(size)
        }
    }
}
