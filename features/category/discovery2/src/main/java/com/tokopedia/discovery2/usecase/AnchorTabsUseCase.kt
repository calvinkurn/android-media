package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.di.DiscoveryScope
import javax.inject.Inject

@DiscoveryScope
class AnchorTabsUseCase @Inject constructor() {

    var selectedId = ""

}