package com.tokopedia.editshipping.ui.shopeditaddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.editshipping.domain.mapper.AutoCompleteMapper
import com.tokopedia.editshipping.domain.model.shopeditaddress.DistrictLocation
import com.tokopedia.editshipping.domain.model.shopeditaddress.ShopEditAddressState
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictDetailsResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.data.response.KeroDistrictRecommendation
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocCheckCouriers
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocCheckCouriersNewLocResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocUpdateWarehouse
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocationUpdateWarehouseResponse
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopEditAddressViewModelTest  {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val keroRepo: KeroRepository = mockk(relaxed = true)
    private val shopRepo: ShopLocationRepository = mockk(relaxed = true)
    private val autoCompleteMapper = AutoCompleteMapper()


    private val autoCompleteListObserver: Observer<Result<Place>> = mockk(relaxed = true)
    private val districtLocationObserver: Observer<Result<DistrictLocation>> = mockk(relaxed = true)
    private val zipCodeListObserver: Observer<Result<KeroDistrictRecommendation>> = mockk(relaxed = true)
    private val districtGeocodeObserver: Observer<Result<KeroMapsAutofill>> = mockk(relaxed = true)
    private val checkCouriersObserver: Observer<ShopEditAddressState<ShopLocCheckCouriers>> = mockk(relaxed = true)
    private val saveEditShopObserver: Observer<ShopEditAddressState<ShopLocUpdateWarehouse>> = mockk(relaxed = true)

    private lateinit var shopEditAddressViewModel: ShopEditAddressViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        shopEditAddressViewModel = ShopEditAddressViewModel(keroRepo, shopRepo, autoCompleteMapper)
        shopEditAddressViewModel.autoCompleteList.observeForever(autoCompleteListObserver)
        shopEditAddressViewModel.districtLocation.observeForever(districtLocationObserver)
        shopEditAddressViewModel.zipCodeList.observeForever(zipCodeListObserver)
        shopEditAddressViewModel.districtGeocode.observeForever(districtGeocodeObserver)
        shopEditAddressViewModel.checkCouriers.observeForever(checkCouriersObserver)
        shopEditAddressViewModel.saveEditShop.observeForever(saveEditShopObserver)
    }

    @Test
    fun `Get placeId from warehouse district success`() {
        coEvery { keroRepo.getAutoComplete(any()) } returns AutoCompleteResponse()
        shopEditAddressViewModel.getAutoCompleteList("Jakarta")
        verify { autoCompleteListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get placeId from warehouse district failed`() {
        coEvery { keroRepo.getAutoComplete(any()) } throws defaultThrowable
        shopEditAddressViewModel.getAutoCompleteList("Jakarta")
        verify { autoCompleteListObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get latlong from placeId success`() {
        coEvery { keroRepo.getDistrict(any()) } returns GetDistrictResponse()
        shopEditAddressViewModel.getDistrictLocation("123")
        verify { districtLocationObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get latlong from placeId failed`() {
        coEvery { keroRepo.getDistrict(any()) } throws defaultThrowable
        shopEditAddressViewModel.getDistrictLocation("123")
        verify { districtLocationObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get ZipCode list success`() {
        coEvery { keroRepo.getZipCode(any()) } returns GetDistrictDetailsResponse()
        shopEditAddressViewModel.getZipCode("123")
        verify { zipCodeListObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get ZipCpde list failed`() {
        coEvery { keroRepo.getZipCode(any()) } throws defaultThrowable
        shopEditAddressViewModel.getZipCode("123")
        verify { zipCodeListObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Get formatted address from latlon Success`() {
        coEvery{ keroRepo.getDistrictGeocode(any()) } returns AutoFillResponse()
        shopEditAddressViewModel.getDistrictGeocode("123,123")
        verify { districtGeocodeObserver.onChanged(match { it is Success }) }
    }

    @Test
    fun `Get formatted address from latlon failed`() {
        coEvery { keroRepo.getDistrictGeocode(any()) } throws defaultThrowable
        shopEditAddressViewModel.getDistrictGeocode("123,123")
        verify { districtGeocodeObserver.onChanged(match { it is Fail }) }
    }

    @Test
    fun `Check couriers availability success`() {
        coEvery{ shopRepo.shopCheckCouriersNewLoc(any(), any()) } returns  ShopLocCheckCouriersNewLocResponse()
        shopEditAddressViewModel.checkCouriersAvailability(998, 123)
        verify { checkCouriersObserver.onChanged(match { it is ShopEditAddressState.Success }) }
    }

    @Test
    fun `Check couriers availability failed`() {
        coEvery { shopRepo.shopCheckCouriersNewLoc(any(), any()) } throws defaultThrowable
        shopEditAddressViewModel.checkCouriersAvailability(998, 123)
        verify { checkCouriersObserver.onChanged(match { it is ShopEditAddressState.Fail }) }
    }

    @Test
    fun `Save shop edit address success`() {
        coEvery {
            shopRepo.saveEditShopLocation(any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns ShopLocationUpdateWarehouseResponse()
        shopEditAddressViewModel.saveEditShopLocation(12, 998, "warehouse", 222, "1231,4131", "email@email", "jakarta utara", "1123", "123")
        verify { saveEditShopObserver.onChanged(match { it is ShopEditAddressState.Success }) }
    }

    @Test
    fun `Save shop edit address failed`() {
        coEvery {
            shopRepo.saveEditShopLocation(any(), any(), any(), any(), any(), any(), any(), any(), any())
        } throws defaultThrowable
        shopEditAddressViewModel.saveEditShopLocation(12, 998, "warehouse", 222, "1231,4131", "email@email", "jakarta utara", "1123", "123")
        verify { saveEditShopObserver.onChanged(match { it is ShopEditAddressState.Fail }) }
    }

}