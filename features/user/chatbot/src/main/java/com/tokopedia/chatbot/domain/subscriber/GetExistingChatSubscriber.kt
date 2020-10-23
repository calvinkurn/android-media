package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * @author by nisie on 12/12/18.
 */
class GetExistingChatSubscriber(val onErrorGetChat: (Throwable) -> Unit,
                                val onSuccessGetChat: (ChatroomViewModel) -> Unit,
                                private val chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase,
                                private val onGetChatRatingListMessageError: (String) -> Unit,
                                val mapper: ChatbotGetExistingChatMapper = ChatbotGetExistingChatMapper())
    : Subscriber<GraphqlResponse>() {
    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, GetExistingChatPojo::class.java,
                routingOnNext(graphqlResponse), onErrorGetChat)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<GetExistingChatPojo>(GetExistingChatPojo::class.java)
            val mappedPojo = mapper.map(pojo)
            val ids = getChatCaseIds(mappedPojo)
            if (ids.isNotEmpty()) {
                getChatRatingList(ids, mappedPojo)
            } else {
                onSuccessGetChat(mappedPojo)
            }
        }
    }

    private fun getChatRatingList(ids: String, mappedPojo: ChatroomViewModel) {
        val input = ChipGetChatRatingListInput().apply { caseChatIDs = ids }
        chipGetChatRatingListUseCase.execute(chipGetChatRatingListUseCase.generateParam(input),
                ChipGetChatRatingListSubscriber(onGetChatRatingListError, onChatRatingListSuccess(mappedPojo)))
    }

    private fun onChatRatingListSuccess(mappedPojo: ChatroomViewModel): (ChipGetChatRatingListResponse.ChipGetChatRatingList?) -> Unit = { ratings ->
        updateMappedPojo(mappedPojo, ratings)
        onSuccessGetChat(mappedPojo)
    }

    private fun updateMappedPojo(mappedPojo: ChatroomViewModel, ratings: ChipGetChatRatingListResponse.ChipGetChatRatingList?) {
        if (ratings?.ratingListData?.isSuccess == 1) {
            for (rate in ratings.ratingListData.list ?: listOf()) {
                val rateListMsgs = mappedPojo.listChat.filter { msg ->
                    if (msg is HelpFullQuestionsViewModel) {
                        (msg.helpfulQuestion?.caseChatId == rate?.caseChatID)
                    } else {
                        false
                    }
                }
                rateListMsgs.forEach {
                    if (it is HelpFullQuestionsViewModel) {
                        it.isSubmited = rate?.isSubmitted ?: false
                    }
                }

            }
        } else if (!ratings?.messageError.isNullOrEmpty()) {
            print(ratings?.messageError?.get(0))
            onGetChatRatingListMessageError(ratings?.messageError?.get(0) ?: "")
        }

    }

    private val onGetChatRatingListError: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    private fun getChatCaseIds(mappedPojo: ChatroomViewModel): String {
        var chatCaseIds = ""
        for (message in mappedPojo.listChat) {
            if (message is HelpFullQuestionsViewModel) {
                if (chatCaseIds.isEmpty()) {
                    chatCaseIds = message.helpfulQuestion?.caseChatId ?: ""
                } else {
                    chatCaseIds = chatCaseIds + "," + message.helpfulQuestion?.caseChatId
                }
            }
        }
        return chatCaseIds
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorGetChat(e)
    }

}