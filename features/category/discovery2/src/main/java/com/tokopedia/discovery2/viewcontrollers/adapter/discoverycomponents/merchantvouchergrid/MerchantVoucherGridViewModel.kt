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

    private val layout: ArrayList<ComponentsItem> = arrayListOf()

    init {
        layout.addShimmer()
    }

    val couponList: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _couponList
    val seeMore: LiveData<Redirection>
        get() = _seeMore
    val noMorePages: LiveData<Unit>
        get() = _noMorePages

    @JvmField
    @Inject
    var useCase: MerchantVoucherUseCase? = null

    private var isLoading = true

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    fun loadFirstPageCoupon() {
        _couponList.value = Success(ArrayList(layout))

        launchCatchError(
            block = {
                if (useCase?.loadFirstPageComponents(componentId = component.id, pageEndPoint = component.pageEndPoint) == true) {
                    val redirection = getComponentAdditionalInfo()?.redirection
                    setVoucherList(
                        redirection = redirection,
                        onEventAfterVoucherListSet = {
                            _seeMore.postValue(redirection)
                            _noMorePages.postValue(Unit)
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

    fun loadMore(
        isAtTheBottomOfThePage: Boolean
    ) {
        if (getComponentAdditionalInfo()?.nextPage.isNullOrBlank() || isLoading || !isAtTheBottomOfThePage) return

        isLoading = true

        launchCatchError(
            block = {
                if (useCase?.getCarouselPaginatedData(componentId = component.id, pageEndPoint = component.pageEndPoint) == true) {
                    setVoucherList(
                        redirection = getComponentAdditionalInfo()?.redirection,
                        onEventAfterVoucherListSet = {
                            _noMorePages.postValue(Unit)
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
        onEventAfterVoucherListSet: () -> Unit,
    ) {
        component.getComponentsItem()?.let { voucherList ->
            if (voucherList.isNotEmpty()) {
                if (hasNextPage() && redirection?.ctaText.isNullOrBlank()) {
                    layout.addVoucherList(voucherList)
                    layout.addShimmer()
                } else {
                    onEventAfterVoucherListSet.invoke()
                    layout.addVoucherList(voucherList)
                }
                _couponList.postValue(Success(ArrayList(layout)))
            } else {
                _couponList.postValue(Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA)))
            }
        }
    }

    private fun getComponentAdditionalInfo(): ComponentAdditionalInfo? = component.getComponentAdditionalInfo()
}
