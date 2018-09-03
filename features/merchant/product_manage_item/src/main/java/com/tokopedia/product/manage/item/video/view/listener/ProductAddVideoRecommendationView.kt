package com.tokopedia.product.manage.item.video.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView

interface ProductAddVideoRecommendationView : CustomerView {

    val contextView : Context

    fun showSnackbarGreen(message: String)

    fun showSnackbarRed(message: String)

}