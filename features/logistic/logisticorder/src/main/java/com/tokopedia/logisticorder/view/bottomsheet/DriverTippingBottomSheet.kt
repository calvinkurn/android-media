package com.tokopedia.logisticorder.view.bottomsheet

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
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
import com.tokopedia.logisticorder.utils.TippingConstant.OPEN
import com.tokopedia.logisticorder.utils.TippingConstant.SUCCESS_PAYMENT
import com.tokopedia.logisticorder.utils.TippingConstant.SUCCESS_TO_GOJEK
import com.tokopedia.logisticorder.utils.TippingConstant.WAITING_PAYMENT
import com.tokopedia.logisticorder.view.TrackingPageViewModel
import com.tokopedia.logisticorder.view.adapter.TippingValueAdapter
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
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

        clearContentPadding = true
        isKeyboardOverlap = false
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
                    showSoftError(it.throwable)
                }
            }
        })
    }

    private fun setDriverTipLayout(logisticDriverModel: LogisticDriverModel) {
        when (logisticDriverModel.status) {
            OPEN -> {
                setTitle(getString(com.tokopedia.logisticorder.R.string.title_prepayment_tipping))
                binding.paymentTippingLayout.visibility = View.VISIBLE
                binding.resultTippingLayout.visibility = View.GONE
                binding.btnTipping.text = getString(com.tokopedia.logisticorder.R.string.button_tipping_payment)

                val chipsLayoutManagerTipping = ChipsLayoutManager.newBuilder(binding.root.context)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_VIEW)
                        .withLastRow(true)
                        .build()

                ViewCompat.setLayoutDirection(binding.rvChipsTip, ViewCompat.LAYOUT_DIRECTION_LTR)
                tippingValueAdapter = TippingValueAdapter(this)
                tippingValueAdapter.tippingValueList = logisticDriverModel.prepayment.presetAmount.toMutableList()

                binding.rvChipsTip.apply {
                    layoutManager = chipsLayoutManagerTipping
                    adapter = tippingValueAdapter
                    addItemDecoration( object : SpacingItemDecoration(8,8) {
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)
                            val itemWidth = parent.measuredWidth / 4
                            view.layoutParams.width = itemWidth
                        }
                    })
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
            WAITING_PAYMENT -> {
            }
            SUCCESS_PAYMENT, SUCCESS_TO_GOJEK -> {
                binding.resultTippingLayout.visibility = View.VISIBLE
                binding.paymentTippingLayout.visibility = View.GONE
                binding.btnTipping.run {
                    text = getString(com.tokopedia.logisticorder.R.string.button_tipping_done)
                    isEnabled = true
                    setOnClickListener {
                        dismiss()
                    }
                }

                binding.apply {
                    imgTipDriver.setImage(R.drawable.ic_succes_tipping_gojek, 0F)
                    tvTipResult.text = getString(if (logisticDriverModel.status == SUCCESS_PAYMENT) com.tokopedia.logisticorder.R.string.tipping_success_payment_text else com.tokopedia.logisticorder.R.string.tipping_success_to_gojek_text)
                    tvTipResultDesc.text = MethodChecker.fromHtml(getString(com.tokopedia.logisticorder.R.string.tipping_result_desc))
                    tvResiValue.text = trackingDataModel?.trackOrder?.shippingRefNum
                    tvDriverNameValue.text = logisticDriverModel.lastDriver.name
                    tvPhoneNumberValue.text = logisticDriverModel.lastDriver.phone
                    tvLicenseValue.text = logisticDriverModel.lastDriver.licenseNumber

                    chipsPayment.chip_image_icon.setImageUrl(logisticDriverModel.payment.methodIcon)
                    chipsPayment.chip_text.run {
                        setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)
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

    private fun showSoftError(error: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, error)
        view?.let { Toaster.build(it, message, Toaster.LENGTH_SHORT).show() }
    }
}