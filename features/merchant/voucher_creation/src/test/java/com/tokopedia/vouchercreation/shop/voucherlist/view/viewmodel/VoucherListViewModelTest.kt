package com.tokopedia.vouchercreation.shop.voucherlist.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.shop.detail.domain.usecase.VoucherDetailUseCase
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherSubsidy
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherVps
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
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
import java.util.*

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
    lateinit var initiateVoucherUseCase: InitiateVoucherUseCase

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
            initiateVoucherUseCase,
            CoroutineTestDispatchersProvider
        )

        with(mViewModel) {
            successVoucherLiveData.observeForever(successVoucherObserver)
            stopVoucherResponseLiveData.observeForever(stopVoucherResponseObserver)
            cancelVoucherResponseLiveData.observeForever(cancelVoucherResponseObserver)
        }
    }

    @After
    fun cleanup() {
        with(mViewModel) {
            successVoucherLiveData.removeObserver(successVoucherObserver)
            stopVoucherResponseLiveData.removeObserver(stopVoucherResponseObserver)
            cancelVoucherResponseLiveData.observeForever(cancelVoucherResponseObserver)
        }
    }

    @Test
    fun `success getting active voucher list first time will change shop basic data and voucher list`() =
        runBlocking {
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

                val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
                getActiveVoucherList(true, sourceRequestParams = sourceRequestParams)

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

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            getActiveVoucherList(true, sourceRequestParams = sourceRequestParams)

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
            } returns listOf(voucherUiModel)

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            getActiveVoucherList(true, sourceRequestParams = sourceRequestParams)

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

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            getActiveVoucherList(false, sourceRequestParams = sourceRequestParams)

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

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            getActiveVoucherList(false, sourceRequestParams = sourceRequestParams)

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

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            getVoucherListHistory(
                anyInt(),
                anyString(),
                listOf(anyInt()),
                anyString(),
                anyInt(),
                anyBoolean(),
                sourceRequestParams,
                anyString()
            )

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

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            getVoucherListHistory(
                anyInt(),
                anyString(),
                listOf(anyInt()),
                anyString(),
                anyInt(),
                anyBoolean(),
                sourceRequestParams,
                anyString()
            )

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }

            assert(voucherList.value is Fail)
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

    @Test
    fun `success getBroadCastMetaData`() = runBlocking {
        with(mViewModel) {
            coEvery {
                getBroadCastMetaDataUseCase.executeOnBackground()
            } returns ChatBlastSellerMetadata()
            getBroadCastMetaData()
            coVerify {
                getBroadCastMetaDataUseCase.executeOnBackground()
            }

            assert(broadCastMetadata.value is Success)
        }
    }

    @Test
    fun `fail getBroadCastMetaData`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getBroadCastMetaDataUseCase.executeOnBackground()
            } throws dummyThrowable

            getBroadCastMetaData()

            coVerify {
                getBroadCastMetaDataUseCase.executeOnBackground()
            }

            assert(broadCastMetadata.value is Fail)
        }
    }

    @Test
    fun `check whether showBroadCastChatTicker value is true after set to true`() {
        with(mViewModel) {
            setShowBroadCastChatTicker(true)
            assert(getShowBroadCastChatTicker())
        }
    }

    @Test
    fun `check whether broadcast chat ticker is expired if given timestamp is already expired`() {
        with(mViewModel) {
            assert(isBroadCastChatTickerExpired(1524017252L))
        }
    }

    @Test
    fun `check whether broadcast chat ticker is not expired if given timestamp is not expired`() {
        with(mViewModel) {
            assert(!isBroadCastChatTickerExpired(Date().time))
        }
    }

    @Test
    fun `check whether voucher source request param is returning seller only`() {
        with(mViewModel) {

            val dummyPair = Pair(VoucherSubsidy.SELLER, VoucherVps.NON_VPS)
            isSellerCreated = true
            isVps = false
            isSubsidy = false

            val sourceRequestParams = getVoucherSourceRequestParams(
                isSellerCreated,
                isVps,
                isSubsidy
            )

            assert(sourceRequestParams == dummyPair)
        }
    }

    @Test
    fun `check whether voucher source request param is returning vps only`() {
        with(mViewModel) {

            val dummyPair = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.VPS)
            isSellerCreated = false
            isVps = true
            isSubsidy = false

            val sourceRequestParams = getVoucherSourceRequestParams(
                isSellerCreated,
                isVps,
                isSubsidy
            )

            assert(sourceRequestParams == dummyPair)
        }
    }

    @Test
    fun `check whether voucher source request param is returning subsidy only`() {
        with(mViewModel) {

            val dummyPair = Pair(VoucherSubsidy.TOKOPEDIA, VoucherVps.NON_VPS)
            isSellerCreated = false
            isVps = false
            isSubsidy = true

            val sourceRequestParams = getVoucherSourceRequestParams(
                isSellerCreated,
                isVps,
                isSubsidy
            )

            assert(sourceRequestParams == dummyPair)
        }
    }

    @Test
    fun `check whether voucher source request param is returning seller, vps, & subsidy`() {
        with(mViewModel) {

            val dummyPair = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            isSellerCreated = true
            isVps = true
            isSubsidy = true

            val sourceRequestParams = getVoucherSourceRequestParams(
                isSellerCreated,
                isVps,
                isSubsidy
            )

            assert(sourceRequestParams == dummyPair)
        }
    }

    @Test
    fun `check whether voucher source request param is returning seller vps`() {
        with(mViewModel) {

            val dummyPair = Pair(VoucherSubsidy.SELLER, VoucherVps.ALL)
            val isSellerCreated = true
            val isVps = true
            val isSubsidy = false

            val sourceRequestParams = getVoucherSourceRequestParams(
                isSellerCreated,
                isVps,
                isSubsidy
            )

            assert(sourceRequestParams == dummyPair)
        }
    }

