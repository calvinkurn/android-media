package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MerchantVoucherGridItemViewModel(
    application: Application,
    component: ComponentsItem,
    val position: Int
): DiscoveryBaseViewModel(component), CoroutineScope {

    private val componentData = MutableLiveData<MerchantVoucherGridModel>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        component.data?.firstOrNull()?.let {
            componentData.value = it.mapToAutomateCouponGridModel()
        }
    }

    private fun DataItem.mapToAutomateCouponGridModel(): MerchantVoucherGridModel {
        return MerchantVoucherGridModel(
            buttonText = this.buttonText.orEmpty(),
            url = shopInfo?.url.orEmpty(),
            appLink = shopInfo?.appLink.orEmpty(),
            automateCouponModel = AutomateCouponModel.Grid(
                backgroundUrl = backgroundImageUrl.orEmpty(),
                benefit = DynamicColorText(
                    value = "$subtitle"
                ),
                tnc = DynamicColorText(
                    value = subtitle_1.orEmpty(),
                ),
                iconUrl = shopInfo?.iconUrl.orEmpty(),
                shopName = DynamicColorText(
                    value = shopInfo?.name.orEmpty()
                ),
                type = DynamicColorText(
                    value = title.orEmpty(),
                    colorHex = fontColor.orEmpty(),
                ),
                badgeText = badge.ifEmpty { null },
                isTopads = isTopads ?: false,
                topAdsClickUrl = topadsClickUrl
            )
        )
    }

    fun getComponentData(): LiveData<MerchantVoucherGridModel> = componentData
}
