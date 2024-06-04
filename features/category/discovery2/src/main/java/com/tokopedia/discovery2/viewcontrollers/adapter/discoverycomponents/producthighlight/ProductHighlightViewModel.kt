package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOcsUseCase
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.producthighlight.DiscoveryOCSDataModel
import com.tokopedia.discovery2.discoveryext.checkForNullAndSize
import com.tokopedia.discovery2.usecase.producthighlightusecase.ProductHighlightUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductHighlightViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {
    private val productHighlightCardList: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val _hideShimmer = SingleLiveEvent<Boolean>()
    private val _showErrorState = SingleLiveEvent<Boolean>()

    private val _ocsLiveData = MutableLiveData<DiscoveryOCSDataModel>()
    val redirectToOCS: LiveData<DiscoveryOCSDataModel>
        get() = _ocsLiveData

    private val _ocsErrorState = MutableLiveData<String>()
    val ocsErrorMessage: LiveData<String>
        get() = _ocsErrorState

    @JvmField
    @Inject
    var productHighlightUseCase: ProductHighlightUseCase? = null

    @JvmField
    @Inject
    var atcUseCase: AddToCartOcsUseCase? = null

    @JvmField
    @Inject
    var dispatcher: CoroutineDispatchers? = null

    fun getProductHighlightCardItemsListData(): LiveData<ComponentsItem> = productHighlightCardList
    val hideShimmer: LiveData<Boolean> = _hideShimmer
    val showErrorState: LiveData<Boolean> = _showErrorState

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchProductHighlightData()
    }

    private fun fetchProductHighlightData() {
        launchCatchError(block = {
            if (productHighlightUseCase?.loadFirstPageComponents(
                    components.id,
                    components.pageEndPoint
                ) == true
            ) {
                if (components.data.isNullOrEmpty()) {
                    _hideShimmer.value = true
                }
            }
            productHighlightCardList.value = components
        }, onError = {
                components.noOfPagesLoaded = 1
                if (it is UnknownHostException || it is SocketTimeoutException) {
                    components.verticalProductFailState = true
                    _showErrorState.value = true
                } else {
                    _hideShimmer.value = true
                }
            })
    }

    fun layoutSelector(): Int {
        return when (components.properties?.type) {
            "single" -> R.layout.disco_item_shimmer_single_product_highlight_layout
            "double" -> R.layout.disco_item_shimmer_double_pl_layout
            "triple" -> R.layout.disco_item_shimmer_triple_pl_layout
            else -> R.layout.multi_banner_layout
        }
    }

    fun onCardClicked(position: Int, context: Context) {
        productHighlightCardList.value?.data.checkForNullAndSize(position)?.let { listItem ->
            val item = listItem[position]
            if (!item.applinks.isNullOrEmpty()) {
                navigate(context, item.applinks)
            }
        }
    }

    fun onOCSClicked(context: Context, dataItem: DataItem) {
        val atcRequestParam = AddToCartOcsRequestParams().apply {
            productId = dataItem.productId.toZeroStringIfNull()
            shopId = dataItem.shopId.toZeroStringIfNull()
            quantity = dataItem.minQuantity
            customerId = userId
            productName = dataItem.productName.orEmpty()
            category = dataItem.category.orEmpty()
            price = dataItem.price.orEmpty()
            shopName = dataItem.shopName.orEmpty()
            atcFromExternalSource = OCS_SOURCE_PARAM
        }

        launchCatchError(block = {
            atcUseCase?.invoke(atcRequestParam)?.let {
                handleAvailableResult(it, dataItem)
                return@launchCatchError
            }

            handleUnavailableResult()
        }, onError = {
            _ocsErrorState.postValue(ErrorHandler.getErrorMessage(context, it))
            Timber.e(it)
        })
    }

    fun reload() {
        components.noOfPagesLoaded = 0
        fetchProductHighlightData()
    }

    fun shouldShowShimmer(): Boolean {
        return components.noOfPagesLoaded != 1 && !components.verticalProductFailState
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    private fun handleAvailableResult(
        result: AddToCartDataModel,
        dataItem: DataItem
    ) {
        if (result.isDataError()) {
            val message = result.getAtcErrorMessage()
            message?.run { _ocsErrorState.postValue(this) }

            Timber.e(message)
        } else {
            _ocsLiveData.postValue(DiscoveryOCSDataModel(dataItem, result))
        }
    }

    private fun handleUnavailableResult() {
        val message = "Failed to request ATC"
        _ocsErrorState.postValue(message)
        Timber.e(message)
    }

    companion object {
        private const val OCS_SOURCE_PARAM = "product_highlight_atc"
    }
}