//    @Test
//    fun `check whether voucher source request param is returning seller subsidy`() {
//        with(mViewModel) {
//
//            val dummyPair = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.NON_VPS)
//            isSellerCreated = true
//            isVps = false
//            isSubsidy = false
//
//            val sourceRequestParams = getVoucherSourceRequestParams(
//                isSellerCreated,
//                isVps,
//                isSubsidy
//            )
//
//            assert(sourceRequestParams == dummyPair)
//        }
//    }
//
//    @Test
//    fun `check whether voucher source request param is returning vps subsidy`() {
//        with(mViewModel) {
//
//            val dummyPair = Pair(VoucherSubsidy.TOKOPEDIA, VoucherVps.VPS)
//            isSellerCreated = false
//            isVps = true
//            isSubsidy = false
//
//            val sourceRequestParams = getVoucherSourceRequestParams(
//                isSellerCreated,
//                isVps,
//                isSubsidy
//            )
//
//            assert(sourceRequestParams == dummyPair)
//        }
//    }

    @Test
    fun `check whether voucher source request param is returning default value`() {
        with(mViewModel) {

            val dummyPair = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            isSellerCreated = null
            isVps = null
            isSubsidy = null

            val sourceRequestParams = getVoucherSourceRequestParams(
                isSellerCreated,
                isVps,
                isSubsidy
            )

            assert(sourceRequestParams == dummyPair)
        }
    }

    @Test
    fun `success search voucher by keyword from active voucher`() = runBlocking {
        with(mViewModel) {
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            searchVoucherByKeyword(
                isActiveVoucher = true,
                keyword = "test",
                sourceRequestParams = sourceRequestParams,
                targetBuyer = null
            )

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }
            assert(voucherList.value == Success(listOf(voucherUiModel, voucherUiModel)))
        }
    }

    @Test
    fun `fail search voucher by keyword from active voucher`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable
            coEvery {
                getNotStartedVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            searchVoucherByKeyword(
                isActiveVoucher = true,
                keyword = "test",
                sourceRequestParams = sourceRequestParams,
                targetBuyer = null
            )

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }
            assert(voucherList.value is Fail)
        }
    }

    @Test
    fun `success search voucher by keyword from voucher list history`() = runBlocking {
        with(mViewModel) {
            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } returns listOf(voucherUiModel)

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            searchVoucherByKeyword(
                isActiveVoucher = false,
                keyword = "test",
                sourceRequestParams = sourceRequestParams,
                targetBuyer = null
            )

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }
            assert(voucherList.value == Success(listOf( voucherUiModel)))
        }
    }

    @Test
    fun `fail search voucher by keyword from voucher list history`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getVoucherListUseCase.executeOnBackground()
            } throws dummyThrowable

            val sourceRequestParams = Pair(VoucherSubsidy.SELLER_AND_TOKOPEDIA, VoucherVps.ALL)
            searchVoucherByKeyword(
                isActiveVoucher = false,
                keyword = "test",
                sourceRequestParams = sourceRequestParams,
                targetBuyer = null
            )

            coVerify {
                getVoucherListUseCase.executeOnBackground()
            }
            assert(voucherList.value is Fail)
        }
    }

}