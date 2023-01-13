package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.categorylevels.analytics.*
import com.tokopedia.categorylevels.model.CategoryGetDetailModularData
import com.tokopedia.categorylevels.raw.GQL_CATEGORY_GET_DETAIL_MODULAR
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class CategoryGqlPageRepository(private val departmentName: String,
                                private val departmentId: String,
                                private val categoryUrl: String?) : BaseRepository(), DiscoveryPageRepository {
    companion object {
        const val IDENTIFIER = "identifier"
        const val IS_LATEST_VERSION = "isLatestVersion"
        const val SEARCH_APPLINK = "tokopedia://search-autocomplete"
        const val ENCODING_UTF_8 = "UTF-8"
        const val BANNED = 1
        const val INDEX_ONE = "1"
        const val LEVEL_3_CATEGORY = 3
        const val TABS_HORIZONTAL_SCROLL="tabs-horizontal-scroll"
        const val SEMUA="Semua"
        const val DEFAULT_TARGET_COMPONENT_ID="2,3,4,5,6,7"
    }

    val componentMap = mutableMapOf<String, String>()

    init {
        componentMap["chip-horizontal-scroll"] = ComponentNames.NavigationChips.componentName
        componentMap["product-card-horizontal-scroll"] = ComponentNames.CategoryBestSeller.componentName
        componentMap["product-list-filter"] = ComponentNames.QuickFilter.componentName
        componentMap["product-list-infinite-scroll"] = ComponentNames.ProductCardRevamp.componentName
        componentMap["static-text"] = ComponentNames.LihatSemua.componentName
        componentMap["headline-ads"] = ComponentNames.TopadsHeadlineView.componentName
        componentMap["tabs-horizontal-scroll"] = ComponentNames.Tabs.componentName
        componentMap["featured-product"] = ComponentNames.CLPFeaturedProducts.componentName
    }

    override suspend fun getDiscoveryPageData(pageIdentifier: String, extraParams: Map<String,Any>?): DiscoveryResponse {
        val data = getGQLData(GQL_CATEGORY_GET_DETAIL_MODULAR, CategoryGetDetailModularData::class.java, createRequestParameterCategory(pageIdentifier)).categoryGetDetailModular
        return data.basicInfo.let { basicInfo ->
            withContext(Dispatchers.Default) {
                DiscoveryResponse(
                    components = getCategoryComponents(pageIdentifier, data),
                    pageInfo = PageInfo(
                            identifier = pageIdentifier, name = basicInfo.name, type = "", path = basicInfo.url, id = basicInfo.id
                            ?: 0, showChooseAddress = true,
                            searchTitle = "Cari di ${basicInfo.name}",
                            searchApplink = "${SEARCH_APPLINK}/searchbox?hint=${encodeURL("Cari di ${basicInfo.name}")}&navsource=catpage&srp_page_id=${basicInfo.id}&srp_page_title=${encodeURL(basicInfo.name)}",
                            redirectionUrl = basicInfo.appRedirectionURL,
                            isAdult = basicInfo.isAdult,
                            origin = AdultManager.ORIGIN_CATEGORY_PAGE,
                            share = Share(
                                    enabled = true,
                                    description = "Beli ${basicInfo.name} Dengan Pilihan Terlengkap dan Harga Termurah. Belanja Produk ${basicInfo.name} Aman dan Nyaman di Tokopedia. Pengiriman Cepat dan Terpercaya.",
                                    url = "https://www.tokopedia.com${basicInfo.url}", title = basicInfo.titleTag, image = basicInfo.iconImageURL)),
                    title = basicInfo.name ?: departmentName,
                additionalInfo = AdditionalInfo(
                    null, hashMapOf(
                        KEY_CATEGORY_ID_MAP to basicInfo.id.toString(),
                        KEY_ROOT_ID to (basicInfo.rootId.toString() ?: ""),
                        KEY_PARENT to (basicInfo.parent.toString() ?: ""),
                        KEY_URL to (basicInfo.url ?: ""),
                        KEY_REDIRECTION_URL to (basicInfo.appRedirectionURL ?: ""),
                        KEY_TREE to (basicInfo.tree.toString())
                    )
                )
            )}
        }
    }

    private fun getCategoryComponents(
        pageIdentifier: String,
        data: CategoryGetDetailModularData.CategoryGetDetailModular
    ): ArrayList<ComponentsItem> {
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
                    properties = Properties(targetId = component.targetId.toString(),
                    background = component.properties.background,
                    backgroundImageUrl = component.properties.backgroundImageURL,
                    dynamic = component.properties.dynamic,
                    categoryDetail = component.properties.categoryDetail))
            if(component.data.isNotEmpty()) {
                val dataItems = arrayListOf<DataItem>()
                if(component.type== TABS_HORIZONTAL_SCROLL) dataItems.add(
                    DataItem(
                        name = SEMUA,
                        id = if(data.basicInfo.tree == LEVEL_3_CATEGORY) data.basicInfo.parent.toString() else departmentId,
                        targetComponentId = component.data.firstOrNull()?.targetComponentId
                            ?: DEFAULT_TARGET_COMPONENT_ID))
                component.data.forEachIndexed { index, dataItem ->
                    dataItems.add(DataItem(title = if(dataItem.text!=null) dataItem.text else dataItem.name,
                        id = dataItem.id.toString(),
                        applinks = dataItem.applinks,
                        positionForParentItem = index,
                        targetComponentId = dataItem.targetComponentId,
                        name = dataItem.categoryName,
                        isSelected = pageIdentifier == dataItem.id.toString() || data.basicInfo.id == dataItem.id))
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
        request.putBoolean(IS_LATEST_VERSION, true)
        return request.parameters
    }

    private fun encodeURL(url: String?): String {
        return URLEncoder.encode(url, ENCODING_UTF_8)
    }
}
