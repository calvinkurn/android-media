package com.tokopedia.dilayanitokopedia.common.view

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by irpan on 12/09/22.
 */
interface DtView {
    fun getFragmentPage(): Fragment
    fun getFragmentManagerPage(): FragmentManager
    fun refreshLayoutPage()
    fun getScrollState(adapterPosition: Int): Parcelable?
    fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?)
    fun saveParallaxState(mapParallaxState: Map<String, Float>)
    fun getParallaxState(): Map<String, Float>
}
