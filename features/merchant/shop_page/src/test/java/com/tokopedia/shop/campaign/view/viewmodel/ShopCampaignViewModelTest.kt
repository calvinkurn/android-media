package com.tokopedia.shop.campaign.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.shop.campaign.util.mapper.ShopPageCampaignMapper
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.data.model.WidgetIdList
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetDynamicTabUseCase
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetV2
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopCampaignViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    @RelaxedMockK
    lateinit var getShopPageLayoutV2UseCase: GetShopPageHomeLayoutV2UseCase

    @RelaxedMockK
    lateinit var getShopDynamicTabUseCase: GqlShopPageGetDynamicTabUseCase

    @RelaxedMockK
    lateinit var getPromoVoucherListUseCase: GetPromoVoucherListUseCase

    @RelaxedMockK
    lateinit var redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase

    private lateinit var viewModel: ShopCampaignViewModel

    private val mockExtParam = "fs_widget%3D23600"
    private val mockShopId = "1234"
    private val addressWidgetData: LocalCacheModel = LocalCacheModel()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopCampaignViewModel(
            testCoroutineDispatcherProvider,
            getShopPageLayoutV2UseCase,
            getPromoVoucherListUseCase,
            redeemPromoVoucherUseCase,
            getShopDynamicTabUseCase
        )
    }

    @Test
    fun `when getWidgetContentData success should return expected results`() {
        runBlocking {
            val shopHomeWidgetContentData = async {
                viewModel.shopCampaignWidgetContentData.first()
            }
            coEvery {
                getShopPageLayoutV2UseCase.executeOnBackground()
            } returns ShopLayoutWidgetV2()
            mockkObject(ShopPageCampaignMapper)
            every { ShopPageCampaignMapper.mapToListShopCampaignWidget(
                any(),
                any(),
                any(),
            ) } returns listOf(
                ShopCampaignWidgetCarouselProductUiModel(widgetId = "1")
            )
            viewModel.getWidgetContentData(
                listOf(
                    ShopPageWidgetUiModel(
                        widgetId = "1"
                    ),
                    ShopPageWidgetUiModel(
                        widgetId = "2"
                    )
                ),
                mockShopId,
                addressWidgetData
            )
            assert(shopHomeWidgetContentData.await() is Success)
            assert((shopHomeWidgetContentData.await() as? Success)?.data?.isNotEmpty() == true)
        }
    }

    @Test
    fun `when getWidgetContentData success should return null`() {
        runBlocking {
            val shopHomeWidgetContentData = async {
                viewModel.shopCampaignWidgetContentData.first()
            }
            coEvery {
                getShopPageLayoutV2UseCase.executeOnBackground()
            } returns ShopLayoutWidgetV2()

            mockkObject(ShopPageCampaignMapper)
            every { ShopPageCampaignMapper.mapToListShopCampaignWidget(
                any(),
                any(),
                any(),
            ) } returns listOf(
                ShopCampaignWidgetCarouselProductUiModel()
            )

            viewModel.getWidgetContentData(
                listOf(
                    ShopPageWidgetUiModel(
                        widgetId = "2",
                        widgetType = WidgetType.CAMPAIGN,
                        widgetName = WidgetName.BIG_CAMPAIGN_THEMATIC
                    )
                ),
                mockShopId,
                addressWidgetData
            )
            assert((shopHomeWidgetContentData.await() as? Success)?.data?.values?.first() == null)
        }
    }

    @Test
    fun `when getWidgetContentData error should return expected results`() {
        runBlocking {
            val shopHomeWidgetContentData = async {
                viewModel.shopCampaignWidgetContentData.first()
            }
            val shopHomeWidgetContentDataError = async {
                viewModel.shopCampaignHomeWidgetContentDataError.first()
            }
            coEvery {
                getShopPageLayoutV2UseCase.executeOnBackground()
            } throws Exception()
            viewModel.getWidgetContentData(
                listOf(ShopPageWidgetUiModel()),
                mockShopId,
                addressWidgetData
            )
            assert(shopHomeWidgetContentData.await() is Fail)
            assert(shopHomeWidgetContentDataError.await().isNotEmpty())
        }
    }

    @Test
    fun `when call getLatestShopHomeWidgetData success and tab data is empty, then latestShopHomeWidgetData value should be success with empty list`() {
        coEvery {
            getShopDynamicTabUseCase.executeOnBackground()
        } returns ShopPageGetDynamicTabResponse()
        viewModel.getLatestShopCampaignWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData,
            "CampaignTab"
        )
        val result = viewModel.latestShopCampaignWidgetLayoutData.value
        assert(result is Success)
        assert((result as? Success)?.data?.listWidgetLayout?.isEmpty() == true)
    }

    @Test
    fun `when call getLatestShopHomeWidgetData success and with campaign tab data exists, then latestShopHomeWidgetData value should be success with no empty list`() {
        coEvery {
            getShopDynamicTabUseCase.executeOnBackground()
        } returns ShopPageGetDynamicTabResponse(
            shopPageGetDynamicTab = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                tabData = listOf(
                    ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData(
                        name = ShopPageHeaderTabName.CAMPAIGN,
                        data = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData.Data(
                            homeLayoutData = HomeLayoutData(
                                widgetIdList = listOf(
                                    WidgetIdList(
                                        widgetId = "1"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        viewModel.getLatestShopCampaignWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData,
            "CampaignTab"
        )
        val result = viewModel.latestShopCampaignWidgetLayoutData.value
        assert(result is Success)
        assert((result as? Success)?.data?.listWidgetLayout?.isEmpty() != true)
    }

    @Test
    fun `when call getLatestShopHomeWidgetData success and with campaign tab data not exists, then latestShopHomeWidgetData value should be success with no empty list`() {
        coEvery {
            getShopDynamicTabUseCase.executeOnBackground()
        } returns ShopPageGetDynamicTabResponse(
            shopPageGetDynamicTab = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                tabData = listOf(
                    ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData(
                        name = "other tab",
                        data = ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData.Data(
                            homeLayoutData = HomeLayoutData(
                                widgetIdList = listOf(
                                    WidgetIdList(
                                        widgetId = "1"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        viewModel.getLatestShopCampaignWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData,
            "CampaignTab"
        )
        val result = viewModel.latestShopCampaignWidgetLayoutData.value
        assert(result is Success)
        assert((result as? Success)?.data?.listWidgetLayout?.isEmpty() == true)
    }

    @Test
    fun `when call getLatestShopHomeWidgetData error, then latestShopHomeWidgetData value should be fail`() {
        coEvery {
            getShopDynamicTabUseCase.executeOnBackground()
        } throws Exception()
        viewModel.getLatestShopCampaignWidgetLayoutData(
            mockShopId,
            mockExtParam,
            addressWidgetData,
            "CampaignTab"
        )
        val result = viewModel.latestShopCampaignWidgetLayoutData.value
        assert(result is Fail)
    }

    @Test
    fun `when call getVoucherSliderData success, then voucherSliderWidgetData value should be success`() {
        val mockExclusiveLaunchVoucher = ExclusiveLaunchVoucher(
            id = 1L,
            voucherName = "",
            minimumPurchase = 1L,
            remainingQuota = 2,
            slug = "",
            isDisabledButton = false,
            couponCode = "",
            buttonStr = "",
        )
        coEvery {
            getPromoVoucherListUseCase.execute(any())
        } returns listOf(mockExclusiveLaunchVoucher)
        viewModel.getVoucherSliderData(
            ShopWidgetVoucherSliderUiModel()
        )
        val result = viewModel.voucherSliderWidgetData.value
        assert(result is Success)
    }

    @Test
    fun `when call getVoucherSliderData error, then voucherSliderWidgetData value should be fail`() {
        coEvery {
            getPromoVoucherListUseCase.execute(any())
        } throws Exception()
        viewModel.getVoucherSliderData(
            ShopWidgetVoucherSliderUiModel()
        )
        val result = viewModel.voucherSliderWidgetData.value
        assert(result is Fail)
    }

    @Test
    fun `when call redeemCampaignVoucherSlider success, then redeemResult value should be success`() {
        val mockExclusiveLaunchVoucher = ExclusiveLaunchVoucher(
            id = 1L,
            voucherName = "",
            minimumPurchase = 1L,
            remainingQuota = 2,
            slug = "",
            isDisabledButton = false,
            couponCode = "",
            buttonStr = "",
        )
        coEvery {
            redeemPromoVoucherUseCase.execute(any())
        } returns RedeemPromoVoucherResult(
            redeemMessage = "",
            voucherCode = "",
            promoId = 1L
        )
        viewModel.redeemCampaignVoucherSlider(
            ShopWidgetVoucherSliderUiModel(),
            mockExclusiveLaunchVoucher
        )
        val result = viewModel.redeemResult.value
        assert(result is Success)
    }

    @Test
    fun `when call redeemCampaignVoucherSlider error, then redeemResult value should be fail`() {
        val mockExclusiveLaunchVoucher = ExclusiveLaunchVoucher(
            id = 1L,
            voucherName = "",
            minimumPurchase = 1L,
            remainingQuota = 2,
            slug = "",
            isDisabledButton = false,
            couponCode = "",
            buttonStr = "",
        )
        coEvery {
            redeemPromoVoucherUseCase.execute(any())
        } throws Exception()
        viewModel.redeemCampaignVoucherSlider(
            ShopWidgetVoucherSliderUiModel(),
            mockExclusiveLaunchVoucher
        )
        val result = viewModel.redeemResult.value
        assert(result is Fail)
    }

    @Test
    fun `when update voucher slider data with empty visitable, then widget data size should be empty`() {
        viewModel.updateVoucherSliderWidgetData(
            mutableListOf(),
            ShopWidgetVoucherSliderUiModel()
        )
        val result = viewModel.campaignWidgetListVisitable.value
        assert(result is Success)
        assert((result as? Success)?.data?.isEmpty() == true)
    }

    @Test
    fun `when update voucher slider data with no voucher slider on visitable, then widget data size should be the same`() {
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopCampaignWidgetCarouselProductUiModel()
        )
        viewModel.updateVoucherSliderWidgetData(
            mockVisitable,
            ShopWidgetVoucherSliderUiModel()
        )
        val result = viewModel.campaignWidgetListVisitable.value
        assert(result is Success)
        assert((result as? Success)?.data?.size == mockVisitable.size)
    }

    @Test
    fun `when update voucher slider data with empty voucher list, then voucher widget should be removed from visitable`() {
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopWidgetVoucherSliderUiModel()
        )
        viewModel.updateVoucherSliderWidgetData(
            mockVisitable,
            ShopWidgetVoucherSliderUiModel()
        )
        val result = viewModel.campaignWidgetListVisitable.value
        assert(result is Success)
        assert((result as? Success)?.data?.filterIsInstance<ShopWidgetVoucherSliderUiModel>()?.isEmpty() == true)
    }

    @Test
    fun `when update voucher slider data with voucher error, then voucher widget should be removed from visitable`() {
        val mockExclusiveLaunchVoucher = ExclusiveLaunchVoucher(
            id = 1L,
            voucherName = "",
            minimumPurchase = 1L,
            remainingQuota = 2,
            slug = "",
            isDisabledButton = false,
            couponCode = "",
            buttonStr = "",
        )
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopWidgetVoucherSliderUiModel(
                listVoucher = listOf(mockExclusiveLaunchVoucher),
            )
        )
        viewModel.updateVoucherSliderWidgetData(
            mockVisitable,
            ShopWidgetVoucherSliderUiModel(
                listVoucher = listOf(mockExclusiveLaunchVoucher),
                isError = true
            )
        )
        val result = viewModel.campaignWidgetListVisitable.value
        assert(result is Success)
        assert((result as? Success)?.data?.filterIsInstance<ShopWidgetVoucherSliderUiModel>()?.isEmpty() == true)
    }

    @Test
    fun `when update voucher slider data success, then voucher widget should be updated`() {
        val mockExclusiveLaunchVoucher = ExclusiveLaunchVoucher(
            id = 1L,
            voucherName = "",
            minimumPurchase = 1L,
            remainingQuota = 2,
            slug = "",
            isDisabledButton = false,
            couponCode = "",
            buttonStr = "",
        )
        val expectedVoucherSliderUiModel = ShopWidgetVoucherSliderUiModel(
            listVoucher = listOf(mockExclusiveLaunchVoucher)
        )
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopWidgetVoucherSliderUiModel(
                listVoucher = listOf(mockExclusiveLaunchVoucher)
            )
        )
        viewModel.updateVoucherSliderWidgetData(
            mockVisitable,
            expectedVoucherSliderUiModel
        )
        val result = viewModel.campaignWidgetListVisitable.value
        assert(result is Success)
        assert((result as? Success)?.data?.filterIsInstance<ShopWidgetVoucherSliderUiModel>()?.first() == expectedVoucherSliderUiModel)
    }

    @Test
    fun `If exception thrown when calling updateVoucherSliderWidgetData, then should return fail`() {
        val observer = mockk<Observer<Result<List<Visitable<*>>>>>()
        viewModel.campaignWidgetListVisitable.observeForever(observer)
        every { observer.onChanged(any()) } throws Exception()
        viewModel.updateVoucherSliderWidgetData(
            mutableListOf(),
            ShopWidgetVoucherSliderUiModel()
        )
        assert(viewModel.campaignWidgetListVisitable.value is Fail)
    }


    @Test
    fun `when toggle banner timer remind me with empty visitable, then visitable list should not include banner timer`() {
        viewModel.toggleBannerTimerRemindMe(mutableListOf(), isRemindMe = true, isClickRemindMe = true)
        val liveDataUpdatedBannerTimerUiModelData = viewModel.updatedBannerTimerUiModelData.value
        val liveDataHomeWidgetListVisitable = viewModel.campaignWidgetListVisitable.value
        assert(liveDataUpdatedBannerTimerUiModelData == null)
        assert(liveDataHomeWidgetListVisitable is Success)
        assert((liveDataHomeWidgetListVisitable as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().isEmpty())
    }

    @Test
    fun `when toggle banner timer remind me with banner timer visitable and data is null, then visitable list should include banner timer`() {
        viewModel.toggleBannerTimerRemindMe(
            mutableListOf(
                ShopWidgetDisplayBannerTimerUiModel(),
                ShopCampaignWidgetCarouselProductUiModel()
            ),
            isRemindMe = true,
            isClickRemindMe = true
        )
        val liveDataUpdatedBannerTimerUiModelData = viewModel.updatedBannerTimerUiModelData.value
        val liveDataHomeWidgetListVisitable = viewModel.campaignWidgetListVisitable.value
        assert(liveDataUpdatedBannerTimerUiModelData != null)
        assert(liveDataHomeWidgetListVisitable is Success)
        assert((liveDataHomeWidgetListVisitable as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().isNotEmpty())
    }

    @Test
    fun `when not click toggle banner timer remind me with banner timer visitable and data is not null, then visitable list should include banner timer`() {
        viewModel.toggleBannerTimerRemindMe(
            mutableListOf(
                ShopWidgetDisplayBannerTimerUiModel(
                    data = ShopWidgetDisplayBannerTimerUiModel.Data(
                        totalNotify = 5
                    )
                ),
                ShopCampaignWidgetCarouselProductUiModel()
            ),
            isRemindMe = true,
            isClickRemindMe = false
        )
        val liveDataUpdatedBannerTimerUiModelData = viewModel.updatedBannerTimerUiModelData.value
        val liveDataHomeWidgetListVisitable = viewModel.campaignWidgetListVisitable.value
        assert(liveDataUpdatedBannerTimerUiModelData != null)
        assert(liveDataHomeWidgetListVisitable is Success)
        assert((liveDataHomeWidgetListVisitable as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().isNotEmpty())
    }

    @Test
    fun `when register banner timer remind me, then total notify should increase by one`() {
        val mockTotalNotify = 5
        val expectedTotalNotify = 6
        viewModel.toggleBannerTimerRemindMe(
            mutableListOf(
                ShopWidgetDisplayBannerTimerUiModel(
                    data = ShopWidgetDisplayBannerTimerUiModel.Data(
                        totalNotify = mockTotalNotify
                    )
                ),
                ShopCampaignWidgetCarouselProductUiModel()
            ),
            isRemindMe = true,
            isClickRemindMe = true
        )
        val liveDataUpdatedBannerTimerUiModelData = viewModel.updatedBannerTimerUiModelData.value
        val liveDataHomeWidgetListVisitable = viewModel.campaignWidgetListVisitable.value
        assert(liveDataUpdatedBannerTimerUiModelData != null)
        assert(liveDataHomeWidgetListVisitable is Success)
        val updatedBannerTimerUiModel = (liveDataHomeWidgetListVisitable as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().first()
        assert(updatedBannerTimerUiModel.data?.totalNotify == expectedTotalNotify)
    }

    @Test
    fun `when unregister banner timer remind me, then total notify should decrease by one`() {
        val mockTotalNotify = 5
        val expectedTotalNotify = 4
        viewModel.toggleBannerTimerRemindMe(
            mutableListOf(
                ShopWidgetDisplayBannerTimerUiModel(
                    data = ShopWidgetDisplayBannerTimerUiModel.Data(
                        totalNotify = mockTotalNotify
                    )
                ),
                ShopCampaignWidgetCarouselProductUiModel()
            ),
            isRemindMe = false,
            isClickRemindMe = true
        )
        val liveDataUpdatedBannerTimerUiModelData = viewModel.updatedBannerTimerUiModelData.value
        val liveDataHomeWidgetListVisitable = viewModel.campaignWidgetListVisitable.value
        assert(liveDataUpdatedBannerTimerUiModelData != null)
        assert(liveDataHomeWidgetListVisitable is Success)
        val updatedBannerTimerUiModel = (liveDataHomeWidgetListVisitable as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().first()
        assert(updatedBannerTimerUiModel.data?.totalNotify == expectedTotalNotify)
    }

    @Test
    fun `If exception thrown when calling toggleBannerTimerRemindMe, then should return fail`() {
        val observer = mockk<Observer<Result<List<Visitable<*>>>>>()
        viewModel.campaignWidgetListVisitable.observeForever(observer)
        every { observer.onChanged(any()) } throws Exception()
        viewModel.toggleBannerTimerRemindMe(
            mutableListOf(),
            isRemindMe = false,
            isClickRemindMe = true
        )
        assert(viewModel.campaignWidgetListVisitable.value is Fail)
    }


    @Test
    fun `when update banner timer ui model without banner timer on visitable, then visitable should be the same`() {
        val expectedBannerTimerModel = ShopWidgetDisplayBannerTimerUiModel()
        val mockListVisitable = mutableListOf<Visitable<*>>(
            ShopCampaignWidgetCarouselProductUiModel()
        )
        viewModel.updateBannerTimerWidgetUiModel(
            mockListVisitable,
            expectedBannerTimerModel
        )
        assert(viewModel.campaignWidgetListVisitable.value is Success)
        assert((viewModel.campaignWidgetListVisitable.value as Success).data.size == mockListVisitable.size)
    }

    @Test
    fun `when update banner timer ui model with banner timer on visitable, then visitable should be updated`() {
        val mockBannerTimerUiModel = ShopWidgetDisplayBannerTimerUiModel()
        val expectedBannerTimerModel = ShopWidgetDisplayBannerTimerUiModel()
        val mockListVisitable = mutableListOf<Visitable<*>>(mockBannerTimerUiModel)
        viewModel.updateBannerTimerWidgetUiModel(
            mockListVisitable,
            expectedBannerTimerModel
        )
        assert(viewModel.campaignWidgetListVisitable.value is Success)
        assert((viewModel.campaignWidgetListVisitable.value as Success).data.first { it is ShopWidgetDisplayBannerTimerUiModel } == expectedBannerTimerModel)
    }

    @Test
    fun `If exception thrown when calling updateBannerTimerWidgetUiModel, then should return fail`() {
        val observer = mockk<Observer<Result<List<Visitable<*>>>>>()
        viewModel.campaignWidgetListVisitable.observeForever(observer)
        every { observer.onChanged(any()) } throws Exception()
        viewModel.updateBannerTimerWidgetUiModel(
            mutableListOf(),
            ShopWidgetDisplayBannerTimerUiModel()
        )
        assert(viewModel.campaignWidgetListVisitable.value is Fail)
    }

    @Test
    fun `when calling showBannerTimerRemindMeLoading with empty visitable, then visitable result should be the same`() {
        val mockVisitable = mutableListOf<Visitable<*>>()
        viewModel.showBannerTimerRemindMeLoading(mockVisitable)
        assert(viewModel.campaignWidgetListVisitable.value is Success)
        assert((viewModel.campaignWidgetListVisitable.value as Success).data.size == mockVisitable.size)
    }

    @Test
    fun `when calling showBannerTimerRemindMeLoading with banner timer visitable but data is null, then showRemindMeLoading should be null`() {
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopWidgetDisplayBannerTimerUiModel()
        )
        viewModel.showBannerTimerRemindMeLoading(mockVisitable)
        assert(viewModel.campaignWidgetListVisitable.value is Success)
        assert((viewModel.campaignWidgetListVisitable.value as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().first().data?.showRemindMeLoading  == null)
    }

    @Test
    fun `when calling showBannerTimerRemindMeLoading with banner timer visitable, then showRemindMeLoading should be true`() {
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopWidgetDisplayBannerTimerUiModel(
                data = ShopWidgetDisplayBannerTimerUiModel.Data()
            )
        )
        viewModel.showBannerTimerRemindMeLoading(mockVisitable)
        assert(viewModel.campaignWidgetListVisitable.value is Success)
        assert((viewModel.campaignWidgetListVisitable.value as Success).data.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().first().data?.showRemindMeLoading  == true)
    }

    @Test
    fun `If exception thrown when calling showBannerTimerRemindMeLoading, then should return fail`() {
        val mockVisitable = mutableListOf<Visitable<*>>(
            ShopWidgetDisplayBannerTimerUiModel()
        )
        val observer = mockk<Observer<Result<List<Visitable<*>>>>>()
        viewModel.campaignWidgetListVisitable.observeForever(observer)
        every { observer.onChanged(any()) } throws Exception()
        viewModel.showBannerTimerRemindMeLoading(mockVisitable)
        assert(viewModel.campaignWidgetListVisitable.value is Fail)
    }

    @Test
    fun `isLoadingRedeemVoucher should return false if called directly`() {
        assert(!viewModel.isLoadingRedeemVoucher())
    }
}
