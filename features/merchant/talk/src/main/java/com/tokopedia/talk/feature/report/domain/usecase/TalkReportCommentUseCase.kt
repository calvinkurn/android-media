package com.tokopedia.talk.feature.report.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class TalkReportCommentUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private val query by lazy {
            val talkID = "\$talk_id"
            val reason = "\$reason"
            val reportType = "\$report_type"
            """
                query talkReportTalk($talkID: Int, $reason: String, $reportType: Int) {
                    talkReportTalk(talk_id: $talkID, reason: $reason, report_type: $reportType) {
                        status
                        messageError
                        data {
                            isSuccess
                        }
                        messageErrorOriginal
                }
            """.trimIndent()
        }
    }

    init {

    }


}