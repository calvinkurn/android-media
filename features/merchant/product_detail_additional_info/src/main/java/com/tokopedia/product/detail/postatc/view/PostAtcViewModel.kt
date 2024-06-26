package com.tokopedia.product.detail.postatc.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.product.detail.common.postatc.PostAtcParams
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.data.model.PostAtcInfo
import com.tokopedia.product.detail.postatc.data.model.PostAtcLayout
import com.tokopedia.product.detail.postatc.mapper.mapToUiModel
import com.tokopedia.product.detail.postatc.mapper.toPostAtcInfo
import com.tokopedia.product.detail.postatc.mapper.toUserLocationRequest
import com.tokopedia.product.detail.postatc.usecase.GetPostAtcLayoutUseCase
import com.tokopedia.recommendation_widget_common.viewutil.asFail
import com.tokopedia.recommendation_widget_common.viewutil.asSuccess
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class PostAtcViewModel @Inject constructor(
    private val getPostAtcLayoutUseCase: GetPostAtcLayoutUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _layouts = MutableLiveData<Result<List<PostAtcUiModel>>>()
    val layouts: LiveData<Result<List<PostAtcUiModel>>> = _layouts

    var postAtcInfo: PostAtcInfo = PostAtcInfo()
        private set

    /**
     * Pass data from Arguments
     */
    fun initializeParameters(
        productId: String,
        postAtcParams: PostAtcParams,
        localCacheModel: LocalCacheModel
    ) {
        val addons = postAtcParams.addons?.let {
            PostAtcInfo.Addons.parse(it)
        }

        postAtcInfo = postAtcInfo.copy(
            addons = addons,
            cartId = postAtcParams.cartId,
            layoutId = postAtcParams.layoutId,
            pageSource = postAtcParams.pageSource,
            productId = productId,
            session = postAtcParams.session,
            userLocationRequest = localCacheModel.toUserLocationRequest()
        )

        fetchLayout()
    }

    private fun fetchLayout() {
        launchCatchError(block = {
            val result = getPostAtcLayoutUseCase.execute(
                postAtcInfo.productId,
                postAtcInfo.cartId,
                postAtcInfo.layoutId,
                postAtcInfo.pageSource,
                postAtcInfo.session,
                postAtcInfo.userLocationRequest
            )

            updateInfo(result)

            val components = result.components
            if (components.isEmpty()) throw Throwable()

            val uiModels = result.components.mapToUiModel(postAtcInfo)
            _layouts.value = uiModels.asSuccess()
        }, onError = { _layouts.value = it.asFail() })
    }

    private fun updateInfo(data: PostAtcLayout) {
        val basicInfo = data.basicInfo
        val category = basicInfo.category
        val footer = PostAtcInfo.Footer(
            image = data.postAtcInfo.image,
            description = data.postAtcInfo.title,
            buttonText = data.postAtcInfo.button.text,
            cartId = data.postAtcInfo.button.cartId
        )

        postAtcInfo = postAtcInfo.copy(
            categoryId = category.id,
            categoryName = category.name,
            footer = footer,
            layoutName = data.name,
            shopId = basicInfo.shopId,
            price = basicInfo.price,
            originalPrice = basicInfo.originalPrice,
            condition = basicInfo.condition,
            warehouseInfo = data.warehouseInfo.toPostAtcInfo()
        )
    }
}
