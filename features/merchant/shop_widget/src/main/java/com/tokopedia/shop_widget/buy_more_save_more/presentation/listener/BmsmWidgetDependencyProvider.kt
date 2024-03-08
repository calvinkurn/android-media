package com.tokopedia.shop_widget.buy_more_save_more.presentation.listener

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

interface BmsmWidgetDependencyProvider {
    val bmsmWidgetHostFragmentManager: FragmentManager
    val bmsmWidgetHostLifecycle: Lifecycle
}
