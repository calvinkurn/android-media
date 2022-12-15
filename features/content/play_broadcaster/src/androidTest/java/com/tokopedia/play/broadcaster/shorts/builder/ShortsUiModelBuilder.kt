package com.tokopedia.play.broadcaster.shorts.builder

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
class ShortsUiModelBuilder {

    fun buildShortsConfig(
        shortsId: String = "123",
        shortsAllowed: Boolean = true,
        tncList: List<TermsAndConditionUiModel> = listOf(),
        maxTaggedProduct: Int = 30,
        maxTitleCharacter: Int = 24,
    ) = PlayShortsConfigUiModel(
        shortsId = shortsId,
        shortsAllowed = shortsAllowed,
        tncList = tncList,
        maxTaggedProduct = maxTaggedProduct,
        maxTitleCharacter = maxTitleCharacter,
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
                    enable = tncShop
                ),
                ContentAccountUiModel(
                    id = idBuyer,
                    type = ContentCommonUserType.TYPE_USER,
                    name = "Buyer",
                    iconUrl = "icon.url.buyer",
                    badge = "icon.badge",
                    hasUsername = usernameBuyer,
                    enable = tncBuyer
                ),
            )
        }
    }

    fun buildProductTagSectionList(
        size: Int = 2,
    ): List<ProductTagSectionUiModel> {
        return List(1) {
            ProductTagSectionUiModel("", CampaignStatus.Ongoing, List(size) { productCounter ->
                ProductUiModel(productCounter.toString(), "Product $it", "", 1, OriginalPrice("Rp1000.00", 1000.0))
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
        size: Int = 3
    ): Set<PlayTagUiModel> {
        return mutableSetOf<PlayTagUiModel>().apply {
            repeat(size) {
                add(
                    PlayTagUiModel(
                        tag = "Tag $it",
                        isChosen = false,
                    )
                )
            }
        }
    }
}
