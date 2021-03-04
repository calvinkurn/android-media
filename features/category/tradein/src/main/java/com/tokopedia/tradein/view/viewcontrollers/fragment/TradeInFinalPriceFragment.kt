package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInFinalPriceDetailsBottomSheet
import com.tokopedia.tradein.viewmodel.FinalPriceViewModel
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.tradein_address_fragment.global_error
import kotlinx.android.synthetic.main.tradein_address_fragment.iv_back
import kotlinx.android.synthetic.main.tradein_address_fragment.progress_bar_layout
import kotlinx.android.synthetic.main.tradein_final_price_fragment.*
import kotlinx.android.synthetic.main.tradein_final_price_fragment.btn_continue
import javax.inject.Inject

class TradeInFinalPriceFragment : BaseViewModelFragment<FinalPriceViewModel>() {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: FinalPriceViewModel
    private lateinit var tradeinHomeViewModel: TradeInHomeViewModel
    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics

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
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelProvider)
            tradeinHomeViewModel = viewModelProvider.get(TradeInHomeViewModel::class.java)
        }
        init()
    }

    private fun setUpObservers() {
        viewModel.deviceDiagData.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isEligible == true)
                renderDetails(it)
            else {
                global_error.show()
                global_error.setType(GlobalError.SERVER_ERROR)
                global_error.errorTitle.text = it.message
                global_error.errorDescription.hide()
                global_error.setActionClickListener {
                    viewModel.getDiagnosticData()
                }
            }
        })

        viewModel.getProgBarVisibility().observe(viewLifecycleOwner, Observer {
            if (it)
                progress_bar_layout.show()
            else
                progress_bar_layout.hide()
        })

        viewModel.getWarningMessage().observe(viewLifecycleOwner, Observer {
            global_error.show()
            global_error.setType(GlobalError.SERVER_ERROR)
            global_error.errorDescription.text = it
            global_error.setActionClickListener {
                viewModel.getDiagnosticData()
            }
        })
    }

    private fun renderDetails(dataResponse: DeviceDataResponse) {
        valid_till.text = getString(R.string.tradein_price_valid_until, dataResponse.expiryTimeFmt)
        tv_final_amt.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(dataResponse.remainingPrice, true)
        tukar_tambah_price.text = getString(R.string.tradein_minus, CurrencyFormatUtil.convertPriceValueToIdrFormat(dataResponse.oldPrice, true))
        total_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(dataResponse.remainingPrice, true)
        val deviceReview: ArrayList<String> = arrayListOf()
        arguments?.apply {
            deviceReview.add(getString(R.string.tradein_model_with_name, getString(EXTRA_DEVICE_DISPLAY_NAME, "")))
        }
        deviceReview.addAll(dataResponse.deviceReview)
        tv_link_detail.setOnClickListener {
            tradeInAnalytics.clickFinalPriceCheckDetails()
            val bottomSheet = TradeInFinalPriceDetailsBottomSheet.newInstance(deviceReview)
            bottomSheet.show(childFragmentManager, "")
        }
        btn_continue.setOnClickListener {
            tradeinHomeViewModel.onHargaFinalClick(dataResponse.deviceAttr?.imei?.get(0), dataResponse.oldPrice.toString())
        }
    }

    private fun init() {
        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.tradeInParams = tradeinHomeViewModel.tradeInParams
        arguments?.apply {
            product_name.text = viewModel.tradeInParams?.productName
            product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(viewModel.tradeInParams?.newPrice
                    ?: 0, true)
        }
        setUpObservers()
        viewModel.getDiagnosticData()
        tradeInAnalytics.openFinalPricePage()

        tv_slash_amt.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = CurrencyFormatUtil.convertPriceValueToIdrFormat(viewModel.tradeInParams?.newPrice
                    ?: 0, true)
        }
        ImageHandler.LoadImage(pdp_image, viewModel.tradeInParams?.productImage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tradein_final_price_fragment, container, false)
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<FinalPriceViewModel> {
        return FinalPriceViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as FinalPriceViewModel
    }

    companion object {

        private const val EXTRA_DEVICE_DISPLAY_NAME = "EXTRA_DEVICE_DISPLAY_NAME"

        fun getFragmentInstance(deviceDisplayName: String?): Fragment {
            return TradeInFinalPriceFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_DEVICE_DISPLAY_NAME, deviceDisplayName)
                }
            }
        }
    }
}