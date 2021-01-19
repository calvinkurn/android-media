package com.tokopedia.digital_checkout.data

import androidx.annotation.IntDef

/**
 * @author by jessica on 12/01/21
 */

@IntDef(DEFAULT, MYBILLS, SUBSCRIBED)
annotation class DigitalCartCrossSellingType

const val DEFAULT = 0
const val MYBILLS = 2
const val SUBSCRIBED = 3