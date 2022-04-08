package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.analytics.PdpFintechWidgetAnalytics
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PdpFintechWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {


    private var idToPriceMap = HashMap<String, FintechPriceDataModel>()
    private var priceToChip = HashMap<String, ArrayList<ChipsData>>()
    private var categoryId: String? = null
    private var productID: String? = ""
    private var productPrice: String? = ""
    private val NOT_BRANDER_CHIPS = "not branded chips"
    private val BRANDER_CHIPS = "branded chips"
    private var logInStatus = false


    @Inject
    lateinit var pdpWidgetAnalytics: dagger.Lazy<PdpFintechWidgetAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var baseView: View? = null
    private lateinit var titleTextView: Typography
    private var fintechWidgetAdapter: FintechWidgetAdapter? = null
    private var instanceProductUpdateListner: ProductUpdateListner? = null
    private lateinit var fintechWidgetViewModel: FintechWidgetViewModel


    init {
        initInjector()
        initView()
        initRecycler()
    }


    fun updateBaseFragmentContext(
        parentViewModelStore: ViewModelStore,
        parentLifeCycleOwner: LifecycleOwner
    ) {
        fintechWidgetViewModel =
            ViewModelProvider(parentViewModelStore, viewModelFactory.get()).get(
                FintechWidgetViewModel::class.java
            )
        observeWidgetInfo(parentLifeCycleOwner)

    }

    private fun observeWidgetInfo(parentLifeCycleOwner: LifecycleOwner) {
        fintechWidgetViewModel.widgetDetailLiveData.observe(parentLifeCycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessData(it)
                }
                is Fail -> {
                    instanceProductUpdateListner?.removeWidget()
                }
            }
        }
    }

    private fun onSuccessData(it: Success<WidgetDetail>) {
        setPriceToChipMap(it.data)
        updateTestForChip(it.data)
        getChipDataAndUpdate(idToPriceMap[productID]?.price)
    }

    private fun updateTestForChip(widgetDetail: WidgetDetail) {
        widgetDetail.baseWidgetResponse?.baseData?.let { baseChipResponse ->
            if (baseChipResponse.list.size > 0)
                titleTextView.text = baseChipResponse.list[0].title
        }
    }

    private fun setPriceToChipMap(widgetDetail: WidgetDetail) {
        widgetDetail.baseWidgetResponse?.baseData?.let {
            for (i in it.list.indices) {
                priceToChip[it.list[i].price.toString()] = it.list[i].chips
            }
        }
    }


    private fun initInjector() {
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initRecycler() {
        val recyclerView = baseView?.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fintechWidgetAdapter = FintechWidgetAdapter(context, object : WidgetClickListner {
            override fun clickedWidget(
                fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass
            ) {
                routeToPdp(fintechRedirectionWidgetDataClass)
            }

        })
        recyclerView?.adapter = fintechWidgetAdapter
    }

    private fun sendClickEvent(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
        rediretionLink: String
    ) {

        if (fintechRedirectionWidgetDataClass.gatewayId == 0)
            FintechWidgetAnalyticsEvent.PdpWidgetClick(
                this.productID,
                fintechRedirectionWidgetDataClass.linkingStatus,
                fintechRedirectionWidgetDataClass.userStatus, "NON-BRANDED",
                "${fintechRedirectionWidgetDataClass.tenure}",
                fintechRedirectionWidgetDataClass.gatewayPartnerName, rediretionLink
            )
        else
            FintechWidgetAnalyticsEvent.PdpWidgetClick(
                this.productID,
                fintechRedirectionWidgetDataClass.linkingStatus,
                fintechRedirectionWidgetDataClass.userStatus, "BRANDED",
                "${fintechRedirectionWidgetDataClass.tenure}",
                fintechRedirectionWidgetDataClass.gatewayPartnerName, rediretionLink
            )
    }


    private fun routeToPdp(fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass) {
        val rediretionLink = fintechRedirectionWidgetDataClass.redirectionUrl +
                "?productID=${this.productID}" +
                "&tenure=${fintechRedirectionWidgetDataClass.tenure}" +
                "&gatewayCode=${fintechRedirectionWidgetDataClass.gatewayCode}" +
                "&gatewayID=${fintechRedirectionWidgetDataClass.gatewayId}" +
                "&productURL=${setProductUrl()}"

        sendClickEvent(fintechRedirectionWidgetDataClass, rediretionLink)
        instanceProductUpdateListner?.fintechChipClicked(
            fintechRedirectionWidgetDataClass,
            rediretionLink
        )
    }

    private fun setProductUrl(): String {
        return UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, this.productID).encodeToUtf8()
    }


    private fun initView() {
        baseView = inflate(context, R.layout.pdp_fintech_widget_layout, this)
        baseView?.let {
            titleTextView = it.findViewById(R.id.quickText)
        }
    }


    fun updateProductId(
        productID: String,
        fintechWidgetViewHolder: ProductUpdateListner,
        loggedIn: Boolean
    ) {
        try {
            fintechWidgetViewHolder.removeWidget()
            if (this.productID == productID && this.logInStatus == loggedIn && priceToChip.isNotEmpty()) {
                if (idToPriceMap[productID] != null)
                    getChipDataAndUpdate(idToPriceMap[productID]?.price)
                else
                    getDetailFromApi(productID, fintechWidgetViewHolder)
            } else {

                getDetailFromApi(productID, fintechWidgetViewHolder)
            }
        } catch (e: Exception) {
            instanceProductUpdateListner?.removeWidget()
        } finally {
            this.logInStatus = loggedIn
        }
    }

    private fun getDetailFromApi(
        productID: String,
        fintechWidgetViewHolder: ProductUpdateListner
    ) {
        this.productID = productID
        this.instanceProductUpdateListner = fintechWidgetViewHolder
        categoryId?.let {
            fintechWidgetViewModel.getWidgetData(
                it,
                idToPriceMap
            )
        }
    }

    private fun getChipDataAndUpdate(productPrice: String?) {
        if (productPrice != null) {
            this.productPrice = productPrice
        }
        productPrice?.let {
            priceToChip[it]?.let { chipList ->
                instanceProductUpdateListner?.showWidget()
                fintechWidgetAdapter?.setData(chipList)
                sendPdpImpression(chipList)
            } ?: run {
                instanceProductUpdateListner?.removeWidget()
            }
        } ?: run {
            instanceProductUpdateListner?.removeWidget()
        }
    }

    private fun sendPdpImpression(chipList: ArrayList<ChipsData>) {
        for (i in 0 until chipList.size) {
            if (chipList[i].gatewayId == 0)
                pdpWidgetAnalytics.get().sendAnalyticsEvent(
                    FintechWidgetAnalyticsEvent.PdpWidgetImpression(
                        productID ?: "",
                        chipList[i].linkingStatus ?: "",
                        chipList[i].userStatus ?: "",
                        NOT_BRANDER_CHIPS,
                        chipList[i].productCode ?: ""
                    )
                )
            else
                pdpWidgetAnalytics.get().sendAnalyticsEvent(
                    FintechWidgetAnalyticsEvent.PdpWidgetImpression(
                        productID ?: "",
                        chipList[i].linkingStatus ?: "",
                        chipList[i].userStatus ?: "",
                        BRANDER_CHIPS,
                        chipList[i].productCode ?: ""
                    )
                )

        }

    }

    fun updateIdToPriceMap(
        productIdToPrice: HashMap<String, FintechPriceDataModel>,
        productCategoryId: String?

    ) {
        idToPriceMap = productIdToPrice
        categoryId = productCategoryId
    }

    companion object {
        const val ACTIVATION_LINKINING_FLOW = 2
    }

}

data class FintechPriceDataModel(
    var price: String? = null
)
