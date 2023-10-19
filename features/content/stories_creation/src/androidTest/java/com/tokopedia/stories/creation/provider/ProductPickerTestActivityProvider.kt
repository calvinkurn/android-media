package com.tokopedia.stories.creation.provider

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
object ProductPickerTestActivityProvider {

    var productTag: List<ProductTagSectionUiModel> = emptyList()

    var selectedAccount: ContentAccountUiModel = ContentAccountUiModel(
        id = "123",
        type = ContentCommonUserType.TYPE_SHOP,
        name = "Shop",
        iconUrl = "icon.url.shop",
        badge = "icon.badge",
        hasUsername = true,
        hasAcceptTnc = true,
        enable = true
    )

    var maxProduct: Int = 0

    var mockRepository: ContentProductPickerSellerRepository = mockk(relaxed = true)
}
