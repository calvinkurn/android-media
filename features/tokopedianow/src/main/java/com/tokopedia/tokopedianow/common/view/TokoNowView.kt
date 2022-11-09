package com.tokopedia.tokopedianow.common.view

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface TokoNowView {
    fun getFragmentPage(): Fragment
    fun getFragmentManagerPage(): FragmentManager
    fun refreshLayoutPage()
    fun getScrollState(adapterPosition: Int): Parcelable?
    fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?)
    fun getParallaxState(adapterPosition: Int): Map<String, Float>?
    fun saveParallaxState(adapterPosition: Int, mapParallaxState: Map<String, Float>)
}

