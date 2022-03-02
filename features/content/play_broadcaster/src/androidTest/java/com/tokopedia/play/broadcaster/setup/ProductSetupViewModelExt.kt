package com.tokopedia.play.broadcaster.setup

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
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
): PlayBroProductSetupViewModel {
    return PlayBroProductSetupViewModel(
        productSectionList = productSectionList,
        savedStateHandle = handle,
        repo = repo,
        configStore = configStore,
        userSession = userSession,
        dispatchers = dispatchers
    )
}