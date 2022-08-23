package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

class DoubleLineListenerDelegate @Inject constructor(
    @ApplicationContext context: Context?,
): DoubleLineListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onDoubleLineSeeAllClick(data: DoubleLineDataView) {
        RouteManager.route(context, data.applink)
    }
}