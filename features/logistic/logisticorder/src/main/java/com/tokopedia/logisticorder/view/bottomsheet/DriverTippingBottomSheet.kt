package com.tokopedia.logisticorder.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.BottomsheetTippingGojekBinding
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent
import com.tokopedia.logisticorder.di.TrackingPageComponent
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.view.TrackingPageViewModel
import com.tokopedia.logisticorder.view.adapter.TippingValueAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class DriverTippingBottomSheet: BottomSheetUnify(), HasComponent<TrackingPageComponent>, TippingValueAdapter.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<BottomsheetTippingGojekBinding>()
    private val viewModel: TrackingPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TrackingPageViewModel::class.java]
    }

    private var orderId: String? = ""
    private var trackingDataModel: TrackingDataModel? = null
    private lateinit var tippingValueAdapter: TippingValueAdapter

    init {
        setOnDismissListener {
            dismiss()
        }

        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniInjector()
        initChildLayout()
        setInitialViewState()
        initObserver()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

   private fun iniInjector() {
       component.inject(this)
   }

    private fun initChildLayout() {
        binding = BottomsheetTippingGojekBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    private fun setInitialViewState() {
        setTitle("")
        binding.progressBar.visibility = View.VISIBLE
        binding.paymentTippingLayout.visibility = View.GONE
        binding.resultTippingLayout.visibility = View.GONE
        viewModel.getDriverTipsData(orderId)
    }

    override fun getComponent(): TrackingPageComponent {
        return DaggerTrackingPageComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }


    private fun initObserver() {
        viewModel.driverTipData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    setDriverTipLayout(it.data)
                }

                is Fail -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setDriverTipLayout(logisticDriverModel: LogisticDriverModel) {
        when (logisticDriverModel.status) {
            100 -> {
                //driver found
                setTitle(getString(com.tokopedia.logisticorder.R.string.title_prepayment_tipping))
                binding.paymentTippingLayout.visibility = View.VISIBLE
                binding.resultTippingLayout.visibility = View.GONE
                binding.btnTipping.text = getString(com.tokopedia.logisticorder.R.string.button_tipping_payment)

                val chipsLayoutManagerTipping = ChipsLayoutManager.newBuilder(binding.root.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()

                ViewCompat.setLayoutDirection(binding.rvChipsTip, ViewCompat.LAYOUT_DIRECTION_LTR)
                tippingValueAdapter = TippingValueAdapter(this)
                tippingValueAdapter.tippingValueList = logisticDriverModel.prepayment.presetAmount.toMutableList()

                binding.rvChipsTip.apply {
                    layoutManager = chipsLayoutManagerTipping
                    adapter = tippingValueAdapter
                }


                binding.tickerTippingGojek.apply {
                    setHtmlDescription(String.format(getString(R.string.driver_tipping_ticker_new), logisticDriverModel.prepayment.info.first(), logisticDriverModel.prepayment.info.last()))
                }

                binding.etNominalTip.textFieldInput.addTextChangedListener(setWrapperWatcherTipping(binding.etNominalTip.textFieldWrapper))

                binding.btnTipping.setOnClickListener {
                    val paymentApplink = logisticDriverModel.prepayment.paymentLink.replace("{{amount}}", binding.etNominalTip.textFieldInput.text.toString())

                    RouteManager.route(
                        context,
                        paymentApplink
                    )
                }
            }
            150 -> {
                //tip sedang diproses
            }
            200, 210 -> {
                //post payment
                binding.resultTippingLayout.visibility = View.VISIBLE
                binding.paymentTippingLayout.visibility = View.GONE
                binding.btnTipping.text = getString(com.tokopedia.logisticorder.R.string.button_tipping_done)

                binding.apply {
                    imgTipDriver.setImage(R.drawable.ic_succes_tipping_gojek, 0F)
                    tvTipResult.text = getString(com.tokopedia.logisticorder.R.string.tipping_result_text)
                    tvTipResultDesc.text = MethodChecker.fromHtml(getString(com.tokopedia.logisticorder.R.string.tipping_result_desc))
                    tvResiValue.text = trackingDataModel?.trackOrder?.shippingRefNum
                    tvDriverNameValue.text = logisticDriverModel.lastDriver.name
                    tvPhoneNumberValue.text = logisticDriverModel.lastDriver.phone
                    tvLicenseValue.text = logisticDriverModel.lastDriver.licenseNumber

                    chipsPayment.chip_image_icon.setImageUrl(logisticDriverModel.payment.methodIcon)
                    chipsPayment.chip_text.run {
                        text = MethodChecker.fromHtml(String.format(getString(R.string.payment_value), logisticDriverModel.payment.method, logisticDriverModel.payment.amountFormatted))
                        setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                    }
                }

            }
        }
    }

    private fun setWrapperWatcherTipping(wrapper: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = binding.etNominalTip.textFieldInput.text.toString()
                if (s.isNotEmpty() && text.toInt() < 1000) {
                    setWrapperError(wrapper, getString(com.tokopedia.logisticorder.R.string.minimum_tipping))
                    binding.btnTipping.isEnabled = false
                } else if (s.isNotEmpty() && text.toInt() > 20000) {
                    setWrapperError(wrapper, getString(com.tokopedia.logisticorder.R.string.maksimum_tipping))
                    binding.btnTipping.isEnabled = false
                } else {
                    setWrapperError(wrapper, null)
                    binding.btnTipping.isEnabled = true
                }
            }

            override fun afterTextChanged(text: Editable) {

            }
        }
    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.error = s
        }
    }

    override fun onTippingValueClicked(tippingValue: Int) {
        binding.etNominalTip.textFieldInput.setText(tippingValue.toString())
    }


    fun show(fm: FragmentManager, orderId: String?, trackingDataModel: TrackingDataModel) {
        this.orderId = orderId
        this.trackingDataModel = trackingDataModel
        show(fm, "TAG")
    }
}