package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common_category.data.raw.GQL_NAV_CATEGORY_DETAIL_V3
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
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
        const val BANNED= 1
    }

    override suspend fun getDiscoveryPageData(pageIdentifier: String): DiscoveryResponse {
        val data = getGQLData(GQL_NAV_CATEGORY_DETAIL_V3, BannedCategoryResponse::class.java, createRequestParameterCategory(departmentId)).categoryDetailQuery?.data ?: Data()
        return DiscoveryResponse(
                components = getCategoryComponents(data),
                pageInfo = PageInfo(
                        identifier = departmentId, name = departmentName, type = "", path = "",
                        searchApplink = SEARCH_APPLINK,
                        redirectionUrl = data.appRedirectionURL,
                        share = Share(
                                enabled = true,
                                description = "Beli $departmentName Dengan Pilihan Terlengkap dan Harga Termurah. Belanja Produk Category Name Aman dan Nyaman di Tokopedia. Pengiriman Cepat dan Terpercaya.",
                                url = categoryUrl, title = "", image = "")),
                title = departmentName,
                additionalInfo = AdditionalInfo(null))
    }

    private fun getCategoryComponents(bannedData: Data): ArrayList<ComponentsItem> {
        val components = ArrayList<ComponentsItem>()
        if(!bannedData.appRedirectionURL.isNullOrEmpty())
            return components
        if(bannedData.isBanned == BANNED){
            components.add(ComponentsItem(name = ComponentNames.BannedView.componentName, id = "1", renderByDefault = true, title = bannedData.bannedMsgHeader, description = bannedData.bannedMessage))
            return components
        }
        components.add(ComponentsItem(name = ComponentNames.ChildCategories.componentName, id = "1", renderByDefault = true))
        components.add(ComponentsItem(name = ComponentNames.Margin.componentName, id = "2", renderByDefault = true, data = arrayListOf(DataItem(sizeMobile = "12"))))
        components.add(ComponentsItem(name = ComponentNames.QuickFilter.componentName, id = "3", renderByDefault = true))
        components.add(ComponentsItem(name = ComponentNames.Margin.componentName, id = "4", renderByDefault = true, data = arrayListOf(DataItem(sizeMobile = "12"))))
        components.add(ComponentsItem(name = ComponentNames.ProductCardRevamp.componentName, id = "5", renderByDefault = true))
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