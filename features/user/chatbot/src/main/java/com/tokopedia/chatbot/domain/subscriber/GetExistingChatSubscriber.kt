package com.tokopedia.chatbot.domain.subscriber

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.util.handleError
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_CSAT_OPTIONS
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_OPTION_LIST
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
            val inputList = getChatRatingData(mappedPojo)
            if (!inputList.list.isNullOrEmpty()) {
                getChatRatingList(inputList, mappedPojo)
            } else {
                onSuccessGetChat(mappedPojo)
            }
        }
    }

    private fun getChatRatingList(inputList: ChipGetChatRatingListInput, mappedPojo: ChatroomViewModel) {
        val input = inputList
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
                    when {
                        msg is HelpFullQuestionsViewModel && rate.attachmentType == TYPE_OPTION_LIST.toIntOrZero()
                        -> (msg.helpfulQuestion?.caseChatId == rate.caseChatID)
                        msg is CsatOptionsViewModel && rate.attachmentType == TYPE_CSAT_OPTIONS.toIntOrZero()
                        -> (msg.csat?.caseChatId == rate.caseChatID)
                        else -> false
                    }
                }
                rateListMsgs.forEach {
                    if (it is HelpFullQuestionsViewModel) {
                        it.isSubmited = rate.isSubmitted ?: true
                    }else if (it is CsatOptionsViewModel){
                        it.isSubmited = rate.isSubmitted ?: true
                    }
                }

            }
        } else if (!ratings?.messageError.isNullOrEmpty()) {
            onGetChatRatingListMessageError(ratings?.messageError?.get(0) ?: "")
        }

    }

    private val onGetChatRatingListError: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    private fun getChatRatingData(mappedPojo: ChatroomViewModel): ChipGetChatRatingListInput {
        val input = ChipGetChatRatingListInput()
        for (message in mappedPojo.listChat) {
            if (message is HelpFullQuestionsViewModel) {
                input.list.add(ChipGetChatRatingListInput.ChatRating(TYPE_OPTION_LIST.toIntOrZero(),message.helpfulQuestion?.caseChatId ?: "" ))
            }else if (message is CsatOptionsViewModel) {
                input.list.add(ChipGetChatRatingListInput.ChatRating(TYPE_CSAT_OPTIONS.toIntOrZero(),message.csat?.caseChatId ?: "" ))
            }
        }
        return input
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorGetChat(e)
    }

}