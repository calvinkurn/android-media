package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.UpdateShopLocationMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class UpdateShopLocationUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<UpdateShopLocationMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<UpdateShopLocationMutation>(context, UpdateShopLocationMutation::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_update_shop_location)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[ID] = requestParams.getString(ID, "")
                val name = requestParams.getString(NAME, "")
                if (!TextUtils.isEmpty(name)) {
                    variables[NAME] = name
                }
                val address = requestParams.getString(ADDRESS, "")
                if (!TextUtils.isEmpty(address)) {
                    variables[ADDRESS] = address
                }
                val districtId = requestParams.getLong(DISTRICT_ID, 0)
                if (districtId > 0) {
                    variables[DISTRICT_ID] = districtId
                }
                val cityId = requestParams.getLong(CITY_ID, 0)
                if (cityId > 0) {
                    variables[CITY_ID] = cityId
                }
                val stateId = requestParams.getLong(STATE_ID, 0)
                if (stateId > 0) {
                    variables[STATE_ID] = stateId
                }
                val postalCode = requestParams.getInt(POSTAL_CODE, 0)
                if (postalCode > 0) {
                    variables[POSTAL_CODE] = postalCode
                }
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
        private val ID = "id"
        private val NAME = "name"
        private val ADDRESS = "address"
        private val DISTRICT_ID = "districtId"
        private val CITY_ID = "cityId"
        private val STATE_ID = "stateId"
        private val POSTAL_CODE = "postalCode"
        private val EMAIL = "email"
        private val PHONE = "phone"
        private val FAX = "fax"

        @JvmStatic
        fun createRequestParams(id: String,
                                name: String?, address: String?,
                                districtId: Long, cityId: Long,
                                stateId: Long, postalCode: Int,
                                email: String?, phone: String?, fax: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(ID, id)
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
