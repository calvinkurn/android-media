package com.tokopedia.vouchercreation.shop.detail.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.vouchercreation.R

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class ButtonDownload(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_mvc_button_download, this)
    }
}