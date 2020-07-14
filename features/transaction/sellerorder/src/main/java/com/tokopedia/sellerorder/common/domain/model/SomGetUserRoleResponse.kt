package com.tokopedia.sellerorder.common.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomGetUserRoleResponse(
        @SerializedName("GoldGetUserShopInfo")
        @Expose
        val goldGetUserShopInfo: GoldGetUserShopInfo?
)

data class GoldGetUserShopInfo(
        @SerializedName("Data")
        @Expose
        val data: SomGetUserRoleDataModel?
)

data class SomGetUserRoleDataModel(
        @SerializedName("Roles")
        @Expose
        var roles: List<Roles> = emptyList()
): Parcelable {
    constructor(parcel: Parcel): this() {
        val rolesString: MutableList<String> = mutableListOf()
        parcel.readStringList(rolesString)
        roles = rolesString.map { Roles.valueOf(it) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(roles.map { it.name })
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SomGetUserRoleDataModel> {
        override fun createFromParcel(parcel: Parcel): SomGetUserRoleDataModel {
            return SomGetUserRoleDataModel(parcel)
        }

        override fun newArray(size: Int): Array<SomGetUserRoleDataModel?> {
            return arrayOfNulls(size)
        }
    }
}

enum class Roles {
    MANAGE_SHOP,
    MANAGE_TX,
    MANAGE_INBOX,
    MANAGE_SHOPSTATS,
    MANAGE_TA,
    MANAGE_KS
}