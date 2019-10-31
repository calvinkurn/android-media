package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.gql.feed.Badge
import com.tokopedia.home.beranda.domain.gql.feed.Label
import com.tokopedia.home.beranda.domain.gql.feed.LabelGroup
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class HomeFeedViewModel(val productId: String,
                        val productName: String,
                        var categoryBreadcrumbs: String,
                        var recommendationType: String,
                        val imageUrl: String,
                        val price: String,
                        val rating: Int,
                        val countReview: Int,
                        val clickUrl: String,
                        val trackerImageUrl: String,
                        val slashedPrice: String,
                        val discountPercentage: String,
                        var priceNumber: Int,
                        val isTopAds: Boolean,
                        val position: Int,
                        val labelGroups: List<LabelGroup>,
                        val labels: List<Label>,
                        val badges: List<Badge>,
                        val location: String,
                        val wishlistUrl: String,
                        var isWishList: Boolean,
                        val isFreeOngkirActive: Boolean,
                        val freeOngkirImageUrl: String) : ImpressHolder(), Visitable<HomeFeedTypeFactory> {

    override fun type(typeFactory: HomeFeedTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun convertFeedTabModelToImpressionDataForLoggedInUser(
            tabName: String
    ): Any {
        return DataLayer.mapOf(
                DATA_NAME, productName,
                DATA_ID, productId,
                DATA_PRICE, priceNumber,
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, categoryBreadcrumbs,
                DATA_LIST, String.format(
                DATA_LIST_VALUE,
                tabName,
                recommendationType),
                DATA_POSITION, position.toString(),
                DATA_DIMENSION_83, if(isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER)
    }

    fun convertFeedTabModelToImpressionDataForNonLoginUser(
            tabName: String
    ): Any {
        return DataLayer.mapOf(
                DATA_NAME, productName,
                DATA_ID, productId,
                DATA_PRICE, priceNumber,
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, categoryBreadcrumbs,
                DATA_LIST, String.format(
                DATA_LIST_VALUE_NON_LOGIN,
                tabName,
                recommendationType
        ),
                DATA_POSITION, position.toString(),
                DATA_DIMENSION_83, if(isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER)
    }

    fun convertFeedTabModelToClickData(): Any {
        return DataLayer.mapOf(
                DATA_NAME, productName,
                DATA_ID, productId,
                DATA_PRICE, priceNumber,
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, categoryBreadcrumbs,
                DATA_POSITION, position.toString(),
                DATA_DIMENSION_83, if(isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER)
    }

    companion object {
        private val DATA_NONE_OTHER = "none / other"
        private val DATA_NAME = "name"
        private val DATA_ID = "id"
        private val DATA_PRICE = "price"
        private val DATA_BRAND = "brand"
        private val DATA_VARIANT = "variant"
        private val DATA_CATEGORY = "category"
        private val DATA_LIST = "list"
        private val DATA_POSITION = "position"
        private val DATA_LIST_VALUE = "/ - p2 - %s - rekomendasi untuk anda - %s"
        private val DATA_LIST_VALUE_NON_LOGIN = "/ - p2 - non login - %s - rekomendasi untuk anda - %s"
        private val DATA_DIMENSION_83 = "dimension83"
        private val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private val VALUE_NONE_OTHER = "none / other"
    }
}
