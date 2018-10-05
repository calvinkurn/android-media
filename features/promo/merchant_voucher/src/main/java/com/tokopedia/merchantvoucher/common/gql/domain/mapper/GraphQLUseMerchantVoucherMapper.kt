package com.tokopedia.merchantvoucher.common.gql.domain.mapper

import android.text.TextUtils

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLDataError
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLResult

import rx.Observable
import rx.functions.Func1

/**
 * Created by hendry on 08/08/18.
 */

class GraphQLUseMerchantVoucherMapper : Func1<UseMerchantVoucherQuery, Observable<Boolean>> {

    override fun call(query: UseMerchantVoucherQuery): Observable<Boolean> {
        val useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult? = query.result
        if (useMerchantVoucherQueryResult == null) {
            return Observable.error(RuntimeException())
        }
        val errorCode: Int? = useMerchantVoucherQueryResult.errorCode
        return if (errorCode == null || TextUtils.isEmpty(useMerchantVoucherQueryResult.errorMessage)) {
            Observable.just(useMerchantVoucherQueryResult.result)
        } else {
            Observable.error(MessageTitleErrorException(useMerchantVoucherQueryResult.errorMessageTitle,
                    useMerchantVoucherQueryResult.errorMessage,
                    useMerchantVoucherQueryResult.errorCode.toString()))
        }
    }
}
