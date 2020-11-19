package com.tokopedia.categorylevels.domain.repository

import com.google.gson.Gson
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository

class CategoryGqlPageRepository(private val departmentName: String,
                                private val departmentId: String,
                                private val categoryUrl: String?) : BaseRepository(), DiscoveryPageRepository {
    override suspend fun getDiscoveryPageData(pageIdentifier: String): DiscoveryResponse {
        //TODO Niranjan need to find a better way to write this json
        val json = "{\"title\":\"$departmentName\",\"components\":[{\"target\":[],\"name\":\"child_categories\",\"title\":\"\",\"data\":[],\"id\":123,\"skiprender\":false,\"render_by_default\":true},{\"target\":[],\"name\":\"margin\",\"title\":\"\",\"data\":[{\"size_mobile\":\"12\"}],\"id\":124,\"skiprender\":false,\"render_by_default\":true},{\"target\":[],\"name\":\"chips_filter\",\"title\":\"\",\"data\":[],\"id\":125,\"skiprender\":false,\"render_by_default\":true},{\"target\":[],\"name\":\"margin\",\"title\":\"\",\"data\":[{\"size_mobile\":\"12\"}],\"id\":124,\"skiprender\":false,\"render_by_default\":true},{\"ab_default\":false,\"skiprender\":false,\"name\":\"product_card_revamp\",\"title\":\"\",\"data\":[],\"id\":127,\"render_by_default\":true,\"is_ab\":false,\"ab_modulo\":\"\"}],\"page_info\":{\"Identifier\":\"$departmentId\",\"Name\":\"$departmentName\",\"Path\":\"\",\"Type\":\"\",\"search_applink\":\"tokopedia://search-autocomplete\",\"search_url\":\"https://m.tokopedia.com/search\",\"share\":{\"description\":\"Beli $departmentName Dengan Pilihan Terlengkap dan Harga Termurah. Belanja Produk Category Name Aman dan Nyaman di Tokopedia. Pengiriman Cepat dan Terpercaya.\",\"enabled\":true,\"title\":\"\",\"image\":\"\",\"url\":\"${categoryUrl?.replace(DeeplinkConstant.SCHEME_INTERNAL, DeeplinkConstant.SCHEME_TOKOPEDIA)}\"},\"campaign_code\":\"\"},\"layout_info\":{\"id\":0,\"name\":\"\",\"column_type\":0,\"base_layout\":0},\"seo_info\":{},\"additional_info\":{}}"
        return Gson().fromJson(json, DiscoveryResponse::class.java)
    }
}