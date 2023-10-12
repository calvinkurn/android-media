package com.tokopedia.tokopedianow.category.presentation.callback

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokopedianow.common.view.TokoNowView

class TokoNowViewCallback(
    private val fragment: Fragment,
    private val refreshLayout: () -> Unit
): TokoNowView {
    override fun getFragmentPage(): Fragment = fragment

    override fun getFragmentManagerPage(): FragmentManager = fragment.childFragmentManager

    override fun refreshLayoutPage() = refreshLayout.invoke()

    override fun getScrollState(adapterPosition: Int): Parcelable? = null

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) {
        /* nothing to do */
    }
}
