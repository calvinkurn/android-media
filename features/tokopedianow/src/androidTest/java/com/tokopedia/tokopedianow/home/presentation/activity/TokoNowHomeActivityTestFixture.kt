package com.tokopedia.tokopedianow.home.presentation.activity

import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.tokopedianow.test.dispatcher.MockWebserverDispatcher
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.environment.ActivityScenarioTestRule
import com.tokopedia.tokopedianow.home.di.component.HomeComponentStubFactory
import com.tokopedia.tokopedianow.home.di.factory.HomeComponentFactory
import com.tokopedia.tokopedianow.home.domain.repository.CartGraphqlResponse.addToCartResponse
import com.tokopedia.tokopedianow.home.domain.repository.CartGraphqlResponse.getMiniCartV3Response
import com.tokopedia.tokopedianow.home.domain.repository.HomeGraphqlResponse.getStateChoosenAddressResponse
import com.tokopedia.tokopedianow.home.domain.repository.HomeGraphqlResponse.getTargetedTickerResponse
import com.tokopedia.tokopedianow.home.robot.HomePageRobot
import com.tokopedia.tokopedianow.test.stub.UserSessionStub
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

@UiTest
open class TokoNowHomeActivityTestFixture {

    companion object {
        private const val PORT_NUMBER = 8090
    }

    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<TokoNowHomeActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().context

    private lateinit var componentFactory: HomeComponentStubFactory
    private lateinit var mockWebServer: MockWebServer

    protected lateinit var robot: HomePageRobot

    @Before
    fun setUp() {
        Intents.init()
        initRobot()
        initMockWebServer()
        setupDaggerComponent()
        mockGraphql()
    }

    @After
    fun tearDown() {
        Intents.release()
        mockWebServer.shutdown()
    }

    protected fun mockGraphql(gqlResponsePair: () -> Pair<String, Any>) {
        componentFactory.apply {
            val mockGql = gqlResponsePair.invoke()
            appModuleStub.gqlQueryMap[mockGql.first] = mockGql.second
            homeModuleStub.gqlQueryMap[mockGql.first] = mockGql.second
        }
    }

    protected fun mockIsUserLoggedIn(loggedIn: Boolean) {
        componentFactory.homeModuleStub.apply {
            userSession = object: UserSessionStub() {
                override fun isLoggedIn(): Boolean {
                    return loggedIn
                }
            }
        }
    }

    private fun initRobot() {
        robot = HomePageRobot(context, activityScenarioRule)
    }

    private fun initMockWebServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start(PORT_NUMBER)
        mockWebServer.dispatcher = MockWebserverDispatcher()
    }

    private fun setupDaggerComponent() {
        componentFactory = HomeComponentStubFactory()
        HomeComponentFactory.instance = componentFactory
        componentFactory.homeComponent.inject(this)
    }

    private fun mockGraphql() {
        mockGraphql {
            "GetTargetedTicker" to getTargetedTickerResponse
        }
        mockGraphql {
            "KeroAddrGetStateChosenAddress" to getStateChoosenAddressResponse
        }
    }

    protected fun mockGetMiniCartSuccess() {
        mockGraphql {
            "mini_cart_v3" to getMiniCartV3Response
        }
    }

    protected fun mockAddToCartSuccess() {
        mockGraphql {
            "add_to_cart_v2" to addToCartResponse
        }
    }
}
