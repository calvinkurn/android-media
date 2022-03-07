package com.tokopedia.product_bundle.single.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfo
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import java.io.File

@ExperimentalCoroutinesApi
abstract class SingleProductBundleViewModelTestFixture {

    companion object {
        private const val SINGLE_BUNDLE_VARIANT = "json/getBundleInfo_single_bundle_variant.json"
        private const val SINGLE_BUNDLE_EMPTY = "json/getBundleInfo_single_bundle_inactive.json"
    }

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var addToCartBundleUseCase: AddToCartBundleUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    val singleBundleVariant: GetBundleInfo by lazy {
        val jsonString = getJsonFromFile(SINGLE_BUNDLE_VARIANT)
        val jsonObject: JsonObject = CommonUtils.fromJson(
            jsonString,
            JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = GetBundleInfoResponse::class.java
        CommonUtils.fromJson(data, objectType).getBundleInfo
    }

    val singleBundleEmpty: GetBundleInfo by lazy {
        val jsonString = getJsonFromFile(SINGLE_BUNDLE_EMPTY)
        val jsonObject: JsonObject = CommonUtils.fromJson(
            jsonString,
            JsonObject::class.java
        )
        val data = jsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
        val objectType = GetBundleInfoResponse::class.java
        CommonUtils.fromJson(data, objectType).getBundleInfo
    }

    protected val viewModel: SingleProductBundleViewModel by lazy {
        spyk(SingleProductBundleViewModel(
            CoroutineTestDispatchersProvider,
            addToCartBundleUseCase,
            userSession
        ))
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    private fun getJsonFromFile(unit_test_path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(unit_test_path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}