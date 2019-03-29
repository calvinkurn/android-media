package com.tokopedia.merchantvoucher.common.gql.domain.mapper

import android.text.TextUtils
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import rx.Observable
import rx.functions.Func1

/**
 * Created by hendry on 08/08/18.
 */

class GraphQLUseMerchantVoucherMapper : Func1<UseMerchantVoucherQuery, Observable<UseMerchantVoucherQueryResult>> {

    override fun call(query: UseMerchantVoucherQuery): Observable<UseMerchantVoucherQueryResult> {
        val useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult? = query.result
        if (useMerchantVoucherQueryResult == null) {
            return Observable.error(RuntimeException())
        }
        return if (useMerchantVoucherQueryResult.result) {
            Observable.just(useMerchantVoucherQueryResult)
        } else {
            Observable.error(MessageTitleErrorException(useMerchantVoucherQueryResult.errorMessageTitle,
                    useMerchantVoucherQueryResult.errorMessage))
        }
    }
}
