package com.tokopedia.privacycenter.dsar.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.databinding.BottomSheetRangePickerBinding
import com.tokopedia.privacycenter.databinding.FragmentDsarLayoutBinding
import com.tokopedia.privacycenter.databinding.ItemTransactionHistoryRangeBinding
import com.tokopedia.privacycenter.dsar.DsarConstants.DATE_RANGE_CUSTOM
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PAYMENT
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PERSONAL
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_TRANSACTION
import com.tokopedia.privacycenter.dsar.model.GetRequestDetailResponse
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import com.tokopedia.privacycenter.dsar.viewmodel.DsarViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class DsarFragment: BaseDaggerFragment(), OnDateChangedListener {

    private var binding by autoClearedNullable<FragmentDsarLayoutBinding>()

    override fun getScreenName(): String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    var state = STATE_START

    var startDate: Long = 0L
    var endDate: Long = 0L

    lateinit var rangePickerDialogBinding: BottomSheetRangePickerBinding
    lateinit var rangePickerBottomSheet: BottomSheetUnify
    lateinit var datePicker: DateTimePickerUnify

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            DsarViewModel::class.java
        )
    }

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(userSession.email.isEmpty()) {
            goToAddEmail()
        }
    }

    fun goToAddEmail() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.DSAR_ADD_EMAIL)
        startActivityForResult(intent, REQUEST_ADD_EMAIL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDsarLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()

        viewModel.showMainLayout.observe(viewLifecycleOwner) {
            if(it) {
                binding?.mainLayout?.visible()
            } else {
                binding?.mainLayout?.gone()
            }
        }

        viewModel.mainLoader.observe(viewLifecycleOwner) {
            if(it) {
                showMainLoader()
            } else {
                hideMainLoader()
            }
        }

        viewModel.mainButtonLoading.observe(viewLifecycleOwner) {
            binding?.btnNext?.isLoading = it
        }

        viewModel.toasterError.observe(viewLifecycleOwner) {
            showToasterError(it)
        }

        viewModel.itemRangeData.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                if(::rangePickerBottomSheet.isInitialized && rangePickerBottomSheet.isVisible) {
                    updateRangeItemsView(it)
                    binding?.layoutOptions?.itemTransactionHistory
                } else {
                    showTransactionHistoryBtmSheet(it)
                }
            }
        }

        viewModel.requestDetails.observe(viewLifecycleOwner) {
            renderOnProgressView(it)
        }

        viewModel.submitRequest.observe(viewLifecycleOwner) {
            onSubmitSuccess(it.email, it.deadline)
        }

        viewModel.customDate.observe(viewLifecycleOwner) {
            if (::rangePickerDialogBinding.isInitialized) {
                rangePickerDialogBinding.txtStartDate.editText
                    .setText(it.startDate)
                rangePickerDialogBinding.txtEndDate.editText.setText(it.endDate)
            }
        }

        viewModel.showSummary.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                showSummary(it)
            } else {
                hideSummary()
            }
        }
        viewModel.checkRequestStatus()
    }

    private fun showToasterError(errMsg: String) {
        view?.let {
            Toaster.build(it, errMsg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onSubmitSuccess(email: String, deadline: String) {
        val intent = Intent(activity, DsarSuccessActivity::class.java)
        intent.putExtra(DsarSuccessActivity.EXTRA_EMAIL, email)
        intent.putExtra(DsarSuccessActivity.EXTRA_DEADLINE, deadline)
        startActivity(intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SECURITY_QUESTION) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                viewModel.submitRequest()
            } else if(resultCode == Activity.RESULT_CANCELED) {
                showToasterError("Gagal Verifikasi")
            }
        } else if(requestCode == REQUEST_ADD_EMAIL && resultCode == Activity.RESULT_OK) {
            renderProfileHeader()
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
    private fun toVerification() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE)
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION)
    }

    private fun showSummary(data: String) {
        binding?.layoutSummary?.root?.visible()
        binding?.layoutSummary?.txtSummary?.text = Html.fromHtml(data)
        binding?.btnNext?.apply {
            text = "Ajukan Download Data"
            setOnClickListener { toVerification() }
        }
        binding?.layoutOptions?.root?.gone()
    }

    private fun hideSummary() {
        binding?.layoutOptions?.root?.visible()
        binding?.layoutSummary?.root?.gone()

        binding?.btnNext?.apply {
            text = getString(R.string.dsar_btn_lanjut_title)
            setOnClickListener { viewModel.showSummary() }
        }
    }

    private fun updateRangeItemsView(items: List<ItemRangeModel>) {
        if(items.count() == rangePickerDialogBinding.layoutDateRange.childCount) {
            items.forEach {
                val view = rangePickerDialogBinding.layoutDateRange.getChildAt(it.id)
                val radio = view.findViewById<RadioButtonUnify>(R.id.radioTransactionHistoryItem)
                if(radio is RadioButtonUnify) {
                    radio.isChecked = it.selected
                }
            }
        }
    }

    private fun renderProfileHeader() {
        binding?.personalInfo?.apply {
            txtProfileName.text = userSession.name
            txtProfileEmail.text = userSession.email
            txtProfilePhone.text = userSession.phoneNumber
            imgProfilePicture.loadImage(userSession.profilePicture)
        }
    }

    private fun setupViews() {
        renderProfileHeader()
        binding?.layoutOptions?.itemPersonalInfo?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if (!it.isActivated) {
                    checkIcon.invisible()
                    viewModel.removeFilter(FILTER_TYPE_PERSONAL)
                } else {
                    viewModel.addFilter(FILTER_TYPE_PERSONAL)
                    checkIcon.visible()
                }
            }
        }
        binding?.layoutOptions?.itemPaymentInfo?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if(!it.isActivated) {
                    viewModel.removeFilter(FILTER_TYPE_PAYMENT)
                    checkIcon.invisible()
                } else {
                    viewModel.addFilter(FILTER_TYPE_PAYMENT)
                    checkIcon.visible()
                }
            }
        }
        binding?.layoutOptions?.itemTransactionHistory?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if(!it.isActivated) {
                    viewModel.removeFilter(FILTER_TYPE_TRANSACTION)
                    checkIcon.invisible()
                } else {
                    viewModel.addFilter(FILTER_TYPE_TRANSACTION)
                    viewModel.populateRangeItems()
                    checkIcon.visible()
                }
            }
        }
        binding?.btnNext?.setOnClickListener {
            viewModel.showSummary()
        }
    }

    override fun onDateChanged(date: Long) {}

    private fun renderEndState() {
        datePicker.setTitle("Sampai")
    }

    private fun renderInitialState() {
        datePicker.setTitle("Mulai dari")
    }

    private fun showDatePicker() {
        val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext())).apply {
            add(Calendar.YEAR, -3)
        }

        val defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext()))
        val maxDate = defaultDate

        context.let {
            datePicker = DateTimePickerUnify(
                requireContext(),
                minDate,
                defaultDate,
                maxDate,
                this,
                DateTimePickerUnify.TYPE_DATEPICKER
            )

            datePicker.apply {
                setCloseClickListener { dismiss() }
                datePickerButton.text = "Pilih"
                datePickerButton.setOnClickListener {
                    onDateSelected()
                }
            }

            renderInitialState()
            datePicker.show(parentFragmentManager, "")
        }
    }

    private fun onDateSelected() {
        val selected = datePicker.getDate()

        if(state == STATE_START) {
            viewModel.setStartDate(selected.time)
            state = STATE_END
            startDate = selected.time.time
            renderEndState()
        } else {
            viewModel.setEndDate(selected.time)
            endDate = selected.time.time
            state = STATE_START
            renderInitialState()
            datePicker.dismiss()
        }
    }

    private fun onCheckedChanged(id: Int, isChecked: Boolean) {
        if(isChecked) {
            viewModel.setSelectedRangeItems(id)
            if(id == DATE_RANGE_CUSTOM) {
                showDatePicker()
                rangePickerDialogBinding.layoutCustomDate.visible()
            } else {
                rangePickerDialogBinding.layoutCustomDate.gone()
            }
        }
    }

    private fun showTransactionHistoryBtmSheet(itemRange: ArrayList<ItemRangeModel>) {
        if(!::rangePickerDialogBinding.isInitialized) {
            rangePickerDialogBinding = BottomSheetRangePickerBinding.inflate(LayoutInflater.from(activity))
        }

        if(!::rangePickerBottomSheet.isInitialized) {
            rangePickerBottomSheet = BottomSheetUnify()

            itemRange.forEachIndexed { index, itemRangeModel ->
                val binding = ItemTransactionHistoryRangeBinding.inflate(LayoutInflater.from(requireContext()))
                binding.root.layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                binding.mainTextTransactionHistoryItem.text = itemRangeModel.title
                binding.radioTransactionHistoryItem.setOnCheckedChangeListener { _, isChecked -> onCheckedChanged(itemRangeModel.id, isChecked) }
                rangePickerDialogBinding.layoutDateRange.addView(binding.root)
            }
            rangePickerBottomSheet.setChild(rangePickerDialogBinding.root)
            rangePickerDialogBinding.btnApplyFilter.setOnClickListener {
                rangePickerBottomSheet.dismiss()
            }
        }
        rangePickerBottomSheet.show(parentFragmentManager, "")
    }

    private fun renderOnProgressView(searchResult: GetRequestDetailResponse) {
        binding?.mainLayout?.gone()
        binding?.layoutProgress?.root?.visible()
        binding?.layoutProgress?.imgSuccess?.loadImage(getString(R.string.dsar_on_progress_illustration))
        binding?.layoutProgress?.btnDetail?.setOnClickListener {
            showBottomSheetDetails(searchResult)
        }
        binding?.layoutProgress?.btnBack?.setOnClickListener {
            activity?.finish()
        }
        val formattedDate = DateUtil.formatDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS_SSS_Z, DateUtil.DEFAULT_VIEW_FORMAT, searchResult.deadline)
        binding?.layoutProgress?.txtProgressDescription?.text = getString(R.string.dsar_progress_description, searchResult.email, formattedDate)
    }

    private fun showBottomSheetDetails(searchResult: GetRequestDetailResponse) {
        RequestDetailBottomSheet.showDetailsBottomSheet(parentFragmentManager, requireContext(), userSession, searchResult)
    }

    private fun showMainLoader() {
        binding?.mainLoader?.visible()
        binding?.mainLayout?.gone()
        binding?.layoutProgress?.root?.gone()
    }

    private fun hideMainLoader() {
        binding?.mainLoader?.gone()
    }

    companion object {
        const val STATE_START = 0
        const val STATE_END = 1
        const val REQUEST_SECURITY_QUESTION = 100
        const val REQUEST_ADD_EMAIL = 101
        const val OTP_TYPE = 170

        fun newInstance() = DsarFragment()
    }
}
