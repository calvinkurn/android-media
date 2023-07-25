package com.tokopedia.sellerpersona.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class PersonaDataUiModel(
    //default data
    val persona: String = String.EMPTY,
    val personaStatus: PersonaStatus = PersonaStatus.UNDEFINED,
    val personaData: PersonaUiModel = PersonaUiModel(),
    val isShopOwner: Boolean = false,
    val isFirstVisit: Boolean = false,

    //fragment arguments
    val args: PersonaArgsUiModel = PersonaArgsUiModel(),

    //ui component state
    val isSwitchChecked: Boolean = false,
    val isApplyLoading: Boolean = false
) {
    fun getActiveStatusStringRes(): Int {
        return if (isSwitchChecked) {
            R.string.sp_active
        } else {
            R.string.sp_inactive
        }
    }

    fun getApplyButtonStringRes(): Int {
        return if (isFirstVisit) {
            R.string.sp_apply
        } else {
            R.string.sp_apply_changes
        }
    }

    fun isApplyButtonVisible(): Boolean {
        val isVisibleOnCheckedChanged =
            args.paramPersona.isBlank() && personaStatus.isActive() != isSwitchChecked
        return args.paramPersona.isNotBlank() || (isVisibleOnCheckedChanged)
    }
}