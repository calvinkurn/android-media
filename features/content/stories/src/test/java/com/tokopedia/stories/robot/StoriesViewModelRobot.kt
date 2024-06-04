package com.tokopedia.stories.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.utils.StoriesPreference
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.StoriesUiState
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

internal class StoriesViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    args: StoriesArgsModel = StoriesArgsModel(),
    userSession: UserSessionInterface = mockk(relaxed = true),
    repository: StoriesRepository = mockk(relaxed = true),
    sharedPref: StoriesPreference = mockk(relaxed = true)
) : Closeable {

    private val viewModel = StoriesViewModel(
        args = args,
        repository = repository,
        userSession = userSession,
        sharedPref = sharedPref
    )

    fun getViewModel() = viewModel

    fun recordState(fn: suspend StoriesViewModelRobot.() -> Unit): StoriesUiState {
        return recordStateAsList(fn).last()
    }

    fun recordStateAsList(fn: suspend StoriesViewModelRobot.() -> Unit): List<StoriesUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val storiesStateList = mutableListOf<StoriesUiState>()
        scope.launch {
            viewModel.storiesState.collect {
                storiesStateList.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return storiesStateList
    }

    fun recordEvent(fn: suspend StoriesViewModelRobot.() -> Unit): List<StoriesUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val storiesEvents = mutableListOf<StoriesUiEvent>()
        scope.launch {
            viewModel.storiesEvent.collect {
                storiesEvents.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return storiesEvents
    }

    fun recordStateAndEvents(fn: suspend StoriesViewModelRobot.() -> Unit): Pair<StoriesUiState, List<StoriesUiEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val storiesEvents = mutableListOf<StoriesUiEvent>()
        val storiesStateList = mutableListOf<StoriesUiState>()
        scope.launch {
            viewModel.storiesState.collect {
                storiesStateList.add(it)
            }
        }
        scope.launch {
            viewModel.storiesEvent.collect {
                storiesEvents.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return storiesStateList.last() to storiesEvents
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    fun entryPointTestCase(selectedGroup: Int = 0) {
        viewModel.submitAction(StoriesUiAction.SetInitialData)
        viewModel.submitAction(StoriesUiAction.SelectGroup(selectedGroup, false))
        viewModel.submitAction(StoriesUiAction.SetMainData(selectedGroup))
    }

    fun initialDataTestCase() {
        viewModel.submitAction(StoriesUiAction.SetInitialData)
    }

    fun setTrackActivity(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.ContentIsLoaded)
    }

    fun mainDataTestCase(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
    }

    fun tapNextDetailToNextDetail(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.NextDetail)
    }

    fun tapNextDetailToNextGroup(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.NextDetail)
        viewModel.submitAction(StoriesUiAction.SelectGroup(selectedGroup.plus(1), true))
        viewModel.submitAction(StoriesUiAction.SetMainData(selectedGroup.plus(1)))
        viewModel.submitAction(StoriesUiAction.PageIsSelected)
        viewModel.submitAction(StoriesUiAction.ContentIsLoaded)
    }

    fun tapNextDetailToCloseRoom(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.NextDetail)
    }

    fun tapPreviousDetailToPrevDetail(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.PreviousDetail)
    }

    fun tapPrevDetailToPrevGroup(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.PreviousDetail)
        viewModel.submitAction(StoriesUiAction.SelectGroup(selectedGroup.minus(1), true))
        viewModel.submitAction(StoriesUiAction.SetMainData(selectedGroup.minus(1)))
        viewModel.submitAction(StoriesUiAction.ContentIsLoaded)
        viewModel.submitAction(StoriesUiAction.PageIsSelected)
    }

    fun tapPrevDetailToResetTimer(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.PreviousDetail)
    }

    fun tapPauseStories(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.PauseStories)
    }

    fun tapResumeStories(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.PageIsSelected)
        viewModel.submitAction(StoriesUiAction.ContentIsLoaded)
        viewModel.submitAction(StoriesUiAction.ResumeStories())
    }

    fun tapResumeStoriesButContentNotLoaded(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.PageIsSelected)
        viewModel.submitAction(StoriesUiAction.ResumeStories())
    }

    fun tapResumeStoriesButPageIsNotSelected(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.ContentIsLoaded)
        viewModel.submitAction(StoriesUiAction.ResumeStories())
    }

    fun collectImpressionGroup(selectedGroup: Int, data: StoriesGroupHeader) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.CollectImpressedGroup(data))
    }

    fun collectImpressionGroupDuplicate(selectedGroup: Int, data: StoriesGroupHeader) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.CollectImpressedGroup(data))
        viewModel.submitAction(StoriesUiAction.CollectImpressedGroup(data.copy(groupId = "groupId 123")))
        viewModel.submitAction(StoriesUiAction.CollectImpressedGroup(data))
    }

    fun deleteStories(selectedGroup: Int = 0) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.DeleteStory)
    }

    fun deleteStoriesUntilEmpty(selectedGroup: Int = 0) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.ShowDeleteDialog)
        viewModel.submitAction(StoriesUiAction.DeleteStory)
    }

    fun openKebabBottomSheet() {
        viewModel.submitAction(StoriesUiAction.OpenKebabMenu)
    }

    fun openReport() {
        viewModel.submitAction(StoriesUiAction.OpenReport)
    }

    fun resetReportState() {
        viewModel.submitAction(StoriesUiAction.ResetReportState)
    }

    fun selectReason(reason: PlayUserReportReasoningUiModel.Reasoning) {
        viewModel.submitAction(StoriesUiAction.SelectReportReason(reason))
    }

    fun updateDuration(duration: Int) {
        viewModel.submitAction(StoriesUiAction.UpdateStoryDuration(duration))
    }

    fun buffering() {
        viewModel.submitAction(StoriesUiAction.VideoBuffering)
    }

    fun openProductBottomSheet() {
        entryPointTestCase(0)
        viewModel.submitAction(StoriesUiAction.OpenProduct)
    }

    fun openShareBottomSheet() {
        viewModel.submitAction(StoriesUiAction.TapSharing)
    }

    fun openVariantBottomSheet(product: ContentTaggedProductUiModel) {
        viewModel.submitAction(StoriesUiAction.ShowVariantSheet(product))
    }

    fun closeBottomSheet(type: BottomSheetType) {
        viewModel.submitAction(StoriesUiAction.DismissSheet(type))
    }

    fun hasSeenTimestampCoachMark() {
        viewModel.submitAction(StoriesUiAction.HasSeenDurationCoachMark)
    }

    fun testGetProducts() {
        viewModel.submitAction(StoriesUiAction.FetchProduct)
    }

    fun testProductAction(action: StoriesProductAction, product: ContentTaggedProductUiModel) {
        viewModel.submitAction(StoriesUiAction.ProductAction(action, product))
    }

    fun testNav(appLink: String) {
        viewModel.submitAction(StoriesUiAction.Navigate(appLink))
    }

    fun forceResumeStories(selectedGroup: Int) {
        entryPointTestCase(selectedGroup)
        viewModel.submitAction(StoriesUiAction.ResumeStories(forceResume = true))
    }

    fun testShowVariantSheet(product: ContentTaggedProductUiModel) {
        viewModel.submitAction(StoriesUiAction.ShowVariantSheet(product))
    }

    private fun <T> getPrivateField(name: String): T {
        val field = viewModel.javaClass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(viewModel) as T
    }

    override fun close() {
        cancelRemainingTasks()
    }

    fun getBottomSheetState(): Map<BottomSheetType, Boolean> =
        getPrivateField<MutableStateFlow<Map<BottomSheetType, Boolean>>>("_bottomSheetStatusState").value
}
