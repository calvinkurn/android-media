package com.tokopedia.minicart.v2

import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

interface MiniCartV2WidgetListener : MiniCartWidgetListener {

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
