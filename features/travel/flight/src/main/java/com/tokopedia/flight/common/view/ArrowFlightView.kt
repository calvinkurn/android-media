package com.tokopedia.flight.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.flight.R

/**
 * Created by Furqan on 06/10/2021.
 */
class ArrowFlightView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.include_arrow_flight, this)
    }
}