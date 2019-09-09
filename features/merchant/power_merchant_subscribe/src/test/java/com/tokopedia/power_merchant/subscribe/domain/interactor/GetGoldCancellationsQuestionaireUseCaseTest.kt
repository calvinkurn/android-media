package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString

class GetGoldCancellationsQuestionaireUseCaseTest {

    lateinit var getGoldCancellationsQuestionaireUseCase: GetGoldCancellationsQuestionaireUseCase

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        graphqlUseCase
        getGoldCancellationsQuestionaireUseCase = GetGoldCancellationsQuestionaireUseCase(
                graphqlUseCase,
                anyString()
        )
    }

    @Test
    fun testCreateObservable(){
        getGoldCancellationsQuestionaireUseCase.createObservable(requestParams)
        verifySequence {
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(any())
            graphqlUseCase.createObservable(requestParams)
        }
    }

}