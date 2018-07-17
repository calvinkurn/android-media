package com.tokopedia.settingbank.addeditaccount.view.viewmodel

/**
 * @author by nisie on 7/13/18.
 */
data class ValidateBankViewModel(
        val isSuccess: Boolean? = false,
        val isDataChanged: Boolean? = false,
        val paramName: String?,
        val message: String?
)