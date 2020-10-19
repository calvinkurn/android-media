package com.tokopedia.home_account.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class HomeAccountUser @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)

//    : GraphqlUseCase<GetObjectPojo>(graphqlRepository)
{

//    fun getBottomSheetData(onSuccess: (GetObjectPojo) -> Unit, onError: (Throwable) -> Unit){
//        rawQueries[AdditionalCheckConstants.QUERY_CHECK_BOTTOM_SHEET]?.let { query ->
//            setTypeClass(GetObjectPojo::class.java)
//            setGraphqlQuery(query)
//            execute({
//                onSuccess(it)
//            }, onError)
//        }
//    }
}