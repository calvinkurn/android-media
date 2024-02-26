package com.tokopedia.wishlist.detail.util

import android.content.res.Resources
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.detail.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionEmptyStateData
import com.tokopedia.wishlist.detail.data.model.WishlistEmptyStateData
import com.tokopedia.wishlist.detail.data.model.WishlistRecommendationDataModel
import com.tokopedia.wishlist.detail.data.model.WishlistTickerCleanerData
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.data.model.WishlistUiModel

object WishlistUtils {
    private var recommPosition = 4
    private var recommPositionDefault = 4
    private var recommWithTickerPosition = 5
    private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"
    private val listProductId = arrayListOf<String>()

    fun toDp(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun toDp(dp: Double): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun organizeWishlistV2Data(
            wishlistV2UiModel: WishlistUiModel,
            typeLayout: String?,
            isAutomaticDelete: Boolean,
            recomm: WishlistRecommendationDataModel,
            topadsData: TopAdsImageViewModel?,
            isUsingCollection: Boolean
    ): List<WishlistTypeLayoutData> {
        var listData = arrayListOf<WishlistTypeLayoutData>()

        var isFilterActive = false
        for (itemFilter in wishlistV2UiModel.sortFilters) {
            if (itemFilter.isActive) isFilterActive = true
        }

        // empty wishlist
        if (wishlistV2UiModel.items.isEmpty() && wishlistV2UiModel.page <= 1) {
            listData = mapToEmptyState(wishlistV2UiModel, listData, isFilterActive, recomm, isUsingCollection)

            // non-empty wishlist
        } else {
            if (wishlistV2UiModel.page == 1) {
                listProductId.clear()
                wishlistV2UiModel.items.forEach { item ->
                    listProductId.add(item.id)
                }
                recommPosition = recommPositionDefault
                if (wishlistV2UiModel.ticker.message.isNotEmpty()) {
                    recommPosition = recommWithTickerPosition
                    val bundleTickerData = WishlistTickerCleanerData(
                        tickerCleanerData = wishlistV2UiModel.ticker,
                        bottomSheetCleanerData = wishlistV2UiModel.storageCleanerBottomSheet,
                        countRemovableItems = wishlistV2UiModel.countRemovableItems
                    )
                    listData.add(
                        WishlistTypeLayoutData(
                            bundleTickerData,
                                WishlistConsts.TYPE_TICKER
                        )
                    )
                }
            }

            // only for wishlist which has 1 page response
            if (wishlistV2UiModel.page == 1 && !wishlistV2UiModel.hasNextPage) {
                mapToProductCardList(
                    listData,
                    wishlistV2UiModel.items,
                    typeLayout,
                    isAutomaticDelete
                )

                when {
                    // if user has 0-3 products, recom widget is at the bottom of the page (vertical/infinite scroll)
                    wishlistV2UiModel.items.size < recommPosition -> {
                        listData = mapToRecommendation(0, listData, recomm)
                    }

                    // if user has 4 products, banner ads is after 4th of products, and recom widget is after TDN (at the bottom of the page)
                    wishlistV2UiModel.items.size == recommPosition -> {
                        mapToTopads(0, listData, topadsData)
                        mapToRecommendation(0, listData, recomm)
                    }

                    // if user has > 4 products, banner ads is after 4th of products, while recom widget is always at the bottom of the page
                    wishlistV2UiModel.items.size > recommPosition -> {
                        mapToTopads(recommPosition, listData, topadsData)
                        mapToRecommendation(0, listData, recomm)
                    }
                }
            } else {
                mapToProductCardList(
                    listData,
                    wishlistV2UiModel.items,
                    typeLayout,
                    isAutomaticDelete
                )

                if (wishlistV2UiModel.items.size >= recommPosition) {
                    if (wishlistV2UiModel.page % 2 == 0) {
                        mapToRecommendation(recommPosition, listData, recomm)
                    } else {
                        mapToTopads(recommPosition, listData, topadsData)
                    }
                } else {
                    mapToRecommendation(0, listData, recomm)
                }
            }
        }
        return listData
    }

    private fun mapToEmptyState(
            wishlistV2Response: WishlistUiModel,
            listData: ArrayList<WishlistTypeLayoutData>,
            isFilterActive: Boolean,
            recomm: WishlistRecommendationDataModel,
            isUsingCollection: Boolean
    ): ArrayList<WishlistTypeLayoutData> {
        if (isUsingCollection) {
            val listEmptyButton = arrayListOf<WishlistCollectionEmptyStateData.Button>()
            if (wishlistV2Response.emptyState.button.isNotEmpty()) {
                wishlistV2Response.emptyState.button.forEach { dataButton ->
                    val button = WishlistCollectionEmptyStateData.Button(
                        text = dataButton.text,
                        action = dataButton.action,
                        url = dataButton.url
                    )
                    listEmptyButton.add(button)
                }
            }
            var emptyData = WishlistCollectionEmptyStateData()
            if (wishlistV2Response.emptyState.messages.isNotEmpty()) {
                emptyData = WishlistCollectionEmptyStateData(
                    img = wishlistV2Response.emptyState.messages[0].imageUrl,
                    desc = wishlistV2Response.emptyState.messages[0].description,
                    title = wishlistV2Response.emptyState.messages[0].title,
                    listButton = listEmptyButton,
                    query = wishlistV2Response.query
                )
            }

            listData.add(WishlistTypeLayoutData(emptyData, WishlistConsts.TYPE_EMPTY_STATE_COLLECTION))
        } else {
            if (wishlistV2Response.query.isNotEmpty()) {
                listData.add(
                    WishlistTypeLayoutData(
                        wishlistV2Response.query,
                            WishlistConsts.TYPE_EMPTY_NOT_FOUND
                    )
                )
            } else if (isFilterActive) {
                val wishlistV2Empty = WishlistEmptyStateData(
                    R.string.empty_state_img,
                    R.string.empty_state_desc,
                    R.string.empty_state_title,
                    R.string.empty_state_button
                )
                listData.add(
                    WishlistTypeLayoutData(
                        wishlistV2Empty,
                            WishlistConsts.TYPE_EMPTY_STATE
                    )
                )
            } else if (!isFilterActive && wishlistV2Response.query.isEmpty()) {
                listData.add(WishlistTypeLayoutData("", WishlistConsts.TYPE_EMPTY_STATE_CAROUSEL))
            }
        }

        listData.add(
            WishlistTypeLayoutData(
                recomm.title,
                    WishlistConsts.TYPE_RECOMMENDATION_TITLE
            )
        )
        recomm.recommendationProductCardModelData.forEachIndexed { index, productCardModel ->
            if (recomm.listRecommendationItem.isNotEmpty()) {
                listData.add(
                    WishlistTypeLayoutData(
                        productCardModel,
                            WishlistConsts.TYPE_RECOMMENDATION_LIST,
                        recommItem = recomm.listRecommendationItem[index]
                    )
                )
            }
        }
        return listData
    }

    fun mapToRecommendation(
            index: Int,
            listData: ArrayList<WishlistTypeLayoutData>,
            recommItems: WishlistRecommendationDataModel
    ): ArrayList<WishlistTypeLayoutData> {
        if (index > 0) {
            listData.add(
                index,
                WishlistTypeLayoutData(
                        WishlistConsts.RECOMMENDED_FOR_YOU,
                        WishlistConsts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
                )
            )
            listData.add(
                index + 1,
                WishlistTypeLayoutData(
                    recommItems,
                        WishlistConsts.TYPE_RECOMMENDATION_CAROUSEL
                )
            )
        } else {
            listData.add(
                WishlistTypeLayoutData(
                    recommItems.title,
                        WishlistConsts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
                )
            )
            listData.add(
                WishlistTypeLayoutData(
                    recommItems,
                        WishlistConsts.TYPE_RECOMMENDATION_CAROUSEL
                )
            )
        }

        return listData
    }

    fun mapToTopads(
            index: Int,
            listData: ArrayList<WishlistTypeLayoutData>,
            topadsData: TopAdsImageViewModel?
    ): ArrayList<WishlistTypeLayoutData> {
        if (topadsData != null) {
            if (index > 0) {
                listData.add(
                    index,
                    WishlistTypeLayoutData(
                        topadsData,
                            WishlistConsts.TYPE_TOPADS
                    )
                )
            } else {
                listData.add(WishlistTypeLayoutData(topadsData, WishlistConsts.TYPE_TOPADS))
            }
        }
        return listData
    }

    private fun mapToProductCardList(
            listData: ArrayList<WishlistTypeLayoutData>,
            items: List<WishlistUiModel.Item>,
            typeLayout: String?,
            isAutomaticDelete: Boolean
    ): ArrayList<WishlistTypeLayoutData> {
        items.forEach { item ->
            val listGroupLabel = arrayListOf<ProductCardModel.LabelGroup>()

            item.labelGroup.forEach { labelGroupItem ->
                val labelGroup = ProductCardModel.LabelGroup(
                    position = labelGroupItem.position,
                    title = labelGroupItem.title,
                    type = labelGroupItem.type,
                    imageUrl = labelGroupItem.url
                )
                listGroupLabel.add(labelGroup)
            }

            val isButtonAtc = item.buttons.primaryButton.action == WishlistCollectionDetailFragment.ATC_WISHLIST

            val listBadge = arrayListOf<ProductCardModel.ShopBadge>()
            item.badges.forEach { badgesItem ->
                listBadge.add(ProductCardModel.ShopBadge(imageUrl = badgesItem.imageUrl))
            }

            val productModel = ProductCardModel(
                productImageUrl = item.imageUrl,
                productName = item.name,
                shopName = item.shop.name,
                formattedPrice = item.priceFmt,
                shopLocation = item.shop.location,
                isShopRatingYellow = true,
                hasButtonThreeDotsWishlist = true,
                hasAddToCartWishlist = isButtonAtc,
                hasSimilarProductWishlist = !isButtonAtc,
                labelGroupList = listGroupLabel,
                shopBadgeList = listBadge,
                discountPercentage = item.discountPercentageFmt,
                countSoldRating = item.rating,
                slashedPrice = item.originalPriceFmt,
                freeOngkir = ProductCardModel.FreeOngkir(
                    item.bebasOngkir.imageUrl.isNotEmpty(),
                    item.bebasOngkir.imageUrl
                ),
                isOutOfStock = !item.available
            )
            listData.add(WishlistTypeLayoutData(productModel, typeLayout, item, isAutomaticDelete))
        }
        return listData
    }

    fun convertRecommendationIntoProductDataModel(data: List<RecommendationItem>): List<ProductCardModel> {
        return data.map { element ->
            ProductCardModel(
                slashedPrice = element.slashedPrice,
                productName = element.name,
                formattedPrice = element.price,
                productImageUrl = element.imageUrl,
                isTopAds = element.isTopAds,
                discountPercentage = element.discountPercentage,
                reviewCount = element.countReview,
                ratingCount = element.rating,
                shopLocation = element.location,
                isWishlistVisible = true,
                isWishlisted = element.isWishlist,
                shopBadgeList = element.badges.map {
                    ProductCardModel.ShopBadge(
                        title = it.title,
                        imageUrl = it.imageUrl,
                    )
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                    isActive = element.isFreeOngkirActive,
                    imageUrl = element.freeOngkirImageUrl
                ),
                labelGroupList = element.labelGroupList.map { recommendationLabel ->
                    ProductCardModel.LabelGroup(
                        position = recommendationLabel.position,
                        title = recommendationLabel.title,
                        type = recommendationLabel.type,
                        imageUrl = recommendationLabel.imageUrl
                    )
                }
            )
        }
    }

    /*Note:
    at first, no need to convert to ui model (just use response data model),
    but both collection & wishlistV2 have same layout, but a bit difference of response data model
    to achieve same layout with 1 data model, both response of collection & wishlistV2 need to converted to 1 ui model
    later on, after wishlistV2 is removed, will remove this method also*/

    fun convertWishlistV2IntoWishlistUiModel(data: WishlistV2Response.Data.WishlistV2): WishlistUiModel {
        val newListSortFilters = arrayListOf<WishlistUiModel.SortFiltersItem>()
        data.sortFilters.forEach { item ->
            val newListOptions = arrayListOf<WishlistUiModel.SortFiltersItem.OptionsItem>()
            item.options.forEach { option ->
                val newOptionItem = WishlistUiModel.SortFiltersItem.OptionsItem(
                    isSelected = option.isSelected,
                    description = option.description,
                    optionId = option.optionId,
                    text = option.text
                )
                newListOptions.add(newOptionItem)
            }
            val sortFilterItem = WishlistUiModel.SortFiltersItem(
                selectionType = item.selectionType,
                isActive = item.isActive,
                name = item.name,
                options = newListOptions,
                id = item.id,
                text = item.text
            )
            newListSortFilters.add(sortFilterItem)
        }

        val listItem = arrayListOf<WishlistUiModel.Item>()
        data.items.forEach { item ->
            val listLabelGroup = arrayListOf<WishlistUiModel.Item.LabelGroupItem>()
            item.labelGroup.forEach { labelGroupItem ->
                val newLabelGroup = WishlistUiModel.Item.LabelGroupItem(
                    position = labelGroupItem.position,
                    title = labelGroupItem.title,
                    type = labelGroupItem.type,
                    url = labelGroupItem.url
                )
                listLabelGroup.add(newLabelGroup)
            }

            val newFulfillment = WishlistUiModel.Item.Shop.Fulfillment(
                text = item.shop.fulfillment.text,
                isFulfillment = item.shop.fulfillment.isFulfillment
            )

            val newShop = WishlistUiModel.Item.Shop(
                isTokonow = item.shop.isTokonow,
                name = item.shop.name,
                location = item.shop.location,
                id = item.shop.id,
                fulfillment = newFulfillment,
                url = item.shop.url
            )

            val listWholesalePrice = arrayListOf<WishlistUiModel.Item.WholesalePriceItem>()
            item.wholesalePrice.forEach { wholesalePriceItem ->
                val newWholesalePrice = WishlistUiModel.Item.WholesalePriceItem(
                    price = wholesalePriceItem.price,
                    maximum = wholesalePriceItem.maximum,
                    minimum = wholesalePriceItem.minimum
                )
                listWholesalePrice.add(newWholesalePrice)
            }

            val listAdditionalButton = arrayListOf<WishlistUiModel.Item.Buttons.AdditionalButtonsItem>()
            item.buttons.additionalButtons.forEach { additionalButtonsItem ->
                val newAdditionalButtonItem = WishlistUiModel.Item.Buttons.AdditionalButtonsItem(
                    action = additionalButtonsItem.action,
                    text = additionalButtonsItem.text,
                    url = additionalButtonsItem.url
                )
                listAdditionalButton.add(newAdditionalButtonItem)
            }

            val newPrimaryButton = WishlistUiModel.Item.Buttons.PrimaryButton(
                action = item.buttons.primaryButton.action,
                text = item.buttons.primaryButton.text,
                url = item.buttons.primaryButton.url
            )

            val newButtons = WishlistUiModel.Item.Buttons(
                additionalButtons = listAdditionalButton,
                primaryButton = newPrimaryButton
            )

            val newListLabel = arrayListOf<String>()
            item.labels.forEach {
                newListLabel.add(it)
            }

            val newListBadge = arrayListOf<WishlistUiModel.Item.BadgesItem>()
            item.badges.forEach { badgesItem ->
                val newBadgesItem = WishlistUiModel.Item.BadgesItem(
                    imageUrl = badgesItem.imageUrl,
                    title = badgesItem.title
                )
                newListBadge.add(newBadgesItem)
            }

            val newBebasOngkir = WishlistUiModel.Item.BebasOngkir(
                imageUrl = item.bebasOngkir.imageUrl,
                type = item.bebasOngkir.type,
                title = item.bebasOngkir.title
            )

            val newListCategoryItem = arrayListOf<WishlistUiModel.Item.CategoryItem>()
            item.category.forEach { categoryItem ->
                val newCategoryItem = WishlistUiModel.Item.CategoryItem(
                    categoryName = categoryItem.categoryName,
                    categoryId = categoryItem.categoryId
                )
                newListCategoryItem.add(newCategoryItem)
            }

            val newItem = WishlistUiModel.Item(
                originalPrice = item.originalPrice,
                labelGroup = listLabelGroup,
                shop = newShop,
                priceFmt = item.priceFmt,
                available = item.available,
                rating = item.rating,
                originalPriceFmt = item.originalPriceFmt,
                discountPercentage = item.discountPercentage,
                defaultChildId = item.defaultChildId,
                price = item.price,
                wholesalePrice = listWholesalePrice,
                id = item.id,
                buttons = newButtons,
                imageUrl = item.imageUrl,
                discountPercentageFmt = item.discountPercentageFmt,
                wishlistId = item.wishlistId,
                variantName = item.variantName,
                labelStock = item.labelStock,
                url = item.url,
                labelStatus = item.labelStatus,
                labels = newListLabel,
                badges = newListBadge,
                name = item.name,
                minOrder = item.minOrder,
                bebasOngkir = newBebasOngkir,
                category = newListCategoryItem,
                preorder = item.preorder,
                soldCount = item.soldCount
            )
            listItem.add(newItem)
        }

        val listButtonEmptyState = arrayListOf<WishlistUiModel.EmptyState.ButtonEmptyState>()
        val newEmptyStateButtonItem = WishlistUiModel.EmptyState.ButtonEmptyState(
            action = data.emptyState.button.action,
            text = data.emptyState.button.text,
            url = data.emptyState.button.url
        )
        listButtonEmptyState.add(newEmptyStateButtonItem)

        val listMessageEmptyState = arrayListOf<WishlistUiModel.EmptyState.MessageEmptyState>()
        data.emptyState.messages.forEach { message ->
            val messageEmptyState = WishlistUiModel.EmptyState.MessageEmptyState(
                title = message.title,
                description = message.desc,
                imageUrl = message.imageUrl
            )
            listMessageEmptyState.add(messageEmptyState)
        }

        val newEmptyState = WishlistUiModel.EmptyState(
            button = listButtonEmptyState,
            messages = listMessageEmptyState,
            type = data.emptyState.type
        )

        val newButtonTicker = WishlistUiModel.TickerState.ButtonTicker(
            action = data.ticker.button.action,
            text = data.ticker.button.text
        )
        val newTickerState = WishlistUiModel.TickerState(
            message = data.ticker.message,
            type = data.ticker.type,
            button = newButtonTicker
        )

        val newListStorageCleanerBottomSheet = arrayListOf<WishlistUiModel.StorageCleanerBottomSheet.OptionCleanerBottomsheet>()
        data.storageCleanerBottomSheet.options.forEach { optionItem ->
            val newOptionItem = WishlistUiModel.StorageCleanerBottomSheet.OptionCleanerBottomsheet(
                name = optionItem.name,
                description = optionItem.description
            )
            newListStorageCleanerBottomSheet.add(newOptionItem)
        }

        val newBtnClean = WishlistUiModel.StorageCleanerBottomSheet.ButtonCleanBottomSheet(
            text = data.storageCleanerBottomSheet.btnCleanBottomSheet.text
        )

        val newStorageCleanerBottomSheet = WishlistUiModel.StorageCleanerBottomSheet(
            title = data.storageCleanerBottomSheet.title,
            description = data.storageCleanerBottomSheet.description,
            options = newListStorageCleanerBottomSheet,
            btnCleanBottomSheet = newBtnClean
        )

        return WishlistUiModel(
            errorMessage = data.errorMessage,
            offset = data.offset,
            hasNextPage = data.hasNextPage,
            query = data.query,
            sortFilters = newListSortFilters,
            limit = data.limit,
            totalData = data.totalData,
            page = data.page,
            items = listItem,
            emptyState = newEmptyState,
            ticker = newTickerState,
            storageCleanerBottomSheet = newStorageCleanerBottomSheet,
            countRemovableItems = data.countRemovableItems,
            showDeleteProgress = data.showDeleteProgress
        )
    }

    fun convertCollectionItemsIntoWishlistUiModel(data: GetWishlistCollectionItemsResponse.GetWishlistCollectionItems): WishlistUiModel {
        val newListSortFilters = arrayListOf<WishlistUiModel.SortFiltersItem>()
        data.sortFilters.forEach { item ->
            val newListOptions = arrayListOf<WishlistUiModel.SortFiltersItem.OptionsItem>()
            item.options.forEach { option ->
                val newOptionItem = WishlistUiModel.SortFiltersItem.OptionsItem(
                    isSelected = option.isSelected,
                    description = option.description,
                    optionId = option.optionId,
                    text = option.text
                )
                newListOptions.add(newOptionItem)
            }
            val sortFilterItem = WishlistUiModel.SortFiltersItem(
                selectionType = item.selectionType,
                isActive = item.isActive,
                name = item.name,
                options = newListOptions,
                id = item.id,
                text = item.text
            )
            newListSortFilters.add(sortFilterItem)
        }

        val listItem = arrayListOf<WishlistUiModel.Item>()
        data.items.forEach { item ->
            val listLabelGroup = arrayListOf<WishlistUiModel.Item.LabelGroupItem>()
            item.labelGroup.forEach { labelGroupItem ->
                val newLabelGroup = WishlistUiModel.Item.LabelGroupItem(
                    position = labelGroupItem.position,
                    title = labelGroupItem.title,
                    type = labelGroupItem.type,
                    url = labelGroupItem.url
                )
                listLabelGroup.add(newLabelGroup)
            }

            val newFulfillment = WishlistUiModel.Item.Shop.Fulfillment(
                text = item.shop.fulfillment.text,
                isFulfillment = item.shop.fulfillment.isFulfillment
            )

            val newShop = WishlistUiModel.Item.Shop(
                isTokonow = item.shop.isTokonow,
                name = item.shop.name,
                location = item.shop.location,
                id = item.shop.id,
                fulfillment = newFulfillment,
                url = item.shop.url
            )

            val listWholesalePrice = arrayListOf<WishlistUiModel.Item.WholesalePriceItem>()
            item.wholesalePrice.forEach { wholesalePriceItem ->
                val newWholesalePrice = WishlistUiModel.Item.WholesalePriceItem(
                    price = wholesalePriceItem.price,
                    maximum = wholesalePriceItem.maximum,
                    minimum = wholesalePriceItem.minimum
                )
                listWholesalePrice.add(newWholesalePrice)
            }

            val listAdditionalButton = arrayListOf<WishlistUiModel.Item.Buttons.AdditionalButtonsItem>()
            item.buttons.additionalButtons.forEach { additionalButtonsItem ->
                val newAdditionalButtonItem = WishlistUiModel.Item.Buttons.AdditionalButtonsItem(
                    action = additionalButtonsItem.action,
                    text = additionalButtonsItem.text,
                    url = additionalButtonsItem.url
                )
                listAdditionalButton.add(newAdditionalButtonItem)
            }

            val newPrimaryButton = WishlistUiModel.Item.Buttons.PrimaryButton(
                action = item.buttons.primaryButton.action,
                text = item.buttons.primaryButton.text,
                url = item.buttons.primaryButton.url
            )

            val newButtons = WishlistUiModel.Item.Buttons(
                additionalButtons = listAdditionalButton,
                primaryButton = newPrimaryButton
            )

            val newListLabel = arrayListOf<String>()
            item.labels.forEach {
                newListLabel.add(it)
            }

            val newListBadge = arrayListOf<WishlistUiModel.Item.BadgesItem>()
            item.badges.forEach { badgesItem ->
                val newBadgesItem = WishlistUiModel.Item.BadgesItem(
                    imageUrl = badgesItem.imageUrl,
                    title = badgesItem.title
                )
                newListBadge.add(newBadgesItem)
            }

            val newBebasOngkir = WishlistUiModel.Item.BebasOngkir(
                imageUrl = item.bebasOngkir.imageUrl,
                type = item.bebasOngkir.type,
                title = item.bebasOngkir.title
            )

            val newListCategoryItem = arrayListOf<WishlistUiModel.Item.CategoryItem>()
            item.category.forEach { categoryItem ->
                val newCategoryItem = WishlistUiModel.Item.CategoryItem(
                    categoryName = categoryItem.categoryName,
                    categoryId = categoryItem.categoryId
                )
                newListCategoryItem.add(newCategoryItem)
            }

            val newItem = WishlistUiModel.Item(
                originalPrice = item.originalPrice,
                labelGroup = listLabelGroup,
                shop = newShop,
                priceFmt = item.priceFmt,
                available = item.available,
                rating = item.rating,
                originalPriceFmt = item.originalPriceFmt,
                discountPercentage = item.discountPercentage,
                defaultChildId = item.defaultChildId,
                price = item.price,
                wholesalePrice = listWholesalePrice,
                id = item.id,
                buttons = newButtons,
                imageUrl = item.imageUrl,
                discountPercentageFmt = item.discountPercentageFmt,
                wishlistId = item.wishlistId,
                variantName = item.variantName,
                labelStock = item.labelStock,
                url = item.url,
                labelStatus = item.labelStatus,
                labels = newListLabel,
                badges = newListBadge,
                name = item.name,
                minOrder = item.minOrder,
                bebasOngkir = newBebasOngkir,
                category = newListCategoryItem,
                preorder = item.preorder,
                soldCount = item.soldCount
            )
            listItem.add(newItem)
        }

        val listButtonEmptyState = arrayListOf<WishlistUiModel.EmptyState.ButtonEmptyState>()
        data.emptyState.buttons.forEach { buttonsItem ->
            val newEmptyStateButtonItem = WishlistUiModel.EmptyState.ButtonEmptyState(
                action = buttonsItem.action,
                text = buttonsItem.text,
                url = buttonsItem.url
            )
            listButtonEmptyState.add(newEmptyStateButtonItem)
        }

        val listMessageEmptyState = arrayListOf<WishlistUiModel.EmptyState.MessageEmptyState>()
        data.emptyState.messages.forEach { message ->
            val messageEmptyState = WishlistUiModel.EmptyState.MessageEmptyState(
                title = message.title,
                description = message.desc,
                imageUrl = message.imageUrl
            )
            listMessageEmptyState.add(messageEmptyState)
        }

        val newEmptyState = WishlistUiModel.EmptyState(
            button = listButtonEmptyState,
            messages = listMessageEmptyState,
            type = data.emptyState.type
        )

        val newButtonTicker = WishlistUiModel.TickerState.ButtonTicker(
            action = data.ticker.button.action,
            text = data.ticker.button.text
        )
        val newTickerState = WishlistUiModel.TickerState(
            message = data.ticker.message,
            type = data.ticker.type,
            button = newButtonTicker
        )

        return WishlistUiModel(
            errorMessage = data.errorMessage,
            offset = data.offset,
            hasNextPage = data.hasNextPage,
            query = data.query,
            sortFilters = newListSortFilters,
            limit = data.limit,
            totalData = data.totalData,
            page = data.page,
            items = listItem,
            emptyState = newEmptyState,
            ticker = newTickerState,
            storageCleanerBottomSheet = mapToStorageCleanerBottomSheet(data.storageCleanerBottomsheet),
            countRemovableItems = data.countRemovableItems,
            showDeleteProgress = data.showDeleteProgress
        )
    }

    fun mapToStorageCleanerBottomSheet(storageCleanerBottomsheet: GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.StorageCleanerBottomsheet): WishlistUiModel.StorageCleanerBottomSheet {
        val newListStorageCleanerBottomSheet = arrayListOf<WishlistUiModel.StorageCleanerBottomSheet.OptionCleanerBottomsheet>()
        storageCleanerBottomsheet.options.forEach { optionItem ->
            val newOptionItem = WishlistUiModel.StorageCleanerBottomSheet.OptionCleanerBottomsheet(
                name = optionItem.name,
                description = optionItem.description
            )
            newListStorageCleanerBottomSheet.add(newOptionItem)
        }

        val newBtnClean = WishlistUiModel.StorageCleanerBottomSheet.ButtonCleanBottomSheet(
            text = storageCleanerBottomsheet.button.text
        )

        return WishlistUiModel.StorageCleanerBottomSheet(
            title = storageCleanerBottomsheet.title,
            description = storageCleanerBottomsheet.description,
            options = newListStorageCleanerBottomSheet,
            btnCleanBottomSheet = newBtnClean
        )
    }
}
