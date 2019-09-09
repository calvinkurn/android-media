package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class GetPMCancellationsQuestionaireDataUseCaseTest {

    lateinit var getPMCancellationsQuestionnaireDataUseCase: GetPMCancellationQuestionnaireDataUseCase

    @RelaxedMockK
    lateinit var getShopStatusUseCase: GetShopStatusUseCase

    @RelaxedMockK
    lateinit var getGoldCancellationsQuestionnaireUseCase: GetGoldCancellationsQuestionaireUseCase

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getPMCancellationsQuestionnaireDataUseCase = GetPMCancellationQuestionnaireDataUseCase(
                getShopStatusUseCase,
                getGoldCancellationsQuestionnaireUseCase
        )
    }

    @Test
    fun testCreateObservable() {
        getPMCancellationsQuestionnaireDataUseCase.createObservable(requestParams)
        verify {
            getShopStatusUseCase.createObservable(any())
            getGoldCancellationsQuestionnaireUseCase.createObservable(any())
        }
    }

}