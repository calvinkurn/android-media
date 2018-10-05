package com.tokopedia.merchantvoucher.common.gql.domain.mapper

import android.text.TextUtils

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLDataError
import com.tokopedia.merchantvoucher.common.gql.data.base.GraphQLResult

import rx.Observable
import rx.functions.Func1

/**
 * Created by hendry on 08/08/18.
 */

class GraphQLMerchantListMapper : Func1<MerchantVoucherQuery, Observable<ArrayList<MerchantVoucherModel>>> {

    override fun call(merchantVoucherQuery: MerchantVoucherQuery): Observable<ArrayList<MerchantVoucherModel>> {
        val merchantVoucherQueryResult: MerchantVoucherQueryResult? = merchantVoucherQuery.result
        if (merchantVoucherQueryResult == null) {
            return Observable.error(RuntimeException())
        }
        val errorCode: Int? = merchantVoucherQueryResult.errorCode
        return if (errorCode == null || TextUtils.isEmpty(merchantVoucherQueryResult.errorMessage)) {
            Observable.just(merchantVoucherQueryResult.vouchers)
        } else {
            Observable.error(MessageTitleErrorException(merchantVoucherQueryResult.errorMessageTitle,
                    merchantVoucherQueryResult.errorMessage,
                    merchantVoucherQueryResult.errorCode.toString()))
        }
    }
}
