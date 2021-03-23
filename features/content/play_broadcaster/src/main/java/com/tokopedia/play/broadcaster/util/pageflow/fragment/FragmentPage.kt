package com.tokopedia.play.broadcaster.util.pageflow.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.play.broadcaster.util.pageflow.Page

/**
 * Created by jegul on 22/03/21
 */
data class FragmentPage(
        override val id: String,
        val fragmentClass: Class<out Fragment>
) : Page