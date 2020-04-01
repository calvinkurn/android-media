package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R

/**
 * @author by furqan on 23/04/19
 */
class RatingStarView(context: Context) : BaseCustomView(context) {

    init {
        inflate(context, R.layout.layout_widget_rating_star, this)
    }

}