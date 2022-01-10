package com.tokopedia.discovery2.repository.section

interface SectionRepository {
    suspend fun getComponents(pageIdentifier: String,sectionId:String,filterParams:Map<String,Any>?)
}