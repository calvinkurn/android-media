package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.analytics.PdpFintechWidgetAnalytics
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetBottomsheet
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp.fintech.view.activity.ActivationBottomSheetActivity
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet.Companion.ACTIVATION_BOTTOMSHEET_DETAIl
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet.Companion.ACTIVATION_WEBVIEW_LINK
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PdpFintechWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {


    private var idToPriceUrlMap = HashMap<String, FintechPriceUrlDataModel>()
    private var priceToChip = HashMap<String, ArrayList<ChipsData>>()
    private var categoryId: String? = null
    private lateinit var productID: String
    private lateinit var productPrice: String

    @Inject
    lateinit var pdpWidgetAnalytics: dagger.Lazy<PdpFintechWidgetAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private lateinit var baseView: View
    private lateinit var titleTextView: Typography
    private lateinit var fintechWidgetAdapter: FintechWidgetAdapter
    private lateinit var instanceProductUpdateListner: ProductUpdateListner
    private lateinit var fintechWidgetViewModel: FintechWidgetViewModel

    private var counter = 0


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
                    setPriceToChipMap(it.data)
                     it.data.baseWidgetResponse?.baseData?.let { baseChipResponse->
                         if(baseChipResponse.list.size>0)
                            titleTextView.text = baseChipResponse.list[0].title
                    }
                    getChipDataAndUpdate(idToPriceUrlMap[productID]?.price)
                }
                is Fail -> {
                    instanceProductUpdateListner.removeWidget()
                }
            }
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
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fintechWidgetAdapter = FintechWidgetAdapter(context, object : WidgetClickListner {
            override fun clickedWidget(cta: Int, url: String, tenure: Int, gatewayBrand:
            String,widgetBottomsheet: WidgetBottomsheet,gateWayID:Int,userStatus:String) {
                customRouter( FintechRedirectionWidgetDataClass(
                    cta =  cta,
                    redirectionUrl =  url,
                    tenure= tenure,
                    gatewayId= gateWayID,
                    productUrl =  idToPriceUrlMap[productID]?.url,
                    gatewayCode =  gatewayBrand,
                    widgetBottomSheet = widgetBottomsheet,
                    userStatus =  userStatus
                ))

            }

        })
        recyclerView.adapter = fintechWidgetAdapter
    }

    private fun customRouter(fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass) {
        fintechRedirectionWidgetDataClass.redirectionUrl?.let {
            val rediretionLink = fintechRedirectionWidgetDataClass.redirectionUrl +
                    "?productID=${this.productID}" +
                    "&tenure=${fintechRedirectionWidgetDataClass.tenure}" +
                    "&productURL=${fintechRedirectionWidgetDataClass.productUrl}" +
                    "&gatewayCode=${fintechRedirectionWidgetDataClass.gatewayCode}" +
                    "&gatewayID=${fintechRedirectionWidgetDataClass.gatewayId}"

            if (fintechRedirectionWidgetDataClass.cta == 2 &&
                fintechRedirectionWidgetDataClass.widgetBottomSheet?.show == false) {
                openWebViewUrl(url = rediretionLink, false)
            } else if (fintechRedirectionWidgetDataClass.cta == 2 &&
                fintechRedirectionWidgetDataClass.widgetBottomSheet?.show == true) {
                val intent = Intent(context,ActivationBottomSheetActivity::class.java)
                intent.putExtra(ACTIVATION_BOTTOMSHEET_DETAIl, fintechRedirectionWidgetDataClass)
                intent.putExtra(ACTIVATION_WEBVIEW_LINK,rediretionLink)
                startActivity(context,intent,null)
            }

            else {

                 RouteManager.route(context, rediretionLink)

            }
        }
    }


    private fun openWebViewUrl(url: String,showTitleBar:Boolean = false) {
        val webViewUrl = String.format(
            Locale.getDefault(),
            "%s?titlebar=${showTitleBar}&url=%s",
            ApplinkConst.WEBVIEW,
            url
        )
        RouteManager.route(context, webViewUrl)
    }


    private fun initView() {
        baseView = inflate(context, R.layout.pdp_fintech_widget_layout, this)
        titleTextView = baseView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.quickText)
    }


    fun updateProductId(
        productID: String,
        fintechWidgetViewHolder: ProductUpdateListner
    ) {
        try {
            this.productID = productID
            this.instanceProductUpdateListner = fintechWidgetViewHolder
            if (counter == 0) {
                counter++
                categoryId?.let {
                    fintechWidgetViewModel.getWidgetData(
                        it,
                        idToPriceUrlMap
                    )
                }
            } else {
                if (priceToChip.size != 0)
                    getChipDataAndUpdate(idToPriceUrlMap[productID]?.price)
                else
                    categoryId?.let {
                        fintechWidgetViewModel.getWidgetData(it, idToPriceUrlMap)
                    }
            }

        } catch (e: Exception) {
            instanceProductUpdateListner.removeWidget()
        }
    }

    private fun getChipDataAndUpdate(productPrice: String?) {
        if (productPrice != null) {
            this.productPrice = productPrice
        }
        productPrice?.let {
            priceToChip[it]?.let { chipList ->
                instanceProductUpdateListner.showWidget()
                fintechWidgetAdapter.setData(chipList)
                sendPdpImpression(chipList)
            } ?: run {
                instanceProductUpdateListner.removeWidget()
            }
        } ?: run {
            instanceProductUpdateListner.removeWidget()
        }
    }

    private fun sendPdpImpression(chipList: ArrayList<ChipsData>) {
        for(i in 0 until chipList.size) {
            chipList[i].userStatus?.let { userStatus->
                chipList[i].productCode?.let { partnerName->
                    pdpWidgetAnalytics.get().sendAnalyticsEvent(FintechWidgetAnalyticsEvent.PdpWidgetImpression(productID, userStatus,
                        partnerName
                    ))
                }
            }
        }

    }

    fun updateIdToPriceMap(
        productIdToPrice: HashMap<String, FintechPriceUrlDataModel>,
        productCategoryId: String?,
    ) {
        idToPriceUrlMap = productIdToPrice
        categoryId = productCategoryId
    }

}

data class FintechPriceUrlDataModel(
    var url:String? = null,
    var price:String? = null
)
