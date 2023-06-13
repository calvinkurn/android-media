package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class GetFirstTimeInboxReputationUseCase @Inject constructor(
    useCase: GraphqlUseCase,
    mapper: InboxReputationMapper
) : GetInboxReputationUseCase(useCase, mapper) {
    companion object {
        fun getFirstTimeParam(tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE)
            params.putInt(PARAM_PAGE, DEFAULT_FIRST_PAGE)
            params.putInt(PARAM_ROLE, getRole(tab))
            params.putInt(PARAM_TIME_FILTER, DEFAULT_TIME_FILTER)
            params.putInt(PARAM_STATUS, tab)
            return params
        }
    }
}