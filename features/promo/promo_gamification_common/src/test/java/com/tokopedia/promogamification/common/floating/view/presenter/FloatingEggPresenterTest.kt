package com.tokopedia.promogamification.common.floating.view.presenter

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promogamification.common.R
import com.tokopedia.promogamification.common.floating.data.entity.FloatingButtonResponseEntity
import com.tokopedia.promogamification.common.floating.data.entity.GamiFloatingButtonEntity
import com.tokopedia.promogamification.common.floating.view.contract.FloatingEggContract
import com.tokopedia.promotion.common.idling.TkpdIdlingResource
import com.tokopedia.promotion.common.idling.TkpdIdlingResourceProvider.provideIdlingResource
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FloatingEggPresenterTest {

    lateinit var presenter: FloatingEggPresenter
    lateinit var getTokenTokopointsUseCase: GraphqlUseCase
    lateinit var userSession: UserSessionInterface
    lateinit var view: FloatingEggContract.View
    var idlingResource: TkpdIdlingResource? = null
    val context: Context = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        preparePresenter()

        every { getTokenTokopointsUseCase.clearRequest() } just runs
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        every { getTokenTokopointsUseCase.addRequest(any()) } just runs
        every { getTokenTokopointsUseCase.execute(any()) } just runs
    }

    fun preparePresenter() {
        userSession = mockk()
        getTokenTokopointsUseCase = mockk()
        presenter = FloatingEggPresenter(getTokenTokopointsUseCase, userSession)

        val resources: Resources = mockk()
        view = spyk(getView(resources))
        presenter.attachView(view)
        every { GraphqlHelper.loadRawString(resources, R.raw.core_gami_floating_query) } returns ""
        idlingResource = (provideIdlingResource("FloatingEgg")!!)
    }

    private fun getView(resources: Resources) = object : FloatingEggContract.View {
        override fun onSuccessGetToken(gamiFloatingButtonEntity: GamiFloatingButtonEntity?) {
            //Do nothing
        }

        override fun getResources(): Resources {
            return resources
        }

        override fun onErrorGetToken(throwable: Throwable?) {
            //Do nothing
        }
    }

    @Test
    fun testGetGetTokenTokopoints() {
        presenter.getGetTokenTokopoints()

        verifyOrder {
            getTokenTokopointsUseCase.clearRequest()
            GraphqlHelper.loadRawString(any(), any())
            getTokenTokopointsUseCase.addRequest(any())
            getTokenTokopointsUseCase.execute(any())
        }
        Assert.assertEquals(idlingResource?.countingIdlingResource?.isIdleNow, false)
    }

    @Test
    fun testSubscriberOnComplete() {
        val subscriber = presenter.subscriber
        subscriber.onCompleted()
        Assert.assertEquals(subscriber != null, true)
    }

    @Test
    fun testSubscriberOnError() {
        val subscriber = presenter.subscriber
        val exception = Exception()
        if(idlingResource?.countingIdlingResource?.isIdleNow == true){
            idlingResource?.increment()
        }
        subscriber.onError(exception)
        verify { view.onErrorGetToken(exception) }
        Assert.assertEquals(idlingResource?.countingIdlingResource?.isIdleNow, true)
    }

    @Test
    fun testSubscriberOnNext() {
        val subscriber = presenter.subscriber
        val graphqlResponse: GraphqlResponse = mockk()
        val floatingButtonResponseEntity: FloatingButtonResponseEntity = mockk()
        val gamiFloatingButtonEntity: GamiFloatingButtonEntity = mockk()
        coEvery { graphqlResponse.getData(FloatingButtonResponseEntity::class.java) as FloatingButtonResponseEntity } returns floatingButtonResponseEntity
        every { floatingButtonResponseEntity.getGamiFloatingButtonEntity() } returns gamiFloatingButtonEntity

        if(idlingResource?.countingIdlingResource?.isIdleNow == true){
            idlingResource?.increment()
        }

        subscriber.onNext(graphqlResponse)
        verify { view.onSuccessGetToken(gamiFloatingButtonEntity) }
        Assert.assertEquals(idlingResource?.countingIdlingResource?.isIdleNow, true)
    }

    @Test
    fun testDetachView() {
        every { getTokenTokopointsUseCase.unsubscribe() } just runs
        presenter.detachView()
        verify { getTokenTokopointsUseCase.unsubscribe() }
    }

    @Test
    fun testIsUserLogin() {
        every { userSession.isLoggedIn() } returns false
        presenter.isUserLogin
        verify { userSession.isLoggedIn() }
    }
}