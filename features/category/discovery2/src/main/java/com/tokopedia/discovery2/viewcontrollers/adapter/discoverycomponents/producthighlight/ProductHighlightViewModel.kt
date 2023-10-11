package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoveryext.checkForNullAndSize
import com.tokopedia.discovery2.usecase.producthighlightusecase.ProductHighlightUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductHighlightViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productHighlightCardList: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val _hideShimmer = SingleLiveEvent<Boolean>()
    private val _showErrorState = SingleLiveEvent<Boolean>()

    @JvmField
    @Inject
    var productHighlightUseCase: ProductHighlightUseCase? = null

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
            if (productHighlightUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint) == true) {
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
}
