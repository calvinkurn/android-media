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

class GraphQLUseMerchantVoucherMapper : Func1<UseMerchantVoucherQuery, Observable<Boolean>> {

    override fun call(query: UseMerchantVoucherQuery): Observable<Boolean> {
        val useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult? = query.result
        if (useMerchantVoucherQueryResult == null) {
            return Observable.error(RuntimeException())
        }
        return if (TextUtils.isEmpty(useMerchantVoucherQueryResult.errorMessage)) {
            Observable.just(useMerchantVoucherQueryResult.result)
        } else {
            Observable.error(MessageTitleErrorException(useMerchantVoucherQueryResult.errorMessageTitle,
                    useMerchantVoucherQueryResult.errorMessage))
        }
    }
}
