package com.tokopedia.play.broadcaster.shorts.builder

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.content.product.picker.sgc.model.OriginalPrice
import com.tokopedia.content.product.picker.sgc.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.sgc.model.campaign.CampaignStatusUiModel
import com.tokopedia.content.product.picker.sgc.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.sgc.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.sgc.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.sgc.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.sgc.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.sgc.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
class ShortsUiModelBuilder {

    fun buildShortsConfig(
        shortsId: String = "123",
        shortsAllowed: Boolean = true,
        isBanned: Boolean = false,
        hasContent: Boolean = false,
        tncList: List<TermsAndConditionUiModel> = listOf(),
        maxTaggedProduct: Int = 30,
        maxTitleCharacter: Int = 24,
        shortsVideoSourceId: String = "asdf"
    ) = PlayShortsConfigUiModel(
        shortsId = shortsId,
        shortsAllowed = shortsAllowed,
        isBanned = isBanned,
        tncList = tncList,
        maxTaggedProduct = maxTaggedProduct,
        maxTitleCharacter = maxTitleCharacter,
        shortsVideoSourceId = shortsVideoSourceId,
        hasContent = hasContent,
    )

    fun buildAccountListModel(
        idShop: String = "1234",
        idBuyer: String = "5678",
        tncShop: Boolean = true,
        usernameShop: Boolean = true,
        tncBuyer: Boolean = true,
        usernameBuyer: Boolean = true,
        onlyShop: Boolean = false,
        onlyBuyer: Boolean = false
    ): List<ContentAccountUiModel> {
        return when {
            onlyShop -> listOf(
                ContentAccountUiModel(
                    id = idShop,
                    type = ContentCommonUserType.TYPE_SHOP,
                    name = "Shop",
                    iconUrl = "icon.url.shop",
                    badge = "icon.badge",
                    hasUsername = usernameShop,
                    hasAcceptTnc = tncShop,
                    enable = tncShop
                )
            )
            onlyBuyer -> listOf(
                ContentAccountUiModel(
                    id = idBuyer,
                    type = ContentCommonUserType.TYPE_USER,
                    name = "Buyer",
                    iconUrl = "icon.url.buyer",
                    badge = "icon.badge",
                    hasUsername = usernameBuyer,
                    hasAcceptTnc = tncBuyer,
                    enable = tncBuyer
                )
            )
            else -> listOf(
                ContentAccountUiModel(
                    id = idShop,
                    type = ContentCommonUserType.TYPE_SHOP,
                    name = "Shop",
                    iconUrl = "icon.url.shop",
                    badge = "icon.badge",
                    hasUsername = usernameShop,
                    hasAcceptTnc = tncShop,
                    enable = tncShop
                ),
                ContentAccountUiModel(
                    id = idBuyer,
                    type = ContentCommonUserType.TYPE_USER,
                    name = "Buyer",
                    iconUrl = "icon.url.buyer",
                    badge = "icon.badge",
                    hasUsername = usernameBuyer,
                    hasAcceptTnc = tncBuyer,
                    enable = tncBuyer
                ),
            )
        }
    }

    fun buildEtalaseList(
        size: Int = 1
    ): List<EtalaseUiModel> {
        return List(size) {
            EtalaseUiModel(
                id = it.toString(),
                imageUrl = "",
                title = "Etalase $it",
                totalProduct = it,
            )
        }
    }

    fun buildCampaignList(
        size: Int = 1,
    ): List<CampaignUiModel> {
        return List(size) {
            CampaignUiModel(
                id = it.toString(),
                imageUrl = "",
                title = "Campaign $it",
                totalProduct = it,
                startDateFmt = "",
                endDateFmt = "",
                status = CampaignStatusUiModel(
                    status = CampaignStatus.Ongoing,
                    text = "Berlangsung"
                )
            )
        }
    }

    fun buildProductTagSectionList(
        size: Int = 2,
    ): List<ProductTagSectionUiModel> {
        return List(1) {
            ProductTagSectionUiModel("", CampaignStatus.Ongoing, List(size) { productCounter ->
                ProductUiModel(productCounter.toString(), "Product $it", false, "", 0, false,"", 1, OriginalPrice("Rp1000.00", 1000.0), PinProductUiModel.Empty, "")
            })
        }
    }

    fun buildEtalaseProducts(
        size: Int = 2,
        hasNextPage: Boolean = false,
    ): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = buildProductTagSectionList(size).flatMap { it.products },
            hasNextPage = hasNextPage,
        )
    }

    fun buildTags(
        size: Int = 3,
        minTags: Int = 1,
        maxTags: Int = 2,
    ): PlayTagUiModel {
        return PlayTagUiModel(
            tags = mutableSetOf<PlayTagItem>().apply {
                repeat(size) {
                    add(
                        PlayTagItem(
                            tag = "Tag $it",
                            isChosen = false,
                            isActive = true,
                        )
                    )
                }
            },
            minTags = minTags,
            maxTags = maxTags,
        )
    }

    fun buildLastTaggedProducts(
        size: Int = 10,
        hasNextPage: Boolean = true,
        nextCursor: String = "",
    ): com.tokopedia.content.common.producttag.view.uimodel.PagedDataUiModel<com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel> {
        return com.tokopedia.content.common.producttag.view.uimodel.PagedDataUiModel(
            dataList = List(size) {
                com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel(
                    id = it.toString(),
                    shopID = it.toString(),
                    shopName = "Shop $it",
                    shopBadge = listOf(),
                    name = "Product $it",
                    coverURL = "",
                    webLink = "",
                    appLink = "",
                    star = (it % 5).toString(),
                    price = (it * 10000).toDouble(),
                    priceFmt = "Rp ${(it * 10000)}",
                    isDiscount = true,
                    discount = 5.0,
                    discountFmt = "5%",
                    priceOriginal = (it * 10000).toDouble(),
                    priceOriginalFmt = "Rp ${(it * 10000).toDouble()}",
                    priceDiscount = (it * 10000).toDouble(),
                    priceDiscountFmt = "Rp ${(it * 10000).toDouble()}",
                    totalSold = it,
                    totalSoldFmt = it.toString(),
                    isBebasOngkir = false,
                    bebasOngkirStatus = "",
                    bebasOngkirURL = "",
                )
            },
            hasNextPage = hasNextPage,
            nextCursor = nextCursor,
        )
    }
}
