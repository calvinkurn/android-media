package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInImeiHelpBottomSheet.Companion.newInstance
import com.tokopedia.tradein.viewmodel.TradeInInitialPriceViewModel
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.*
import javax.inject.Inject

class TradeInInitialPriceFragment : BaseViewModelFragment<TradeInInitialPriceViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var tradeInInitialPriceViewModel: TradeInInitialPriceViewModel
    private var tradeInInitialPriceClick: TradeInInitialPriceClick? = null

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
            DaggerTradeInComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        init()
    }

    fun setListener(tradeInInitialPriceClick: TradeInInitialPriceClick) {
        this.tradeInInitialPriceClick = tradeInInitialPriceClick
    }

    private fun init() {
        arguments?.apply {
            product_name.text = getString(EXTRA_PRODUCT_NAME)
            product_price.text = getString(EXTRA_PRODUCT_PRICE)
            ImageHandler.loadImageWithoutPlaceholder(product_image, getString(EXTRA_PRODUCT_IMAGE))
            tradeInInitialPriceViewModel.checkAndroid10(getString(EXTRA_DEVICE_ID))
            no_imei_value.text = getString(EXTRA_DEVICE_ID)
            setMaxPrice(getString(EXTRA_MAX_PRICE, "-"))
            if (getBoolean(EXTRA_IS_ELIGIBLE, false)) {
                isElligible()
            } else {
                notElligible(getString(EXTRA_NOT_ELIGIBLE_MESSAGE, ""))
            }
        }
        model_value.text = StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString()
        typography_imei_help.setOnClickListener {
            val tradeInImeiHelpBottomSheet = newInstance()
            tradeInImeiHelpBottomSheet.show(childFragmentManager, "")
        }
        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpObservers() {
        tradeInInitialPriceViewModel.imeiStateLiveData.observe(this, Observer { showImei: Boolean ->
            if (showImei) {
                handleImei()
            } else {
                imei_view.hide()
                btn_continue.setOnClickListener {
                    tradeInInitialPriceClick?.onInitialPriceClick(null)
                }
            }
        })
    }

    private fun handleImei() {
        imei_view.show()
        no_imei.hide()
        no_imei_value.hide()
        btn_continue.setOnClickListener {
            when {
                edit_text_imei.text.length == 15 -> {
                    tradeInInitialPriceClick?.onInitialPriceClick(edit_text_imei.text.toString())
                }
                edit_text_imei.text.isEmpty() -> {
                    typography_imei_description.text = getString(R.string.enter_the_imei_number_text)
                    typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
                }
                else -> {
                    typography_imei_description.text = getString(R.string.wrong_imei_string)
                    typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tradein_initial_price_fragment, container, false)
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInInitialPriceViewModel> {
        return TradeInInitialPriceViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        tradeInInitialPriceViewModel = viewModel as TradeInInitialPriceViewModel
    }

    fun notElligible(notEligibleText: String) {
        progress_bar_layout.hide()
        btn_continue.isEnabled = false
        save_upto.hide()
        phone_valid_ticker.show()
        phone_valid_ticker.setTextDescription(notEligibleText)
    }

    private fun setMaxPrice(maxPrice: String) {
        max_price_value.text = maxPrice
        val spannableString = SpannableString(getString(R.string.tradein_save_upto, maxPrice))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 18, 18 + maxPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        save_upto.text = spannableString
    }

    fun isElligible() {
        progress_bar_layout.hide()
        btn_continue.isEnabled = true
    }

    interface TradeInInitialPriceClick {
        fun onInitialPriceClick(imei: String?)
    }

    companion object {
        private const val EXTRA_PRODUCT_NAME = "EXTRA_PRODUCT_NAME"
        private const val EXTRA_PRODUCT_IMAGE = "EXTRA_PRODUCT_IMAGE"
        private const val EXTRA_PRODUCT_PRICE = "EXTRA_PRODUCT_PRICE"
        private const val EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID"
        private const val EXTRA_MAX_PRICE = "EXTRA_MAX_PRICE"
        private const val EXTRA_IS_ELIGIBLE = "EXTRA_IS_ELIGIBLE"
        private const val EXTRA_NOT_ELIGIBLE_MESSAGE = "EXTRA_NOT_ELIGIBLE_MESSAGE"

        fun getFragmentInstance(productName: String, productImage: String, productPrice: String, deviceId: String?, maxPrice: String, isEligible: Boolean, notEligibleMessage: String): Fragment {
            val fragment = TradeInInitialPriceFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_NAME, productName)
            bundle.putString(EXTRA_PRODUCT_IMAGE, productImage)
            bundle.putString(EXTRA_PRODUCT_PRICE, productPrice)
            bundle.putString(EXTRA_DEVICE_ID, deviceId)
            bundle.putString(EXTRA_MAX_PRICE, maxPrice)
            bundle.putString(EXTRA_NOT_ELIGIBLE_MESSAGE, notEligibleMessage)
            bundle.putBoolean(EXTRA_IS_ELIGIBLE, isEligible)
            fragment.arguments = bundle
            return fragment
        }
    }
}