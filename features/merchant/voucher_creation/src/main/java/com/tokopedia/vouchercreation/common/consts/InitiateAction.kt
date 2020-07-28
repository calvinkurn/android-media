package com.tokopedia.vouchercreation.common.consts

import androidx.annotation.StringDef


@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(InitiateAction.CREATE, InitiateAction.UPDATE)
annotation class InitiateAction {
    companion object {
        const val CREATE = "create"
        const val UPDATE = "update"
    }
}