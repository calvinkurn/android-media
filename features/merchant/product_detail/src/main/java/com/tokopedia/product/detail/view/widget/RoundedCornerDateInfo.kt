package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.product.detail.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 29/09/21
 */
class RoundedCornerDateInfo : LinearLayout {

    private var icClock: ImageView? = null
    private var txtDay: Typography? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.rounded_corner_date_info_layout, this)
        icClock = findViewById(R.id.ic_clock_date_info)
        txtDay = findViewById(R.id.txt_day_date_info)

        icClock?.setColorFilter(ContextCompat.getColor(context,
                com.tokopedia.unifycomponents.R.color.Unify_R500
        ))
    }

    fun setDateText(date: String) {
        txtDay?.text = date
    }
}