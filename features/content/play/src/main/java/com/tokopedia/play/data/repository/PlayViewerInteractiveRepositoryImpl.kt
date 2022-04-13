package com.tokopedia.play.data.repository

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.domain.interactive.AnswerQuizUseCase
import com.tokopedia.play.domain.interactive.PostInteractiveTapUseCase
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetLeaderboardSlotResponse
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by jegul on 30/06/21
 */
class PlayViewerInteractiveRepositoryImpl @Inject constructor(
        private val getCurrentInteractiveUseCase: GetCurrentInteractiveUseCase,
        private val postInteractiveTapUseCase: PostInteractiveTapUseCase,
        private val getInteractiveLeaderboardUseCase: GetInteractiveLeaderboardUseCase,
        private val answerQuizUseCase: AnswerQuizUseCase,
        private val mapper: PlayUiModelMapper,
        private val dispatchers: CoroutineDispatchers,
        private val interactiveStorage: PlayInteractiveStorage
) : PlayViewerInteractiveRepository, PlayInteractiveStorage by interactiveStorage {

    override suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel = withContext(dispatchers.io) {
        val response = getCurrentInteractiveUseCase.apply {
            setRequestParams(GetCurrentInteractiveUseCase.createParams(channelId))
        }.executeOnBackground()
        return@withContext mapper.mapInteractive(response.data.interactive)
    }

    override suspend fun postInteractiveTap(channelId: String, interactiveId: String): Boolean = withContext(dispatchers.io) {
        return@withContext try {
            postInteractiveTapUseCase.apply {
                setRequestParams(PostInteractiveTapUseCase.createParams(channelId, interactiveId))
            }.executeOnBackground()
            true
        } catch (e: MessageErrorException) { false }
    }

    override suspend fun getInteractiveLeaderboard(channelId: String): PlayLeaderboardInfoUiModel = withContext(dispatchers.io) {
//        val response = getInteractiveLeaderboardUseCase.execute(channelId)
        val data = """
          {
              "slots": [
                {
                  "title": "Bagi-bagi sembako",
                  "winners":[
                    {
                      "username": "Jokowi",
                      "imageURL": "http://google.com"
                    },
                    {
                      "username": "Emak",
                      "imageURL": "http://google.com"
                    },
                    {
                      "username": "Bapak",
                      "imageURL": "http://google.com"
                    }
                  ],
                  "otherParticipantCountText": "Dari 101 peserta",
                  "otherParticipantCount": 101,
                  "emptyLeaderboardCopyText": "Belum ada yang ikut main di sesi ini. [Giveaway]"
                },
                {
                  "question": "Siapa aku?",
                  "reward": "500 perak",
                  "userChoice": 1,
                  "choices": [
                    {
                      "id": 1,
                      "text": "Aku",
                      "isCorrect" : false
                    }, 
                    {
                      "id": 2,
                      "text": "Kamu",
                      "isCorrect" : true
                    }, 
                    {
                      "id": 3,
                      "text": "Dirinya",
                      "isCorrect" : false
                    }
                  ],
                  "winners":[
                    {
                      "username": "Jokowi",
                      "imageURL": "http://google.com"
                    },
                    {
                      "username": "Emak",
                      "imageURL": "http://google.com"
                    },
                    {
                      "username": "Bapak",
                      "imageURL": "http://google.com"
                    }
                  ],
                  "otherParticipantCountText": "Dari 101 peserta",
                  "otherParticipantCount": 101,
                  "emptyLeaderboardCopyText": "Belum ada yang ikut main di sesi ini.[Quiz]"
                }
              ]
            }
        """.trimIndent()
        val response = try { Gson().fromJson(data, GetLeaderboardSlotResponse::class.java) }catch (e: Exception){ }
        return@withContext mapper.mapInteractiveLeaderboard(response as GetLeaderboardSlotResponse)
    }

    override suspend fun answerQuiz(interactiveId: String, choiceId: String): String = withContext(dispatchers.io) {
        return@withContext answerQuizUseCase.apply {
                setRequestParams(answerQuizUseCase.createParam(interactiveId, choiceId))
        }.executeOnBackground().data.correctAnswerID
    }
}