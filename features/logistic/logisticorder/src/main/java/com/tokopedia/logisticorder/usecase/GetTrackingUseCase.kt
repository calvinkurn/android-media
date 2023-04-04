package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticorder.domain.response.GetLogisticTrackingResponse
import com.tokopedia.logisticorder.usecase.params.GetTrackingParam
import javax.inject.Inject

class GetTrackingUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetTrackingParam, GetLogisticTrackingResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return QUERY_GET_TRACKING
    }

    override suspend fun execute(getTrackingParam: GetTrackingParam): GetLogisticTrackingResponse {
        return gql.request(graphqlQuery(), getTrackingParam)
    }

    fun getParam(
        shopId: String,
        from: String
    ): GetTrackingParam {
        return GetTrackingParam(
            GetTrackingParam.TrackingParam(
                shopId = shopId,
                from = from
            )
        )
    }

    companion object {

        private const val QUERY_GET_TRACKING = """
        query logistic_tracking (${'$'}input: MpLogisticTrackingInputParams!) {
          logistic_tracking(input: ${'$'}input) {
            message_error
            status
            data {
              track_order {
                detail {
            	  shipper_city
        		  shipper_name
                  receiver_city
                  receiver_name
                  send_date_time
                  send_date
                  send_time
                  service_code
                  tracking_url
                  eta {
                    triggered_by
                    eta_min
                    eta_max
                    event_time
                    is_updated
                    user_info
                    user_updated_info
                    eta_histories { 
                        triggered_by
                        eta_min
                        eta_max
                        event_time
                    }
                  }
                }
                track_history {
                  date_time
                  date
                  status
                  city
                  time
                  proof {
                    image_id
                    copy_writing_disclaimer
                  }
                  partner_name
                }
                change
                status
                order_status
                no_history
                receiver_name
                shipping_ref_num
                invalid
              }
              page {
                additional_info {
                  title
                  notes
                  url_detail
                  url_text
                }
              }
              last_driver {
                photo
                name
                phone
                license_number
                is_changed
              }
              tipping {
                status
                status_title
                status_subtitle
                last_driver {
                  photo
                  name
                  phone
                  license_number
                  is_changed
                }
              }
            }
          }
        }

    """
    }
}
