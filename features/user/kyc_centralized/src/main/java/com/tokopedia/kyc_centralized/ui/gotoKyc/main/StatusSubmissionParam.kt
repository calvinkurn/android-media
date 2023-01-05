package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class StatusSubmissionParam(
    /*
    * set value true if goto kyc came from account page, other than that set value false
    * The purpose of this field is to set the type of layout that appears,
    * because there are different layouts if the user comes from the account page
    * */
    val isCameFromAccountPage: Boolean = false,

    /*
    * dataSource is a number
    * The purpose of this field is to know user get progressive or non progressive
    * if the value is 2, that mean user get progressive, to simplify logic other then that user get to non progressive
    * */
    val dataSource: String = "",

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
    val listReason: List<String> = emptyList()
): Parcelable
