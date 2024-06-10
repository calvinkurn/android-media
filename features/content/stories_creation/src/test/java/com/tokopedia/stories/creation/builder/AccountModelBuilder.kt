package com.tokopedia.stories.creation.builder

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class AccountModelBuilder {

    fun build(
        idShop: String = "1234",
        idBuyer: String = "5678",
        tncShop: Boolean = true,
        usernameShop: Boolean = true,
        tncBuyer: Boolean = true,
        usernameBuyer: Boolean = true,
        hasShopAccount: Boolean = true,
        hasUserAccount: Boolean = true,
    ): List<ContentAccountUiModel> {
        val accountList = mutableListOf<ContentAccountUiModel>()

        if (hasShopAccount) {
            accountList.add(
                ContentAccountUiModel(
                    id = idShop,
                    type = ContentCommonUserType.TYPE_SHOP,
                    name = "Shop",
                    iconUrl = "icon.url.shop",
                    hasUsername = usernameShop,
                    hasAcceptTnc = tncShop,
                    enable = tncShop
                )
            )
        }

        if (hasUserAccount) {
            accountList.add(
                ContentAccountUiModel(
                    id = idBuyer,
                    type = ContentCommonUserType.TYPE_USER,
                    name = "Buyer",
                    iconUrl = "icon.url.buyer",
                    hasUsername = usernameBuyer,
                    hasAcceptTnc = tncBuyer,
                    enable = tncBuyer
                )
            )
        }

        return accountList
    }
}
