package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.basemvvm.repository.BaseRepository
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
                                url = categoryUrl?.replace(DeeplinkConstant.SCHEME_INTERNAL + "://", DOMAIN_URL_LIVE), title = "", image = "")),
                title = data.name ?: departmentName,
                additionalInfo = AdditionalInfo(null))
    }

    private fun getCategoryComponents(bannedData: Data): ArrayList<ComponentsItem> {
        val components = ArrayList<ComponentsItem>()
        if(!bannedData.appRedirectionURL.isNullOrEmpty()) {
            components.add(ComponentsItem(name = ComponentNames.LoadMore.componentName, id = "1", renderByDefault = true))
            return components
        }
        if(bannedData.isBanned == BANNED){
            components.add(ComponentsItem(name = ComponentNames.BannedView.componentName, id = "1", renderByDefault = true, title = bannedData.bannedMsgHeader, description = bannedData.bannedMessage))
            return components
        }
        val navigationChipsItems = arrayListOf<DataItem>()
        bannedData.child?.forEachIndexed { index, item ->
            navigationChipsItems.add(DataItem(title = item?.name, id = item?.id?.toString(), applinks = item?.applinks, positionForParentItem = index))
        }
        components.add(ComponentsItem(name = ComponentNames.NavigationChips.componentName, id = "1", renderByDefault = true, data = navigationChipsItems))
        components.add(ComponentsItem(name = ComponentNames.QuickFilter.componentName, id = "2", renderByDefault = true, showFilter = false, properties = Properties(targetId = "3"), isSticky = true))
        components.add(ComponentsItem(name = ComponentNames.ProductCardRevamp.componentName, id = "3", renderByDefault = true, pagePath = bannedData.url ?: ""))
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