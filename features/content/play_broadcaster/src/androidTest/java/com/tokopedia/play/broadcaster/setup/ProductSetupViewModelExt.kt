package com.tokopedia.play.broadcaster.setup

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.util.preference.ProductTagPreference
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
fun productSetupViewModel(
    productSectionList: List<ProductTagSectionUiModel> = emptyList(),
    handle: SavedStateHandle = SavedStateHandle(),
    repo: PlayBroadcastRepository = mockk(relaxed = true),
    configStore: HydraConfigStore = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    dispatchers: CoroutineDispatchers = CoroutineDispatchersProvider,
    isEligibleForPin: Boolean = false,
): PlayBroProductSetupViewModel {
    return PlayBroProductSetupViewModel(
        productSectionList = productSectionList,
        savedStateHandle = handle,
        repo = repo,
        configStore = configStore,
        userSession = userSession,
        dispatchers = dispatchers,
        isEligibleForPin = isEligibleForPin,
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