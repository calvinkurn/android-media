package com.tokopedia.gm.common.presentation.model

/**
 * Created by @ilhamsuaib on 09/06/22.
 */

data class DeactivationResultUiModel(
    val errorCode: String
) {
    companion object {
        const val APP_UPDATE_ERROR_CODE = "err.validate.app_outdated"
    }
}