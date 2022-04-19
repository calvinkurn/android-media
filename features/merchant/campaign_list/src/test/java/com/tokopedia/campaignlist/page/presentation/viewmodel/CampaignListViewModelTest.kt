package com.tokopedia.campaignlist.page.presentation.viewmodel

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.campaignlist.common.data.model.response.*
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase.Companion.NPL_CAMPAIGN_TYPE
import com.tokopedia.campaignlist.common.usecase.GetMerchantBannerUseCase
import com.tokopedia.campaignlist.common.usecase.GetSellerMetaDataUseCase
import com.tokopedia.campaignlist.common.util.ResourceProvider
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

class CampaignListViewModelTest {

    @RelaxedMockK
    lateinit var getCampaignListUseCase: GetCampaignListUseCase

    @RelaxedMockK
    lateinit var getMerchantBannerUseCase: GetMerchantBannerUseCase

    @RelaxedMockK
    lateinit var getSellerMetaDataUseCase: GetSellerMetaDataUseCase

    @RelaxedMockK
    lateinit var getCampaignListObserver: Observer<in Result<GetCampaignListV2Response>>

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val shop = ShopData(name = "Compass")
    private val campaign = Campaign(name = "Flash Deal")
    private val merchantBannerData = GetMerchantCampaignBannerGeneratorData()
    
    companion object {
        private const val EMPTY_STRING = ""
    }
    
