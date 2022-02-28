package com.tokopedia.officialstore.official.presentation.adapter.typefactory

import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.officialstore.base.diffutil.OfficialTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationTypeFactory

interface OfficialHomeTypeFactory : OfficialTypeFactory, HomeComponentTypeFactory, RecommendationTypeFactory {

    fun type(officialLoadingDataModel: OfficialLoadingDataModel): Int

    fun type(officialLoadingMoreDataModel: OfficialLoadingMoreDataModel): Int

    fun type(officialBannerDataModel: OfficialBannerDataModel): Int

    fun type(officialBenefitDataModel: OfficialBenefitDataModel): Int

    fun type(officialFeaturedShopDataModel: OfficialFeaturedShopDataModel): Int

    fun type(dynamicChannelDataModel: DynamicChannelDataModel): Int

    fun type(productRecommendationTitleDataModel: ProductRecommendationTitleDataModel): Int

    fun type(productRecommendationDataModel: ProductRecommendationDataModel): Int

    override fun type(merchantVoucherDataModel: MerchantVoucherDataModel): Int

}
