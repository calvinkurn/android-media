package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.VisitableMapper.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.VisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowShoppingListViewModel @Inject constructor(
    private val addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    affiliateService,
    getTargetedTickerUseCase,
    addressData,
    userSession,
    dispatchers
) {
    private val layout = mutableListOf<Visitable<*>>()

    private val _firstPage = MutableLiveData<List<Visitable<*>>>()

    val firstPage: LiveData<List<Visitable<*>>>
        get() = _firstPage

    var headerSpace: Int = Int.ZERO
    var headerModel: HeaderModel = HeaderModel()

    fun loadFirstPage() {
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeader(
            headerModel = headerModel
        )

        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        layout.addHeaderSpace(
            space = headerSpace,
            headerModel = headerModel
        )
        _firstPage.value = layout
    }
}
