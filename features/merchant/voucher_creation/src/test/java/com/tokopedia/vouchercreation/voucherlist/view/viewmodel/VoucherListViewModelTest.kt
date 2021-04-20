package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

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
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*

@ExperimentalCoroutinesApi
class VoucherListViewModelTest {

    @RelaxedMockK
    lateinit var getVoucherListUseCase: GetVoucherListUseCase

    @RelaxedMockK
    lateinit var getNotStartedVoucherListUseCase: GetVoucherListUseCase

    @RelaxedMockK
    lateinit var cancelVoucherUseCase: CancelVoucherUseCase

    @RelaxedMockK
    lateinit var shopBasicDataUseCase: ShopBasicDataUseCase

    @RelaxedMockK
    lateinit var voucherDetailUseCase: VoucherDetailUseCase

    @RelaxedMockK
    lateinit var getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase

    @RelaxedMockK
    lateinit var voucherUiModel: VoucherUiModel

    @RelaxedMockK
    lateinit var successVoucherObserver: Observer<in Result<VoucherUiModel>>

    @RelaxedMockK
    lateinit var stopVoucherResponseObserver: Observer<in Result<Int>>

    @RelaxedMockK
    lateinit var cancelVoucherResponseObserver: Observer<in Result<Int>>

    @RelaxedMockK
    lateinit var localVoucherListObserver: Observer<in List<VoucherUiModel>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mViewModel: VoucherListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = VoucherListViewModel(
                getVoucherListUseCase,
                getNotStartedVoucherListUseCase,
                cancelVoucherUseCase,
                shopBasicDataUseCase,
                voucherDetailUseCase,
                getBroadCastMetaDataUseCase,
                CoroutineTestDispatchersProvider
        )

