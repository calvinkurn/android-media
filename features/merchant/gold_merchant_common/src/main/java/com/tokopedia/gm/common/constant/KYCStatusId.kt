package com.tokopedia.gm.common.constant

/**
 * Created By @ilhamsuaib on 09/03/21
 */

//about kyc status id, please refer https://tokopedia.atlassian.net/wiki/spaces/AUT/pages/452132984/KYC+-+Know+Your+Customer
object KYCStatusId {

    const val REJECTED = -1
    const val PENDING = 0
    const val VERIFIED = 1
    const val NOT_VERIFIED = 3
    const val APPROVED = 4
    const val BLACKLIST = 5
}