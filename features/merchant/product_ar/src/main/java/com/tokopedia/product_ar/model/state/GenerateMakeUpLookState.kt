package com.tokopedia.product_ar.model.state

import com.modiface.mfemakeupkit.effects.MFEMakeupLook

data class GenerateMakeUpLookState(
        val mode: GenerateMakeUpMode = GenerateMakeUpMode.INITIAL,
        val mfeMakeupLook: MFEMakeupLook = MFEMakeupLook()
)

enum class GenerateMakeUpMode {
    INITIAL,
    SELECTION
}
