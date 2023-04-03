package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusSubmissionParam(
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
    * listReason is list reason why status rejected
    * This list only show when status was rejected (status = -1)
    * */
    val listReason: List<String> = emptyList(),

    /*
    * The purpose of this field is to know user get progressive or non progressive
    * */
    val gotoKycType: String = "",

    /*
    * The purpose of this field is to know how long user has to wait
    * */
    val waitTimeInSeconds: Int = 0
): Parcelable
