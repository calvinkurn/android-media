package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.data.createHomeLayoutListForQuestOnly
import com.tokopedia.tokopedianow.data.createQuestWidgetList
import com.tokopedia.tokopedianow.data.createQuestWidgetListEmpty
import com.tokopedia.tokopedianow.home.domain.model.Progress
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.domain.model.QuestUser
import com.tokopedia.tokopedianow.home.domain.model.Task
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestFinishedWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestReloadWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TokoNowHomeViewModelTestQuestWidget: TokoNowHomeViewModelTestFixture() {

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
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly(
            subtitle = finishedWidgetTitle,
            widgetParam = finishedWidgetContentDescription
        ))
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
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly(
            subtitle = finishedWidgetTitle,
            widgetParam = finishedWidgetContentDescription
        ))
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(
            code = successCode,
            questWidgetList = questWidgetList
        ))

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
                )
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
                )
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
                )
            )
        )

        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly(
            subtitle = finishedWidgetTitle,
            widgetParam = finishedWidgetContentDescription
        ))
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(code))

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        code = "200"
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(
            code = code,
            questWidgetList = questWidgetList
        ))

        viewModel.refreshQuestWidget()

        val expectedQuestWidgetItem = HomeQuestWidgetUiModel(
            id = "55678",
            title = "Main Quest",
            questList = listOf(
                HomeQuestCardItemUiModel(
                    id = "1233",
                    title = "Beli 2 beras 50kg dapat cashback 50rb",
                    description = "Expired tanggal 15/02/2025",
                    isLockedShown = false,
                    currentProgress = 1f,
                    totalProgress = 1f
                ),
                HomeQuestCardItemUiModel(
                    id = "1234",
                    title = "Beli 3 beras 50kg dapat cashback 50rb",
                    description = "Expired tanggal 16/02/2025",
                    isLockedShown = false,
                    currentProgress = 0f,
                    totalProgress = 1f
                ),
                HomeQuestCardItemUiModel(
                    id = "1235",
                    title = "Beli 4 beras 50kg dapat cashback 50rb",
                    description = "Expired tanggal 17/02/2025",
                    isLockedShown = true,
                    currentProgress = 0f,
                    totalProgress = 1f
                )
            ),
            currentProgressPosition = 1
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
}
