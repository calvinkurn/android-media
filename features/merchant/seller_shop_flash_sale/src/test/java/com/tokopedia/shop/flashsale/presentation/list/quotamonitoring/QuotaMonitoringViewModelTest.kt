package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignPackageListUseCase
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.VpsPackageDummyDataHelper.generateDummyVpsPackageAvailability
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.VpsPackageDummyDataHelper.generateDummyVpsPackages
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.VpsPackageDummyDataHelper.generateDummyVpsPackagesWithDynamicEndTime
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuotaMonitoringViewModelTest {

    @RelaxedMockK
    lateinit var getSellerCampaignPackageListUseCase: GetSellerCampaignPackageListUseCase

    @RelaxedMockK
    lateinit var vpsPackagesObserver: Observer<in Result<List<VpsPackage>>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: QuotaMonitoringViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = QuotaMonitoringViewModel(
            CoroutineTestDispatchersProvider,
            getSellerCampaignPackageListUseCase
        )

        with(viewModel) {
            vpsPackages.observeForever(vpsPackagesObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            vpsPackages.removeObserver(vpsPackagesObserver)
        }
    }

    @Test
    fun `when getVpsPackages success, observer will successfully receive the data`() {
        with(viewModel) {
            coEvery {
                getSellerCampaignPackageListUseCase.execute()
            } returns generateDummyVpsPackages()

            getVpsPackages()

            val expected = Success(generateDummyVpsPackages())
            val actual = vpsPackages.getOrAwaitValue()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVpsPackages return error, observer will receive error result`() {
        with(viewModel) {
            val dummyThrowable = MessageErrorException("Error")

            coEvery {
                getSellerCampaignPackageListUseCase.execute()
            } throws dummyThrowable

            getVpsPackages()

            val expected = Fail(dummyThrowable)
            val actual = vpsPackages.getOrAwaitValue()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getPackageAvailability is called, will successfully return package availability data accordingly`() {
        with(viewModel) {
            val dummyPackages = generateDummyVpsPackagesWithDynamicEndTime()

            val expected = generateDummyVpsPackageAvailability()
            val actual = getPackageAvailability(dummyPackages)

            assertEquals(expected, actual)
        }
    }
}
