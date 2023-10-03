package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.analytics.PdpFintechWidgetAnalytics
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetailV3
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetailV3Item
import com.tokopedia.pdp.fintech.helper.Utils
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.pdp_fintech.databinding.PdpFintechWidgetV2LayoutBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.resources.isDarkMode
import timber.log.Timber
import javax.inject.Inject

class PdpFintechWidgetV2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {


    private var idToPriceUrlMap = HashMap<String, FintechPriceURLDataModel>()
    private var priceToMessages = HashMap<String, WidgetDetailV3Item>()
    private var categoryId: String? = null
    private var productID: String? = ""
    private var parentId: String = ""
    private var productPrice: String? = ""
    private var logInStatus = false
    private var binding: PdpFintechWidgetV2LayoutBinding? = null

    @Inject
    lateinit var pdpWidgetAnalytics: dagger.Lazy<PdpFintechWidgetAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var instanceProductUpdateListner: ProductUpdateListner? = null
    private var fintechWidgetViewModel: FintechWidgetViewModel? = null


    init {
        initInjector()
        initView()
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
        fintechWidgetViewModel?.widgetDetailV3LiveData?.observe(parentLifeCycleOwner) {
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

    private fun onSuccessData(it: Success<WidgetDetailV3>) {
        setPriceToChipMap(it.data)
        getMessagesDataAndUpdate(idToPriceUrlMap[productID]?.price)
    }

    private fun setPriceToChipMap(widgetDetail: WidgetDetailV3) {
        widgetDetail.baseWidgetResponse.baseData.list.let { list ->
            list.forEach {
                priceToMessages[it.price.toString()] = it
            }
        }
    }

    private fun initInjector() {
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initView() {
        binding =  PdpFintechWidgetV2LayoutBinding.inflate(
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
            if (this.productID == productID && this.logInStatus == loggedIn && priceToMessages.isNotEmpty()) {
                if (idToPriceUrlMap[productID] != null)
                    getMessagesDataAndUpdate(idToPriceUrlMap[productID]?.price)
                else
                    getDetailFromApi(productID, fintechWidgetViewHolder, shopID, parentId)
            } else {

                getDetailFromApi(productID, fintechWidgetViewHolder, shopID, parentId)
            }
        } catch (e: Exception) {
            instanceProductUpdateListner?.removeWidget()
            Timber.e(e)
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
            fintechWidgetViewModel?.getWidgetV3Data(
                it,
                idToPriceUrlMap,
                shopID,
                parentId
            )
        }
    }

    private fun getMessagesDataAndUpdate(productPrice: String?) {
        if (productPrice != null) {
            this.productPrice = productPrice
        }
        productPrice?.let {
            priceToMessages[it]?.let { model ->
                instanceProductUpdateListner?.showWidget()
                setMessagesWidget(model.messages)
                setClickListener(model)
                setIcon(model)
                pdpWidgetAnalytics.get().sendAnalyticsEvent(
                    analyticsEvent = FintechWidgetAnalyticsEvent.BnplWidgetImpression(
                        usecaseRank = model.usecaseRank.toString(),
                        linkingStatus = model.linkingStatus,
                        userState = model.userState,
                        widgetType = model.widgetType,
                        productId = productID.toString(),
                        productPrice = productPrice.toString(),
                        installmentAmt = model.installmentAmt.toString(),
                        partner = model.gatewayId,
                    )
                )
            } ?: run {
                instanceProductUpdateListner?.removeWidget()
            }
        } ?: run {
            instanceProductUpdateListner?.removeWidget()
        }
    }

    private fun setIcon(model: WidgetDetailV3Item) {
        binding?.sliderIcon?.setImageUrl(
            if (context.isDarkMode()) model.iconUrlDark else model.iconUrlLight
        )
    }

    private fun setClickListener(model: WidgetDetailV3Item) {
        binding?.sliderContainer?.setOnClickListener {
            pdpWidgetAnalytics.get().sendAnalyticsEvent(
                analyticsEvent = FintechWidgetAnalyticsEvent.BnplWidgetClick(
                    usecaseRank = model.usecaseRank.toString(),
                    linkingStatus = model.linkingStatus,
                    userState = model.userState,
                    widgetType = model.widgetType,
                    productId = productID.toString(),
                    productPrice = productPrice.toString(),
                    installmentAmt = model.installmentAmt.toString(),
                    partner = model.productCode
                )
            )

            val fintechRedirectionWidgetData =
                FintechRedirectionWidgetDataClass(gatewayId = model.gatewayId, gatewayCode = model.productCode, tenure = model.tenure)

            instanceProductUpdateListner?.fintechChipClicked(
                fintechRedirectionWidgetData,
                Utils.createRedirectionAppLink(fintechRedirectionWidgetData, this.productID)
            )
        }
    }

    private fun setMessagesWidget(messages: List<String>) {
        val firstTextView = Typography(context).apply {
            text = Html.fromHtml(messages.getOrNull(Int.ZERO) ?: String.EMPTY)
        }

        val secondTextView = Typography(context).apply {
            text = Html.fromHtml(messages.getOrNull(Int.ONE) ?: String.EMPTY)
        }

        binding?.sliderView?.setItems(arrayListOf(firstTextView, secondTextView), messages.size > Int.ONE)
    }

    fun updateIdToPriceMap(
        productIdToPrice: HashMap<String, FintechPriceURLDataModel>,
        productCategoryId: String?

    ) {
        idToPriceUrlMap = productIdToPrice
        categoryId = productCategoryId
    }

    companion object {
        const val LIHAT_SEMU_GATEWAY_ID = "0"
        const val PARAM_PARENT_ID = "parentId"
        const val PARAM_CATEGORY_ID = "categoryId"
    }

}
