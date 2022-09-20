package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContext
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class RelatedItemListenerDelegate @Inject constructor(
    @UniversalSearchContext context: Context?,
    private val iris: Iris,
): RelatedItemListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onRelatedItemClick(data: RelatedItemDataView) {
        data.click(TrackApp.getInstance().gtm)
        RouteManager.route(context, data.applink)
    }

    override fun onRelatedItemImpressed(data: RelatedItemDataView) {
        data.impress(iris)
    }
}