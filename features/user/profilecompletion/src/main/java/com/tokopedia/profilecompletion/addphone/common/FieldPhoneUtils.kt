package com.tokopedia.profilecompletion.addphone.common

private const val MAX_PHONE_NUMBER = 15
private const val MIN_PHONE_NUMBER = 8

fun isPhoneTooShortLength(phone: String): Boolean =
    phone.length < MIN_PHONE_NUMBER

fun isPhoneExceedMaximumLength(phone: String): Boolean =
    phone.length > MAX_PHONE_NUMBER

