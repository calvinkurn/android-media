package com.tokopedia.sellerpersona.view.model

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

const val PERSONA_STATUS_UNDEFINED = -1
const val PERSONA_STATUS_INACTIVE = 0
const val PERSONA_STATUS_ACTIVE = 1
const val PERSONA_STATUS_NOT_ROLLED_OUT = 3

enum class PersonaStatus(val value: Int) {
    INACTIVE(PERSONA_STATUS_INACTIVE),
    ACTIVE(PERSONA_STATUS_ACTIVE),
    UNDEFINED(PERSONA_STATUS_UNDEFINED)
}