package com.tokopedia.flight.orderdetail.presentation.model

import android.graphics.drawable.Drawable

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailButtonModel(
        val leftIcon: Drawable?,
        val topText: String,
        val bottomText: String,
        val isVisible: Boolean,
        val isActionAvailable: Boolean,
        val isClickable: Boolean = true
)