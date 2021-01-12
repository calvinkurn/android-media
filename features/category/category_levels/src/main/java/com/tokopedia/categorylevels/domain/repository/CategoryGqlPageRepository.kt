package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.categorylevels.analytics.*
import com.tokopedia.common_category.data.raw.GQL_NAV_CATEGORY_DETAIL_V3
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.usecase.RequestParams

class CategoryGqlPageRepository(private val departmentName: String,
                                private val departmentId: String,
                                private val categoryUrl: String?) : BaseRepository(), DiscoveryPageRepository {
    companion object {
        const val INTERMEDIARY = "intermediary"
        const val IDENTIFIER = "identifier"
        const val SAFESEARCH = "safeSearch"
        const val SEARCH_APPLINK= "tokopedia://search-autocomplete"
        const val DOMAIN_URL_LIVE = "https://www.tokopedia.com/"
        const val BANNED= 1
        const val INDEX_ONE = "1"
        const val INDEX_TWO = "2"
        const val INDEX_THREE = "3"
    }

    override suspend fun getDiscoveryPageData(pageIdentifier: String): DiscoveryResponse {
        val data = getGQLData(GQL_NAV_CATEGORY_DETAIL_V3, BannedCategoryResponse::class.java, createRequestParameterCategory(departmentId)).categoryDetailQuery?.data ?: Data()
        return DiscoveryResponse(
                components = getCategoryComponents(data),
                pageInfo = PageInfo(
                        identifier = departmentId, name = data.name, type = "", path = data.url,
                        searchApplink = SEARCH_APPLINK,
                        redirectionUrl = data.appRedirectionURL,
                        isAdult = data.isAdult,
                        origin = AdultManager.ORIGIN_CATEGORY_PAGE,
                        share = Share(
                                enabled = true,
                                description = "Beli ${data.name} Dengan Pilihan Terlengkap dan Harga Termurah. Belanja Produk ${data.name} Aman dan Nyaman di Tokopedia. Pengiriman Cepat dan Terpercaya.",
                                url = "https://www.tokopedia.com${data.url}", title = "", image = "")),
                title = data.name ?: departmentName,
                additionalInfo = AdditionalInfo(null, hashMapOf(
                        KEY_CATEGORY_ID_MAP to data.id.toString(),
                        KEY_ROOT_ID to (data.rootId ?: ""),
                        KEY_PARENT to (data.parent ?: ""),
                        KEY_URL to (data.url ?: ""),
                        KEY_REDIRECTION_URL to (data.appRedirectionURL ?: "")
                )))
    }

    private fun getCategoryComponents(bannedData: Data): ArrayList<ComponentsItem> {
        val components = ArrayList<ComponentsItem>()
        if(!bannedData.appRedirectionURL.isNullOrEmpty()) {
            components.add(ComponentsItem(name = ComponentNames.LoadMore.componentName, id = INDEX_ONE, renderByDefault = true))
            return components
        }
        if(bannedData.isBanned == BANNED){
            components.add(ComponentsItem(name = ComponentNames.BannedView.componentName, id = INDEX_ONE, renderByDefault = true, title = bannedData.bannedMsgHeader, description = bannedData.bannedMessage))
            return components
        }
        val navigationChipsItems = arrayListOf<DataItem>()
        bannedData.child?.forEachIndexed { index, item ->
            navigationChipsItems.add(DataItem(title = item?.name, id = item?.id?.toString(), applinks = item?.applinks, positionForParentItem = index))
        }
        components.add(ComponentsItem(name = ComponentNames.NavigationChips.componentName, id = INDEX_ONE, renderByDefault = true, data = navigationChipsItems))
        components.add(ComponentsItem(name = ComponentNames.QuickFilter.componentName, id = INDEX_TWO, renderByDefault = true, showFilter = false, properties = Properties(targetId = INDEX_THREE), isSticky = true))
        components.add(ComponentsItem(name = ComponentNames.ProductCardRevamp.componentName, id = INDEX_THREE, renderByDefault = true, pagePath = bannedData.url ?: ""))
        return components
    }

    private fun createRequestParameterCategory(categoryId: String): Map<String, Any> {
        val request = RequestParams.create()
        request.putString(IDENTIFIER, categoryId)
        request.putBoolean(INTERMEDIARY, false)
        request.putBoolean(SAFESEARCH, false)
        return request.parameters
    }
}