package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.ClearAllDraftProductsUseCase
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.GetAllDraftProductsCountUseCase
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductDraftListCountViewModel @Inject constructor(
        private val getAllDraftProductsCountUseCase: GetAllDraftProductsCountUseCase,
        private val clearAllDraftProductsUseCase: ClearAllDraftProductsUseCase,
        private val getProductManageAccessUseCase: GetProductManageAccessUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val getAllDraftCountResult
        get() = _getAllDraftCountResult

    private val _getAllDraftCountResult = MutableLiveData<Result<Long>>()

    fun getAllDraftCount() {
        launchCatchError(block = {
            var draftCount = 0L

            withContext(dispatchers.io) {
                val access = if (userSession.isShopOwner) {
                    ProductManageAccessMapper.mapProductManageOwnerAccess()
                } else {
                    val shopId = userSession.shopId
                    val response = getProductManageAccessUseCase.execute(shopId)
                    ProductManageAccessMapper.mapToProductManageAccess(response)
                }

                if(access.addProduct) {
                    draftCount = getAllDraftProductsCountUseCase.getData(RequestParams.EMPTY)
                }
            }

            _getAllDraftCountResult.value = Success(draftCount)
        }) {
            _getAllDraftCountResult.value = Fail(it)
        }
    }

    fun clearAllDraft() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                clearAllDraftProductsUseCase.getData(RequestParams.EMPTY)
            }
        }) {
            // do nothing
        }
    }

    fun detachView() {
        getAllDraftProductsCountUseCase.unsubscribe()
        clearAllDraftProductsUseCase.unsubscribe()
    }
}