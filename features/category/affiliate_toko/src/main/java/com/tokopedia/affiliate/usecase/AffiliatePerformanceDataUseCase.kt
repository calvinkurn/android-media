package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

class AffiliatePerformanceDataUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParamsList(
        dateRangeRequest: String,
        lastID: String,
        pageType: Int
    ): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_DATE_RANGE] = dateRangeRequest
        request[PARAM_LAST_ID] = lastID
        request[PARAM_PAGE_TYPE] = pageType
        return request
    }

    @GqlQuery("GQLAffiliatePerformanceList", GQL_Affiliate_Performance_List)
    suspend fun affiliateItemPerformanceList(
        dateRangeRequest: String,
        lastID: String,
        pageType: Int
    ): AffiliatePerformanceListData {
        return repository.getGQLData(
            GQLAffiliatePerformanceList.GQL_QUERY,
            AffiliatePerformanceListData::class.java,
            createRequestParamsList(dateRangeRequest, lastID, pageType)
        )
    }

    companion object {
        private const val PARAM_DATE_RANGE = "dayRange"
        private const val PARAM_LAST_ID = "lastID"
        private const val PARAM_PAGE_TYPE = "pageType"

        private const val GQL_Affiliate_Performance_List: String =
            """query getAffiliatePerformanceList(${"$"}dayRange: String!,${"$"}lastID: String!, ${"$"}pageType: Int!) {
                  getAffiliatePerformanceList(dayRange: ${"$"}dayRange,lastID: ${"$"}lastID, pageType: ${"$"}pageType) {
                   Data {
                      Status
                      Error {
                        ErrorType
                        Message
                        CtaText
                        CtaLink {
                          DesktopURL
                          MobileURL
                        }
                      }
                      Data {
                        AffiliateID
                        LastID
                        ItemTotalCount
                        ItemTotalCountFmt
                        StartTime
                        EndTime
                        DayRange
                        Items {
                          ItemID
                          ItemType
                          ItemTitle
                          Status
                          DefaultLinkURL
                          BadgeURL
                          LinkGeneratedAt
                          Image {
                            AndroidURL
                            IosURL
                            DesktopURL
                            MobileURL
                          }
                          ImageArray {
                            AndroidURL: DesktopURL
                            IosURL: IosURL
                            DesktopURL: DesktopURL
                            MobileURL: MobileURL
                          }
                          Metrics {
                            MetricType
                            MetricTitle
                            MetricValue
                            MetricValueFmt
                            MetricDifferenceValue
                            MetricDifferenceValueFmt
                            Order
                            Tooltip {
                              Description
                            }
                          }
                          Label {
                            LabelType
                            LabelText
                          }
                          SSAStatus
                          ssaMessage
                          message
                        }
                      }
                    }
                  }
                }"""
    }
}
