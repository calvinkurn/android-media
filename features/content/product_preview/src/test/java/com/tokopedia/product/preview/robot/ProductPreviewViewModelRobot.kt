package com.tokopedia.product.preview.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreference
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewUiEvent
import com.tokopedia.content.product.preview.viewmodel.state.ProductPreviewUiState
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

internal class ProductPreviewViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    source: ProductPreviewSourceModel = mockk(relaxed = true),
    repository: ProductPreviewRepository = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    sharedPref: ProductPreviewSharedPreference = mockk(relaxed = true),
) : Closeable {

    private val viewModel = ProductPreviewViewModel(
        productPreviewSource = source,
        repo = repository,
        userSessionInterface = userSession,
        productPrevSharedPref = sharedPref,
    )

    fun recordState(fn: suspend ProductPreviewViewModelRobot.() -> Unit): ProductPreviewUiState {
        return recordStateAsList(fn).last()
    }

    fun recordStateAsList(fn: suspend ProductPreviewViewModelRobot.() -> Unit): List<ProductPreviewUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiState = mutableListOf<ProductPreviewUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiState.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordEvent(fn: suspend ProductPreviewViewModelRobot.() -> Unit): List<ProductPreviewUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ProductPreviewUiEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvent.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return uiEvent
    }

    fun recordStateAndEvents(
        fn: suspend ProductPreviewViewModelRobot.() -> Unit
    ): Pair<ProductPreviewUiState, List<ProductPreviewUiEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ProductPreviewUiEvent>()
        val uiState = mutableListOf<ProductPreviewUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiState.add(it)
            }
        }
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvent.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return uiState.last() to uiEvent
    }

    val _reviewPosition = getPrivateField<MutableStateFlow<Int>>("_reviewPosition")
    val _currentTabPosition = getPrivateField<MutableStateFlow<Int>>("_currentTabPosition")

    private fun <T> getPrivateField(name: String): T {
        val field = viewModel.javaClass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(viewModel) as T
    }

    fun checkInitialSourceTestCase() {
        viewModel.onAction(ProductPreviewAction.CheckInitialSource)
    }

    fun fetchMiniInfoTestCase() {
        viewModel.onAction(ProductPreviewAction.FetchMiniInfo)
    }

    fun initializeProductMainDataTestCase() {
        viewModel.onAction(ProductPreviewAction.InitializeProductMainData)
    }

    fun initializeReviewMainDataTestCase() {
        viewModel.onAction(ProductPreviewAction.InitializeReviewMainData)
    }

    fun productMediaVideoEndedTestCase() {
        initializeProductMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.ProductMediaVideoEnded)
    }

    fun productMediaSelectedTestCase(position: Int) {
        initializeProductMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.ProductMediaSelected(position))
    }

    fun reviewContentSelectedTestCase(position: Int) {
        initializeReviewMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.ReviewContentSelected(position))
    }

    fun reviewContentScrollingState(position: Int, isScrolling: Boolean) {
        initializeReviewMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.ReviewContentScrolling(position, isScrolling))
    }

    fun reviewMediaSelectedTestCase(mediaPosition: Int) {
        initializeReviewMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.ReviewMediaSelected(mediaPosition))
    }

    fun tabSelectedTestCase(position: Int) {
        checkInitialSourceTestCase()
        viewModel.onAction(ProductPreviewAction.TabSelected(position))
    }

    fun productActionAddToChartTestCase(model: BottomNavUiModel) {
        viewModel.onAction(ProductPreviewAction.FetchMiniInfo)
        viewModel.onAction(ProductPreviewAction.ProductAction(model))
    }

    fun navigateAppTestCase(appLink: String) {
        viewModel.onAction(ProductPreviewAction.Navigate(appLink))
    }

    fun submitReportTestCase(model: ReviewReportUiModel) {
        viewModel.onAction(ProductPreviewAction.SubmitReport(model))
    }

    fun clickMenuTestCase(isFromLogin: Boolean) {
        initializeReviewMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.ClickMenu(isFromLogin))
    }

    fun likeFromResultTestCase(isDoubleTap: Boolean) {
        initializeReviewMainDataTestCase()
        viewModel.onAction(ProductPreviewAction.Like(isDoubleTap))
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }

}
