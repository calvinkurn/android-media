package com.tokopedia.libra.domain.robot

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.libra.LibraOwner
import com.tokopedia.libra.data.repository.CacheRepository
import com.tokopedia.libra.domain.model.ItemLibraUiModel
import com.tokopedia.libra.domain.model.LibraUiModel
import com.tokopedia.libra.domain.usecase.GetLibraRemoteUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk

class SetLibraUseCaseRobot(
    private val gqlRepository: GraphqlRepository,
    private val cacheRepository: CacheRepository,
    private val dispatchers: CoroutineDispatchers
) {

    private val owner = LibraOwner.Home

    val libraUiModel = LibraUiModel(
        listOf(
            ItemLibraUiModel("foo", "bar")
        )
    )

    suspend fun getResult(): LibraUiModel {
        return setLibraUseCase()(owner)
    }

    private fun setLibraUseCase() = GetLibraRemoteUseCase(
        gqlRepository,
        cacheRepository,
        dispatchers
    )
}

fun createSetLibraUseCaseRobot(
    gqlRepository: GraphqlRepository = mockk<GraphqlRepository>(relaxed = true),
    cacheRepository: CacheRepository = mockk<CacheRepository>(),
    fn: SetLibraUseCaseRobot.() -> Unit = {}
) = SetLibraUseCaseRobot(
    gqlRepository = gqlRepository,
    cacheRepository = cacheRepository,
    dispatchers = CoroutineTestDispatchers
).apply(fn)
