package com.tokopedia.attachvoucher.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.data.FilterParam
import com.tokopedia.attachvoucher.data.GetVoucherParam
import com.tokopedia.attachvoucher.data.voucherv2.Data
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.data.voucherv2.MerchantPromotionGetMVList
import com.tokopedia.attachvoucher.data.voucherv2.Voucher
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.stubRepository
import com.tokopedia.attachvoucher.stubRepositoryAsThrow
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetVoucherUseCaseTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: GraphqlRepository
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val mapper = VoucherMapper()

    private lateinit var useCase: GetVoucherUseCase
    private var testPage = 1
    private var testThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = GetVoucherUseCase(repository, dispatchers, mapper)
    }

    @Test
    fun success_get_voucher_data_with_full_params() {
        //Given
        val testVoucherList = arrayListOf(
            Voucher(
                voucherName = "test123",
                voucherCode = "123123",
                remainingQuota = 1,
                voucherType = GetVoucherUseCase.MVFilter.VoucherType.paramDiscount,
                voucherFinishTime = "2023-02-12T023:30:00") //Not expired date
        )
        val expectedResponse = GetMerchantPromotionGetMVListResponse(
            MerchantPromotionGetMVList(Data(vouchers = testVoucherList))
        )
        val expectedResult = mapper.map(expectedResponse)
        repository.stubRepository(expectedResponse, onError = mapOf())

        //Then
        runBlocking {
            val voucherParam = GetVoucherParam(
                GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing,
                GetVoucherUseCase.MVFilter.PerPage.default,
                testPage).also {
                    it.voucher_type = GetVoucherUseCase.MVFilter.VoucherType.paramDiscount
            }
            val params = FilterParam(voucherParam)
            val result = useCase(params)
            Assert.assertEquals(
                expectedResult.first().voucherName,
                result.first().voucherName)
        }
    }

    @Test
    fun success_get_voucher_data_without_full_params() {
        //Given
        val testVoucherList = arrayListOf(
            Voucher(
                voucherName = "test123",
                voucherCode = "123123",
                remainingQuota = 1,
                voucherFinishTime = "2023-02-12T023:30:00") //Not expired date
        )
        val expectedResponse = GetMerchantPromotionGetMVListResponse(
            MerchantPromotionGetMVList(Data(vouchers = testVoucherList))
        )
        val expectedResult = mapper.map(expectedResponse)
        repository.stubRepository(expectedResponse, onError = mapOf())

        //Then
        runBlocking {
            val voucherParam = GetVoucherParam(
                GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing,
                GetVoucherUseCase.MVFilter.PerPage.default,
                testPage).also {
                it.voucher_type = GetVoucherUseCase.MVFilter.VoucherType.paramDiscount
            }
            val params = FilterParam(voucherParam)
            val result = useCase(params)
            Assert.assertEquals(
                expectedResult.first().voucherName,
                result.first().voucherName)
        }
    }

    @Test
    fun success_get_voucher_data_without_params() {
        //Given
        val testVoucherList = arrayListOf(
            Voucher(
                voucherName = "test123",
                voucherCode = "123123",
                remainingQuota = 0,
                voucherFinishTime = "2022-01-12T023:30:00") //Expired Date
        )
        val expectedResponse = GetMerchantPromotionGetMVListResponse(
            MerchantPromotionGetMVList(Data(vouchers = testVoucherList))
        )
        val expectedResult = mapper.map(expectedResponse)
        repository.stubRepository(expectedResponse, onError = mapOf())

        //Then
        runBlocking {
            val params = FilterParam()
            val result = useCase(params)
            Assert.assertEquals(
                expectedResult.first().voucherName,
                result.first().voucherName)
        }
    }

    @Test
    fun fail_to_get_voucher_data_with_full_params() {
        //Given
        repository.stubRepositoryAsThrow(testThrowable)

        //Then
        runBlocking {
            assertThrows<Throwable> {
                val voucherParam = GetVoucherParam(
                    GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing,
                    GetVoucherUseCase.MVFilter.PerPage.default,
                    testPage).also {
                    it.voucher_type = GetVoucherUseCase.MVFilter.VoucherType.paramDiscount
                }
                val params = FilterParam(voucherParam)
                useCase(params)
            }
        }
    }

    @Test
    fun fail_to_get_voucher_data_without_full_params() {
        //Given
        repository.stubRepositoryAsThrow(testThrowable)

        //Then
        runBlocking {
            assertThrows<Throwable> {
                val voucherParam = GetVoucherParam(
                    GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing,
                    GetVoucherUseCase.MVFilter.PerPage.default,
                    testPage).also {
                    it.voucher_type = GetVoucherUseCase.MVFilter.VoucherType.paramDiscount
                }
                val params = FilterParam(voucherParam)
                useCase(params)
            }
        }
    }

    @Test
    fun fail_to_get_voucher_data_without_params() {
        //Given
        repository.stubRepositoryAsThrow(testThrowable)

        //Then
        runBlocking {
            assertThrows<Throwable> {
                val voucherParam = GetVoucherParam(
                    GetVoucherUseCase.MVFilter.VoucherStatus.paramOnGoing,
                    GetVoucherUseCase.MVFilter.PerPage.default,
                    testPage).also {
                    it.voucher_type = GetVoucherUseCase.MVFilter.VoucherType.paramDiscount
                }
                val params = FilterParam(voucherParam)
                useCase(params)
            }
        }
    }

}