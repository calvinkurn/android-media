package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.tradein.R
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.mapper.TradeInMapper
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInOutsideCoverageBottomSheet
import com.tokopedia.tradein.viewmodel.TradeInAddressViewModel
import kotlinx.android.synthetic.main.tradein_address_fragment.*
import javax.inject.Inject

private const val PINPOINT_ACTIVITY_REQUEST_CODE = 1302
private const val KERO_TOKEN = "token"
private const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"

class TradeInAddressFragment : BaseViewModelFragment<TradeInAddressViewModel>() {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var tradeInAddressViewModel: TradeInAddressViewModel

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
            DaggerTradeInComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObservers()
        getAddress()
    }

    fun getAddress(){
        arguments?.apply {
            tradeInAddressViewModel.getAddress(getString(EXTRA_ORIGIN, ""), getInt(EXTRA_WEIGHT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tradein_address_fragment, container, false)
    }

    private fun setUpObservers() {
        tradeInAddressViewModel.getAddressLiveData().observe(this, Observer {
            if (it.defaultAddress != null) {
                setAddress(it.defaultAddress)
            } else {
                val intent = RouteManager.getIntent(
                        context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                intent.putExtra(KERO_TOKEN, it.token)
                startActivityForResult(intent, PINPOINT_ACTIVITY_REQUEST_CODE)
            }
        })

        tradeInAddressViewModel.getTradeInEligibleLiveData().observe(this, Observer {
            if (it) {
                address_valid_ticker.apply {
                    show()
                    setTextDescription(getString(R.string.tradein_ticker_continue))
                    btn_continue.apply {
                        isEnabled = true
                        setOnClickListener {
                            //redirect to initial price page
                        }
                    }
                }
            } else {
                address_valid_ticker.hide()
                btn_continue.isEnabled = false
                val bottomSheet = TradeInOutsideCoverageBottomSheet.newInstance(arguments?.getString(EXTRA_PRODUCT_NAME, "")
                        ?: "")
                bottomSheet.show(childFragmentManager, "")
            }
        })

        tradeInAddressViewModel.getProgBarVisibility().observe(this, Observer {
            if (it)
                progress_bar_layout.show()
            else
                progress_bar_layout.hide()
        })

        tradeInAddressViewModel.getErrorMessage().observe(this, Observer {
            global_error.errorTitle.text = it
            global_error.setType(GlobalError.SERVER_ERROR)
            global_error.setActionClickListener {
                getAddress()
            }
            global_error.show()
        })
    }

    private fun setAddress(defaultAddress: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data) {
        address_name.text = defaultAddress.addrName
        address.text = (defaultAddress.address1 + ", "
                + defaultAddress.districtName + ", "
                + defaultAddress.cityName + ", "
                + defaultAddress.provinceName)
        change_address.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT_ADDRESS_SELECTION)
            intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST)
            startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS) run {
            onResultFromRequestCodeAddressOptions(resultCode, data)
        } else if (requestCode == PINPOINT_ACTIVITY_REQUEST_CODE) {
            onResultFromRequestCodeNewAddress(data)
        }
    }

    private fun onResultFromRequestCodeNewAddress(data: Intent?) {
        if (data != null && data.hasExtra(EXTRA_ADDRESS_NEW)) {
            val address = data.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
            if (address != null)
                tradeInAddressViewModel.setAddress(TradeInMapper.mapSavedAddressToKeroAddress(address))
        }
    }


    private fun onResultFromRequestCodeAddressOptions(resultCode: Int, data: Intent?) {
        if (resultCode == CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS) {
            when (resultCode) {
                CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS -> {
                    if (data != null) {
                        val addressModel = data.getParcelableExtra<RecipientAddressModel>(
                                CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA
                        )
                        if (addressModel != null)
                            tradeInAddressViewModel.setAddress(TradeInMapper.mapAddressToKeroAddress(addressModel))
                    }
                }

                else -> activity?.finish()
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInAddressViewModel> {
        return TradeInAddressViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        tradeInAddressViewModel = viewModel as TradeInAddressViewModel
    }

    companion object {

        private const val EXTRA_ORIGIN = "EXTRA_ORIGIN"
        private const val EXTRA_WEIGHT = "EXTRA_WEIGHT"
        private const val EXTRA_PRODUCT_NAME = "EXTRA_PRODUCT_NAME"

        fun getFragmentInstance(origin: String?, weight: Int, productName: String): Fragment {
            val fragment = TradeInAddressFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_ORIGIN, origin)
            bundle.putInt(EXTRA_WEIGHT, weight)
            bundle.putString(EXTRA_PRODUCT_NAME, productName)
            fragment.arguments = bundle
            return fragment
        }
    }
}