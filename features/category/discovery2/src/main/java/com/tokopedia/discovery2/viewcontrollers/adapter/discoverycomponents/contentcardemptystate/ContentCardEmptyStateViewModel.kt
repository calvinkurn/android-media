package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate

import android.app.Application
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ContentCardEmptyStateViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    fun getToastMessage(discoItemCount: Int?): Pair<String, String> {
        if (discoItemCount != null) {
            if (position == discoItemCount - 1) {
                return Pair(application.getString(R.string.card_empty_text_lc), application.getString(R.string.card_empty_toast_lc))
            }
        }
        return Pair(application.getString(R.string.card_empty_text_nlc), application.getString(R.string.card_empty_toast_nlc))
    }
}
