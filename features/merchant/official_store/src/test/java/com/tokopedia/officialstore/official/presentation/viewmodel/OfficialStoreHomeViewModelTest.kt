package com.tokopedia.officialstore.official.presentation.viewmodel

import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.*
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.tokopedia.usecase.coroutines.Success

class OfficialStoreHomeViewModelTest: OfficialStoreHomeViewModelTestFixture() {

    @Test
    fun given_get_data_success__when_load_first_data__should_set_success_value() {
        runBlocking {
            val category = Category()
            val osBanners = OfficialStoreBanners()
            val osBenefits = OfficialStoreBenefits()
            val osFeatured = OfficialStoreFeaturedShop()
            val osDynamicChannel = DynamicChannel()

            onGetOfficialStoreBanners_thenReturn(osBanners)
            onGetOfficialStoreBenefits_thenReturn(osBenefits)
            onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)

            viewModel.loadFirstData(category)

            val expectedOSBanners = Success(osBanners)
            val expectedOSBenefits = Success(osBenefits)
            val expectedOSFeaturedShop = Success(osFeatured)
            val expectedOSDynamicChannel = Success(osDynamicChannel)

            verifyOfficialStoreBannersEquals(expectedOSBanners)
            verifyOfficialStoreBenefitsEquals(expectedOSBenefits)
            verifyOfficialStoreFeaturedShopEquals(expectedOSFeaturedShop)
            verifyOfficialStoreDynamicChannelEquals(expectedOSDynamicChannel)
        }
    }

    @Test
    fun given_get_data_error__when_load_first_data__should_set_error_value() {
        runBlocking {
            val category = Category()
            val error = NullPointerException()

            onGetOfficialStoreBanners_thenReturn(error)
            onGetOfficialStoreBenefits_thenReturn(error)
            onGetOfficialStoreFeaturedShop_thenReturn(error)

            viewModel.loadFirstData(category)

            val expectedError = Fail(NullPointerException())

            verifyOfficialStoreBannersError(expectedError)
            verifyOfficialStoreBenefitsError(expectedError)
            verifyOfficialStoreFeaturedShopError(expectedError)
            verifyOfficialStoreDynamicChannelError(expectedError)
        }
    }
}