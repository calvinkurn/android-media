package com.tokopedia.play.util.measurement

import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 04/08/20
 */
interface ScreenOrientationDataSource {

    fun getScreenOrientation(): ScreenOrientation
}