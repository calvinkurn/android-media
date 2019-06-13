package  com.tokopedia.topupbills.telco.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class DigitalValidation : Parcelable {

    var regex: String? = null
    var error: String? = null

    constructor(regex: String, error: String) {
        this.regex = regex
        this.error = error
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.regex)
        dest.writeString(this.error)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.regex = `in`.readString()
        this.error = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<DigitalValidation> {
        override fun createFromParcel(source: Parcel): DigitalValidation {
            return DigitalValidation(source)
        }

        override fun newArray(size: Int): Array<DigitalValidation?> {
            return arrayOfNulls(size)
        }
    }
}
