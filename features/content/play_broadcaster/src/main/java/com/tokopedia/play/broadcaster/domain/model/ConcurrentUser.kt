package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 22/06/20.
 */
data class ConcurrentUser(
        @SerializedName("total_concurrent")
        val totalConcurrent: Int,
        @SerializedName("total_concurrent_guest")
        val totalConcurrentGuest: Int,
        @SerializedName("total_concurrent_users")
        val totalConcurrentUsers: Int,
        @SerializedName("total_guests")
        val totalGuests: Int,
        @SerializedName("total_registered_users")
        val totalRegisteredUsers: Int,
        @SerializedName("total_users")
        val totalUsers: Int
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.ConcurrentUser
}