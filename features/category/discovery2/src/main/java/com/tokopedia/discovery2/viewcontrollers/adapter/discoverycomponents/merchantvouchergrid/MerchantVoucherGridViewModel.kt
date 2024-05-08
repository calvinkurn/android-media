package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase.Companion.VOUCHER_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addShimmer
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addVoucherList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MerchantVoucherGridViewModel(
    application: Application,
    component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(component), CoroutineScope {
    companion object {
        const val ERROR_MESSAGE_EMPTY_DATA = "empty data"
        const val ERROR_MESSAGE_UNAVAILABLE_NEXT_PAGE = "unavailable next page"
        const val SHIMMER_COMPONENT_HEIGHT = 300
    }

    private val _couponList: MutableLiveData<Result<ArrayList<ComponentsItem>>> = MutableLiveData()
    private val _seeMore: MutableLiveData<Result<Redirection>> = MutableLiveData()
    private val _noMorePages: MutableLiveData<Unit> = MutableLiveData()

    private val layout: ArrayList<ComponentsItem> = arrayListOf()
    private var isLoading = true

    val couponList: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _couponList
    val seeMore: LiveData<Result<Redirection>>
        get() = _seeMore
    val noMorePages: LiveData<Unit>
        get() = _noMorePages

    @JvmField
    @Inject
    var useCase: MerchantVoucherUseCase? = null

    init {
        layout.addShimmer()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + SupervisorJob()

    private fun hasNextPage(): Boolean = Utils.nextPageAvailable(component, VOUCHER_PER_PAGE)

    private fun setVoucherList(
        redirection: Redirection?,
        onEventAfterVoucherListSet: () -> Unit
    ) {
        val components = component.getComponentsItem()
        if (!components.isNullOrEmpty()) {
            if (hasNextPage() && !redirection.isAvailable()) {
                layout.addVoucherList(components)
                layout.addShimmer()
            } else {
                onEventAfterVoucherListSet.invoke()
                layout.addVoucherList(components)
            }
            _couponList.value = Success(ArrayList(layout))
        } else {
            _couponList.value = Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA))
        }
    }

    private fun Redirection?.isAvailable(): Boolean =
        !this?.ctaText.isNullOrBlank() && !this?.applink.isNullOrBlank()

    private fun getComponentAdditionalInfo(): ComponentAdditionalInfo? = component.getComponentAdditionalInfo()

    fun loadFirstPageCoupon() {
        _couponList.value = Success(ArrayList(layout))

        launchCatchError(
            block = {
                useCase?.loadFirstPageComponents(
                    componentId = component.id,
                    pageEndPoint = component.pageEndPoint
                )

                val redirection = getComponentAdditionalInfo()?.redirection
                setVoucherList(
                    redirection = redirection,
                    onEventAfterVoucherListSet = {
                        setLoadMoreButton(redirection)
                        _noMorePages.value = Unit
                    }
                )

                isLoading = false
            },
            onError = {
                Timber.e(it)
                _couponList.value = Fail(it)
                isLoading = false
            }
        )
    }

    private fun setLoadMoreButton(redirection: Redirection?) {
        val result = redirection?.let {
            if (it.isAvailable()) {
                Success(redirection)
            } else {
                Fail(Throwable(ERROR_MESSAGE_UNAVAILABLE_NEXT_PAGE))
            }
        } ?: Fail(Throwable(ERROR_MESSAGE_UNAVAILABLE_NEXT_PAGE))

        this@MerchantVoucherGridViewModel._seeMore.value = result
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean
    ) {
        if (isLoading || !isAtTheBottomOfThePage) return

        isLoading = true

        launchCatchError(
            block = {
                if (useCase?.getCarouselPaginatedData(componentId = component.id, pageEndPoint = component.pageEndPoint) == true) {
                    setVoucherList(
                        redirection = getComponentAdditionalInfo()?.redirection,
                        onEventAfterVoucherListSet = {
                            _noMorePages.value = Unit
                        }
                    )
                }
                isLoading = false
            },
            onError = {
                _couponList.value = Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA))
                isLoading = false
            }
        )
    }
}
