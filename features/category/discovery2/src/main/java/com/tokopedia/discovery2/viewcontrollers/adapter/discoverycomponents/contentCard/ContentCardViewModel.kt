package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.contentCardUseCase.ContentCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ContentCardViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {

    companion object {
        const val ERROR_MESSAGE_EMPTY_DATA = "Empty Data"
    }

    @JvmField
    @Inject
    var contentCardUseCase: ContentCardUseCase? = null
    private var isDarkMode: Boolean = false

    private val _contentCardList: MutableLiveData<Result<ArrayList<ComponentsItem>>> =
        MutableLiveData()

    val contentCardList: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _contentCardList

    private val layout: ArrayList<ComponentsItem> = arrayListOf()

    init {
        layout.addShimmer()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun loadContentCard() {
        _contentCardList.value = Success(ArrayList(layout))

        launchCatchError(block = {
            contentCardUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint,
                isDarkMode = isDarkMode
            )

            setContentCardList()
        }, onError = {
            Timber.e(it)
            _contentCardList.value = Fail(it)
        })
    }

    fun checkForDarkMode(context: Context?) {
        if (context != null) {
            isDarkMode = context.isDarkMode()
        }
    }

    private fun setContentCardList() {
        getContentCardList()?.let {
            if (it.isNotEmpty()) {
                layout.addContentCardList(it)
                _contentCardList.value = Success(ArrayList(layout))
            } else {
                _contentCardList.value = Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA))
            }
        }
    }

    private fun getContentCardList(): List<ComponentsItem>? {
        components.getComponentsItem()?.let { contentCards ->
            return contentCards.evenUp()
        }

        return null
    }

    private fun List<ComponentsItem>.evenUp(): List<ComponentsItem> {
        if (size.isEven()) return this

        return toMutableList()
            .apply {
                add(ComponentsItem(name = ComponentNames.ContentCardEmptyState.componentName))
            }
    }

    private fun ArrayList<ComponentsItem>.addShimmer() {
        val shimmerComponent = ComponentsItem(
            name = ComponentNames.ShimmerProductCard.componentName,
            shimmerHeight = MerchantVoucherGridViewModel.SHIMMER_COMPONENT_HEIGHT,
            shouldRefreshComponent = true
        )
        add(shimmerComponent)
        add(shimmerComponent)
    }

    private fun ArrayList<ComponentsItem>.addContentCardList(items: List<ComponentsItem>) {
        clear()
        addAll(items)
    }
}