    private val viewModel by lazy {
        CampaignListViewModel(
            resourceProvider,
            CoroutineTestDispatchersProvider,
            getCampaignListUseCase,
            getMerchantBannerUseCase,
            getSellerMetaDataUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.getCampaignListResult.observeForever(getCampaignListObserver)
    }

    @Test
    fun `When get campaign list success, should emit success to observer`() = runBlocking {
        coEvery { getCampaignListUseCase.executeOnBackground() } returns GetCampaignListV2Response()

        viewModel.getCampaignList()

        assert(viewModel.getCampaignListResult.value is Success)
    }

    @Test
    fun `When get campaign list error, should emit error to observer`() = runBlocking {
        val error = MessageErrorException("Some error message")

        coEvery { getCampaignListUseCase.executeOnBackground() } throws error

        viewModel.getCampaignList()

        assert(viewModel.getCampaignListResult.value is Fail)
    }

    @Test
    fun `When get seller banner success, should emit success to observer`() = runBlocking {
        coEvery { getMerchantBannerUseCase.executeOnBackground() } returns GetMerchantCampaignBannerGeneratorDataResponse()

        viewModel.getSellerBanner(anyInt())

        assert(viewModel.getMerchantBannerResult.value is Success)
    }

    @Test
    fun `When get seller banner error, should emit error to observer`() = runBlocking {
        val error = MessageErrorException("Some error message")

        coEvery { getMerchantBannerUseCase.executeOnBackground() } throws error

        viewModel.getSellerBanner(anyInt())

        assert(viewModel.getMerchantBannerResult.value is Fail)
    }

    @Test
    fun `When get seller metadata success, should emit success to observer`() = runBlocking {
        coEvery { getSellerMetaDataUseCase.executeOnBackground() } returns GetSellerCampaignSellerAppMetaResponse()

        viewModel.getSellerMetaData()

        assert(viewModel.getSellerMetaDataResult.value is Success)
    }

    @Test
    fun `When get seller metadata error, should emit error to observer`() = runBlocking {
        val error = MessageErrorException("Some error message")

        coEvery { getSellerMetaDataUseCase.executeOnBackground() } throws error

        viewModel.getSellerMetaData()

        assert(viewModel.getSellerMetaDataResult.value is Fail)
    }

    @Test
    fun `When set campaign name, should set correct campaign name`() {
        val campaignName = "Flash Sale 12.12"
        val expected = "Flash Sale 12.12"

        viewModel.setCampaignName(campaignName)

        val actual = viewModel.getCampaignName()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set campaign type id, should set correct campaign type`() {
        val campaignType = 20
        val expected = 20

        viewModel.setCampaignTypeId(campaignType)

        val actual = viewModel.getCampaignTypeId()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set campaign status, should set correct campaign status`() {
        val campaignType = listOf(5,6,7)
        val expected = listOf(5,6,7)

        viewModel.setCampaignStatusId(campaignType)

        val actual = viewModel.getCampaignStatusId()

        assertEquals(expected, actual)
        assertEquals(expected.size, actual.size)
    }


    @Test
    fun `When set active campaign, should set correct active campaign`() = runBlocking {
        val activeCampaign = ActiveCampaign(campaignName = "Flash Sale 12.12")
        val expected = ActiveCampaign(campaignName = "Flash Sale 12.12")

        viewModel.setSelectedActiveCampaign(activeCampaign)

        val actual = viewModel.getSelectedActiveCampaign()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set default campaign type, when we get default campaign type should return only selected campaign type`() {
        val selectedCampaignType = listOf(
            CampaignTypeSelection(campaignTypeName = "Flash Sale 11.11", isSelected = true),
            CampaignTypeSelection(campaignTypeName = "Flash Sale 12.12", isSelected = false)
        )

        val expected = CampaignTypeSelection(campaignTypeName = "Flash Sale 11.11", isSelected = true)

        viewModel.setDefaultCampaignTypeSelection(selectedCampaignType)

        val actual = viewModel.getSelectedCampaignTypeSelection()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set default campaign type and there are no selected campaign type, should return null`() {
        val selectedCampaignType = listOf(
            CampaignTypeSelection(campaignTypeName = "Flash Sale 11.11", isSelected = false),
            CampaignTypeSelection(campaignTypeName = "Flash Sale 12.12", isSelected = false)
        )

        val expected = null

        viewModel.setDefaultCampaignTypeSelection(selectedCampaignType)

        val actual = viewModel.getSelectedCampaignTypeSelection()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set merchant banner, should set correct merchant banner`() {
        val merchantBanner = GetMerchantCampaignBannerGeneratorData()
        val expected = GetMerchantCampaignBannerGeneratorData()

        viewModel.setMerchantBannerData(merchantBanner)

        val actual = viewModel.getMerchantBannerData()

        assertEquals(expected, actual)
    }

    @Test
    fun `When map campaign list data, should correctly map to active campaign`() {
        val campaigns = listOf(
            CampaignListV2(
                campaignName = "Flash Sale 11.11",
                campaignBanner = emptyList(),
                campaignDynamicRule = CampaignDynamicRule(),
                sellerCampaignInfo = SellerCampaignInfo(AcceptedProduct = 20),
                startDate = "26-11-2021 11:25",
                endDate = "20-12-2021 22:00"
            ),
            CampaignListV2(
                campaignName = "Flash Sale 12.12",
                campaignBanner = emptyList(),
                campaignDynamicRule = CampaignDynamicRule(),
                sellerCampaignInfo = SellerCampaignInfo(AcceptedProduct = 20),
                startDate = "26-11-2021 11:30",
                endDate = "30-12-2021 23:00",
            )
        )
        val expected = listOf(
            ActiveCampaign(
                campaignName = "Flash Sale 11.11",
                startDate = "26-11-2021",
                endDate = "20-12-2021",
                startTime = "11:25",
                endTime = "22:00",
                productQty = "20"
            ),
            ActiveCampaign(
                campaignName = "Flash Sale 12.12",
                startDate = "26-11-2021",
                endDate = "30-12-2021",
                startTime = "11:30",
                endTime = "23:00",
                productQty = "20"
            )
        )

        val actual = viewModel.mapCampaignListDataToActiveCampaignList(campaigns)

        assertEquals(expected, actual)
    }

    @Test
    fun `When map campaign type data, should correctly map to selected campaign type`() {
        val unknownCampaignTypeId = "70"
        val specialReleaseCampaignTypeId = NPL_CAMPAIGN_TYPE.toString()

        val campaigns = listOf(
            CampaignTypeData(
                campaignTypeId = unknownCampaignTypeId,
                campaignTypeName = "Rilisan Khusus",
            ),
            CampaignTypeData(
                campaignTypeId = specialReleaseCampaignTypeId,
                campaignTypeName = "Rilisan Special",
            )
        )
        val expected = listOf(
            CampaignTypeSelection(
                campaignTypeId = unknownCampaignTypeId,
                campaignTypeName = "Rilisan Khusus",
                isSelected = false
            ),
            CampaignTypeSelection(
                campaignTypeId = specialReleaseCampaignTypeId,
                campaignTypeName = "Rilisan Special",
                isSelected = true
            )
        )

        val actual = viewModel.mapCampaignTypeDataToCampaignTypeSelections(campaigns)

        assertEquals(expected, actual)
    }

    @Test
    fun `When map campaign status, should correctly map to campaign status ui model`() {
        val campaigns = listOf(
            CampaignStatus(statusText = "Berlangsung"),
            CampaignStatus(statusText = "Mendatang")
        )
        val expected = listOf(
            CampaignStatusSelection(
                statusText = "Berlangsung",
                isSelected = false
            ),
            CampaignStatusSelection(
                statusText = "Mendatang",
                isSelected = false
            )
        )

        val actual = viewModel.mapCampaignStatusToCampaignStatusSelections(campaigns)

        assertEquals(expected, actual)
    }

    @Test
    fun `When get bottomsheet title success, should return correct title`() {
        val shopName = "Compass Official"
        val expected = "Rilisan Spesial dari $shopName"

        every { resourceProvider.getShareTitle() } returns "Rilisan Spesial dari %s"

        val actual = viewModel.getShareBottomSheetTitle(shopName)

        assertEquals(expected, actual)
    }

    @Test
    fun `When get bottomsheet title error, should return empty title`() {
        val shopName = "Compass Official"
        val expected = EMPTY_STRING

        every { resourceProvider.getShareTitle() } returns null

        val actual = viewModel.getShareBottomSheetTitle(shopName)

        assertEquals(expected, actual)
    }


    @Test
    fun `When get share description wording while campaign status is not ongoing, should return correct wording`() {
        val campaignStatusUpcoming = "6"
        val shareUri = "https://api.whatsapp.com?phone=082210000000"
        val campaign = Campaign(name = "Flash Deal")
        val expected =
            "Wah, ada ${campaign.name} dari ${shop.name} di @Tokopedia, lho. Mulai 03 Jan 2022 pukul 11:20 WIB! Cek sekarang, yuk! $shareUri"
        val merchantBanner = GetMerchantCampaignBannerGeneratorData(formattedStartDate = "03 Jan 2022, 11:20 WIB")

        every { resourceProvider.getShareCampaignDescriptionWording() } returns "Wah, ada %s dari %s di @Tokopedia, lho. Mulai %s pukul %s! Cek sekarang, yuk!"

        val actual = viewModel.getShareDescriptionWording(
                shopData = shop,
                campaignData = campaign,
                merchantBannerData = merchantBanner,
                shareUri = shareUri,
                campaignStatusId = campaignStatusUpcoming
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When failed to fetch ongoing description wording, should return only share URL`() {
        val campaignStatusOngoing = "7"
        val shareUri = "https://api.whatsapp.com?phone=082210000000"
        val campaign = Campaign(name = "Flash Deal")
        val expected = " $shareUri"
        val merchantBanner = GetMerchantCampaignBannerGeneratorData(formattedStartDate = "03 Jan 2022, 11:20 WIB")

        every { resourceProvider.getShareOngoingCampaignDescriptionWording() } returns null

        val actual = viewModel.getShareDescriptionWording(
            shopData = shop,
            campaignData = campaign,
            merchantBannerData = merchantBanner,
            shareUri = shareUri,
            campaignStatusId = campaignStatusOngoing
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When failed to fetch upcoming description wording, should return only share URL`() {
        val campaignStatusUpcoming = "6"
        val shareUri = "https://api.whatsapp.com?phone=082210000000"
        val campaign = Campaign(name = "Flash Deal")
        val expected = " $shareUri"
        val merchantBanner = GetMerchantCampaignBannerGeneratorData(formattedStartDate = "03 Jan 2022, 11:20 WIB")

        every { resourceProvider.getShareCampaignDescriptionWording() } returns null

        val actual = viewModel.getShareDescriptionWording(
            shopData = shop,
            campaignData = campaign,
            merchantBannerData = merchantBanner,
            shareUri = shareUri,
            campaignStatusId = campaignStatusUpcoming
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When get wording ongoing campaign return null, should return share uri only`() {
        val campaignStatusOngoing = "7"
        val shareUri = "https://api.whatsapp.com?phone=082210000000"
        val campaign = Campaign(name = "Flash Deal", startDate = "26-11-2021 11:30")
        val expected = " https://api.whatsapp.com?phone=082210000000"

        every { resourceProvider.getShareCampaignDescriptionWording() } returns null

        val actual = viewModel.getShareDescriptionWording(
                shopData = shop,
                campaignData = campaign,
                merchantBannerData = merchantBannerData,
                shareUri = shareUri,
                campaignStatusId = campaignStatusOngoing
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When get share description wording while campaign status is ongoing, should return correct wording`() {
        val campaignStatusOngoing = "7"
        val shareUri = "https://api.whatsapp.com?phone=082210000000"
        val campaign = Campaign(name = "Flash Deal")
        val expected =
            "Kejar eksklusif launching ${campaign.name} dari ${shop.name} hanya di @Tokopedia. Hanya sampai 03 Jan 2022 pukul 14:00 WIB! $shareUri"
        val merchantBanner = GetMerchantCampaignBannerGeneratorData(formattedEndDate = "03 Jan 2022, 14:00 WIB")

        every { resourceProvider.getShareOngoingCampaignDescriptionWording() } returns "Kejar eksklusif launching %s dari %s hanya di @Tokopedia. Hanya sampai %s pukul %s!"

        val actual = viewModel.getShareDescriptionWording(
                shopData = shop,
                campaignData = campaign,
                merchantBannerData = merchantBanner,
                shareUri = shareUri,
                campaignStatusId = campaignStatusOngoing
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When get wording non-ongoing campaign return null, should return share uri only`() {
        val campaignStatusUpcoming = "6"
        val shareUri = "https://api.whatsapp.com?phone=082210000000"
        val campaign = Campaign(name = "Flash Deal", startDate = "26-11-2021 11:30")
        val expected = " https://api.whatsapp.com?phone=082210000000"

        every { resourceProvider.getShareOngoingCampaignDescriptionWording() } returns null

        val actual = viewModel.getShareDescriptionWording(
                shopData = shop,
                campaignData = campaign,
                merchantBannerData = merchantBannerData,
                shareUri = shareUri,
                campaignStatusId = campaignStatusUpcoming
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When get title wording for linker data return success, should produce correct values`() {
        val campaignStatusOngoing = "7"
        val shareModel = ShareModel.Whatsapp()

        every { resourceProvider.getShareOgTitle() } returns "%s dari %s"

        val expected = LinkerData().apply {
            name = "Flash Deal dari Compass"
            ogTitle  = "Flash Deal dari Compass"
        }


        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusOngoing
        )

        assertEquals(expected.name, actual.linkerData.name)
        assertEquals(expected.ogTitle, actual.linkerData.ogTitle)
    }

    @Test
    fun `When get title wording for linker data return null, should produce empty wording`() {
        val campaignStatusOngoing = "7"
        val shareModel = ShareModel.Whatsapp()

        every { resourceProvider.getShareOgTitle() } returns null

        val expected = LinkerData().apply {
            name = EMPTY_STRING
            ogTitle  = EMPTY_STRING
        }


        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusOngoing
        )

        assertEquals(expected.name, actual.linkerData.name)
        assertEquals(expected.ogTitle, actual.linkerData.ogTitle)
    }

    @Test
    fun `When get ongoing campaign description for linker data return null, should produce empty string`() {
        val campaignStatusOngoing = "7"
        val shareModel = ShareModel.Whatsapp()

        val expected = LinkerData().apply {
            ogDescription  = EMPTY_STRING
        }

        every { resourceProvider.getShareOngoingDescription() } returns null

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusOngoing
        )

        assertEquals(expected.ogDescription, actual.linkerData.ogDescription)
    }

    @Test
    fun `When get ongoing campaign description for linker data return success, should produce correct values`() {
        val campaignStatusOngoing = "7"
        val shareModel = ShareModel.Whatsapp()
        val stubbedDescription = "Kejar eksklusif launching"

        val expected = LinkerData().apply {
            ogDescription  = stubbedDescription
        }

        every { resourceProvider.getShareOngoingDescription() } returns stubbedDescription

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusOngoing
        )

        assertEquals(expected.ogDescription, actual.linkerData.ogDescription)
    }

    @Test
    fun `When get linker data while campaign status is empty, should produce empty string`() {
        val campaignStatus= EMPTY_STRING
        val shareModel = ShareModel.Whatsapp()

        val expected = LinkerData().apply {
            description = EMPTY_STRING
            ogDescription  = EMPTY_STRING
        }

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatus
        )

        assertEquals(expected.description, actual.linkerData.description)
        assertEquals(expected.ogDescription, actual.linkerData.ogDescription)
    }

    @Test
    fun `When get upcoming campaign description for linker data return null, should produce empty string`() {
        val campaignStatusUpcoming = "6"
        val shareModel = ShareModel.Whatsapp()

        val expected = LinkerData().apply {
            description = EMPTY_STRING
        }

        every { resourceProvider.getShareDescription() } returns null

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusUpcoming
        )

        assertEquals(expected.description, actual.linkerData.description)
    }

    @Test
    fun `When get upcoming campaign description for linker data return success, should produce correct values`() {
        val campaignStatusUpcoming = "6"
        val shareModel = ShareModel.Whatsapp()
        val stubbedDescription = "Dapatkan produk Rilisan Spesial"

        val expected = LinkerData().apply {
            description = stubbedDescription
        }

        every { resourceProvider.getShareDescription() } returns stubbedDescription

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusUpcoming
        )

        assertEquals(expected.description, actual.linkerData.description)
    }


    @Test
    fun `When get linker data while share model image url is specified, should set it to linker data`() {
        val campaignStatusUpcoming = "6"
        val imageUrl = "https://aws.tokopedia.com/whatsapp.png"

        val shareModel = ShareModel.Whatsapp()
        shareModel.ogImgUrl = imageUrl

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusUpcoming
        )

        assertEquals(imageUrl, actual.linkerData.ogImageUrl)
    }

    @Test
    fun `When get linker data while share model image url is null, should not set it to linker data`() {
        val campaignStatusUpcoming = "6"
        val shareModel = ShareModel.Whatsapp()

        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } answers { true }

        val actual = viewModel.generateLinkerShareData(
            shop,
            campaign,
            shareModel,
            campaignStatusUpcoming
        )

        assertEquals(null, actual.linkerData.ogImageUrl)
    }

    @Test
    fun `When has overload product, should return valid overload product count`() {
        val expected = 1
        val totalProduct = 4

        val actual = viewModel.calculateOverloadProductCount(totalProduct)

        assertEquals(expected, actual)
    }

    @Test
    fun `When overload product count is negative number, should return 0 as overload product count`() {
        val expected = 0
        val totalProduct = 2

        val actual = viewModel.calculateOverloadProductCount(totalProduct)

        assertEquals(expected, actual)
    }

    @Test
    fun `When product count is not a number, should return 0 as fallback`() {
        val incorrectProductCount = "m"
        val expected = 0

        val actual = viewModel.getProductCount(incorrectProductCount)
        assertEquals(expected, actual)
    }

    @Test
    fun `When product count is a valid number, should return correct value`() {
        val productCount = "20"
        val expected = 20

        val actual = viewModel.getProductCount(productCount)
        assertEquals(expected, actual)
    }

    @After
    fun tearDown() {
        viewModel.getCampaignListResult.removeObserver(getCampaignListObserver)
    }
}