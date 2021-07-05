package com.tokopedia.notifications.model

import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */

@Parcelize
data class ActionButton(
        @SerializedName(CMConstant.PayloadKeys.TEXT)
        @ColumnInfo(name = CMConstant.PayloadKeys.TEXT)
        @Expose
        var text: String? = null,

        @SerializedName(CMConstant.PayloadKeys.APP_LINK)
        @ColumnInfo(name = CMConstant.PayloadKeys.APP_LINK)
        @Expose
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
        @ColumnInfo(name = CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
        @Expose
        var actionButtonIcon: String? = null,

        @SerializedName(CMConstant.PayloadKeys.PD_ACTION)
        @ColumnInfo(name = CMConstant.PayloadKeys.PD_ACTION)
        @Expose
        var pdActions: PreDefineActions? = null,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @ColumnInfo(name = CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
        var element_id: String? = "",

        @SerializedName(CMConstant.PayloadKeys.TYPE)
        @ColumnInfo(name = CMConstant.PayloadKeys.TYPE)
        @Expose
        var type: String? = "",

        @SerializedName(CMConstant.PayloadKeys.ADD_TO_CART)
        @ColumnInfo(name = CMConstant.PayloadKeys.ADD_TO_CART)
        @Expose
        var addToCart: AddToCart? = null
) : Parcelable