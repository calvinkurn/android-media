package com.tokopedia.loginregister.shopcreation.domain.query

/**
 * Created by Ade Fulki on 2019-12-11.
 * ade.hadian@tokopedia.com
 */

object QueryUserProfileCompletion {

    fun getQuery(): String = """
        query userProfileCompletion(){
            userProfileCompletion {
                isActive,
                fullName,
                birthDate,
                birthDay,
                birthMonth,
                birthYear,
                gender,
                email,
                msisdn,
                isMsisdnVerified,
                isCreatedPassword,
                isBiodataDone,
                isEmailDone,
                isPasswordDone,
                isMsisdnDone,
                completionDone,
                completionScore
            }
        }
    """.trimIndent()
}