package com.tokopedia.categorylevels.domain.repository

import com.google.gson.Gson
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import java.util.*

class CategoryGqlPageRepository(private val departmentName: String,
                                private val departmentId: String) : BaseRepository(), DiscoveryPageRepository {
    override suspend fun getDiscoveryPageData(pageIdentifier: String): DiscoveryResponse {
        //TODO Niranjan need to find a better way to pass departmentId to components
        val json = "{\"title\":\"$departmentName\",\"components\":[{\"ab_modulo\":\"\",\"title\":\"\",\"data\":[{\"background\":\"#ffffff\",\"size_desktop\":\"20\",\"size_mobile\":\"5\"}],\"id\":8166,\"is_ab\":false,\"skiprender\":false,\"name\":\"margin\",\"ab_default\":false,\"render_by_default\":true},{\"ab_default\":false,\"skiprender\":false,\"name\":\"product_card_revamp\",\"title\":\"\",\"data\":[],\"id\":$departmentId,\"render_by_default\":true,\"is_ab\":false,\"ab_modulo\":\"\"}],\"page_info\":{\"Identifier\":\"$departmentId\",\"Name\":\"$departmentName\",\"Path\":\"\",\"Type\":\"\",\"search_applink\":\"tokopedia://search-autocomplete\",\"search_url\":\"https://m.tokopedia.com/search\",\"share\":{\"description\":\"\",\"enabled\":true,\"title\":\"\",\"image\":\"\",\"url\":\"\"},\"campaign_code\":\"\"},\"layout_info\":{\"id\":0,\"name\":\"\",\"column_type\":0,\"base_layout\":0},\"seo_info\":{},\"additional_info\":{}}"
        return Gson().fromJson(json, DiscoveryResponse::class.java)
    }
}