        with(mViewModel) {
            successVoucherLiveData.observeForever(successVoucherObserver)
            stopVoucherResponseLiveData.observeForever(stopVoucherResponseObserver)
            cancelVoucherResponseLiveData.observeForever(cancelVoucherResponseObserver)
            localVoucherListLiveData.observeForever(localVoucherListObserver)
        }
    }

    @After
    fun cleanup() {
        with(mViewModel) {
            successVoucherLiveData.removeObserver(successVoucherObserver)
            stopVoucherResponseLiveData.removeObserver(stopVoucherResponseObserver)
            cancelVoucherResponseLiveData.observeForever(cancelVoucherResponseObserver)
            localVoucherListLiveData.removeObserver(localVoucherListObserver)
        }
    }

    @Test
    fun `success getting active voucher list first time will change shop basic data and voucher list`() = runBlocking {
        with(mViewModel) {
            val dummySuccessShopBasicData = ShopBasicDataResult()

            coEvery {
                shopBasicDataUseCase.executeOnBackground()
            } returns dummySuccessShopBasicData
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)

            getActiveVoucherList(true)

            coVerify {
                shopBasicDataUseCase.executeOnBackground()
                getVoucherListUseCase.executeOnBackground()
            }

            assert(shopBasicLiveData.value == Success(dummySuccessShopBasicData))
            assert(voucherList.value == Success(listOf(voucherUiModel, voucherUiModel)))
        }
    }

    @Test
    fun `fail getting active voucher list first time`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")
            val dummySuccessShopBasicData = ShopBasicDataResult()

            coEvery {
                shopBasicDataUseCase.executeOnBackground()
            } returns dummySuccessShopBasicData
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable

            getActiveVoucherList(true)

            coroutineContext[Job]?.children?.forEach { it.join() }

            coVerify {
                shopBasicDataUseCase.executeOnBackground()
                getVoucherListUseCase.executeOnBackground()
            }

            assert(voucherList.value is Fail)
        }
    }

    @Test
    fun `fail getting shop basic data when first time getting active voucher list`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                shopBasicDataUseCase.executeOnBackground()
            } throws dummyThrowable
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } returns  listOf(voucherUiModel)

            getActiveVoucherList(true)

            coVerify {
                shopBasicDataUseCase.executeOnBackground()
            }

            assert(voucherList.value is Fail)
        }
    }

    @Test
    fun `success getting voucher list not for the first time`() = runBlocking {
        with(mViewModel) {
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)

            getActiveVoucherList(false)

            coVerify {
                shopBasicDataUseCase wasNot Called
                getVoucherListUseCase.executeOnBackground()
            }

            assert(voucherList.value == Success(listOf(voucherUiModel, voucherUiModel)))
        }
    }

    @Test
    fun `fail getting voucher list not for the first time`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable

            getActiveVoucherList(false)

            coVerify {
                shopBasicDataUseCase wasNot Called
                getVoucherListUseCase.executeOnBackground()
            }
            assert(voucherList.value is Fail)
        }
    }

    @Test
    fun `success getting voucher list history will add voucher list`() = runBlocking {
        with(mViewModel) {
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)

            getVoucherListHistory(anyInt(), listOf(anyInt()), anyString(), anyInt(), anyBoolean())

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }

            assert(voucherList.value == Success(listOf(voucherUiModel)))
        }
    }

    @Test
    fun `fail getting voucher list history will not add voucher list`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable

            getVoucherListHistory(anyInt(), listOf(anyInt()), anyString(), anyInt(), anyBoolean())

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }

            assert(voucherList.value is Fail)
        }
    }

    @Test
    fun `setting search keyword will change local voucher list`() = runBlocking {
        with(mViewModel) {
            setSearchKeyword(anyString())

            assert(localVoucherListLiveData.value != null)
        }
    }

    @Test
    fun `setting search keyword will return list of voucher that contains that keyword if already loaded voucher history before`() = runBlocking {
        with(mViewModel) {
            val dummyKeyword = "voucher"
            val dummySuccessVoucherHistory = listOf(voucherUiModel, voucherUiModel)

            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns dummySuccessVoucherHistory
            every {
                voucherUiModel.name
            } returns dummyKeyword

            getVoucherListHistory(anyInt(), listOf(anyInt()), anyString(), anyInt(), anyBoolean())

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }

            setSearchKeyword(dummyKeyword)

            assert(localVoucherListLiveData.value  == dummySuccessVoucherHistory)
        }
    }

    @Test
    fun `success stopping voucher`() = runBlocking {
        with(mViewModel) {
            val dummySuccessCancelVoucher = 1

            coEvery {
                cancelVoucherUseCase.executeOnBackground()
            } returns dummySuccessCancelVoucher

            cancelVoucher(anyInt(), false)

            coVerify {
                cancelVoucherUseCase.executeOnBackground()
            }

            assert(stopVoucherResponseLiveData.value == Success(dummySuccessCancelVoucher))
        }
    }

    @Test
    fun `fail stopping voucher`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                cancelVoucherUseCase.executeOnBackground()
            } throws dummyThrowable

            cancelVoucher(anyInt(), false)

            coVerify {
                cancelVoucherUseCase.executeOnBackground()
            }

            assert(stopVoucherResponseLiveData.value is Fail)
        }
    }

    @Test
    fun `success cancelling voucher`() = runBlocking {
        with(mViewModel) {
            val dummySuccessCancelVoucher = 1

            coEvery {
                cancelVoucherUseCase.executeOnBackground()
            } returns dummySuccessCancelVoucher

            cancelVoucher(anyInt(), true)

            coVerify {
                cancelVoucherUseCase.executeOnBackground()
            }

            assert(cancelVoucherResponseLiveData.value == Success(dummySuccessCancelVoucher))
        }
    }

    @Test
    fun `fail cancelling voucher`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                cancelVoucherUseCase.executeOnBackground()
            } throws dummyThrowable

            cancelVoucher(anyInt(), true)

            coVerify {
                cancelVoucherUseCase.executeOnBackground()
            }

            assert(cancelVoucherResponseLiveData.value is Fail)
        }
    }

    @Test
    fun `success get success voucher detail`() = runBlocking {
        with(mViewModel) {
            coEvery {
                voucherDetailUseCase.executeOnBackground()
            } returns voucherUiModel

            getSuccessCreatedVoucher(anyInt())

            coVerify {
                voucherDetailUseCase.executeOnBackground()
            }

            assert(successVoucherLiveData.value == Success(voucherUiModel))
        }
    }

    @Test
    fun `fail get success voucher detail`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                voucherDetailUseCase.executeOnBackground()
            } throws dummyThrowable

            getSuccessCreatedVoucher(anyInt())

            coVerify {
                voucherDetailUseCase.executeOnBackground()
            }

            assert(successVoucherLiveData.value is Fail)
        }
    }

}