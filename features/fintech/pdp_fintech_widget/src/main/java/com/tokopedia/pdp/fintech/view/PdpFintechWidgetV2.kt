package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.text.Html
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
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.analytics.FintechWidgetAnalyticsEvent
import com.tokopedia.pdp.fintech.analytics.PdpFintechWidgetAnalytics
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetailV3
import com.tokopedia.pdp.fintech.helper.Utils
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.pdp_fintech.databinding.PdpFintechWidgetLayoutBinding
import com.tokopedia.pdp_fintech.databinding.PdpFintechWidgetV2LayoutBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PdpFintechWidgetV2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {


    private var idToPriceUrlMap = HashMap<String, FintechPriceURLDataModel>()
    private var priceToMessages = HashMap<String, WidgetV2UIModel>()
    private var categoryId: String? = null
    private var productID: String? = ""
    private var parentId: String = ""
    private var productPrice: String? = ""
    private val NOT_BRANDER_CHIPS = "NON BRANDED"
    private val BRANDER_CHIPS = "BRANDED"
    private var logInStatus = false
    private lateinit var binding: PdpFintechWidgetV2LayoutBinding

    @Inject
    lateinit var pdpWidgetAnalytics: dagger.Lazy<PdpFintechWidgetAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var instanceProductUpdateListner: ProductUpdateListner? = null
    private lateinit var fintechWidgetViewModel: FintechWidgetViewModel


    init {
        initInjector()
        initView()

//        val v1 = Typography(context).apply {
//            text = Html.fromHtml("<span style=\"color:#212121;\">Bisa cicil mulai </span> <b>Rp474.916/bulan! <span style=\"color:#00AA5B\">Lihat Cicilan</span></b>")
//        }
//
//        val v2 = Typography(context).apply {
//            text = Html.fromHtml("<span style=\"color: #009F92\">+ Diskon 25% s.d. Rp100.000</span>")
//        }
////        binding.installmentIcon.setImageUrl("https://images.tokopedia.net/img/fintech/paylater/gopay-later-pdp-logo.png")
//        binding.sliderView.setItems(arrayListOf(v1, v2))
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
        fintechWidgetViewModel.widgetDetailV3LiveData.observe(parentLifeCycleOwner) {
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
                priceToMessages[it.price.toString()] = WidgetV2UIModel(it.messages, it.iconUrlLight, it.iconUrlDark)
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
            fintechWidgetViewModel.getWidgetV3Data(
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
            } ?: run {
                instanceProductUpdateListner?.removeWidget()
            }
        } ?: run {
            instanceProductUpdateListner?.removeWidget()
        }

//        instanceProductUpdateListner?.showWidget()
//        priceToMessages["400000.0"]?.let {
//            instanceProductUpdateListner?.showWidget()
//            setMessagesWidget(it.messages)
//            binding.sliderIcon.setImageUrl(it.iconUrlLight)
//        }
    }

    private fun setMessagesWidget(messages: List<String>) {
        val firstTextView = Typography(context).apply {
            text = Html.fromHtml(messages.getOrNull(Int.ZERO))
        }

        val secondTextView = Typography(context).apply {
            text = Html.fromHtml(messages.getOrNull(Int.ONE))
        }
        binding.sliderView.setItems(arrayListOf(firstTextView, secondTextView))
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

data class WidgetV2UIModel(
    val messages: List<String>,
    val iconUrlLight: String,
    val iconUrlDark: String,
)
