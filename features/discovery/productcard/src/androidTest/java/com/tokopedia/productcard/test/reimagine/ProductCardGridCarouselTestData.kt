package com.tokopedia.productcard.test.reimagine

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_BENEFIT
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.ProductCardModel
import org.hamcrest.Matcher
import com.tokopedia.productcard.R
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.utils.LIGHT_GREEN
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import com.tokopedia.productcard.reimagine.ProductCardModel.FreeShipping
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup
import com.tokopedia.productcard.reimagine.ProductCardModel.ShopBadge

typealias ProductCardReimagineMatcher = Pair<ProductCardModel, Map<Int, Matcher<View?>>>

internal val productCardReimagineCarouselGridTestData = listOf(
    imageNamePrice(),
    ads(),
    twoLineProductName(),
    discountSlashedPrice(),
    slashedPriceWithoutDiscount(),
    labelBenefit(),
    rating(),
    labelCredibility(),
    ratingAndLabelCredibility(),
    shopSection(),
    shopSectionWithoutBadge(),
    shopSectionWithoutTitle(),
    bebasOngkir(),
)

private fun imageNamePrice(): ProductCardReimagineMatcher {
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name very long long loooooonnggg product name",
        price = "Rp79.000",
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
    )

    return model to matcher
}

private fun twoLineProductName(): ProductCardReimagineMatcher {
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "2 Line Product Name so long blabaldfsklj flsakjf ;sljdf klsdf",
        price = "Rp79.000",
        hasMultilineName = true,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
    )

    return model to matcher
}

private fun ads(): ProductCardReimagineMatcher {
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        isAds = true,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardAds to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
    )

    return model to matcher
}

private fun discountSlashedPrice(): ProductCardReimagineMatcher {
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
    )

    return model to matcher
}

private fun slashedPriceWithoutDiscount(): ProductCardReimagineMatcher {
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
    )

    return model to matcher
}

private fun labelBenefit(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel),
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
    )

    return model to matcher
}

private fun rating(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel),
        rating = "4.5",
    )

    val matcher = mapOf<Int, Matcher<View?>>(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardName to isDisplayedWithText(model.name),
        R.id.productCardPrice to isDisplayedWithText(model.price),
        R.id.productCardSlashedPrice to isDisplayedWithText(model.slashedPrice),
        R.id.productCardDiscount to isDisplayedWithText("${model.discountPercentage}%"),
        R.id.productCardLabelBenefit to isDisplayedWithText(reimagineBenefitLabel.title),
        R.id.productCardCredibility to isDisplayed(),
        R.id.productCardRatingIcon to isDisplayed(),
        R.id.productCardRating to isDisplayedWithText(model.rating),
    )

    return model to matcher
}

private fun labelCredibility(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
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
    )

    return model to matcher
}

private fun ratingAndLabelCredibility(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )

    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
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
    )

    return model to matcher
}

private fun shopSection(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
        shopBadge = shopBadge,
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
    )

    return model to matcher
}

private fun shopSectionWithoutBadge(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ShopBadge(
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
        shopBadge = shopBadge,
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
        R.id.productCardShopNameLocation to isDisplayed(),
    )

    return model to matcher
}

private fun shopSectionWithoutTitle(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
        shopBadge = shopBadge,
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
    )

    return model to matcher
}

private fun bebasOngkir(): ProductCardReimagineMatcher {
    val reimagineBenefitLabel = LabelGroup(
        position = LABEL_REIMAGINE_BENEFIT,
        title = "Cashback Rp10 rb",
        type = LIGHT_GREEN,
    )
    val reimagineCredibilityLabel = LabelGroup(
        position = LABEL_REIMAGINE_CREDIBILITY,
        title = "10 rb+ terjual",
        type = TEXT_DARK_GREY,
    )
    val shopBadge = ShopBadge(
        imageUrl = officialStoreBadgeImageUrl,
        title = "Shop Name paling panjang",
    )
    val model = ProductCardModel(
        imageUrl = productImageUrl,
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineBenefitLabel, reimagineCredibilityLabel),
        rating = "4.5",
        shopBadge = shopBadge,
        freeShipping = FreeShipping(
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

    return model to matcher
}
