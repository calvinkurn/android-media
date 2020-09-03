package com.tokopedia.play.broadcaster.util.model

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by jegul on 27/05/20
 */
data class BreadcrumbsModel(
        val fragmentClass: Class<out Fragment>,
        val extras: Bundle
)