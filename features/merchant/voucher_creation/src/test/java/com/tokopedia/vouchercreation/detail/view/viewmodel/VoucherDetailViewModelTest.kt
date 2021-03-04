package com.tokopedia.vouchercreation.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class VoucherDetailViewModelTest {

    @RelaxedMockK
    lateinit var voucherDetailUseCase: VoucherDetailUseCase

    @RelaxedMockK
    lateinit var cancelVoucherUseCase: CancelVoucherUseCase

    @RelaxedMockK
    lateinit var shopBasicDataUseCase: ShopBasicDataUseCase

    @RelaxedMockK
    lateinit var getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase

    @RelaxedMockK
    lateinit var cancelVoucherResultObserver: Observer<Result<Int>>

    @RelaxedMockK
    lateinit var merchantVoucherModelObserver: Observer<Result<VoucherUiModel>>

    @RelaxedMockK
    lateinit var voucherUiModel: VoucherUiModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var mViewModel: VoucherDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = VoucherDetailViewModel(CoroutineTestDispatchersProvider, voucherDetailUseCase, cancelVoucherUseCase, shopBasicDataUseCase, getBroadCastMetaDataUseCase)

        with(mViewModel) {
            cancelVoucherResultLiveData.observeForever(cancelVoucherResultObserver)
            merchantVoucherModelLiveData.observeForever(merchantVoucherModelObserver)
        }
    }

    @After
    fun cleanup() {
        with(mViewModel) {
            cancelVoucherResultLiveData.removeObserver(cancelVoucherResultObserver)
            merchantVoucherModelLiveData.removeObserver(merchantVoucherModelObserver)
        }
    }

    @Test
    fun `success get voucher detail`() = runBlocking {
        with(mViewModel) {
            val dummyShopBasic = ShopBasicDataResult()

            coEvery {
                voucherDetailUseCase.executeOnBackground()
            } returns voucherUiModel
            coEvery {
                shopBasicDataUseCase.executeOnBackground()
            } returns dummyShopBasic

            getVoucherDetail(anyInt())

            coVerify {
                voucherDetailUseCase.executeOnBackground()
                shopBasicDataUseCase.executeOnBackground()
            }

            assert(merchantVoucherModelLiveData.value == Success(voucherUiModel))
            assert(shopBasicLiveData.value == Success(dummyShopBasic))
        }
    }

    @Test
    fun `fail get voucher ui model`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")
            val dummyShopBasic = ShopBasicDataResult()

            coEvery {
                voucherDetailUseCase.executeOnBackground()
            } throws dummyThrowable
            coEvery {
                shopBasicDataUseCase.executeOnBackground()
            } returns dummyShopBasic

            getVoucherDetail(anyInt())

            coVerify {
                voucherDetailUseCase.executeOnBackground()
            }

            assert(merchantVoucherModelLiveData.value is Fail)
        }
    }

    @Test
    fun `fail get shop basic data`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                voucherDetailUseCase.executeOnBackground()
            } returns voucherUiModel
            coEvery {
                shopBasicDataUseCase.executeOnBackground()
            } throws dummyThrowable

            getVoucherDetail(anyInt())

            coVerify {
                voucherDetailUseCase.executeOnBackground()
                shopBasicDataUseCase.executeOnBackground()
            }

            assert(merchantVoucherModelLiveData.value is Fail)
        }
    }

    @Test
    fun `success cancel voucher`() = runBlocking {
        with(mViewModel) {
            val dummyCancelVoucherResult = 1

            coEvery {
                cancelVoucherUseCase.executeOnBackground()
            } returns dummyCancelVoucherResult

            cancelVoucher(anyInt(), anyString())

            coVerify {
                cancelVoucherUseCase.executeOnBackground()
            }

            assert(cancelVoucherResultLiveData.value == Success(dummyCancelVoucherResult))
        }
    }

    @Test
    fun `fail cancel voucher`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                cancelVoucherUseCase.executeOnBackground()
            } throws dummyThrowable

            cancelVoucher(anyInt(), anyString())

            coVerify {
                cancelVoucherUseCase.executeOnBackground()
            }

            assert(cancelVoucherResultLiveData.value is Fail)
        }
    }

}