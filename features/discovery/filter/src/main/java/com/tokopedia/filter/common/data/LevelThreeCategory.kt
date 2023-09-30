package com.tokopedia.filter.common.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import kotlinx.android.parcel.Parcelize

@Parcelize
class LevelThreeCategory(@SerializedName("name")
                         @Expose
                         var name: String = "",

                         @SerializedName("key")
                         @Expose
                         var key: String = "",

                         @SerializedName("value")
                         @Expose
                         var value: String = "",

                         @SerializedName("isPopular")
                         @Expose
                         var isPopular: Boolean = false,

                         @SerializedName(value = "input_type", alternate = ["inputType"])
                         @Expose
                         private var inputType: String = "",

                         @SerializedName(value = "total_data", alternate = ["totalData"])
                         @Expose
                         private var totalData: String = "") : Parcelable {

    fun asOption(): Option {
        val uniqueId = OptionHelper.constructUniqueId(this.key, this.value, this.name)
        val option = OptionHelper.generateOptionFromUniqueId(uniqueId)

        option.isPopular = this.isPopular

        return option
    }

}