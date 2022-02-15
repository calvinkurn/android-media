package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.TradeInPromoActivity
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInExchangeMethodBS
import com.tokopedia.tradein.viewmodel.TradeInHomePageFragmentVM
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class TradeInHomePageFragment : BaseViewModelFragment<TradeInHomePageFragmentVM>(), ChooseAddressWidget.ChooseAddressWidgetListener{

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInHomePageFragmentVM
    private lateinit var tradeInHomePageVM: TradeInHomePageVM
    private var chooseAddressWidget : ChooseAddressWidget? = null
    private var userAddressData: LocalCacheModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tradein_initial_price_parent_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelProvider)
            tradeInHomePageVM = viewModelProvider.get(TradeInHomePageVM::class.java)
        }
        addObservers()
        context?.let {
            userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
        }
        onTradeInDetailSuccess()
    }

    private fun onTradeInDetailSuccess() {
        view?.apply {
            arguments?.getString(CACHE_ID, "")?.let {
                setUpPdpData(this, it)
            }
            chooseAddressWidget = findViewById(R.id.tradein_choose_address_widget)
            chooseAddressWidget?.bindChooseAddress(this@TradeInHomePageFragment)
            findViewById<View>(R.id.exchange_layout).setOnClickListener {
                val bottomSheet = TradeInExchangeMethodBS.newInstance(arrayListOf(), tradeInHomePageVM.is3PLSelected)
                bottomSheet.show(childFragmentManager, "")
            }
            findViewById<View>(R.id.tradein_promo_view).setOnClickListener {
                startActivity(TradeInPromoActivity.getIntent(context, "LAKU6EMANGASIX"))
            }
            findViewById<View>(R.id.collapse_view).setOnClickListener {
                findViewById<ConstraintLayout>(R.id.parent_collapse).let { layout->
                    if(layout.isVisible){
                        layout.hide()
                    } else
                        layout.show()
                }
            }
        }
    }

    private fun setUpPdpData(view: View, id: String) {
        view.apply {
            viewModel.getPDPData(context, id)?.let {
                findViewById<Typography>(R.id.slashed_price).let{ price->
                    price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, true)
                    price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                findViewById<Typography>(R.id.product_price).text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.productPrice, true)
                findViewById<Typography>(R.id.product_name).text = it.productName
                if(!it.productImage.isNullOrEmpty()){
                    (findViewById<ImageUnify>(R.id.product_image)).setImageUrl(it.productImage!!)
                }
                if(!it.shopBadge.isNullOrEmpty()){
                    (findViewById<ImageUnify>(R.id.image_shop_badge)).setImageUrl(it.shopBadge!!)
                }
                findViewById<Typography>(R.id.shop_name).text = it.shopName
                findViewById<Typography>(R.id.shop_location).text = it.shopLocation
            }
        }
    }

    private fun addObservers() {

    }


    private fun showToast(message: String, actionText: String, listener: View.OnClickListener) {
        Toaster.build(requireView(),
            message,
            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionText, listener).show()
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
        DaggerTradeInComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomePageFragmentVM> {
        return TradeInHomePageFragmentVM::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomePageFragmentVM
    }

    companion object {
        const val CACHE_ID = "Trade in cache id"
        fun getFragmentInstance(cacheId : String): Fragment {
            val bundle = Bundle().apply {
                putString(CACHE_ID, cacheId)
            }
            return TradeInHomePageFragment().apply {
                arguments = bundle
            }
        }
    }

    private fun updateChooseAddressWidget() {
        chooseAddressWidget?.updateWidget()
    }

    private fun checkAddressUpdate() {
        context?.let {
            if (userAddressData != null) {
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(it, userAddressData!!)) {
                    userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
//                    showLoadingWithRefresh()
                }
            }
        }
    }
    override fun onLocalizingAddressUpdatedFromWidget() {
        updateChooseAddressWidget()
        checkAddressUpdate()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {

    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressWidget?.hide()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {

    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return TradeinConstants.TRADE_IN_HOST_SOURCE
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return TradeinConstants.TRADE_IN_HOST_TRACKING_SOURCE
    }

    override fun onLocalizingAddressLoginSuccess() {

    }
}