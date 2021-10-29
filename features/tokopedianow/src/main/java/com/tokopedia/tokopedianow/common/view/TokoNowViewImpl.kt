package com.tokopedia.tokopedianow.common.view

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class TokoNowViewImpl : TokoNowView {
    override fun getFragmentPage(): Fragment? = null

    override fun getFragmentManagerPage(): FragmentManager? = null

    override fun refreshLayoutPage() {}

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) {}
}