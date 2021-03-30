package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.categorylevels.analytics.*
import com.tokopedia.categorylevels.model.CategoryGetDetailModularData
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.categorylevels.raw.GQL_CATEGORY_GET_DETAIL_MODULAR
import java.net.URLEncoder

class CategoryGqlPageRepository(private val departmentName: String,
                                private val departmentId: String,
                                private val categoryUrl: String?) : BaseRepository(), DiscoveryPageRepository {
    companion object {
        const val IDENTIFIER = "identifier"
        const val SEARCH_APPLINK = "tokopedia://search-autocomplete"
        const val ENCODING_UTF_8 = "UTF-8"
        const val BANNED = 1
        const val INDEX_ONE = "1"
    }

    val componentMap = mutableMapOf<String, String>()

    init {
        componentMap["chip-horizontal-scroll"] = ComponentNames.NavigationChips.componentName
//        componentMap["product-card-horizontal-scroll"] = ComponentNames.ProductCardCarousel.componentName
        componentMap["product-list-filter"] = ComponentNames.QuickFilter.componentName
        componentMap["product-list-infinite-scroll"] = ComponentNames.ProductCardRevamp.componentName
    }

    override suspend fun getDiscoveryPageData(pageIdentifier: String): DiscoveryResponse {
        val data = getGQLData(GQL_CATEGORY_GET_DETAIL_MODULAR, CategoryGetDetailModularData::class.java, createRequestParameterCategory(departmentId)).categoryGetDetailModular
        return data.basicInfo.let { basicInfo ->
            DiscoveryResponse(
                    components = getCategoryComponents(data),
                    pageInfo = PageInfo(
                            identifier = departmentId, name = basicInfo.name, type = "", path = basicInfo.url, id = basicInfo.id
                            ?: 0,
                            searchTitle = "Cari di ${basicInfo.name}",
                            searchApplink = "${SEARCH_APPLINK}/searchbox?hint=${encodeURL("Cari di ${basicInfo.name}")}&navsource=catpage&srp_page_id=${basicInfo.id}&srp_page_title=${encodeURL(basicInfo.name)}",
                            redirectionUrl = basicInfo.appRedirectionURL,
                            isAdult = basicInfo.isAdult,
                            origin = AdultManager.ORIGIN_CATEGORY_PAGE,
                            share = Share(
                                    enabled = true,
                                    description = "Beli ${basicInfo.name} Dengan Pilihan Terlengkap dan Harga Termurah. Belanja Produk ${basicInfo.name} Aman dan Nyaman di Tokopedia. Pengiriman Cepat dan Terpercaya.",
                                    url = "https://www.tokopedia.com${basicInfo.url}", title = "", image = "")),
                    title = basicInfo.name ?: departmentName,
                    additionalInfo = AdditionalInfo(null, hashMapOf(
                            KEY_CATEGORY_ID_MAP to basicInfo.id.toString(),
                            KEY_ROOT_ID to (basicInfo.rootId.toString() ?: ""),
                            KEY_PARENT to (basicInfo.parent.toString() ?: ""),
                            KEY_URL to (basicInfo.url ?: ""),
                            KEY_REDIRECTION_URL to (basicInfo.appRedirectionURL ?: "")
                    )))
        }
    }

    private fun getCategoryComponents(data: CategoryGetDetailModularData.CategoryGetDetailModular): ArrayList<ComponentsItem> {
        val components = ArrayList<ComponentsItem>()
        data.basicInfo.let { basicInfo ->
            if (!basicInfo.appRedirectionURL.isNullOrEmpty()) {
                components.add(ComponentsItem(name = ComponentNames.LoadMore.componentName, id = INDEX_ONE, renderByDefault = true))
                return components
            }
            if (basicInfo.isBanned == BANNED) {
                components.add(ComponentsItem(name = ComponentNames.BannedView.componentName, id = INDEX_ONE, renderByDefault = true, title = basicInfo.bannedMsgHeader, description = basicInfo.bannedMsg))
                return components
            }
        }
        for (component in data.components) {
            val componentsItem = ComponentsItem(
                    name = componentMap[component.type],
                    id = component.id.toString(),
                    isSticky = component.sticky,
                    pagePath = data.basicInfo.url,
                    showFilterCount = false,
                    renderByDefault = true,
                    properties = Properties(targetId = component.targetId.toString()))
            if(component.data.isNotEmpty()) {
                val dataItems = arrayListOf<DataItem>()
                component.data.forEachIndexed { index, dataItem ->
                    dataItems.add(DataItem(title = dataItem.name, id = dataItem.id.toString(), applinks = dataItem.applinks, positionForParentItem = index))
                }
                componentsItem.data = dataItems
            }
            components.add(componentsItem)
        }
        return components
    }

    private fun createRequestParameterCategory(categoryId: String): Map<String, Any> {
        val request = RequestParams.create()
        request.putString(IDENTIFIER, categoryId)
        return request.parameters
    }

    private fun encodeURL(url: String?): String {
        return URLEncoder.encode(url, ENCODING_UTF_8)
    }
}