package com.tokopedia.instantloan.router

import android.content.Context
import android.content.Intent

/**
 * Created by lavekush on 20/03/18.
 */

interface InstantLoanRouter {
    val isInstantLoanEnabled: Boolean
    fun getInstantLoanActivityIntent(context: Context): Intent
    fun getLoginIntent(context: Context?): Intent
}
