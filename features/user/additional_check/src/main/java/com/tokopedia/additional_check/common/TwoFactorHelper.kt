package com.tokopedia.additional_check.common

interface ActivePageListener {
    fun currentPage(page: String)
}

const val ADD_PHONE_NUMBER_PAGE = "ADD PHONE NUMBER PAGE"
const val ADD_PIN_PAGE = "ADD PIN PAGE"
