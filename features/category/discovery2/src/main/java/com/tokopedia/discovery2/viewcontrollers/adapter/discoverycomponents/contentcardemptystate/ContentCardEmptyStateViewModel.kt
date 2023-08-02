package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcardemptystate

import android.app.Application
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ContentCardEmptyStateViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    fun getToastMessage(discoItemCount: Int?): String {
        if (discoItemCount != null) {
            if (position == discoItemCount - 1) {
                return application.getString(R.string.card_empty_text_lc)
            }
        }
        return application.getString(R.string.card_empty_text_nlc)
    }
}
