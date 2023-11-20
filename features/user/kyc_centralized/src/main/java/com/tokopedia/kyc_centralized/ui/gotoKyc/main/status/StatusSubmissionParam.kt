package com.tokopedia.kyc_centralized.ui.gotoKyc.main.status

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class StatusSubmissionParam(
    val projectId: String,
    /*
    * status submission is a number that you can see at com.tokopedia.kyc_centralized.common.KycStatus
    * Goto KYC Android only possible get several code, that are:
    * Rejected (-1)
    * Pending (0)
    * Verified (1)
    * Blacklisted (5)
    * */
    val status: String = "",

    /*
    * sourcePage is name of calling page goto kyc, please meaningful and simplify sourcePage value
    * The purpose of this field is to set dynamic text of button. when the button clicked that will back to previous page
    * */
    val sourcePage: String = "",

    /*
    * rejectionReason is list reason why status rejected
    * This list only show when status was rejected (status = -1)
    * */
    val rejectionReason: String = "",

    /*
    * The purpose of this field is to know user get progressive or non progressive
    * */
    val gotoKycType: String = "",

    /*
    * The purpose of this field is to set the text in status page
    * */
    val waitMessage: String = "",

    /*
    * The purpose of this field is to redirect to callback when verified
    * */
    val callback: String = ""
): Parcelable
