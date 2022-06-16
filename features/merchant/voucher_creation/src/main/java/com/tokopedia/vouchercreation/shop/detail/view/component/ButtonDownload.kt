package com.tokopedia.vouchercreation.shop.detail.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ViewMvcButtonDownloadBinding
import com.tokopedia.vouchercreation.databinding.ViewMvcStartEndVoucherBinding

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class ButtonDownload(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        ViewMvcButtonDownloadBinding.inflate(LayoutInflater.from(context), this, true)
    }
}