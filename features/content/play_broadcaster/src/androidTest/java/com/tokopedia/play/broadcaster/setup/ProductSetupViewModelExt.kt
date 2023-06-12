package com.tokopedia.play.broadcaster.setup

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.util.preference.ProductTagPreference
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
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
    repo: PlayBroadcastRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    dispatchers: CoroutineDispatchers = CoroutineDispatchersProvider,
    source: PlayBroPageSource = PlayBroPageSource.Live,
    fetchCommissionProduct: Boolean = false,
): PlayBroProductSetupViewModel {
    return PlayBroProductSetupViewModel(
        creationId = creationId,
        maxProduct = maxProduct,
        productSectionList = productSectionList,
        savedStateHandle = handle,
        isEligibleForPin = isEligibleForPin,
        repo = repo,
        userSession = userSession,
        dispatchers = dispatchers,
        source = source,
        fetchCommissionProduct = fetchCommissionProduct,
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
        appLinkAfterAutocomplete = "",
    ),
    repo: ProductTagRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    sharedPref: ProductTagPreference = mockk(relaxed = true),
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
        sharedPref = sharedPref,
    )
}
