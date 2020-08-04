package com.tokopedia.additional_check.domain.usecase

import com.tokopedia.additional_check.data.GetObjectPojo
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class AdditionalCheckUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)
        : GraphqlUseCase<GetObjectPojo>(graphqlRepository) {

    fun getBottomSheetData(onSuccess: (GetObjectPojo) -> Unit, onError: (Throwable) -> Unit){
        rawQueries[AdditionalCheckConstants.QUERY_CHECK_BOTTOM_SHEET]?.let { query ->
            setTypeClass(GetObjectPojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it)
            }, onError)
        }
    }
}