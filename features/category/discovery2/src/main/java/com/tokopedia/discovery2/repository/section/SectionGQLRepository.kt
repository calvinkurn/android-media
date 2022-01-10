package com.tokopedia.discovery2.repository.section

import com.tokopedia.basemvvm.repository.BaseRepository

class SectionGQLRepository : BaseRepository(),SectionRepository {
    override suspend fun getComponents(
        pageIdentifier: String,
        sectionId: String,
        filterParams: Map<String, Any>?
    ) {

    }
}