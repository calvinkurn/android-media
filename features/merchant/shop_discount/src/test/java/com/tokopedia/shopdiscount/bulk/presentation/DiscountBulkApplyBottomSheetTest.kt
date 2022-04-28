package com.tokopedia.shopdiscount.bulk.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class DiscountBulkApplyBottomSheetTest {


    @RelaxedMockK
    lateinit var getSlashPriceBenefitUseCase : GetSlashPriceBenefitUseCase

    @RelaxedMockK
    lateinit var startDateObserver: Observer<in Date>

    @RelaxedMockK
    lateinit var endDateObserver: Observer<in Date>

    @RelaxedMockK
    lateinit var areInputValidObserver: Observer<in DiscountBulkApplyViewModel.ValidationState>

    @RelaxedMockK
    lateinit var discountTypeObserver: Observer<in DiscountType>

    @RelaxedMockK
    lateinit var benefitObserver: Observer<in Result<GetSlashPriceBenefitResponse>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        DiscountBulkApplyViewModel(
            CoroutineTestDispatchersProvider,
            getSlashPriceBenefitUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.startDate.observeForever(startDateObserver)
        viewModel.endDate.observeForever(endDateObserver)
        viewModel.areInputValid.observeForever(areInputValidObserver)
        viewModel.discountType.observeForever(discountTypeObserver)
        viewModel.benefit.observeForever(benefitObserver)
    }

    @After
    fun tearDown() {
        viewModel.startDate.removeObserver(startDateObserver)
        viewModel.endDate.removeObserver(endDateObserver)
        viewModel.areInputValid.removeObserver(areInputValidObserver)
        viewModel.discountType.removeObserver(discountTypeObserver)
        viewModel.benefit.removeObserver(benefitObserver)
    }



    @Test
    fun `When get shop benefit success, observer should receive success result`() =
        runBlocking {
            //Given
            val response = GetSlashPriceBenefitResponse()

            coEvery { getSlashPriceBenefitUseCase.executeOnBackground() } returns response

            //When
            viewModel.getSlashPriceBenefit()

            //Then
            coVerify { benefitObserver.onChanged(Success(response)) }
        }

    @Test
    fun `When  get shop benefit error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            coEvery { getSlashPriceBenefitUseCase.executeOnBackground() } throws error

            //When
            viewModel.getSlashPriceBenefit()

            //Then
            coVerify { benefitObserver.onChanged(Fail(error)) }
        }


    @Test
    fun `When discount type is rupiah and discount amount is invalid, should return invalid discount amount`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.RUPIAH)
        viewModel.onDiscountAmountChanged(0)

        //When

        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.InvalidDiscountAmount) }
    }

    @Test
    fun `When discount type is rupiah and discount amount is valid, should return valid discount amount`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.RUPIAH)
        viewModel.onDiscountAmountChanged(2000)

        //When

        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.Valid) }
    }

    @Test
    fun `When discount type is percentage and discount percentage is zero, should return invalid discount percentage`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.PERCENTAGE)
        viewModel.onDiscountAmountChanged(0)

        //When

        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.InvalidDiscountPercentage) }
    }


    @Test
    fun `When discount type is percentage and discount amount is not in valid range, should return invalid discount percentage`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.PERCENTAGE)
        viewModel.onDiscountAmountChanged(100)

        //When

        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.InvalidDiscountPercentage) }
    }

    @Test
    fun `When discount type is percentage and discount amount is in valid range, should return valid`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.PERCENTAGE)
        viewModel.onDiscountAmountChanged(50)

        //When

        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.Valid) }
    }

    @Test
    fun `When max purchase quantity is bigger than max allowed quantity, should return invalid max purchase`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.RUPIAH)
        viewModel.onDiscountAmountChanged(10_000)
        viewModel.onMaxPurchaseQuantityChanged(100_000)

        //When
        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.InvalidMaxPurchase) }
    }

    @Test
    fun `When max purchase quantity is on allowed quantity range, should return valid`() {
        //Given
        viewModel.onDiscountTypeChanged(DiscountType.RUPIAH)
        viewModel.onDiscountAmountChanged(10_000)
        viewModel.onMaxPurchaseQuantityChanged(500)

        //When
        viewModel.validateInput()

        //Then

        coVerify { areInputValidObserver.onChanged(DiscountBulkApplyViewModel.ValidationState.Valid) }
    }

    @Test
    fun `When get default start date, should return current date and advanced it by ten minutes`() {
        val now = GregorianCalendar(2022, 0, 1, 0,0,0)
        val startDate = GregorianCalendar(2022, 0, 1, 0,10,0)
        val endDate = GregorianCalendar(2023, 0, 1, 0,10,0)

        viewModel.onOneYearPeriodSelected(now)

        assertEquals(startDate.time, viewModel.getSelectedStartDate())
        assertEquals(endDate.time, viewModel.getSelectedEndDate())

        coVerify { startDateObserver.onChanged(startDate.time) }
        coVerify { endDateObserver.onChanged(endDate.time) }
    }

}