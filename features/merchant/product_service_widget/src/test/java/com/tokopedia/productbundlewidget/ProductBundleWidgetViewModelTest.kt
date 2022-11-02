package com.tokopedia.productbundlewidget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfo
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.productbundlewidget.model.BundleTypes
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.productbundlewidget.model.GetBundleParam
import com.tokopedia.productbundlewidget.model.ProductBundleWidgetUiMapper
import com.tokopedia.productbundlewidget.presentation.ProductBundleWidgetViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductBundleWidgetViewModelTest {

    @RelaxedMockK
    lateinit var getBundleInfoUseCase: GetBundleInfoUseCase

    @RelaxedMockK
    lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    @RelaxedMockK
    lateinit var productBundleWidgetUiMapper: ProductBundleWidgetUiMapper

    @RelaxedMockK
    lateinit var bundleUiModelsObserver: Observer<in List<BundleUiModel>>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: ProductBundleWidgetViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ProductBundleWidgetViewModel(
            CoroutineTestDispatchersProvider,
            getBundleInfoUseCase,
            chosenAddressRequestHelper,
            productBundleWidgetUiMapper
        )

        with(viewModel) {
            bundleUiModels.observeForever(bundleUiModelsObserver)
            error.observeForever(errorObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            bundleUiModels.removeObserver(bundleUiModelsObserver)
            error.removeObserver(errorObserver)
        }
    }

    @Test
    fun `When bundle info are successfully fetched from the server`() {
        runBlocking {
            with(viewModel) {
                val expected = listOf(
                    BundleUiModel(
                        bundleType = BundleTypes.SINGLE_BUNDLE
                    )
                )
                val param = GetBundleParam()
                val bundleInfo =
                    GetBundleInfo(
                        bundleInfo = listOf(
                            BundleInfo(
                                status = "1",
                                type = "1",
                                groupID = 1001,
                                shopID = "1002",
                            )
                        )
                    )
                val response = GetBundleInfoResponse(getBundleInfo = bundleInfo).getBundleInfo?.bundleInfo

                coEvery {
                    response?.let { productBundleWidgetUiMapper.groupAndMap(it) }
                } returns expected

                coEvery {
                    getBundleInfoUseCase.executeOnBackground()
                } returns GetBundleInfoResponse(getBundleInfo = bundleInfo)

                getBundleInfo(param)

                val actual = bundleUiModels.getOrAwaitValue()
                assertEquals(expected, actual)
                assertFalse(isBundleEmpty.getOrAwaitValue())
            }
        }
    }

    @Test
    fun `When bundle info are successfully fetched from the server and empty`() {
        runBlocking {
            with(viewModel) {
                val param = GetBundleParam()
                val expected = GetBundleInfoResponse().getBundleInfo?.bundleInfo?.let {
                    productBundleWidgetUiMapper.groupAndMap(it)
                }

                coEvery {
                    getBundleInfoUseCase.executeOnBackground()
                } returns GetBundleInfoResponse()

                getBundleInfo(param)

                val actual = bundleUiModels.getOrAwaitValue()
                assertEquals(expected, actual)
                assertTrue(isBundleEmpty.getOrAwaitValue())
            }
        }
    }

    @Test
    fun `When bundle info are successfully fetched from the server and empty bundle info`() {
        runBlocking {
            with(viewModel) {
                val param = GetBundleParam()
                val expected = emptyList<BundleUiModel>()

                coEvery {
                    getBundleInfoUseCase.executeOnBackground()
                } returns GetBundleInfoResponse(getBundleInfo = null)

                getBundleInfo(param)

                val actual = bundleUiModels.getOrAwaitValue()
                assertEquals(expected, actual)
                assertTrue(isBundleEmpty.getOrAwaitValue())
            }
        }
    }

    @Test
    fun `When bundle info are failed to be fetched from the server`() {
        runBlocking {
            with(viewModel) {
                val throwable = MessageErrorException("Network Error")
                val param = GetBundleParam()

                coEvery {
                    getBundleInfoUseCase.executeOnBackground()
                } throws throwable

                getBundleInfo(param)

                val actual = error.getOrAwaitValue()
                assertEquals(throwable, actual)
            }
        }
    }

}
