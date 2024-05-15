package com.tokopedia.play.broadcaster.setup

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSellerViewModel
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.ugc.util.preference.ProductTagPreference
import com.tokopedia.content.product.picker.ugc.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.product.picker.ugc.view.viewmodel.ProductTagViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
fun productSetupViewModel(
    creationId: String = "123",
    maxProduct: Int = 30,
    productSectionList: List<ProductTagSectionUiModel> = emptyList(),
    handle: SavedStateHandle = SavedStateHandle(),
    isEligibleForPin: Boolean = false,
    repo: ContentProductPickerSellerRepository = mockk(relaxed = true),
    commonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    dispatchers: CoroutineDispatchers = CoroutineDispatchersProvider,
    isNumerationShown: Boolean = true,
    fetchCommissionProduct: Boolean = false,
    selectedAccount: ContentAccountUiModel = ContentAccountUiModel.Empty
): ContentProductPickerSellerViewModel {
    return ContentProductPickerSellerViewModel(
        creationId = creationId,
        maxProduct = maxProduct,
        productSectionList = productSectionList,
        savedStateHandle = handle,
        isEligibleForPin = isEligibleForPin,
        repo = repo,
        commonRepo = commonRepo,
        userSession = userSession,
        dispatchers = dispatchers,
        isNumerationShown = isNumerationShown,
        fetchCommissionProduct = fetchCommissionProduct,
        selectedAccount = selectedAccount
    )
}

fun productUGCViewModel(
    productTagSourceRaw: String = "",
    shopBadge: String = "",
    authorId: String = "",
    authorType: String = "",
    initialSelectedProduct: List<SelectedProductUiModel> = emptyList(),
    productTagConfig: ContentProductTagConfig = ContentProductTagConfig(
        isMultipleSelectionProduct = true,
        isFullPageAutocomplete = false,
        maxSelectedProduct = 30,
        backButton = ContentProductTagConfig.BackButton.Close,
        isShowActionBarDivider = false,
        appLinkAfterAutocomplete = ""
    ),
    repo: ProductTagRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    sharedPref: ProductTagPreference = mockk(relaxed = true)
): ProductTagViewModel {
    return ProductTagViewModel(
        productTagSourceRaw = productTagSourceRaw,
        shopBadge = shopBadge,
        authorId = authorId,
        authorType = authorType,
        initialSelectedProduct = initialSelectedProduct,
        productTagConfig = productTagConfig,
        repo = repo,
        userSession = userSession,
        sharedPref = sharedPref
    )
}
