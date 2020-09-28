package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.topAdsUseCase.DiscoveryTopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MasterProductCardItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val productCardModelLiveData: MutableLiveData<ProductCardModel> = MutableLiveData()
    private lateinit var context: Context
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()

    @Inject
    lateinit var discoveryTopAdsTrackingUseCase: DiscoveryTopAdsTrackingUseCase

    init {
        initDaggerInject()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentPosition.value = position
        components.data?.let {
            if (!it.isNullOrEmpty()) {
                dataItem.value = it[0]
                mapDataItemToProductCardModel(it[0])
            }
        }
    }

    private fun mapDataItemToProductCardModel(dataItem: DataItem) {
        val productCardModel = ProductCardModel(
                productName = dataItem.name ?: "",
                slashedPrice = dataItem.discountedPrice ?: "",
                formattedPrice = dataItem.price ?: "",
                discountPercentage = if (dataItem.discountPercentage?.toIntOrZero() != 0) {
                    "${dataItem.discountPercentage}%"
                } else {
                    ""
                },
                ratingCount = dataItem.rating?.toIntOrZero() ?: 0,
                reviewCount = dataItem.countReview?.toIntOrZero() ?: 0,
                productImageUrl = dataItem.imageUrlMobile ?: "",
                isTopAds = dataItem.isTopads ?: false,
                freeOngkir = ProductCardModel.FreeOngkir(imageUrl = dataItem.freeOngkir?.freeOngkirImageUrl
                        ?: "", isActive = dataItem.freeOngkir?.isActive ?: false),
                pdpViewCount = dataItem.pdpView.takeIf { it.toIntOrZero() != 0 } ?: "",
                labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                    dataItem.labelsGroupList?.forEach { add(ProductCardModel.LabelGroup(it.position, it.title, it.type)) }
                }
        )
        productCardModelLiveData.value = productCardModel
    }

    fun getComponentPosition() = componentPosition

    fun setContext(context: Context) {
        this.context = context
    }

    fun getComponentName(): String {
        var componentName = ""
        components.name?.let {
            componentName = it
        }
        return componentName
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    fun getDataItemValue() = dataItem
    fun getProductModelValue() = productCardModelLiveData

    fun handleNavigation() {
        dataItem.value?.applinks?.let { applink ->
            navigate(context, applink)
        }
    }

    fun sendTopAdsClick() {
        dataItem.value?.let {
            val topAdsClickUrl = it.topadsClickUrl
            if (it.isTopads == true && topAdsClickUrl != null) {
                discoveryTopAdsTrackingUseCase.hitClick(this::class.qualifiedName, topAdsClickUrl, it.productId ?: "", it.name ?: "", it.imageUrl ?: "")
            }
        }
    }

    fun sendTopAdsView() {
        dataItem.value?.let {
            val topAdsViewUrl = it.topadsViewUrl
            if (it.isTopads == true && topAdsViewUrl != null && !components.topAdsTrackingStatus) {
                discoveryTopAdsTrackingUseCase.hitImpressions(this::class.qualifiedName, topAdsViewUrl, it.productId ?: "", it.name ?: "", it.imageUrl ?: "")
                components.topAdsTrackingStatus = true
            }
        }
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

}