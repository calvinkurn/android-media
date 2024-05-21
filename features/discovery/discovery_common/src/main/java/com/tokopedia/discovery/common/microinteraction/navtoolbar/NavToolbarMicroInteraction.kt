package com.tokopedia.discovery.common.microinteraction.navtoolbar

import android.content.Intent
import android.view.View
import com.tokopedia.discovery.common.microinteraction.SEARCH_BAR_MICRO_INTERACTION_FLAG_BUNDLE
import com.tokopedia.discovery.common.microinteraction.SearchBarMicroInteractionAttributes
import java.lang.ref.WeakReference

class NavToolbarMicroInteraction internal constructor() {

    private var searchBarViewRef: WeakReference<View?>? = null
    private var searchBtnRef: WeakReference<View?>? = null
    private val searchBarView: View?
        get() = searchBarViewRef?.get()
    private val searchBtn: View?
        get() = searchBtnRef?.get()

    fun setNavToolbarComponents(searchBarView: View?, searchBtn: View?) {
        this.searchBarViewRef = WeakReference(searchBarView)
        this.searchBtnRef = WeakReference(searchBtn)
    }

    fun animate(autoCompleteIntent: Intent, onAnimationEnd: (Intent) -> Unit?) {
        autoCompleteIntent.putExtra(
            SEARCH_BAR_MICRO_INTERACTION_FLAG_BUNDLE,
            SearchBarMicroInteractionAttributes.create(searchBarView, searchBtn)
        )

        onAnimationEnd(autoCompleteIntent)
    }
}
