package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.AddShopLocationMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class AddShopLocationUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<AddShopLocationMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<AddShopLocationMutation>(context, AddShopLocationMutation::class.java) {
            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_add_shop_location)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[NAME] = requestParams.getString(NAME, "")
                variables[ADDRESS] = requestParams.getString(ADDRESS, "")
                variables[DISTRICT_ID] = requestParams.getLong(DISTRICT_ID, 0)
                variables[CITY_ID] = requestParams.getLong(CITY_ID, 0)
                variables[STATE_ID] = requestParams.getLong(STATE_ID, 0)
                variables[POSTAL_CODE] = requestParams.getInt(POSTAL_CODE, 0)
                val email = requestParams.getString(EMAIL, "")
                if (!TextUtils.isEmpty(email)) {
                    variables[EMAIL] = email
                }
                val phone = requestParams.getString(PHONE, "")
                if (!TextUtils.isEmpty(phone)) {
                    variables[PHONE] = phone
                }
                val fax = requestParams.getString(FAX, "")
                if (!TextUtils.isEmpty(fax)) {
                    variables[FAX] = fax
                }
                return variables
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLSuccessMapper())

    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {
        val NAME = "name"
        val ADDRESS = "address"
        val DISTRICT_ID = "districtId"
        val CITY_ID = "cityId"
        val STATE_ID = "stateId"
        val POSTAL_CODE = "postalCode"
        val EMAIL = "email"
        val PHONE = "phone"
        val FAX = "fax"

        @JvmStatic
        fun createRequestParams(name: String, address: String,
                                districtId: Long, cityId: Long,
                                stateId: Long, postalCode: Int,
                                email: String?, phone: String?, fax: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(NAME, name)
            requestParams.putString(ADDRESS, address)
            requestParams.putLong(DISTRICT_ID, districtId)
            requestParams.putLong(CITY_ID, cityId)
            requestParams.putLong(STATE_ID, stateId)
            requestParams.putInt(POSTAL_CODE, postalCode)
            requestParams.putString(EMAIL, email)
            requestParams.putString(PHONE, phone)
            requestParams.putString(FAX, fax)
            return requestParams
        }
    }
}
