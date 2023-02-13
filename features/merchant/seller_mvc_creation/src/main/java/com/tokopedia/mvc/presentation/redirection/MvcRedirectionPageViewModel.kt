package com.tokopedia.mvc.presentation.redirection

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.GetMerchantPromoListResponse
import com.tokopedia.mvc.domain.usecase.*
import com.tokopedia.mvc.presentation.redirection.uimodel.MvcRedirectionPageAction
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MvcRedirectionPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getMerchantPromoListUseCase: GetMerchantPromoListUseCase
) : BaseViewModel(dispatchers.main) {

    companion object{
        private const val SELLER_MVC_PAGE_ID = 65L
    }

    private val _uiAction = MutableSharedFlow<MvcRedirectionPageAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    fun getRedirectionAppLink() {
        launchCatchError(
            dispatchers.io,
            block = {
                val merchantPromoListData = getMerchantPromoListData().merchantPromotionGetPromoList
                val sellerMvcAppLink = merchantPromoListData.data.pages.firstOrNull{
                    it.pageId.toLongOrZero() == SELLER_MVC_PAGE_ID
                }?.ctaLink ?: ApplinkConst.SellerApp.SELLER_MVC_LIST
                _uiAction.tryEmit(MvcRedirectionPageAction.RedirectTo(sellerMvcAppLink))
            },
            onError = {
                _uiAction.tryEmit(MvcRedirectionPageAction.ShowError(it))
            }
        )
    }

    private suspend fun getMerchantPromoListData(): GetMerchantPromoListResponse {
        return getMerchantPromoListUseCase.execute(userSession.shopId)
    }
}
