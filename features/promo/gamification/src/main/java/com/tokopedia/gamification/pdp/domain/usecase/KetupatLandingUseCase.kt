package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase.Companion.GET_GAMI_SCRATCH_CARD_LANDING_PAGE
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetGamiScratchCardLandingPage", GET_GAMI_SCRATCH_CARD_LANDING_PAGE)
class KetupatLandingUseCase @Inject constructor(
    private val repository: GamificationRepository
) {

    private fun createRequestParams(slug: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[SLUG] = slug
        return request
    }

    suspend fun getScratchCardLandingPage(slug: String): KetupatLandingPageData {
        return repository.getGQLData(
            GetGamiScratchCardLandingPage.GQL_QUERY,
            KetupatLandingPageData::class.java,
            createRequestParams(slug)
        )
    }


    companion object {
        private const val SLUG = "slug"
        const val GET_GAMI_SCRATCH_CARD_LANDING_PAGE = "query gamiGetScratchCardLandingPage(input: {${'$'}slug: String!}) {\n" +
            "  gamiGetScratchCardLandingPage(input: {slug: ${"$"}slug}) {" +
            "    resultStatus {" +
            "      code" +
            "      reason" +
            "      message" +
            "    }" +
            "    scratchCard {" +
            "      id" +
            "      name" +
            "      description" +
            "      slug" +
            "      startTime" +
            "      endTime" +
            "    }" +
            "    appBar {" +
            "      title" +
            "      isShownShareIcon" +
            "      shared {" +
            "        ogTitle" +
            "        ogDescription" +
            "        ogImageURL" +
            "        message" +
            "        page" +
            "        identifier" +
            "      }" +
            "    }" +
            "    sections {" +
            "      id" +
            "      title" +
            "      type" +
            "      assets {" +
            "        key" +
            "        value" +
            "      }" +
            "      text {" +
            "        key" +
            "        value" +
            "      }" +
            "      cta {" +
            "        text" +
            "        url" +
            "        appLink" +
            "        type" +
            "        backgroundColor" +
            "        color" +
            "        iconURL" +
            "        imageURL" +
            "      }" +
            "      jsonParameter" +
            "    }" +
            "  }" +
            "}"
    }
}
