package com.tokopedia.fakeresponse.presentation.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.tokopedia.fakeresponse.R

class ImportDialog(context: Context): AppCompatDialog(context,0) {

    fun getLayout() = R.layout.fake_dialog_import
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }
}