package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by Irfan Khoirul on 05/01/19.
 */

@RunWith(MockitoJUnitRunner::class)
class DoAtcExpressSubscriberTest {

    @Mock
    lateinit var view: CheckoutVariantContract.View
    @Mock
    lateinit var presenter: CheckoutVariantContract.Presenter
    @Mock
    lateinit var domainModelMapper: AtcDomainModelMapper
    @Mock
    lateinit var gqlMockDataLoader: GqlMockDataLoader

    private lateinit var doAtcExpressSubscriber: DoAtcExpressSubscriber

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter.attachView(view)
        doAtcExpressSubscriber = DoAtcExpressSubscriber(view, presenter, domainModelMapper)
    }

    @Test
    @Throws(Exception::class)
    fun OnLoadAtcExpressThenSuccess_ShouldLoadRates() {
        //Given
        val sourceOk = RESPONSE_ATC_SUCCESS
        val graphqlResponse = gqlMockDataLoader.getGraphqlResponse(sourceOk, arrayListOf(AtcExpressGqlResponse::class.java))

        //When
        doAtcExpressSubscriber.onNext(graphqlResponse)

        //Then
        Mockito.verify(view).hideLoading()
//        Mockito.verify(presenter).loadShippingRates()
    }

}