package com.tokopedia.verification.common

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.tokopedia.verification.R

/**
 * Created by Ade Fulki on 05/10/20.
 */

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_dialog_otp)
        setCancelable(false)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp

        window?.setBackgroundDrawableResource(android.R.color.transparent);
    }
}
