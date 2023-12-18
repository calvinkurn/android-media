package com.tokopedia.productcard.test.reimagine

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_BENEFIT
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_RIBBON
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.longProductName
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.utils.LIGHT_GREEN
import com.tokopedia.productcard.utils.RED
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import com.tokopedia.productcard.utils.WORDING_SEGERA_HABIS
import org.hamcrest.Matcher

internal val productCardReimagineCarouselGridTestData =
    productCardReimagineTestData + listOf(
        twoLineProductName(),
        atc(),
        stockInfoTersedia(),
        stockInfoSegeraHabis(),
        ribbonAndSlashedPriceInline(),
        bmsm(),
        benefitPriorityOverBMSM(),
        imageBlurred(),
        imageBlurredWithStock(),
        itemInBackground(),
        itemInBackgroundHasRibbonAndSlashedPriceInline(),
    )

private fun twoLineProductName(): ProductCardReimagineMatcher {
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        hasMultilineName = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
    )

    return Triple(model, matcher, "Two line product name")
}

private fun atc(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        hasAddToCart = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardAddToCart to isDisplayed(),
    )

    return Triple(model, matcher, "ATC")
}

private fun stockInfoTersedia(): ProductCardReimagineMatcher {
    val stockInfo = ProductCardModel.StockInfo(
        percentage = 40,
        label = "Tersedia"
    )
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        stockInfo = stockInfo
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
    )

    return Triple(model, matcher, "Stock Info Tersedia")
}

private fun stockInfoSegeraHabis(): ProductCardReimagineMatcher {
    val stockInfo = ProductCardModel.StockInfo(
        percentage = 90,
        label = WORDING_SEGERA_HABIS
    )
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        stockInfo = stockInfo
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
    )

    return Triple(model, matcher, "Stock Info Segera Habis")
}

private fun ribbonAndSlashedPriceInline(): ProductCardReimagineMatcher {
    val stockInfo = ProductCardModel.StockInfo(
        percentage = 90,
        label = WORDING_SEGERA_HABIS
    )
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val reimagineRibbon = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_RIBBON,
        title = "20%",
        type = RED,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineRibbon
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        stockInfo = stockInfo
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPriceInline to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscountInline to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
        R.id.productCardRibbonText to isDisplayedWithText(reimagineRibbon.title),
        R.id.productCardRibbonArch to isDisplayed(),
        R.id.productCardRibbonBackground to isDisplayed(),
    )

    return Triple(model, matcher, "Ribbon & Slashed Price Inline")
}

private fun bmsm(): ProductCardReimagineMatcher {
    val reimagineProductOffers = labelGroupProductOffers()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineCredibilityLabel, reimagineProductOffers),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBMSM to isDisplayedWithText(reimagineProductOffers.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
    )

    return Triple(model, matcher, "BMSM / Product Offers")
}

private fun benefitPriorityOverBMSM(): ProductCardReimagineMatcher {
    val reimagineProductOffers = labelGroupProductOffers()
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel, reimagineProductOffers),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
    )

    return Triple(model, matcher, "Benefit prioritized over BMSM")
}

private fun imageBlurred(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )

    val reimagineRibbon = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_RIBBON,
        title = "20%",
        type = RED,
    )

    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        isAds = true,
        labelGroupList = listOf(
            reimagineRibbon,
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        isSafeProduct = true
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPriceInline to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscountInline to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),
        R.id.productCardSafeContainer to ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
        R.id.productCardSafeNameBackground to isDisplayed(),
        R.id.productCardRibbonText to isDisplayedWithText(reimagineRibbon.title),
        R.id.productCardRibbonArch to isDisplayed(),
        R.id.productCardRibbonBackground to isDisplayed(),
    )

    return Triple(model, matcher, "Image is blurred")
}

private fun imageBlurredWithStock(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )

    val reimagineRibbon = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_RIBBON,
        title = "20%",
        type = RED,
    )

    val stockInfo = ProductCardModel.StockInfo(
        percentage = 40,
        label = "Tersedia"
    )

    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        isAds = true,
        labelGroupList = listOf(
            reimagineRibbon,
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        isSafeProduct = true,
        stockInfo = stockInfo
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPriceInline to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscountInline to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),
        R.id.productCardSafeContainer to ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
        R.id.productCardSafeNameBackground to isDisplayed(),
        R.id.productCardRibbonText to isDisplayedWithText(reimagineRibbon.title),
        R.id.productCardRibbonArch to isDisplayed(),
        R.id.productCardRibbonBackground to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
    )

    return Triple(model, matcher, "Image is blurred")
}

private fun itemInBackground(): ProductCardReimagineMatcher {
    val reimagineProductOffers = labelGroupProductOffers()
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineProductOffers,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        isInBackground = true
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardOutline to isDisplayed(),
        )

    return Triple(model, matcher, "Item In Background")
}

private fun itemInBackgroundHasRibbonAndSlashedPriceInline(): ProductCardReimagineMatcher {
    val stockInfo = ProductCardModel.StockInfo(
        percentage = 90,
        label = WORDING_SEGERA_HABIS
    )
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val reimagineRibbon = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_RIBBON,
        title = "20%",
        type = RED,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineRibbon
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        stockInfo = stockInfo,
        isInBackground = true
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPriceInline to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscountInline to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardFreeShipping to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
        R.id.productCardRibbonText to isDisplayedWithText(reimagineRibbon.title),
        R.id.productCardRibbonArch to isDisplayed(),
        R.id.productCardRibbonBackground to isDisplayed(),
        R.id.productCardOutline to isDisplayed(),
    )

    return Triple(model, matcher, "Item in Background, Slashed Price Inline")
}
