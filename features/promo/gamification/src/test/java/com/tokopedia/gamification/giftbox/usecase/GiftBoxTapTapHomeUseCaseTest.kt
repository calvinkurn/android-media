package com.tokopedia.gamification.giftbox.usecase

import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapHomeUseCase
import com.tokopedia.gamification.pdp.data.GqlUseCaseWrapper
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GiftBoxTapTapHomeUseCaseTest {
    lateinit var giftBoxTapTapHomeUseCase: GiftBoxTapTapHomeUseCase
    val queryString = ""

    @MockK
    lateinit var gqlWrapper: GqlUseCaseWrapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        giftBoxTapTapHomeUseCase = GiftBoxTapTapHomeUseCase(queryString, gqlWrapper)
    }

    @Test
    fun `check function invokation of Gql wrapper`() {
        runBlockingTest {
            val clazz = TapTapBaseEntity::class.java
            val map = HashMap<String, Any>()
            coEvery { gqlWrapper.getResponse(clazz, queryString, map) } returns mockk()

            giftBoxTapTapHomeUseCase.getResponse(map)
            coVerify(exactly = 1) { gqlWrapper.getResponse(clazz, queryString, map) }
        }
    }

}