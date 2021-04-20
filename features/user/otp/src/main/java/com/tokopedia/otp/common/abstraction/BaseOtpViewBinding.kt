package com.tokopedia.otp.common.abstraction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Ade Fulki on 21/04/20.
 * ade.hadian@tokopedia.com
 */

abstract class BaseOtpViewBinding {
    abstract val layoutResId: Int
    abstract fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View
}