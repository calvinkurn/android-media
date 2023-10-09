package com.tokopedia.product.usecase

import com.google.gson.JsonObject
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.data.model.datamodel.CacheState
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.internal.filterList
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.lang.reflect.Type

@OptIn(ExperimentalCoroutinesApi::class)
class GetPdpLayoutUseCaseTest {

    companion object {
        const val GQL_GET_PDP_LAYOUT_JSON = "json/gql_get_pdp_layout.json"
        const val GQL_GET_PDP_LAYOUT_USECASE_JSON = "json/gql_get_pdp_layout_usecase_response.json"
        const val GQL_GET_PDP_LAYOUT_NON_CAMPAIGN_USECASE_JSON = "json/gql_get_pdp_layout_non_campaign_usecase_response.json"
        const val GQL_GET_PDP_LAYOUT_REMOVE_COMPONENT_JSON = "json/gql_get_pdp_layout_remove_component.json"
        const val GQL_GET_PDP_LAYOUT_ERROR_TOBACCO_JSON = "json/gql_get_pdp_layout_tobacco.json"
        const val GQL_GET_PDP_LAYOUT_MINI_VARIANT_JSON = "json/gql_get_pdp_layout_mini_varaint.json"
    }

    @RelaxedMockK
    lateinit var gqlUseCase: MultiRequestGraphqlUseCase

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    @get:Rule
    val testRule = UnconfinedTestRule()

    private val useCaseTest by lazy {
        GetPdpLayoutUseCase(gqlUseCase, "", remoteConfig, CoroutineTestDispatchersProvider)
    }

    private val useCaseTestLayoutId by lazy {
        GetPdpLayoutUseCase(gqlUseCase, "56", remoteConfig, CoroutineTestDispatchersProvider)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun onSuccessExecuteOnBackground() = runTest {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.SUCCESS)

        val result = useCaseTest.executeOnBackground().first()
        Assert.assertTrue(result.listOfLayout.isNotEmpty())

        coVerify {
            gqlUseCase.clearRequest()
            gqlUseCase.addRequest(any())
            gqlUseCase.executeOnBackground()
        }
    }

    @Test
    fun onDataNullExecuteOnBackground() = runTest {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.RUNTIME)

        runCatching {
            useCaseTest.executeOnBackground().first()
        }.onSuccess {
            Assert.fail()
        }.onFailure {
            Assert.assertTrue(it is MessageErrorException)
        }

