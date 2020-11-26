package com.tokopedia.discovery2.repository.emptystate

import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import javax.inject.Inject

const val TITLE = "Awas keduluan pembeli lain!"
const val DESCRIPTION = "Aktifkan pengingat supaya kamu nggak ketinggalan penawaran seru dari seller-seller Tokopedia!"

class DiscoveryEmptyStateRepository @Inject constructor() : EmptyStateRepository {
    override fun getEmptyStateData(): EmptyStateModel {
        return EmptyStateModel(isHorizontal = false,
                title = TITLE,
                description = DESCRIPTION)
    }
}