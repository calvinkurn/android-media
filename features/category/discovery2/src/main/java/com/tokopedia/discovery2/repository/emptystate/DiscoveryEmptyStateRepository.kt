package com.tokopedia.discovery2.repository.emptystate

import com.tokopedia.discovery2.Constant.EmptyStateTexts.DESCRIPTION
import com.tokopedia.discovery2.Constant.EmptyStateTexts.FILTER_BUTTON_TEXT
import com.tokopedia.discovery2.Constant.EmptyStateTexts.FILTER_DESCRIPTION
import com.tokopedia.discovery2.Constant.EmptyStateTexts.FILTER_TITLE
import com.tokopedia.discovery2.Constant.EmptyStateTexts.TITLE
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.datamapper.getComponent
import javax.inject.Inject

class DiscoveryEmptyStateRepository @Inject constructor() : EmptyStateRepository {
    override fun getEmptyStateData(component: ComponentsItem): EmptyStateModel {
        return handleEmptyState(component)
    }

    private fun handleEmptyState(component: ComponentsItem): EmptyStateModel {
        getComponent(component.parentComponentId, component.pageEndPoint)?.let {
            if ((it.selectedSort != null && it.selectedFilters != null) &&
                    (it.selectedSort?.isNotEmpty() == true ||
                            it.selectedFilters?.isNotEmpty() == true)) {
                return EmptyStateModel(isHorizontal = false,
                        title = FILTER_TITLE,
                        description = FILTER_DESCRIPTION,
                        buttonText = FILTER_BUTTON_TEXT,
                        isFilterState = true)

            }
        }
        return EmptyStateModel(isHorizontal = false,
                title = TITLE,
                description = DESCRIPTION)
    }
}