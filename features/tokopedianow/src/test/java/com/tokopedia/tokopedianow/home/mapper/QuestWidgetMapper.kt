package com.tokopedia.tokopedianow.home.mapper

import com.tokopedia.tokopedianow.home.domain.model.GetQuestListResponse
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.Progress
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.domain.model.QuestListResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestUser
import com.tokopedia.tokopedianow.home.domain.model.ResultStatus
import com.tokopedia.tokopedianow.home.domain.model.StartQuestResponse
import com.tokopedia.tokopedianow.home.domain.model.Task

object QuestWidgetMapper {

    fun createHomeLayoutResponse(): List<HomeLayoutResponse> {
        return listOf(
            HomeLayoutResponse(
                id = "34923",
                layout = "tokonow_main_quest",
                header = Header(
                    name = "Quest Widget",
                    serverTimeUnix = 0
                )
            )
        )
    }

    fun createStartQuestResponse(code: String): StartQuestResponse {
        return StartQuestResponse(
            StartQuestResponse.QuestStartResponse(
                StartQuestResponse.ResultStatusResponse(
                    code = code,
                    message = emptyList(),
                    status = ""
                )
            )
        )
    }

    fun createQuestListResponse(): GetQuestListResponse {
        return GetQuestListResponse(
            questWidgetList = QuestListResponse(
                questWidgetList = listOf(
                    QuestList(
                        id = "155",
                        title = "Quest Title",
                        description = "Quest Description",
                        config = createConfigJson(
                            title = "TokopediaNOW! Quest",
                            description = "4.4 Quest Campaign"
                        ),
                        questUser = QuestUser("5", "Claimed"),
                        task = listOf(Task("7", "Quest Task", Progress(0f, 100f))),
                        sequenceQuestIDs = listOf(0)
                    ),
                    QuestList(
                        id = "255",
                        title = "Quest Title",
                        description = "Quest Description",
                        config = createConfigJson(
                            title = "TokopediaNOW! Quest 2",
                            description = "12.12 Quest Campaign"
                        ),
                        questUser = QuestUser("5", "Idle"),
                        task = listOf(Task("7", "Quest Task", Progress(0f, 100f))),
                        sequenceQuestIDs = listOf(0)
                    )
                ),
                resultStatus = ResultStatus(
                    code = "200",
                    ""
                )
            )
        )
    }

    private fun createConfigJson(title: String, description: String): String {
        return """
            {
               banner_title:"$title",
               banner_description:"$description"
            }
        """.trimIndent()
    }
}
