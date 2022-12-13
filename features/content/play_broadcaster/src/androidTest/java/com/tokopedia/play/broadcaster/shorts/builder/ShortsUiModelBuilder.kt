package com.tokopedia.play.broadcaster.shorts.builder

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel

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
}
