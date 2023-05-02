package com.tokopedia.product.usecase

import com.google.gson.JsonObject
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.data.util.TobacoErrorException
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.io.File
import java.lang.reflect.Type

class GetPdpLayoutUseCaseTest {

    companion object {
        const val GQL_GET_PDP_LAYOUT_JSON = "json/gql_get_pdp_layout.json"
        const val GQL_GET_PDP_LAYOUT_USECASE_JSON = "json/gql_get_pdp_layout_usecase_response.json"
        const val GQL_GET_PDP_LAYOUT_REMOVE_COMPONENT_JSON = "json/gql_get_pdp_layout_remove_component.json"
        const val GQL_GET_PDP_LAYOUT_ERROR_TOBACCO_JSON = "json/gql_get_pdp_layout_tobacco.json"
        const val GQL_GET_PDP_LAYOUT_MINI_VARIANT_JSON = "json/gql_get_pdp_layout_mini_varaint.json"
    }

    @RelaxedMockK
    lateinit var gqlUseCase: MultiRequestGraphqlUseCase

    @get:Rule
    public val thrown = ExpectedException.none()

    private val useCaseTest by lazy {
        GetPdpLayoutUseCase(gqlUseCase, "")
    }

    private val useCaseTestLayoutId by lazy {
        GetPdpLayoutUseCase(gqlUseCase, "56")
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun onSuccessExecuteOnBackground() {
        runBlocking {
            coEvery {
                gqlUseCase.executeOnBackground()
            } returns createMockGraphqlResponse(ERROR_TYPE.SUCCESS)

            val getPdpLayoutData = useCaseTest.executeOnBackground()

            coVerify {
                gqlUseCase.clearRequest()
                gqlUseCase.addRequest(any())
                gqlUseCase.executeOnBackground()
            }
            Assert.assertNotNull(getPdpLayoutData)
        }
    }

    @Test
    fun onDataNullExecuteOnBackground() {
        runBlocking {
            coEvery {
                gqlUseCase.executeOnBackground()
            } returns createMockGraphqlResponse(ERROR_TYPE.RUNTIME)

            thrown.expect(RuntimeException::class.java)
            useCaseTest.executeOnBackground()
            coVerify {
                gqlUseCase.clearRequest()
                gqlUseCase.addRequest(any())
                gqlUseCase.executeOnBackground()
            }
        }
    }

    @Test
    fun onErrorTobaccoExecuteOnBackground() {
        runBlocking {
            coEvery {
                gqlUseCase.executeOnBackground()
            } returns createMockGraphqlResponse(ERROR_TYPE.TOBACCO)

            thrown.expect(TobacoErrorException::class.java)
            useCaseTest.executeOnBackground()

            coVerify {
                gqlUseCase.clearRequest()
                gqlUseCase.addRequest(any())
                gqlUseCase.executeOnBackground()
            }
        }
    }

    @Test
    fun `given layout id from dev option should match`() = runBlocking {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ERROR_TYPE.SUCCESS)

        useCaseTestLayoutId.executeOnBackground()

        val layoutId = useCaseTestLayoutId.requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")
        Assert.assertEquals(layoutId, "56")
    }

    @Test
    fun `given layout id from dev and from param should use from param`() = runBlocking {
        coEvery {
            gqlUseCase.executeOnBackground()
        } returns createMockGraphqlResponse(ERROR_TYPE.SUCCESS)

        useCaseTestLayoutId.requestParams = GetPdpLayoutUseCase.createParams("", "", "", "", "122", UserLocationRequest(), "", TokoNowParam())
        useCaseTestLayoutId.executeOnBackground()

        val layoutId = useCaseTestLayoutId.requestParams.getString(ProductDetailCommonConstant.PARAM_LAYOUT_ID, "")
        Assert.assertEquals(layoutId, "122")
    }

    private fun createMockGraphqlResponse(type: ERROR_TYPE): GraphqlResponse {
        return when (type) {
            ERROR_TYPE.TOBACCO -> {
                createMockGraphqlErrorResponseTobacco()
            }
            ERROR_TYPE.RUNTIME -> {
                createMockGraphqlErrorResponseDataNull()
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

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}

internal enum class ERROR_TYPE {
    TOBACCO,
    RUNTIME,
    SUCCESS
}
