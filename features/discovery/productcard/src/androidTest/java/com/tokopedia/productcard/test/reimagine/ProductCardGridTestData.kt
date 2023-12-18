package com.tokopedia.productcard.test.reimagine

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_BENEFIT
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.isNotDisplayed
import com.tokopedia.productcard.test.utils.longProductName
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.productVideoUrl
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import org.hamcrest.Matcher

internal val productCardReimagineGridTestData =
    productCardReimagineTestData + listOf(
        video(),
        threeDots(),
        imageBlurred(),
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
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        videoUrl = productVideoUrl,
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
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        videoUrl = productVideoUrl,
        hasThreeDots = true,
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
        R.id.productCardVideoIdentifier to isDisplayed(),
        R.id.productCardThreeDots to isDisplayed(),
    )

    return Triple(model, matcher, "Three Dots")
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

    val reimaginePreventiveThematicLabel = labelGroupPreventiveThematic()

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
            reimaginePreventiveThematicLabel
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        isSafeProduct = true,
        videoUrl = productVideoUrl,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
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
        R.id.productCardLabelPreventiveThematic to isNotDisplayed(),
        R.id.productCardVideoIdentifier to isNotDisplayed(),
    )

    return Triple(model, matcher, "Image is blurred")
}
