package com.tokopedia.additional_check.domain.usecase

import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class AdditionalCheckUseCase @Inject constructor(
        private val userSession: UserSessionInterface) {

    //    : CoroutineScope {

//    fun getStickerGroup(
//            isSeller: Boolean,
//            onLoading: (ChatListGroupStickerResponse) -> Unit,
//            onSuccess: (ChatListGroupStickerResponse, List<StickerGroup>) -> Unit,
//            onError: (Throwable) -> Unit
//    ) {
//        launchCatchError(dispatchers.IO,
//                {
//                    val params = generateParams(isSeller)
//                    val cache = getCacheStickerGroup(isSeller)?.also {
//                        withContext(dispatchers.Main) {
//                            onLoading(it)
//                        }
//                    }
//                    val response = gqlUseCase.apply {
//                        setTypeClass(ChatListGroupStickerResponse::class.java)
//                        setRequestParams(params)
//                        setGraphqlQuery(query)
//                    }.executeOnBackground()
//                    val hasDifferentSize = response.hasDifferentGroupSize(cache)
//                    val needToUpdateCache = findNeedToUpdateCache(cache, response)
//                    if (hasDifferentSize || needToUpdateCache.isNotEmpty()) {
//                        saveToCache(response, isSeller)
//                    }
//                    withContext(dispatchers.Main) {
//                        onSuccess(response, needToUpdateCache)
//                    }
//                },
//                { exception ->
//                    withContext(dispatchers.Main) {
//                        onError(exception)
//                    }
//                }
//        )
//    }
}