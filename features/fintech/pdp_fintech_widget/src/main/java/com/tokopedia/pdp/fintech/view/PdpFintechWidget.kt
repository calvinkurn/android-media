package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.analytics.PdpFintechWidgetAnalytics
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.helper.Utils
import com.tokopedia.pdp.fintech.helper.Utils.createRedirectionAppLink
import com.tokopedia.pdp.fintech.helper.Utils.returnRouteObject
import com.tokopedia.pdp.fintech.helper.Utils.setListOfData
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.pdp_fintech.databinding.PdpFintechWidgetLayoutBinding
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
    private var parentId: String = ""
    private var productPrice: String? = ""
    private val NOT_BRANDER_CHIPS = "NON BRANDED"
    private val BRANDER_CHIPS = "BRANDED"
    private var logInStatus = false
    private lateinit var binding: PdpFintechWidgetLayoutBinding




    @Inject
    lateinit var pdpWidgetAnalytics: dagger.Lazy<PdpFintechWidgetAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var fintechWidgetAdapter: FintechWidgetAdapter? = null
    private var instanceProductUpdateListner: ProductUpdateListner? = null
    private lateinit var fintechWidgetViewModel: FintechWidgetViewModel


    init {
        initInjector()
        initView()
        initRecycler()

        val v1 = Typography(context).apply {
            text = "First Row"
        }

        val v2 = Typography(context).apply {
            text = "Second Row"
        }

        val v3 = Typography(context).apply {
            text = "Third Row"
        }

        val v4 = Typography(context).apply {
            text = "Fourth Row"
        }
        binding.sliderView.setItems(arrayListOf(v1, v2))
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
            if (baseChipResponse.list.size > 0) {
                binding.quickText.text = baseChipResponse.list[0].title
                setSeeMoreButton(baseChipResponse.list[0].chips)
            }

        }
    }

    private fun setSeeMoreButton(chips: ArrayList<ChipsData>) {
        if(chips[chips.size-1].gatewayId == LIHAT_SEMU_GATEWAY_ID)
        {
            binding.pdpFintechWidgetSeeMore.visibility = VISIBLE
            binding.pdpFintechWidgetSeeMore.text = chips[chips.size-1].header?.parseAsHtml()
            setSeeMoreListner(chips[chips.size-1])
        }
    }

    private fun setSeeMoreListner(chipsData: ChipsData) {
        val listOfAllChecker   = setListOfData(chipsData)
        if(Utils.safeLet(listOfAllChecker) == true) {
            binding.pdpFintechWidgetSeeMore.setOnClickListener {
                routeToPdp(
                    returnRouteObject(chipsData).also { data ->
                        data.categoryId = categoryId.orEmpty()
                        data.parentId = parentId
                    }
                )
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

        binding.recyclerItems.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fintechWidgetAdapter = FintechWidgetAdapter(context, object : WidgetClickListner {
            override fun clickedWidget(
                fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass
            ) {
                fintechRedirectionWidgetDataClass.categoryId = categoryId.orEmpty()
                fintechRedirectionWidgetDataClass.parentId = parentId
                routeToPdp(fintechRedirectionWidgetDataClass)
            }

        })
        binding.recyclerItems.adapter = fintechWidgetAdapter
    }

    private fun sendClickEvent(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
        rediretionLink: String
    ) {

        if (fintechRedirectionWidgetDataClass.gatewayId == "0")
            pdpWidgetAnalytics.get().sendAnalyticsEvent(
                FintechWidgetAnalyticsEvent.PdpWidgetClick(
                    this.productID,
                    fintechRedirectionWidgetDataClass.linkingStatus,
                    fintechRedirectionWidgetDataClass.userStatus, NOT_BRANDER_CHIPS,
                    fintechRedirectionWidgetDataClass.installmentAmout,
                    "${fintechRedirectionWidgetDataClass.tenure}",
                    fintechRedirectionWidgetDataClass.gatewayCode,
                    rediretionLink,
                    fintechRedirectionWidgetDataClass.promoName,
                    fintechRedirectionWidgetDataClass.subtitle
                )
            )
        else
            pdpWidgetAnalytics.get().sendAnalyticsEvent(
                FintechWidgetAnalyticsEvent.PdpWidgetClick(
                    this.productID,
                    fintechRedirectionWidgetDataClass.linkingStatus,
                    fintechRedirectionWidgetDataClass.userStatus, BRANDER_CHIPS,
                    fintechRedirectionWidgetDataClass.installmentAmout,
                    "${fintechRedirectionWidgetDataClass.tenure}",
                    fintechRedirectionWidgetDataClass.gatewayCode,
                    rediretionLink,
                    fintechRedirectionWidgetDataClass.promoName,
                    fintechRedirectionWidgetDataClass.subtitle
                )
            )
    }


    private fun routeToPdp(fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass) {
        val redirectionLink = createRedirectionAppLink(fintechRedirectionWidgetDataClass,this.productID)
        sendClickEvent(fintechRedirectionWidgetDataClass, redirectionLink)
        instanceProductUpdateListner?.fintechChipClicked(
            fintechRedirectionWidgetDataClass,
            redirectionLink
        )
    }

    private fun setProductUrl(): String {
        return UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, this.productID).encodeToUtf8()
    }


    private fun initView() {
       binding =  PdpFintechWidgetLayoutBinding.inflate(
            LayoutInflater.from(context),this,true
        )
    }


    fun updateProductId(
        productID: String,
        fintechWidgetViewHolder: ProductUpdateListner,
        loggedIn: Boolean,
        shopID: String,
        parentId: String
    ) {
        try {
            if (this.productID == productID && this.logInStatus == loggedIn && priceToChip.isNotEmpty()) {
                if (idToPriceMap[productID] != null)
                    getChipDataAndUpdate(idToPriceMap[productID]?.price)
                else
                    getDetailFromApi(productID, fintechWidgetViewHolder, shopID, parentId)
            } else {

                getDetailFromApi(productID, fintechWidgetViewHolder, shopID, parentId)
            }
        } catch (e: Exception) {
            instanceProductUpdateListner?.removeWidget()
        } finally {
            this.logInStatus = loggedIn
        }
    }

    private fun getDetailFromApi(
        productID: String,
        fintechWidgetViewHolder: ProductUpdateListner,
        shopID: String,
        parentId: String
    ) {
        this.productID = productID
        this.parentId = parentId
        this.instanceProductUpdateListner = fintechWidgetViewHolder
        categoryId?.let {
            fintechWidgetViewModel.getWidgetData(
                it,
                idToPriceMap,
                shopID,
                parentId
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
            if (chipList[i].gatewayId == "0")
                pdpWidgetAnalytics.get().sendAnalyticsEvent(
                    FintechWidgetAnalyticsEvent.PdpWidgetImpression(
                        productID ?: "",
                        chipList[i].linkingStatus ?: "",
                        chipList[i].userStatus ?: "",
                        NOT_BRANDER_CHIPS,
                        chipList[i].productCode ?: "",
                        chipList[i].promoName,
                        chipList[i].subheader
                    )
                )
            else
                pdpWidgetAnalytics.get().sendAnalyticsEvent(
                    FintechWidgetAnalyticsEvent.PdpWidgetImpression(
                        productID ?: "",
                        chipList[i].linkingStatus ?: "",
                        chipList[i].userStatus ?: "",
                        BRANDER_CHIPS,
                        chipList[i].productCode ?: "",
                        chipList[i].promoName,
                        chipList[i].subheader
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
        const val LIHAT_SEMU_GATEWAY_ID = "0"
        const val PARAM_PARENT_ID = "parentId"
        const val PARAM_CATEGORY_ID = "categoryId"
    }

}

data class FintechPriceDataModel(
    var price: String? = null
)
