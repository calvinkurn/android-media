package com.tokopedia.play.view.layout

import android.view.View
import androidx.core.view.WindowInsetsCompat

/**
 * Created by jegul on 13/04/20
 */
interface PlayLayoutManager {

    fun layoutView(view: View)

    fun setupInsets(view: View, insets: WindowInsetsCompat)

    fun onDestroy()
}