        coVerify {
            gqlUseCase.clearRequest()
            gqlUseCase.addRequest(any())
            gqlUseCase.executeOnBackground()
        }
    }

    @Test
    fun onErrorTobaccoExecuteOnBackground() = runTest {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.TOBACCO)

        runCatching {
            useCaseTest.executeOnBackground().first()
        }.onSuccess {
            Assert.fail()
        }.onFailure {
            Assert.assertTrue(it is TobacoErrorException)
        }

        coVerify {
            gqlUseCase.clearRequest()
            gqlUseCase.addRequest(any())
            gqlUseCase.executeOnBackground()
        }
    }

    @Test
    fun `given layout id from dev option should match`() = runTest {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.SUCCESS)

        useCaseTestLayoutId.executeOnBackground()

        val layoutId = useCaseTestLayoutId.requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")
        Assert.assertEquals(layoutId, "56")
    }

    @Test
    fun `given layout id from dev and from param should use from param`() = runTest {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.SUCCESS)

        useCaseTestLayoutId.requestParams = GetPdpLayoutUseCase.createParams("", "", "", "", "122", UserLocationRequest(), "", TokoNowParam(), true)
        useCaseTestLayoutId.executeOnBackground()

        val layoutId = useCaseTestLayoutId.requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")
        Assert.assertEquals(layoutId, "122")
    }

    // region pdp cacheable
    @Test
    fun `given pdp layout from cache and cloud`() = runCoroutineTest {
        val cacheExpected = CacheState(isFromCache = true, cacheFirstThenCloud = false)
        val cloudExpected = CacheState(isFromCache = false, cacheFirstThenCloud = true)
        val results = getPdpLayoutCacheable(scope = this, refreshPage = false)

        // then
        val actualCacheData = results.firstOrNull()?.cacheState
        Assert.assertEquals(cacheExpected, actualCacheData)
        val actualCloudData = results.getOrNull(1)?.cacheState
        Assert.assertEquals(cloudExpected, actualCloudData)
    }

    @Test
    fun `given pdp layout from cloud if it's refresh page`() = runCoroutineTest {
        val cacheStateExpected = CacheState(isFromCache = false, cacheFirstThenCloud = false)
        val results = getPdpLayoutCacheable(scope = this, refreshPage = true, enableCacheable = true)

        // then
        Assert.assertTrue(results.size == 1)
        val actualCacheData = results.firstOrNull()?.cacheState
        Assert.assertEquals(cacheStateExpected, actualCacheData)
    }

    @Test
    fun `given pdp layout from cloud if cacheable inactive`() = runCoroutineTest {
        val cacheStateExpected = CacheState(isFromCache = false, cacheFirstThenCloud = false)
        val results = getPdpLayoutCacheable(scope = this, refreshPage = false, enableCacheable = false)

        // then
        Assert.assertTrue(results.size == 1)
        val actualCacheData = results.firstOrNull()?.cacheState
        Assert.assertEquals(cacheStateExpected, actualCacheData)
    }

    @Test
    fun `pdp layout from cache don't have topAds layout`() = runCoroutineTest {
        val ignoreComponentCacheExpected = useCaseTest.ignoreComponentInCache
        val results = getPdpLayoutCacheable(scope = this, refreshPage = false)

        // then
        val actualCacheLayout = results.firstOrNull()?.listOfLayout.orEmpty().map { it.type() }
        val thenCacheLayout = actualCacheLayout.containsAll(ignoreComponentCacheExpected)
        Assert.assertFalse(thenCacheLayout)
    }

    @Test
    fun `pdp layout from cloud have topAds layout`() = runCoroutineTest {
        val ignoreComponentCacheExpected = useCaseTest.ignoreComponentInCache
        val results = getPdpLayoutCacheable(scope = this, refreshPage = false)

        // then
        val actualCacheLayout = results.getOrNull(1)?.listOfLayout.orEmpty().map { it.type() }
        val thenCacheLayout = ignoreComponentCacheExpected.filterList {
            actualCacheLayout.contains(this)
        }.isNotEmpty()
        Assert.assertTrue(thenCacheLayout)
    }

    private fun getPdpLayoutCacheable(
        scope: CoroutineScope,
        refreshPage: Boolean = false,
        enableCacheable: Boolean = true
    ): List<ProductDetailDataModel> {
        // given
        val results = mutableListOf<ProductDetailDataModel>()
        useCaseTest.requestParams = GetPdpLayoutUseCase.createParams("", "", "", "", "122", UserLocationRequest(), "", TokoNowParam(), refreshPage)

        every {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE)
        } returns enableCacheable

        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.NON_CAMPAIGN)

        // when
        scope.launch {
            useCaseTest.executeOnBackground().toList(results)
        }

        // verify
        verify { remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE) }
        coVerify { gqlUseCase.executeOnBackground() }

        return results
    }

    @Test
    fun `given pdp layout from cloud if product campaign`() = runCoroutineTest {
        val results = mutableListOf<ProductDetailDataModel>()
        val cacheStateExpected = CacheState(isFromCache = false, cacheFirstThenCloud = false)
        // given
        useCaseTest.requestParams = GetPdpLayoutUseCase.createParams("", "", "", "", "122", UserLocationRequest(), "", TokoNowParam(), false)

        every {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE)
        } returns true

        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ErrorType.SUCCESS)

        // when
        launch {
            useCaseTest.executeOnBackground().toList(results)
        }

        // verify
        verify { remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_P1_CACHEABLE) }
        coVerify { gqlUseCase.executeOnBackground() }

        // then
        Assert.assertTrue(results.size == 1)
        val actualCacheData = results.firstOrNull()?.cacheState
        Assert.assertEquals(cacheStateExpected, actualCacheData)
    }
    // endregion

    private fun createMockGraphqlResponse(type: ErrorType): GraphqlResponse {
        return when (type) {
            ErrorType.TOBACCO -> {
                createMockGraphqlErrorResponseTobacco()
            }
            ErrorType.RUNTIME -> {
                createMockGraphqlErrorResponseDataNull()
            }
            ErrorType.NON_CAMPAIGN -> {
                createMockGraphqlNonCampaignResponse()
            }
            else -> {
                createMockGraphqlSuccessResponse()
            }
        }
    }

    private fun createMockGraphqlErrorResponseDataNull(): GraphqlResponse {
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = ProductDetailLayout::class.java
        result[objectType] = null
        val gqlErrorData = GraphqlError().apply {
            message = "Error"
        }
        errors[objectType] = listOf(gqlErrorData)
        return GraphqlResponse(result, errors, false)
    }

    private fun createMockGraphqlErrorResponseTobacco(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
            getJsonFromFile(GQL_GET_PDP_LAYOUT_ERROR_TOBACCO_JSON),
            JsonObject::class.java
        )

        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = ProductDetailLayout::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun createMockGraphqlSuccessResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
            getJsonFromFile(GQL_GET_PDP_LAYOUT_USECASE_JSON),
            JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = ProductDetailLayout::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun createMockGraphqlNonCampaignResponse(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val jsonObject: JsonObject = CommonUtils.fromJson(
            getJsonFromFile(GQL_GET_PDP_LAYOUT_NON_CAMPAIGN_USECASE_JSON),
            JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = ProductDetailLayout::class.java
        val obj: Any = CommonUtils.fromJson(data, objectType)
        result[objectType] = obj
        return GraphqlResponse(result, errors, false)
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    private fun runCoroutineTest(testBody: suspend TestScope.() -> TestResult) =
        runTest(context = testRule.coroutineDispatcher, testBody = testBody)
}

internal enum class ErrorType {
    TOBACCO,
    RUNTIME,
    SUCCESS, // campaign
    NON_CAMPAIGN
}
