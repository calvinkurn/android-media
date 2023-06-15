package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.domain.usecase.param.InboxOptionParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

const val CHIP_GET_INBOX_DETAIL_QUERY = """query chipGetInboxDetailQuery(${'$'}caseID: String!) {
    chipGetInboxDetail(caseID: ${'$'}caseID) {
        data {
            isSuccess
            tickets {
                id
                showRating
                badCsatReasonList{
                    id
                    message
                    messageEn
                }
                comments{
                    id
                    create_time
                    message
                    message_plaintext
                    created_by{
                        name
                        picture
                        role
                    }
                    attachment{
                        url
                        thumbnail
                    }
                    note
                    rating
                }
                number
                subject
                invoice
                status
                readStatus
                message
                createdBy{
                    name
                    picture
                    role
                }
                createTime
                needAttachment
                attachment{
                    url
                    thumbnail
                }
                allowClose
            }
        }
        messageError
    }
}"""

class InboxDetailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<InboxOptionParam, ChipInboxDetails>(dispatchers.io) {

    fun createRequestParams(id: String?): InboxOptionParam {
        return InboxOptionParam(caseID = id.orEmpty())
    }

    override fun graphqlQuery(): String {
        return CHIP_GET_INBOX_DETAIL_QUERY
    }

    override suspend fun execute(params: InboxOptionParam): ChipInboxDetails {
        return repository.request(graphqlQuery(), params)
    }
}
