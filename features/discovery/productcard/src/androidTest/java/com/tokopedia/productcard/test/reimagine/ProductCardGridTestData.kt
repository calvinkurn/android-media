package com.tokopedia.productcard.test.reimagine

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.utils.isDisplayedContainingText
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.longProductName
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.productVideoUrl
import com.tokopedia.productcard.utils.RED
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import com.tokopedia.productcard.utils.WORDING_SEGERA_HABIS
import org.hamcrest.Matcher

internal val productCardReimagineGridTestData =
    productCardReimagineTestData + listOf(
        video(),
        threeDots(),
        blur(),
        stockInfoTersedia(),
        stockInfoSegeraHabis(),
        overlayWithStockInfo(),
        blurWithRibbonLabels(),
        blurWithStockInfo(),
        blurWithLabelAssignedValue(),
        blurWithPreventiveOverlay(),
    )

private fun video(): ProductCardReimagineMatcher {
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
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
        shopBadge = shopBadge,
        videoUrl = productVideoUrl,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardVideoIdentifier to isDisplayed(),
    )

    return Triple(model, matcher, "Video")
}

private fun threeDots(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name panjangggg bangettttt",
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
        videoUrl = productVideoUrl,
        hasThreeDots = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardVideoIdentifier to isDisplayed(),
        R.id.productCardThreeDots to isDisplayed(),
    )

    return Triple(model, matcher, "Three Dots")
}

private fun blur(): ProductCardReimagineMatcher {
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
        isAds = true,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        isSafeProduct = true,
        videoUrl = productVideoUrl,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(""),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),

        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),
    )

    return Triple(model, matcher, "Blur")
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
        stockInfo = stockInfo
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
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
        stockInfo = stockInfo
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
    )

    return Triple(model, matcher, "Stock Info Segera Habis")
}

private fun overlayWithStockInfo(): ProductCardReimagineMatcher {
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
        labelGroupList = listOf(
            reimagineProductOffers,
            reimagineCredibilityLabel,
            labelGroupOverlay1(),
            labelGroupOverlay2(),
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        stockInfo = stockInfo,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardOverlay1 to isDisplayed(),
        R.id.productCardOverlay2 to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelOffer to isDisplayedWithText(reimagineProductOffers.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),
        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
    )

    return Triple(model, matcher, "Label Overlay with Stock Info")
}

private fun blurWithRibbonLabels(): ProductCardReimagineMatcher {
    val reimagineRibbon = labelGroupRibbon(RED)
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
        isAds = true,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineRibbon,
            labelGroupOverlay1(),
            labelGroupOverlay2(),
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        isSafeProduct = true,
        videoUrl = productVideoUrl,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(""),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),

        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),
    )

    return Triple(model, matcher, "Blur With Ribbon Labels")
}

private fun blurWithStockInfo(): ProductCardReimagineMatcher {
    val reimagineRibbon = labelGroupRibbon(RED)
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

    val stockInfo = ProductCardModel.StockInfo(
        percentage = 90,
        label = WORDING_SEGERA_HABIS
    )

    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        isAds = true,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineRibbon,
            labelGroupOverlay1(),
            labelGroupOverlay2(),
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        isSafeProduct = true,
        videoUrl = productVideoUrl,
        stockInfo = stockInfo,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(""),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),

        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),

        R.id.productCardStockInfoBackground to isDisplayed(),
        R.id.productCardStockInfoLabel to isDisplayedWithText(stockInfo.label),
        R.id.productCardStockInfoBar to isDisplayed(),
    )

    return Triple(model, matcher, "Blur With Ribbon Labels")
}

private fun blurWithLabelAssignedValue(): ProductCardReimagineMatcher {
    val reimagineAssignedValueLabel = labelGroupAssignedValue()
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
            reimagineAssignedValueLabel,
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        isSafeProduct = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(""),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),

        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),
    )

    return Triple(model, matcher, "Blur With Label Assigned Value")
}

private fun blurWithPreventiveOverlay(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = labelGroupBenefit()
    val reimaginePreventiveOverlayLabel = labelGroupPreventiveOverlay()
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
            reimagineCredibilityLabel,
            reimaginePreventiveOverlayLabel,
            reimagineBenefitLabel
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        isSafeProduct = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(""),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),

        R.id.productCardLabelBenefit to isDisplayed(),
        R.id.productCardLabelBenefitText to isDisplayedWithText(reimagineBenefitLabel.title),

        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardLabelCredibility to isDisplayedWithText(reimagineCredibilityLabel.title),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
        R.id.productCardRatingDots to isDisplayed(),
        R.id.productCardShopSection to isDisplayed(),
        R.id.productCardShopBadge to isDisplayed(),
        R.id.productCardShopNameLocation to isDisplayed(),

        R.id.productCardSafeDivider to isDisplayed(),
        R.id.productCardSafeIcon to isDisplayed(),
        R.id.productCardSafeTitle to isDisplayed(),
        R.id.productCardSafeDescription to isDisplayed(),
        R.id.productCardSafeCheckInfo to isDisplayed(),
    )

    return Triple(model, matcher, "Blur With Preventive Overlay")
}
