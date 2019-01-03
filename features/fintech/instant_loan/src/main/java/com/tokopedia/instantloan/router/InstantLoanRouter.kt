package com.tokopedia.instantloan.router

import android.content.Context
import android.content.Intent

/**
 * Created by lavekush on 20/03/18.
 */

interface InstantLoanRouter {
    val isInstantLoanEnabled: Boolean
    fun getLoginIntent(context: Context?): Intent
}
