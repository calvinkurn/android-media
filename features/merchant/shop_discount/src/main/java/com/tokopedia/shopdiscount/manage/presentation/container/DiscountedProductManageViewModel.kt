package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shopadmin.common.util.AccessId
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.manage.data.mapper.ProductListMetaMapper
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
import com.tokopedia.shopdiscount.manage.domain.entity.SellerEligibilityData
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListMetaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiscountedProductManageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListMetaUseCase: GetSlashPriceProductListMetaUseCase,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase,
    private val authorizeAccessUseCase: AuthorizeAccessUseCase,
    private val productListMetaMapper: ProductListMetaMapper,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main) {

    private val _productsMeta = MutableLiveData<Result<List<DiscountStatusMeta>>>()
    val productsMeta: LiveData<Result<List<DiscountStatusMeta>>>
        get() = _productsMeta

    private val _sellerEligibility = MutableLiveData<Result<SellerEligibilityData>>()
    val sellerEligibility: LiveData<Result<SellerEligibilityData>>
        get() = _sellerEligibility

    private var selectedTabPosition = 0

    fun getSlashPriceProductsMeta() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListMetaUseCase.setRequestParams()
                val response = getSlashPriceProductListMetaUseCase.executeOnBackground()
                productListMetaMapper.map(response.getSlashPriceProductListMeta.data.tab)
            }

            _productsMeta.value = Success(result)

        }, onError = {
            _productsMeta.value = Fail(it)
        })
    }

    fun checkSellerEligibility() {
        launchCatchError(block = {
            val isAuthorizeAsync = asyncCatchError(dispatchers.io, block = {
                val requestParam = AuthorizeAccessUseCase.createRequestParams(
                    userSession.shopId.toLongOrZero(),
                    AccessId.VIEW_SLASH_PRICE
                )
                authorizeAccessUseCase.execute(requestParam)
            }) {
                _sellerEligibility.value = Fail(it)
                null
            }
            val hasBenefitPackageAsync = asyncCatchError(dispatchers.io, block = {
                getSlashPriceBenefitUseCase.setParams()
                getSlashPriceBenefitUseCase.executeOnBackground().getSlashPriceBenefit.slashPriceBenefits.isNotEmpty()
            }) {
                _sellerEligibility.value = Fail(it)
                null
            }
            isAuthorizeAsync.await()?.let { isAuthorize ->
                hasBenefitPackageAsync.await()?.let { hasBenefitPackage ->
                    _sellerEligibility.value = Success(
                        SellerEligibilityData(
                            isAuthorize,
                            hasBenefitPackage
                        )
                    )
                }
            }
        }, onError = {
            _sellerEligibility.value = Fail(it)
        })
    }

    fun findDiscountStatusCount(tabs : List<PageTab>, discountStatusMeta: List<DiscountStatusMeta>): List<PageTab> {
        return tabs.map { tab ->
            val match = discountStatusMeta.find { meta -> tab.status == meta.id }
            tab.copy(count = match?.productCount.orZero())
        }
    }

    fun setSelectedTabPosition(selectedTabPosition: Int) {
        this.selectedTabPosition = selectedTabPosition
    }

    fun getSelectedTabPosition(): Int {
        return selectedTabPosition
    }
}
