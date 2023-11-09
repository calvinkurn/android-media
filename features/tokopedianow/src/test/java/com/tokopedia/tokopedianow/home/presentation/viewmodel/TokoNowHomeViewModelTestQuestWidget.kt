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
    fun `when getting data quest list should run successfully with empty list result`() {
        // set mock data
        val successCode = "200"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetListEmpty(code = successCode))

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetItem(null)
    }

    @Test
    fun `given error code when get quest list should add reload quest widget item to visitable list`() {
        val finishedWidgetTitle = "\uD83C\uDF89 Hebat, semua misi berhasil selesai!"
        val finishedWidgetContentDescription = "120rb"

        // set mock data
        val errorCode = "12231"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly(
            subtitle = finishedWidgetTitle,
            widgetParam = finishedWidgetContentDescription
        ))
        onGetQuestWidgetList_thenReturn(createQuestWidgetListEmpty(code = errorCode))

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // prepare model for expectedResult
        val expectedQuestWidgetItem = HomeQuestReloadWidgetUiModel(
            id = "55678",
            mainTitle = "Main Quest",
            finishedWidgetTitle = finishedWidgetTitle,
            finishedWidgetContentDescription = finishedWidgetContentDescription
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyReloadQuestWidgetItem(expectedQuestWidgetItem)
    }

    @Test
    fun `given all quest finished when get quest list should add finished widget item to visitable list`() {
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

        // set mock data
        val successCode = "200"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly(
            subtitle = finishedWidgetTitle,
            widgetParam = finishedWidgetContentDescription
        ))
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(
            code = successCode,
            questWidgetList = questWidgetList
        ))

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // prepare model for expectedResult
        val expectedQuestWidgetItem = HomeQuestFinishedWidgetUiModel(
            id = "55678",
            title = finishedWidgetTitle,
            contentDescription = finishedWidgetContentDescription
        )

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyFinishedQuestWidgetItem(expectedQuestWidgetItem)
    }

    @Test
    fun `when getting data quest list should throw an exception and remove the widget`() {
        // set mock data
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(Exception())

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetItem(null)
    }

    @Test
    fun `given error code when refreshQuestList should add quest widget item to visitable list`() {
        // set the code here to make it error and need to refresh
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

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // set the code to make it success and get the list
        code = "200"
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(
            code = code,
            questWidgetList = questWidgetList
        ))

        // put home sequence ui model as param and re-fetch quest list
        viewModel.refreshQuestWidget()

        // prepare model for expectedResult
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

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetItem(expectedQuestWidgetItem)
    }

    @Test
    fun `when getting data quest list should run, fetch quest list again, throw an exception and remove the widget`() {
        // set mock data
        val successCode = "200"
        onGetQuestWidgetAbTest_thenReturn(EXPERIMENT_VARIANT)
        onGetHomeLayoutData_thenReturn(createHomeLayoutListForQuestOnly())
        onGetQuestWidgetList_thenReturn(createQuestWidgetList(successCode))

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // set quest widget list to throw an exception
        onGetQuestWidgetList_thenReturn(Exception())

        // put home sequence ui model as param and re-fetch quest list
        viewModel.refreshQuestList()

        // verify use case called and response
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetQuestWidgetListUseCaseCalled()
        verifyQuestWidgetItem(null)
    }
}
