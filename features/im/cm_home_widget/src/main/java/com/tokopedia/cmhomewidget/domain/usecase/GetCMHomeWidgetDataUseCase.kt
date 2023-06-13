package com.tokopedia.cmhomewidget.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.data.GetCMHomeWidgetDataGqlResponse
import com.tokopedia.cmhomewidget.domain.query.GQL_QUERY_GET_CM_HOME_WIDGET_DATA
import com.tokopedia.cmhomewidget.domain.query.GetCMHomeWidgetGQLQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

@GqlQuery("GetCMHomeWidgetData", GQL_QUERY_GET_CM_HOME_WIDGET_DATA)
class GetCMHomeWidgetDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    @ApplicationContext val context: Context
) : GraphqlUseCase<GetCMHomeWidgetDataGqlResponse>(graphqlRepository) {

    private var previousRefreshTimeMillis = 0L

    fun getCMHomeWidgetData(
        onSuccess: (CMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
        isForceRefresh: Boolean
    ) {
        try {
            if (isForceRefresh || isRefreshNeeded()) {
                this.setTypeClass(GetCMHomeWidgetDataGqlResponse::class.java)
                this.setGraphqlQuery(GetCMHomeWidgetGQLQuery())
                this.setRequestParams(getRequestParams(context))
                this.execute(
                    { result ->
                        if (result.cmHomeWidgetDataResponse.status == STATUS_SUCCESS_CODE) {
                            updatePreviousRefreshTime()
                            onSuccess(result.cmHomeWidgetDataResponse)
                        } else {
                            onError(Throwable())
                        }
                    },
                    { error ->
                        onError(error)
                    }
                )
            }
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(context: Context): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[PARAM] = getNotifierRequestData(context)
        return requestParams
    }

    private fun getNotifierRequestData(context: Context): HashMap<String, Any> {
        val localChooseAddressData = ChooseAddressUtils.getLocalizingAddressData(context)
        val notifierRequestData = HashMap<String, Any>()
        notifierRequestData[DISTRICT_ID] = localChooseAddressData.district_id.toIntOrZero()
        notifierRequestData[CITY_ID] = localChooseAddressData.city_id.toIntOrZero()
        notifierRequestData[LONGITUDE] = localChooseAddressData.long
        notifierRequestData[LATITUDE] = localChooseAddressData.lat
        notifierRequestData[WAREHOUSE_ID] = localChooseAddressData.warehouse_id
        return notifierRequestData
    }

    private fun isRefreshNeeded(): Boolean {
        return (System.currentTimeMillis() - previousRefreshTimeMillis) > REFRESH_INTERVAL_MILLIS
    }

    private fun updatePreviousRefreshTime() {
        previousRefreshTimeMillis = System.currentTimeMillis()
    }

    companion object {
        const val PARAM = "param"
        const val DISTRICT_ID = "user_district_id"
        const val CITY_ID = "user_city_id"
        const val LONGITUDE = "user_longitude"
        const val LATITUDE = "user_latitude"
        const val WAREHOUSE_ID = "warehouse_ids"
        const val REFRESH_INTERVAL_MILLIS = 60000L // 60 sec
        const val STATUS_SUCCESS_CODE = 200
    }
}
