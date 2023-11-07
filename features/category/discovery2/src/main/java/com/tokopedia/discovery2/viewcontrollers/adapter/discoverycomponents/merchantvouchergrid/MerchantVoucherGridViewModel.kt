package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase.Companion.VOUCHER_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
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
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {
    companion object {
        const val ERROR_MESSAGE_EMPTY_DATA = "empty data"
        const val SHIMMER_COMPONENT_HEIGHT = 300
    }

    private val _couponList: MutableLiveData<Result<ArrayList<ComponentsItem>>> = MutableLiveData()
    private val _seeMore: MutableLiveData<Redirection> = MutableLiveData()
    private val _noMorePages: MutableLiveData<Unit> = MutableLiveData()

    val shimmerComponent = ComponentsItem(
        name = ComponentNames.Shimmer.componentName,
        shimmerHeight = SHIMMER_COMPONENT_HEIGHT
    )

    val couponList: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _couponList
    val seeMore: LiveData<Redirection>
        get() = _seeMore
    val noMorePages: LiveData<Unit>
        get() = _noMorePages

    @JvmField
    @Inject
    var useCase: MerchantVoucherUseCase? = null

    var isLoading = false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    fun loadFirstPageCoupon() {
        isLoading = true
        launchCatchError(
            block = {
                if (useCase?.loadFirstPageComponents(componentId = component.id, pageEndPoint = component.pageEndPoint) == true) {
                    val redirection = getComponentAdditionalInfo()?.redirection
                    setVoucherList(
                        redirection = redirection,
                        onEventAfterVoucherListSet = {
                            addSeeMore(redirection)
                        }
                    )
                }
                isLoading = false
            },
            onError = {
                Timber.e(it)
                _couponList.postValue(Fail(it))
                isLoading = false
            }
        )
    }

    fun loadMore() {
        if (getComponentAdditionalInfo()?.nextPage.isNullOrBlank()) return

        isLoading = true

        launchCatchError(
            block = {
                if (useCase?.getCarouselPaginatedData(componentId = component.id, pageEndPoint = component.pageEndPoint) == true) {
                    setVoucherList(
                        redirection = getComponentAdditionalInfo()?.redirection,
                        onEventAfterVoucherListSet = { voucherList ->
                            voucherList.removeShimmer()
                        }
                    )
                }
                isLoading = false
            },
            onError = {
                _couponList.postValue(Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA)))
                isLoading = false
            }
        )
    }

    private fun hasNextPage(): Boolean = Utils.nextPageAvailable(component, VOUCHER_PER_PAGE)

    private fun setVoucherList(
        redirection: Redirection?,
        onEventAfterVoucherListSet: (voucherList: ArrayList<ComponentsItem>) -> Unit,
    ) {
        getVoucherList()?.let { voucherList ->
            if (voucherList.isNotEmpty()) {
                if (hasNextPage() && redirection?.ctaText.isNullOrBlank()) {
                    voucherList.addShimmer()
                } else {
                    onEventAfterVoucherListSet(voucherList)
                }
                _couponList.postValue(Success(voucherList))
            } else {
                _couponList.postValue(Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA)))
            }
        }
    }

    private fun getVoucherList(): ArrayList<ComponentsItem>? {
        component.getComponentsItem()?.let { list ->
            return list as ArrayList<ComponentsItem>
        }
        return null
    }

    private fun getComponentAdditionalInfo(): ComponentAdditionalInfo? = component.getComponentAdditionalInfo()

    private fun addSeeMore(
        redirection: Redirection?
    ) {
        _seeMore.postValue(redirection)
    }

    private fun ArrayList<ComponentsItem>.addShimmer() {
        add(shimmerComponent)
        add(shimmerComponent)
    }

    private fun ArrayList<ComponentsItem>.removeShimmer() {
        remove(shimmerComponent)
        remove(shimmerComponent)
    }
}
