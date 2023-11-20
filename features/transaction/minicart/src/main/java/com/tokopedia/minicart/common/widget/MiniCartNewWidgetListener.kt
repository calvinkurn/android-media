package com.tokopedia.minicart.common.widget

import androidx.fragment.app.FragmentManager

interface MiniCartNewWidgetListener : MiniCartWidgetListener {

    fun onChevronClickListener() {
        /* no-op */
    }

    fun getFragmentManager(): FragmentManager?

    fun onPrimaryButtonClickListener() {
        /* no-op */
    }

    fun onAdditionalButtonClickListener() {
        /* no-op */
    }
}
