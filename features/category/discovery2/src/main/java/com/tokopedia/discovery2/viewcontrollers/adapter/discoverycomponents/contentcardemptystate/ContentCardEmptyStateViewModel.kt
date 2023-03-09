package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate

import android.app.Application
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ContentCardEmptyStateViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    fun getToastMessage(discoItemCount: Int?): Pair<String, String> {
        if (discoItemCount != null) {
            if (position == discoItemCount - 1) {
                return Pair(Constant.EmptyStateTexts.CONTENT_CARD_EMPTY_TEXT_LAST_COMPONENT, Constant.EmptyStateTexts.CONTENT_CARD_EMPTY_TOAST_LAST_COMPONENT)
            }
        }
        return Pair(Constant.EmptyStateTexts.CONTENT_CARD_EMPTY_TEXT_NOT_LAST_COMPONENT, Constant.EmptyStateTexts.CONTENT_CARD_EMPTY_TOAST_NOT_LAST_COMPONENT)
    }
}
