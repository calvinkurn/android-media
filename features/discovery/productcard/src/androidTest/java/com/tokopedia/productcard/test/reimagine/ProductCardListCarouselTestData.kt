package com.tokopedia.productcard.test.reimagine

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.longProductName
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.utils.GOLD
import com.tokopedia.productcard.utils.RED
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import com.tokopedia.productcard.utils.WORDING_SEGERA_HABIS
import org.hamcrest.Matcher

internal val productCardReimagineListCarouselTestData =
    productCardReimagineTestData + listOf(
        atc(),
        stockInfoTersedia(),
        stockInfoSegeraHabis(),
        ribbon(RED),
        ribbon(GOLD),
        benefitPriorityOverOffers(),
        overlayWithStockInfo(),
    )

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
        hasAddToCart = true,
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

private fun ribbon(type: String): ProductCardReimagineMatcher {
    val reimagineRibbon = labelGroupRibbon(type)
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
            reimagineRibbon,
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
        ),
        rating = "4.5",
        shopBadge = shopBadge,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPriceInline to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscountInline to isDisplayedWithText("${model.discountPercentage}%"),

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

        R.id.productCardRibbon to isDisplayed(),
        R.id.productCardRibbonText to isDisplayedWithText(reimagineRibbon.title),
        R.id.productCardRibbonSlip to isDisplayed(),
    )

    return Triple(model, matcher, "Ribbon type $type")
}

private fun benefitPriorityOverOffers(): ProductCardReimagineMatcher {
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
    )

    return Triple(model, matcher, "Benefit prioritized over BMSM")
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
