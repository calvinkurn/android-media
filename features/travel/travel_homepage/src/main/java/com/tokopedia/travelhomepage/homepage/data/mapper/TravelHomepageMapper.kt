package com.tokopedia.travelhomepage.homepage.data.mapper

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

/**
 * @author by jessica on 2019-08-14
 */

class TravelHomepageMapper {

    fun mapToViewModel(layoutData: TravelLayoutSubhomepage.Data, unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageItemModel {
        return when (layoutData.widgetType) {
            ConstantWidgetType.DUAL_PRODUCT_CARD, ConstantWidgetType.QUAD_PRODUCT_CARD -> mapToProductCardModel(layoutData, unifiedModel)
            ConstantWidgetType.DYNAMIC_ICON -> mapToCategoryListModel(unifiedModel)
            ConstantWidgetType.SLIDER_BANNER -> mapToBannerModel(layoutData, unifiedModel)
            ConstantWidgetType.SLIDER_PRODUCT_CARD -> mapToHomepageSectionModel(layoutData, unifiedModel)
            ConstantWidgetType.DUAL_BANNER -> mapToDestinationModel(layoutData, unifiedModel, 2)
            ConstantWidgetType.SINGLE_BANNER -> mapToDestinationModel(layoutData, unifiedModel, 1)
            ConstantWidgetType.DYNAMIC_BANNER -> mapToDestinationModel(layoutData, unifiedModel, 0)
            ConstantWidgetType.LEGO_BANNER -> mapToLegoBannerModel(layoutData, unifiedModel)
            else -> mapToHomepageSectionModel(layoutData, unifiedModel)
        }
    }

    private fun mapToLegoBannerModel(layoutData: TravelLayoutSubhomepage.Data, unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageLegoBannerModel {
        val banners = mutableListOf<LegoBannerItemModel>()
        for (item in unifiedModel) {
            val banner = LegoBannerItemModel()
            banner.appUrl = item.appUrl
            banner.imageUrl = item.imageUrl
            banner.webUrl = item.webUrl

            banners.add(banner)
        }
        return TravelHomepageLegoBannerModel(title = layoutData.title, subtitle = layoutData.subtitle, bannerItem = banners)
    }

    private fun mapToDestinationModel(layoutData: TravelLayoutSubhomepage.Data, unifiedModel: List<TravelUnifiedSubhomepageData>, spanSize: Int): TravelHomepageDestinationModel {
        val products = mutableListOf<TravelHomepageDestinationModel.Destination>()
        for (item in unifiedModel) {
            val product = TravelHomepageDestinationModel.Destination()
            product.attributes.title = item.title
            product.attributes.subtitle = item.subtitle
            product.attributes.webUrl = item.webUrl
            product.attributes.appUrl = item.appUrl
            product.attributes.imageUrl = item.imageUrl

            products.add(product)
        }
        return TravelHomepageDestinationModel(meta = TravelHomepageDestinationModel.MetaModel(layoutData.title),
                destination = products, spanSize = spanSize)
    }

    private fun mapToHomepageSectionModel(layoutData: TravelLayoutSubhomepage.Data, unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageSectionModel {
        val sectionItems = mutableListOf<TravelHomepageSectionModel.Item>()
        for (item in unifiedModel) {
            var sectionItem = TravelHomepageSectionModel.Item()
            sectionItem.title = item.title
            sectionItem.subtitle = item.subtitle
            sectionItem.prefix = item.prefix
            sectionItem.prefixStyling = item.prefixStyle
            sectionItem.value = item.value
            sectionItem.imageUrl = item.imageUrl
            sectionItem.product = item.product

            sectionItems.add(sectionItem)
        }
        return TravelHomepageSectionModel(title = layoutData.title,
                seeAllUrl = layoutData.appUrl,
                list = sectionItems)
    }

    private fun mapToBannerModel(layoutData: TravelLayoutSubhomepage.Data, unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageBannerModel {
        val banners = mutableListOf<TravelCollectiveBannerModel.Banner>()
        for (item in unifiedModel) {
            var banner = TravelCollectiveBannerModel.Banner()
            banner.id = item.id
            banner.product = item.product
            banner.attribute.promoCode = item.promoCode
            banner.attribute.description = item.description
            banner.attribute.webUrl = item.webUrl
            banner.attribute.appUrl = item.appUrl
            banner.attribute.imageUrl = item.imageUrl
            banner.attribute.promoCode = item.promoCode

            banners.add(banner)
        }
        return TravelHomepageBannerModel(TravelCollectiveBannerModel(banners = banners,
                meta = TravelCollectiveBannerModel.MetaModel(appUrl = layoutData.appUrl, webUrl = layoutData.webUrl)))
    }

    private fun mapToProductCardModel(layoutData: TravelLayoutSubhomepage.Data, unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageProductCardModel {
        val productList = mutableListOf<ProductGridCardItemModel>()
        for (item in unifiedModel) {
            var product = ProductGridCardItemModel()
            product.title = item.title
            product.tag = item.promoCode
            product.price = item.subtitle
            product.strikethroughPrice = item.prefix
            product.imageUrl = item.imageUrl
            product.appUrl = item.appUrl

            productList.add(product)
        }
        return TravelHomepageProductCardModel(title = layoutData.title,
                subtitle = layoutData.subtitle,
                clickSeeAllUrl = layoutData.appUrl,
                productItem = productList)
    }

    private fun mapToCategoryListModel(unifiedModel: List<TravelUnifiedSubhomepageData>): TravelHomepageCategoryListModel {
        var icons = mutableListOf<TravelHomepageCategoryListModel.Category>()
        for (item in unifiedModel) {
            var category = TravelHomepageCategoryListModel.Category()
            category.product = item.product
            category.attributes.title = item.title
            category.attributes.webUrl = item.webUrl
            category.attributes.appUrl = item.appUrl
            category.attributes.imageUrl = item.imageUrl
            icons.add(category)
        }
        return TravelHomepageCategoryListModel(icons)
    }
}