package com.tokopedia.sellerpersona.view.model

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

enum class PersonaStatus {
    INACTIVE, ACTIVE, UNDEFINED;

    companion object {
        const val INACTIVE_CONST = "0"
        const val ACTIVE_CONST = "1"
    }
}