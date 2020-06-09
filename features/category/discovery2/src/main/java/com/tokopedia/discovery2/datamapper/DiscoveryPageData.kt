package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.PageInfo

data class DiscoveryPageData(val pageInfo: PageInfo, val title: String) {
    var components: List<ComponentsItem> = ArrayList()
}