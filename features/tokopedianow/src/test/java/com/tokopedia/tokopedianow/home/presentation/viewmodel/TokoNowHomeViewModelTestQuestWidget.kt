package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.data.createHomeLayoutListForQuestOnly
import com.tokopedia.tokopedianow.data.createQuestWidgetList
import com.tokopedia.tokopedianow.data.createQuestWidgetListEmpty
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.Progress
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.domain.model.QuestUser
import com.tokopedia.tokopedianow.home.domain.model.Task
import com.tokopedia.tokopedianow.home.mapper.QuestWidgetMapper.createHomeLayoutResponse
import com.tokopedia.tokopedianow.home.mapper.QuestWidgetMapper.createQuestListResponse
import com.tokopedia.tokopedianow.home.mapper.QuestWidgetMapper.createStartQuestResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestFinishedWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestReloadWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TokoNowHomeViewModelTestQuestWidget : TokoNowHomeViewModelTestFixture() {

    @Test
    fun `given empty quest list response when get quest list should remove widget item from visitable list`() {
        val successCode = "200"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetListEmpty(code = successCode))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetVisitableItem(null)
    }

    @Test
    fun `given error code when get quest list should add reload quest widget item to visitable list`() {
        val errorCode = "12231"
        val finishedWidgetTitle = "\uD83C\uDF89 Hebat, semua misi berhasil selesai!"
        val finishedWidgetContentDescription = "120rb"

        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(
            createHomeLayoutListForQuestOnly(
                subtitle = finishedWidgetTitle,
                widgetParam = finishedWidgetContentDescription
            )
        )
        onGetQuestWidgetList_thenReturn(createQuestWidgetListEmpty(code = errorCode))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val expectedQuestWidgetItem = HomeQuestReloadWidgetUiModel(
            id = "55678",
            mainTitle = "Main Quest",
            finishedWidgetTitle = finishedWidgetTitle,
            finishedWidgetContentDescription = finishedWidgetContentDescription
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyReloadQuestWidgetItem(expectedQuestWidgetItem)
    }

    @Test
    fun `given all quest finished when get quest list should add finished widget item to visitable list`() {
        val successCode = "200"
        val finishedWidgetTitle = "\uD83C\uDF89 Hebat, semua misi berhasil selesai!"
        val finishedWidgetContentDescription = "120rb"

        val questWidgetList = listOf(
            QuestList(
                id = "1233",
                title = "dummy title",
                description = "dummy desc",
                config = "{}",
                questUser = QuestUser(
                    id = "1111",
                    status = "Idle"
                ),
                task = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        progress = Progress(current = 1f, target = 1f)
                    )
                )
            )
        )

        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(
            createHomeLayoutListForQuestOnly(
                subtitle = finishedWidgetTitle,
                widgetParam = finishedWidgetContentDescription
            )
        )
        onGetQuestWidgetList_thenReturn(
            createQuestWidgetList(
                code = successCode,
                questWidgetList = questWidgetList
            )
        )

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        val expectedQuestWidgetItem = HomeQuestFinishedWidgetUiModel(
            id = "55678",
            title = finishedWidgetTitle,
            contentDescription = finishedWidgetContentDescription
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyFinishedQuestWidgetItem(expectedQuestWidgetItem)
    }

    @Test
    fun `given get quest list throws error when get quest list should catch exception and remove the widget`() {
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(Exception())

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetVisitableItem(null)
    }

    @Test
    fun `given error code when refreshQuestWidget should add quest widget item to visitable list`() {
        var code = "12300"
        val finishedWidgetTitle = "\uD83C\uDF89 Hebat, semua misi berhasil selesai!"
        val finishedWidgetContentDescription = "120rb"

        val questWidgetList = listOf(
            QuestList(
                id = "1233",
                title = "",
                description = "",
                config = """{ 
                    banner_title:"Beli 2 beras 50kg dapat cashback 50rb",
                    banner_description:"Expired tanggal 15/02/2025"
                }""",
                questUser = QuestUser(
                    id = "1111",
                    status = "Claimed"
                ),
                task = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        progress = Progress(current = 1f, target = 1f)
                    )
                ),
                sequenceQuestIDs = listOf(1234)
            ),
            QuestList(
                id = "1234",
                title = "",
                description = "",
                config = """{ 
                    banner_title:"Beli 3 beras 50kg dapat cashback 50rb",
                    banner_description:"Expired tanggal 16/02/2025"
                }""",
                questUser = QuestUser(
                    id = "1111",
                    status = "On Progress"
                ),
                task = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        progress = Progress(current = 0f, target = 1f)
                    )
                ),
                sequenceQuestIDs = listOf(1235)
            ),
            QuestList(
                id = "1235",
                title = "",
                description = "",
                config = """{ 
                    banner_title:"Beli 4 beras 50kg dapat cashback 50rb",
                    banner_description:"Expired tanggal 17/02/2025"
                }""",
                questUser = QuestUser(
                    id = "1111",
                    status = "Idle"
                ),
                task = listOf(
                    Task(
                        id = "1",
                        title = "Task 1",
                        progress = Progress(current = 0f, target = 1f)
                    )
                ),
                sequenceQuestIDs = listOf(1235)
            )
        )

        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(
            createHomeLayoutListForQuestOnly(
                subtitle = finishedWidgetTitle,
                widgetParam = finishedWidgetContentDescription
            )
        )
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(code))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        code = "200"
        onGetQuestWidgetList_thenReturn(
            createQuestWidgetList(
                code = code,
                questWidgetList = questWidgetList
            )
        )

        viewModel.refreshQuestWidget()

        val expectedQuestWidgetItem = HomeQuestWidgetUiModel(
            id = "55678",
            title = "Main Quest",
            questList = listOf(
                HomeQuestCardItemUiModel(
                    id = "1233",
                    channelId = "55678",
                    title = "Beli 2 beras 50kg dapat cashback 50rb",
                    description = "Expired tanggal 15/02/2025",
                    isLockedShown = false,
                    showStartBtn = false,
                    isLoading = false,
                    currentProgress = 1f,
                    totalProgress = 1f,
                    isIdle = false
                ),
                HomeQuestCardItemUiModel(
                    id = "1234",
                    channelId = "55678",
                    title = "Beli 3 beras 50kg dapat cashback 50rb",
                    description = "Expired tanggal 16/02/2025",
                    isLockedShown = false,
                    showStartBtn = false,
                    isLoading = false,
                    currentProgress = 0f,
                    totalProgress = 1f,
                    isIdle = false
                ),
                HomeQuestCardItemUiModel(
                    id = "1235",
                    channelId = "55678",
                    title = "Beli 4 beras 50kg dapat cashback 50rb",
                    description = "Expired tanggal 17/02/2025",
                    isLockedShown = true,
                    showStartBtn = false,
                    isLoading = false,
                    currentProgress = 0f,
                    totalProgress = 1f,
                    isIdle = true
                )
            ),
            currentProgressPosition = 1,
            isStarted = true
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetVisitableItem(expectedQuestWidgetItem)
    }

    @Test
    fun `given get quest list throws exception when refreshQuestWidget should catch exception and remove the widget`() {
        val successCode = "200"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(successCode))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetQuestWidgetList_thenReturn(Exception())

        viewModel.refreshQuestWidget()

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetVisitableItem(null)
    }

    @Test
    fun `given get quest list error when refreshQuestWidget should catch exception and remove the widget`() {
        val errorCode = "12231"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(errorCode))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        onGetQuestWidgetList_thenReturn(Exception())

        viewModel.refreshQuestWidget()

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetVisitableItem(null)
    }

    @Test
    fun `given get quest item not in visitable list when refreshQuestWidget should not call get quest list`() {
        viewModel.refreshQuestWidget()

        verifyGetQuestWidgetListUseCaseNotCalled()
    }

    @Test
    fun `given start quest response success when startQuest then should set start quest live data success`() {
        val questId = 155
        val channelId = "34923"
        val abTestValue = "experiment_variant"

        val startQuestResponse = createStartQuestResponse(code = "200")
        val getHomeLayoutResponse = createHomeLayoutResponse()
        val questListResponse = createQuestListResponse()

        onGetQuestWidgetAbTest_thenReturn(abTestValue)
        onGetHomeLayoutData_thenReturn(getHomeLayoutResponse)
        onGetQuestWidgetList_thenReturn(questListResponse)
        onStartQuest_thenReturn(questId, startQuestResponse)

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.startQuest(channelId, questId)

        val homeLayoutListLiveData = HomeLayoutListUiModel(
            items = listOf(
                HomeHeaderUiModel(
                    id = "9",
                    state = HomeLayoutItemState.ERROR
                ),
                HomeQuestWidgetUiModel(
                    id = channelId,
                    title = "Quest Widget",
                    questList = listOf(
                        HomeQuestCardItemUiModel(
                            id = "155",
                            channelId = channelId,
                            title = "TokopediaNOW! Quest",
                            description = "4.4 Quest Campaign",
                            isLockedShown = false,
                            showStartBtn = false,
                            isLoading = false,
                            currentProgress = 0f,
                            totalProgress = 100f,
                            isIdle = false
                        ),
                        HomeQuestCardItemUiModel(
                            id = "255",
                            channelId = channelId,
                            title = "TokopediaNOW! Quest 2",
                            description = "12.12 Quest Campaign",
                            isLockedShown = false,
                            showStartBtn = true,
                            isLoading = false,
                            currentProgress = 0f,
                            totalProgress = 100f,
                            isIdle = true
                        )
                    ),
                    currentProgressPosition = 0,
                    isStarted = true
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(homeLayoutListLiveData))

        viewModel.startQuestResult
            .verifySuccessEquals(Success(startQuestResponse))
    }

    @Test
    fun `given start quest response error when startQuest then should set start quest live data fail`() {
        val questId = 255
        val channelId = "34923"
        val abTestValue = "experiment_variant"
        val error = MessageErrorException()

        val getHomeLayoutResponse = createHomeLayoutResponse()
        val questListResponse = createQuestListResponse()

        onGetQuestWidgetAbTest_thenReturn(abTestValue)
        onGetHomeLayoutData_thenReturn(getHomeLayoutResponse)
        onGetQuestWidgetList_thenReturn(questListResponse)
        onStartQuest_thenReturn(questId, error)

        viewModel.getHomeLayout(LocalCacheModel(), emptyList())
        viewModel.getLayoutComponentData(LocalCacheModel())
        viewModel.startQuest(channelId, questId)

        val homeLayoutListLiveData = HomeLayoutListUiModel(
            items = listOf(
                HomeHeaderUiModel(
                    id = "9",
                    state = HomeLayoutItemState.ERROR
                ),
                HomeQuestWidgetUiModel(
                    id = channelId,
                    title = "Quest Widget",
                    questList = listOf(
                        HomeQuestCardItemUiModel(
                            id = "155",
                            channelId = channelId,
                            title = "TokopediaNOW! Quest",
                            description = "4.4 Quest Campaign",
                            isLockedShown = false,
                            showStartBtn = false,
                            isLoading = false,
                            currentProgress = 0f,
                            totalProgress = 100f,
                            isIdle = false
                        ),
                        HomeQuestCardItemUiModel(
                            id = "255",
                            channelId = channelId,
                            title = "TokopediaNOW! Quest 2",
                            description = "12.12 Quest Campaign",
                            isLockedShown = false,
                            showStartBtn = true,
                            isLoading = false,
                            currentProgress = 0f,
                            totalProgress = 100f,
                            isIdle = true
                        )
                    ),
                    currentProgressPosition = 0,
                    isStarted = true
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(Success(homeLayoutListLiveData))

        viewModel.startQuestResult
            .verifyErrorEquals(Fail(MessageErrorException()))
    }
}
