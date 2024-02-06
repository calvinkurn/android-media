package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.campaign.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.campaign.usecase.GetOfferProductListUseCase
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import com.tokopedia.shop_widget.buy_more_save_more.data.mapper.GetOfferingInfoForBuyerMapper
import com.tokopedia.shop_widget.buy_more_save_more.data.mapper.GetOfferingProductListMapper
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BmsmWidgetTabViewModelTest {

    private lateinit var viewModel: BmsmWidgetTabViewModel

    @RelaxedMockK
    lateinit var getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase

    @RelaxedMockK
    lateinit var getOfferProductListUseCase: GetOfferProductListUseCase

    @RelaxedMockK
    lateinit var getOfferingInfoForBuyerMapper: GetOfferingInfoForBuyerMapper

    @RelaxedMockK
    lateinit var getOfferingProductListMapper: GetOfferingProductListMapper

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var getMiniCartDataUseCase: Lazy<GetMiniCartWidgetUseCase>

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var productListObserver: Observer<in List<Product>>

    @RelaxedMockK
    lateinit var addToCartObserver: Observer<in AddToCartDataModel>
}
