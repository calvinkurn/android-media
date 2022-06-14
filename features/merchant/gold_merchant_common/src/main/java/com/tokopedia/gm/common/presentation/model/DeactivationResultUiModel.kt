package com.tokopedia.gm.common.presentation.model

import com.tokopedia.gm.common.constant.PMConstant

/**
 * Created by @ilhamsuaib on 09/06/22.
 */

data class DeactivationResultUiModel(
    val errorCode: String
) {
    fun shouldUpdateApp(): Boolean {
        return errorCode == PMConstant.APP_UPDATE_ERROR_CODE
    }
}