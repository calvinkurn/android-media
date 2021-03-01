package com.tokopedia.logisticCommon.domain.request
import com.google.gson.annotations.SerializedName


/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressRequest(
    @SerializedName("limit")
    var limit: Int = 10,
    @SerializedName("page")
    var page: Int = 1,
    @SerializedName("search_key")
    var searchKey: String = "",
    @SerializedName("show_address")
    val showAddress: Boolean,
    @SerializedName("show_corner")
    val showCorner: Boolean,
    @SerializedName("whitelist_chosen_address")
    val whitelistChosenAddress: Boolean = true,
    @SerializedName("previous_state")
    val previousState: Int? = null,
    @SerializedName("local_state_chosen_address_id")
    val localStateChosenAddressId: Int? = null
)