package com.tokopedia.gamification.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CrackResultEntity(
    @ColumnInfo(name = "campaignId")
    @SerializedName("campaignId")
    @Expose(serialize = false, deserialize = false)
    var campaignId: Long = 0,

    @ColumnInfo(name = "imageUrl")
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String = "",

    @SerializedName("benefits")
    @Expose
    var benefits: List<CrackBenefitEntity>? = null,

    @Ignore
    @SerializedName("resultStatus")
    @Expose
    var resultStatus: ResultStatusEntity = ResultStatusEntity(),

    @Ignore
    @SerializedName("benefitType")
    @Expose
    var benefitType: String = "",

    @Ignore
    @SerializedName("ctaButton")
    @Expose
    var ctaButton: CrackButtonEntity = CrackButtonEntity(),

    @Ignore
    @SerializedName("returnButton")
    @Expose
    var returnButton: CrackButtonEntity = CrackButtonEntity(),

    @Ignore
    @SerializedName("benefitLabel")
    @Expose(serialize = false, deserialize = false)
    var benefitLabel: String = "",

    @Ignore
    @SerializedName("benefitLabel")
    @Expose(serialize = false, deserialize = false)
    var imageBitmap: Bitmap? = null,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object{
        @Ignore
        @Expose(serialize = false, deserialize = false)
        const val TYPE_BTN_INVISIBLE = "invisible"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        const val TYPE_BTN_DISMISS = "dismiss"

        @Ignore
        @Expose(serialize = false, deserialize = false)
       const val TYPE_BTN_REDIRECT = "redirect"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        const val STATUS_CODE_SERVER_ERROR = "500"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        const val STATUS_CODE_TOKEN_HAS_BEEN_CRACKED = "42501"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_SUCCESS = "200"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_TOKEN_USER_INVALID = "42502"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_TOKEN_EXPIRED = "42503"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_CAMPAIGN_EXPIRED = "42504"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_NO_SESSION_AVAILABLE_TAP_TAP = "42505"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_GRACE_PERIOD_TAP_TAP = "42508"

        @Ignore
        @Expose(serialize = false, deserialize = false)
        private val STATUS_CODE_MAX_QUOTA_REACHED_TAP_TAP = "47004"
    }

    fun isCrackTokenSuccess(): Boolean {
        return resultStatus.code == STATUS_CODE_SUCCESS
    }

    fun isCrackTokenExpired(): Boolean {
        return resultStatus.code == STATUS_CODE_TOKEN_EXPIRED || resultStatus.code == STATUS_CODE_CAMPAIGN_EXPIRED
    }

    fun isTokenHasBeenCracked(): Boolean {
        return resultStatus.code == STATUS_CODE_TOKEN_HAS_BEEN_CRACKED
    }

    fun isTokenUserInvalid(): Boolean {
        return resultStatus.code == STATUS_CODE_TOKEN_USER_INVALID
    }

    fun isTryAgainBtn(): Boolean {
        return resultStatus.code == STATUS_CODE_SERVER_ERROR
    }

    fun isCrackButtonVisible(crackButton: CrackButtonEntity): Boolean {
        return crackButton.type != TYPE_BTN_INVISIBLE
    }

    fun isCrackButtonDismiss(crackButton: CrackButtonEntity): Boolean {
        return crackButton.type == TYPE_BTN_DISMISS
    }

    fun isCrackButtonRedirect(crackButton: CrackButtonEntity): Boolean {
        return crackButton.type == TYPE_BTN_REDIRECT
    }

    fun isCrackButtonErrorTapTap(): Boolean {
        return STATUS_CODE_GRACE_PERIOD_TAP_TAP == resultStatus.code || STATUS_CODE_MAX_QUOTA_REACHED_TAP_TAP == resultStatus.code || STATUS_CODE_NO_SESSION_AVAILABLE_TAP_TAP == resultStatus.code
    }
}
