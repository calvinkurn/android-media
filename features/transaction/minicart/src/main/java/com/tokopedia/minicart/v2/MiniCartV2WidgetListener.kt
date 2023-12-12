package com.tokopedia.minicart.v2

import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

abstract class MiniCartV2WidgetListener : MiniCartWidgetListener {

    open fun onFailedToLoadMiniCartWidget() {
        /* no-op */
    }

    open fun onChevronClickListener() {
        /* no-op */
    }

    open fun getFragmentManager(): FragmentManager? {
        return null
    }

    open fun onPrimaryButtonClickListener() {
        /* no-op */
    }

    open fun onAdditionalButtonClickListener() {
        /* no-op */
    }

    open fun onFailedToGoToCheckoutPage() {
        /* no-op */
    }
}
