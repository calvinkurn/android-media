package com.tokopedia.productcard.test.reimagine

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.longProductName
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.productVideoUrl
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import org.hamcrest.Matcher

internal val productCardReimagineListTestData =
    productCardReimagineTestData + listOf(
        labelPreventiveThematic(),
        video(),
        threeDots(),
        labelAssignedValueGradient(),
        labelAssignedValueSolid(),
        bmsm(),
    )

private fun labelPreventiveThematic(): ProductCardReimagineMatcher {
    val reimaginePreventiveThematicLabel = labelGroupPreventiveThematic()
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = longProductName,
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimaginePreventiveThematicLabel),
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelPreventiveThematic to isDisplayedWithText(
            reimaginePreventiveThematicLabel.title
        ),
    )

    return Triple(model, matcher, "Label Preventive Thematic")
}

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

private fun labelAssignedValueGradient(): ProductCardReimagineMatcher {
    val reimagineAssignedValueLabel = labelGroupAssignedValueGradient()
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
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        videoUrl = productVideoUrl,
        hasThreeDots = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardLabelAssignedValueBackground to isDisplayed(),
        R.id.productCardLabelAssignedValueIcon to isDisplayed(),
        R.id.productCardLabelAssignedValueText to isDisplayedWithText(reimagineAssignedValueLabel.title),
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

    return Triple(model, matcher, "Label Assigned Value Gradient Color")
}

private fun labelAssignedValueSolid(): ProductCardReimagineMatcher {
    val reimagineAssignedValueLabel = labelGroupAssignedValueSolid()
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
        freeShipping = ProductCardModel.FreeShipping(
            imageUrl = freeOngkirImageUrl,
        ),
        videoUrl = productVideoUrl,
        hasThreeDots = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardLabelAssignedValueBackground to isDisplayed(),
        R.id.productCardLabelAssignedValueText to isDisplayedWithText(reimagineAssignedValueLabel.title),
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

    return Triple(model, matcher, "Label Assigned Value Solid Color")
}

private fun bmsm(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = labelGroupBenefit()
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
        R.id.productCardLabelOffer to isDisplayedWithText(reimagineProductOffers.title),
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
