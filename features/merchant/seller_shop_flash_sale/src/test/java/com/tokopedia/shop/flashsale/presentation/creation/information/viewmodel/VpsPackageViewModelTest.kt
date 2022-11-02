package com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.flashsale.common.extension.advanceDayBy
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import io.mockk.MockKAnnotations
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class VpsPackageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy { VpsPackageViewModel() }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    //region markAsSelected
    @Test
    fun `When found matching vps packagedId, isSelected should be true`() {
        //Given
        val selectedVpsPackageId: Long = 101
        val firstVpsPackage = buildVpsPackageUiModel().copy(packageId = 101)
        val secondVpsPackage = buildVpsPackageUiModel().copy(packageId = 102)

        val vpsPackages = listOf(firstVpsPackage, secondVpsPackage)

        val expected = listOf(
            firstVpsPackage.copy(isSelected = true),
            secondVpsPackage.copy(isSelected = false)
        )

        //When
        val actual = viewModel.markAsSelected(selectedVpsPackageId, vpsPackages)

        //Then
        assertEquals(expected, actual)
    }
    //endregion

    //region findSelectedVpsPackage
    @Test
    fun `When found matching vps packagedId, should return the vps package`() {
        //Given
        val firstVpsPackage = buildVpsPackageUiModel().copy(packageId = 101, isSelected = true)
        val secondVpsPackage = buildVpsPackageUiModel().copy(packageId = 102, isSelected = false)

        val vpsPackages = listOf(firstVpsPackage, secondVpsPackage)

        //When
        val actual = viewModel.findSelectedVpsPackage(vpsPackages)

        //Then
        assertEquals(firstVpsPackage, actual)
    }

    @Test
    fun `When found no matching vps packagedId, should return null`() {
        //Given
        val firstVpsPackage = buildVpsPackageUiModel().copy(packageId = 101, isSelected = false)
        val secondVpsPackage = buildVpsPackageUiModel().copy(packageId = 102, isSelected = false)

        val vpsPackages = listOf(firstVpsPackage, secondVpsPackage)

        //When
        val actual = viewModel.findSelectedVpsPackage(vpsPackages)

        //Then
        assertEquals(null, actual)
    }
    //endregion


    //region Helper function
    private fun buildVpsPackageUiModel(): VpsPackageUiModel {
        return VpsPackageUiModel(
            packageId = 101,
            remainingQuota = 5,
            currentQuota = 5,
            originalQuota = 10,
            packageEndTime = Date().advanceDayBy(days = 7),
            packageName = "Paket VPS September",
            packageStartTime = Date(),
            isSelected = true,
            disabled = false,
            isShopTierBenefit = false
        )
    }
    //endregion

}
