package com.tokopedia.discovery2.repository.section

import com.tokopedia.discovery2.data.ComponentsItem

interface SectionRepository {
    suspend fun getComponents(pageIdentifier: String,sectionId:String,filterQueryString:String):List<ComponentsItem>
}