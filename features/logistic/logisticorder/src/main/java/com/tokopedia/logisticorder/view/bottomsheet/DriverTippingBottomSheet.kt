package com.tokopedia.logisticorder.view.bottomsheet

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.BulletSpan
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
import com.tokopedia.kotlin.extensions.view.toIntSafely
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class DriverTippingBottomSheet : BottomSheetUnify(), HasComponent<TrackingPageComponent>, TippingValueAdapter.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoCleared<BottomsheetTippingGojekBinding>()
    private val viewModel: TrackingPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TrackingPageViewModel::class.java]
    }

    private var orderId: String? = ""
    private var trackingDataModel: TrackingDataModel? = null
    private lateinit var tippingValueAdapter: TippingValueAdapter
    private var selectedTippingValue: Int? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.driverTipData.observe(
            viewLifecycleOwner,
            Observer {
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
            }
        )
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
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()

                ViewCompat.setLayoutDirection(binding.rvChipsTip, ViewCompat.LAYOUT_DIRECTION_LTR)
                tippingValueAdapter = TippingValueAdapter(this)
                tippingValueAdapter.tippingValueList = logisticDriverModel.prepayment.presetAmount.toMutableList()

                binding.rvChipsTip.apply {
                    layoutManager = chipsLayoutManagerTipping
                    adapter = tippingValueAdapter
                    addItemDecoration(object : SpacingItemDecoration(TIPPING_SPACING, TIPPING_SPACING) {
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)
                            val itemWidth = parent.measuredWidth / TIPPING_WIDTH_DIVIDER
                            view.layoutParams.width = itemWidth.toInt()
                        }
                    })
                }

                binding.tickerTippingGojek.apply {
                    descriptionView.elevation = 2f
                    description = setTippingDescription(logisticDriverModel.prepayment.info)
                }
                binding.etNominalTip.run {
                    setMessage(getString(R.string.nominal_tip_message, CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(logisticDriverModel.prepayment.minAmount), CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(logisticDriverModel.prepayment.maxAmount)))
                    editText.addTextChangedListener(setWrapperWatcherTipping(binding.etNominalTip.textInputLayout, logisticDriverModel.prepayment.minAmount, logisticDriverModel.prepayment.maxAmount))
                    counterView?.visibility = View.GONE
                }

                binding.btnTipping.setOnClickListener {
                    val paymentApplink = logisticDriverModel.prepayment.paymentLink.replace("{{amount}}", binding.etNominalTip.editText.text.toString())

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
                    imgTipDriver.urlSrc = "https://images.tokopedia.net/img/android/tipping/illus-tipping-success (1).png"
                    tvTipResult.text = getString(if (logisticDriverModel.status == SUCCESS_PAYMENT) com.tokopedia.logisticorder.R.string.tipping_success_payment_text else com.tokopedia.logisticorder.R.string.tipping_success_to_gojek_text)
                    tvTipResultDesc.text = MethodChecker.fromHtml(getString(com.tokopedia.logisticorder.R.string.tipping_result_desc))
                    tvResiValue.text = trackingDataModel?.trackOrder?.shippingRefNum
                    tvDriverNameValue.text = logisticDriverModel.lastDriver.name
                    tvPhoneNumberValue.text = logisticDriverModel.lastDriver.phone
                    tvLicenseValue.text = logisticDriverModel.lastDriver.licenseNumber
                    if (logisticDriverModel.payment.methodIcon.isNotEmpty()) {
                        tippingMethod.setImageUrl(logisticDriverModel.payment.methodIcon)
                    } else {
                        tippingMethod.visibility = View.GONE
                    }
                    tippingValue.run {
                        text = MethodChecker.fromHtml(String.format(getString(R.string.payment_value), logisticDriverModel.payment.method, logisticDriverModel.payment.amountFormatted))
                        setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                    }
                }
            }
        }
    }

    private fun setTippingDescription(descriptionList: List<String>): CharSequence {
        val description = descriptionList.joinToString("\n")
        val result = SpannableString(description)
        var last = 0
        descriptionList.forEach { desc ->
            val start = description.indexOf(desc, last)
            last = start + desc.length
            result.setSpan(BulletSpan(TIPPING_DESCRIPTION_BULLET_GAP_WIDTH), start, last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return result
    }

    private fun setWrapperWatcherTipping(wrapper: TextInputLayout, minAmount: Int, maxAmount: Int): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = binding.etNominalTip.editText.text.toString()
                if (s.isNotEmpty() && text.toIntSafely() < minAmount) {
                    setWrapperError(wrapper, getString(com.tokopedia.logisticorder.R.string.minimum_tipping, CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(minAmount)))
                    binding.btnTipping.isEnabled = false
                } else if (s.isNotEmpty() && text.toIntSafely() > maxAmount) {
                    setWrapperError(wrapper, getString(com.tokopedia.logisticorder.R.string.maksimum_tipping, CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(maxAmount)))
                    binding.btnTipping.isEnabled = false
                } else {
                    setWrapperError(wrapper, null)
                    binding.btnTipping.isEnabled = true
                }

                validateSelectedChip(text)
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    private fun validateSelectedChip(nominalTip: String?) {
        if (selectedTippingValue != nominalTip?.toIntOrNull()) {
            tippingValueAdapter.replaceSelectedChip()
            selectedTippingValue = null
        }
    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.isErrorEnabled = false
        } else {
            wrapper.isErrorEnabled = true
            wrapper.error = s
        }
    }

    override fun onTippingValueClicked(tippingValue: Int) {
        selectedTippingValue = tippingValue
        binding.etNominalTip.editText.setText(tippingValue.toString())
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

    companion object {
        private const val TIPPING_SPACING = 8
        private const val TIPPING_WIDTH_DIVIDER = 3.2
        private const val TIPPING_DESCRIPTION_BULLET_GAP_WIDTH = 16
    }
}
