package com.tokopedia.play.broadcaster.robot

import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.util.TestDoubleModelBuilder
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by jegul on 20/05/21
 */
class PlayEtalasePickerViewModelRobot(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineTestDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
        getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        userSession: UserSessionInterface,
        playBroadcastMapper: PlayBroadcastMapper
) : Robot {

    private val viewModel: PlayEtalasePickerViewModel = PlayEtalasePickerViewModel(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore,
            getSelfEtalaseListUseCase = getSelfEtalaseListUseCase,
            getProductsInEtalaseUseCase = getProductsInEtalaseUseCase,
            userSession = userSession,
            playBroadcastMapper = playBroadcastMapper,
    )

    fun getCurrentEtalaseResult() = viewModel.observableEtalase.getOrAwaitValue()

    fun loadProductPreview(etalaseId: String) {
        viewModel.loadEtalaseProductPreview(etalaseId)
    }

    fun selectProduct(productId: Long) {
        viewModel.selectProduct(productId, true)
    }

    fun deselectProduct(productId: Long) {
        viewModel.selectProduct(productId, false)
    }

    fun getSelectedProducts() = viewModel.selectedProductList

    fun setMaxProductDesc(desc: String) {
        hydraConfigStore.setMaxProductDesc(desc)
    }

    fun getMaxProductDesc() = viewModel.maxProductDesc
}

fun givenPlayEtalasePickerViewModel(
        hydraConfigStore: HydraConfigStore = TestDoubleModelBuilder().buildHydraConfigStore(),
        dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
        setupDataStore: PlayBroadcastSetupDataStore = TestDoubleModelBuilder().buildSetupDataStore(),
        getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase = mockk(relaxed = true),
        getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true),
        userSession: UserSessionInterface = mockk(relaxed = true),
        playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer()),
        fn: PlayEtalasePickerViewModelRobot.() -> Unit = {},
) : PlayEtalasePickerViewModelRobot {
    return PlayEtalasePickerViewModelRobot(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore,
            getSelfEtalaseListUseCase = getSelfEtalaseListUseCase,
            getProductsInEtalaseUseCase = getProductsInEtalaseUseCase,
            userSession = userSession,
            playBroadcastMapper = playBroadcastMapper,
    ).apply(fn)
}