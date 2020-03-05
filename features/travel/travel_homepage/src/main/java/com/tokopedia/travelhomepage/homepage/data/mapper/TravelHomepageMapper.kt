package com.tokopedia.travelhomepage.homepage.data.mapper

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

/**
 * @author by jessica on 2019-08-14
 */

class TravelHomepageMapper {

    fun mapToSectionViewModel(model: TravelHomepageOrderListModel): TravelHomepageSectionModel {
        val viewModel = TravelHomepageSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.orders.map {
            TravelHomepageSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_ORDER_LIST
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelRecentSearchModel): TravelHomepageSectionModel {
        val viewModel = TravelHomepageSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.items.map {
            TravelHomepageSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_RECENT_SEARCH
        viewModel.categoryType = model.travelMeta.type
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelHomepageRecommendationModel): TravelHomepageSectionModel {
        val viewModel = TravelHomepageSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.items.map {
            TravelHomepageSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_RECOMMENDATION
        return viewModel
    }

    fun mapToViewModel(widgetType: String, unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageItemModel {
        return when (widgetType) {
            ConstantWidgetType.SLIDER_PRODUCT_CARD -> mapToProductCardModel(unifiedModel)
            ConstantWidgetType.DYNAMIC_ICON -> mapToCategoryListModel(unifiedModel)
            ConstantWidgetType.DYNAMIC_BANNER -> mapToBannerModel(unifiedModel)
            ConstantWidgetType.SLIDER_PRODUCT_CARD -> mapToHomepageSectionModel(unifiedModel)
            ConstantWidgetType.DUAL_PRODUCT_CARD -> mapToDestinationModel(unifiedModel, 2)
            ConstantWidgetType.SINGLE_BANNER -> mapToDestinationModel(unifiedModel, 1)
            else -> mapToCategoryListModel(unifiedModel)
        }
    }

    private fun mapToDestinationModel(unifiedModel: List<TravelUnifiedSubhomepageData>, spanSize: Int): TravelHomepageDestinationModel {
        val products = mutableListOf<TravelHomepageDestinationModel.Destination>()
        for (item in unifiedModel) {
            var product = TravelHomepageDestinationModel.Destination()
            product.attributes.title = item.title
            product.attributes.appUrl = item.appUrl
            product.attributes.imageUrl = item.imageUrl
            product.attributes.subtitle = item.subtitle

            products.add(product)
        }
        return TravelHomepageDestinationModel(destination = products, spanSize = spanSize)
    }

    private fun mapToHomepageSectionModel(unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageSectionModel {
        val sectionItems = mutableListOf<TravelHomepageSectionModel.Item>()
        for (item in unifiedModel) {
            var sectionItem = TravelHomepageSectionModel.Item()
            sectionItem.product = item.product
            sectionItem.title = item.title
            sectionItem.subtitle = item.subtitle
            sectionItem.prefix = item.prefix
            sectionItem.prefixStyling = item.prefixStyle
            sectionItem.value = item.value
            sectionItem.imageUrl = item.imageUrl

            sectionItems.add(sectionItem)
        }
        return TravelHomepageSectionModel(list = sectionItems)
    }

    private fun mapToBannerModel(unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageBannerModel {
        val banners = mutableListOf<TravelCollectiveBannerModel.Banner>()
        for (item in unifiedModel) {
            var banner = TravelCollectiveBannerModel.Banner()
            banner.id = item.id
            banner.product = item.product
            banner.attribute.promoCode = item.promoCode
            banner.attribute.description = item.description
            banner.attribute.appUrl = item.appUrl
            banner.attribute.imageUrl = item.imageUrl

            banners.add(banner)
        }
        return TravelHomepageBannerModel(TravelCollectiveBannerModel(banners = banners))
    }

    private fun mapToProductCardModel(unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageProductCardModel {
        val productList = mutableListOf<ProductGridCardItemModel>()
        for (item in unifiedModel) {
            var product = ProductGridCardItemModel()
            product.title = item.title
            product.imageUrl = item.imageUrl
            product.appUrl = item.appUrl

            productList.add(product)
        }
        return TravelHomepageProductCardModel(productItem = productList)
    }

    private fun mapToCategoryListModel(unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageCategoryListModel {
        var icons = mutableListOf<TravelHomepageCategoryListModel.Category>()
        for (item in unifiedModel) {
            var category = TravelHomepageCategoryListModel.Category()
            category.product = item.title
            category.attributes.title = item.title
            category.attributes.appUrl = item.appUrl
            category.attributes.imageUrl = item.imageUrl
            icons.add(category)
        }
        return TravelHomepageCategoryListModel(icons)
    }
}