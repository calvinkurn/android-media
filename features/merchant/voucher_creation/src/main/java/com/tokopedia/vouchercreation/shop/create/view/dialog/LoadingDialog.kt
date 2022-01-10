package com.tokopedia.vouchercreation.shop.create.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.LinearLayout
import com.tokopedia.vouchercreation.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)

        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0f)
            attributes?.width = LinearLayout.LayoutParams.MATCH_PARENT
            attributes?.height = LinearLayout.LayoutParams.MATCH_PARENT
        }
    }

}