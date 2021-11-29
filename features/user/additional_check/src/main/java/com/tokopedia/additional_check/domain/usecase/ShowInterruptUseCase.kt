package com.tokopedia.additional_check.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.additional_check.data.ShowInterruptResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class ShowInterruptUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher): CoroutineUseCase<Unit, ShowInterruptResponse>(dispatcher) {

    override suspend fun execute(params: Unit): ShowInterruptResponse {
        return repository.request(graphqlQuery(), Unit)
    }

    override fun graphqlQuery(): String = """
        query showInterrupt{
            show_interrupt {
                popup_2fa
                interval
                show_skip
                error
                account_link_reminder {
                    interval
                    show_reminder
                }
            }
        }
    """.trimIndent()
}