package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContext
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

class RelatedItemListenerDelegate @Inject constructor(
    @UniversalSearchContext context: Context?,
): RelatedItemListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onRelatedItemClick(data: RelatedItemDataView) {
        RouteManager.route(context, data.applink)
    }
}