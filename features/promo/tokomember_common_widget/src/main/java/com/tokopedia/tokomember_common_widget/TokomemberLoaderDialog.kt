package com.tokopedia.tokomember_common_widget

import android.content.Context
import com.tokopedia.loaderdialog.LoaderDialog

object TokomemberLoaderDialog {
    fun showLoaderDialog(context: Context, text: String){
        val loader = LoaderDialog(context)
        loader.setLoadingText(text)
        loader.show()
    }
}