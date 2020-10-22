package com.tokopedia.sellerorder.common.presenter.model

import android.os.Parcel
import android.os.Parcelable

data class SomGetUserRoleUiModel(var roles: List<Roles> = emptyList()): Parcelable {
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

    companion object CREATOR : Parcelable.Creator<SomGetUserRoleUiModel> {
        override fun createFromParcel(parcel: Parcel): SomGetUserRoleUiModel {
            return SomGetUserRoleUiModel(parcel)
        }

        override fun newArray(size: Int): Array<SomGetUserRoleUiModel?> {
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