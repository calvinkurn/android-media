package com.tokopedia.kotlin.extensions.view

import java.math.BigInteger

/**
 * @author by milhamj on 27/04/20.
 */
fun BigInteger?.orZero(): BigInteger = this ?: BigInteger.ZERO