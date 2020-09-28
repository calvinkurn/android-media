package com.tokopedia.sellerhome.view.widget.toolbar

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class SellerHomeToolbar(context: Context?, attrs: AttributeSet?) : Toolbar(context as Context, attrs) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = context?.dpToPx(4) ?: 12f
        }
    }
}