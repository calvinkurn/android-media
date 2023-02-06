package com.tokopedia.privacycenter.ui.dsar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.GetRequestDetailResponse
import com.tokopedia.privacycenter.data.TransactionHistoryModel
import com.tokopedia.privacycenter.databinding.FragmentDsarLayoutBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class DsarFragment : BaseDaggerFragment(), OnDateChangedListener {

    private var binding by autoClearedNullable<FragmentDsarLayoutBinding>()

    override fun getScreenName(): String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var rangePickerBottomSheet: DsarHistoryTransactionBottomSheet? = null

    private var isNeedOtp = true

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
        if (userSession.email.isEmpty()) {
            goToAddEmail()
        }
    }

    private fun goToAddEmail() {
        val intent =
            RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.DSAR_ADD_EMAIL)
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
            if (it) {
                binding?.mainLayout?.visible()
                renderProfileHeader()
            } else {
                binding?.mainLayout?.gone()
            }
        }

        viewModel.mainLoader.observe(viewLifecycleOwner) {
            if (it) {
                showMainLoader()
            } else {
                hideMainLoader()
            }
        }

        viewModel.toasterError.observe(viewLifecycleOwner) {
            showToasterError(it)
        }

        viewModel.globalError.observe(viewLifecycleOwner) {
            binding?.globarErrorDsar?.showWithCondition(it)
            if (it) {
                binding?.globarErrorDsar?.setOnClickListener { _ ->
                    viewModel.fetchInitialData()
                }
            }
        }

        viewModel.transactionHistoryModel.observe(viewLifecycleOwner) {
            if (it.showBottomSheet) {
                showTransactionHistoryBtmSheet()
            } else {
                rangePickerBottomSheet?.dismiss()
            }
            renderSelectedDateLayout(it)
        }

        viewModel.requestDetails.observe(viewLifecycleOwner) {
            renderOnProgressView(it)
        }

        viewModel.submitRequestState.observe(viewLifecycleOwner) {
            if (it.email.isNotEmpty() && it.deadline.isNotEmpty()) {
                onSubmitSuccess(it.email, it.deadline)
            }
        }

        viewModel.showSummary.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showSummary(it)
            } else {
                hideSummary()
            }
        }

        viewModel.fetchInitialData()
    }

    private fun renderSelectedDateLayout(transactionHistoryModel: TransactionHistoryModel) {
        binding?.layoutOptions?.itemTransactionHistory?.checkIcon?.showWithCondition(transactionHistoryModel.isChecked)
        if (transactionHistoryModel.isChecked) {
            val shouldShowDate = transactionHistoryModel.selectedDate.startDate.isNotEmpty() && transactionHistoryModel.selectedDate.endDate.isNotEmpty()
            binding?.layoutOptions?.itemTransactionHistory?.txtChoosenDate?.showWithCondition(shouldShowDate)
            val startDate = DateUtil.formatDate(
                DateUtil.YYYYMMDD,
                DateUtil.DEFAULT_VIEW_FORMAT,
                transactionHistoryModel.selectedDate.startDate
            )
            val endDate = DateUtil.formatDate(
                DateUtil.YYYYMMDD,
                DateUtil.DEFAULT_VIEW_FORMAT,
                transactionHistoryModel.selectedDate.endDate
            )
            if (shouldShowDate) {
                binding?.layoutOptions?.itemTransactionHistory?.txtChoosenDate?.text = "$startDate - $endDate"
            }
        } else {
            binding?.layoutOptions?.itemTransactionHistory?.mainLayout?.isActivated = false
            binding?.layoutOptions?.itemTransactionHistory?.txtChoosenDate?.hide()
        }
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
            isNeedOtp = resultCode != Activity.RESULT_OK
            if (resultCode == Activity.RESULT_OK && data != null) {
                viewModel.submitRequest()
            }
        } else if (requestCode == REQUEST_ADD_EMAIL) {
            if (resultCode == Activity.RESULT_OK) {
                renderProfileHeader()
            } else {
                activity?.finish()
            }
        } else {
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
            text = getString(R.string.dsar_submit_download_btn_title)
            setOnClickListener {
                if (isNeedOtp) {
                    toVerification()
                } else {
                    viewModel.submitRequest()
                }
            }
        }
        binding?.layoutOptions?.root?.gone()
    }

    private fun hideSummary() {
        binding?.layoutOptions?.root?.visible()
        binding?.layoutSummary?.root?.gone()

        binding?.btnNext?.apply {
            text = getString(R.string.dsar_btn_lanjut_title)
            setOnClickListener {
                viewModel.showSummary()
                isNeedOtp = true
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
        binding?.layoutOptions?.itemPersonalInfo?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if (!it.isActivated) {
                    checkIcon.invisible()
                    viewModel.removeFilter(DsarConstants.FILTER_TYPE_PERSONAL)
                } else {
                    viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)
                    checkIcon.visible()
                }
            }
        }
        binding?.layoutOptions?.itemPaymentInfo?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if (!it.isActivated) {
                    viewModel.removeFilter(DsarConstants.FILTER_TYPE_PAYMENT)
                    checkIcon.invisible()
                } else {
                    viewModel.addFilter(DsarConstants.FILTER_TYPE_PAYMENT)
                    checkIcon.visible()
                }
            }
        }
        binding?.layoutOptions?.itemTransactionHistory?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if (!it.isActivated) {
                    viewModel.onTransactionHistoryDeselected()
                } else {
                    viewModel.onTransactionHistorySelected()
                }
            }
        }
        binding?.btnNext?.setOnClickListener {
            viewModel.showSummary()
        }
    }

    override fun onDateChanged(date: Long) {}

    private fun onItemRangeClicked(item: String, isChecked: Boolean, customDateModel: Pair<Date, Date>?) {
        if (item == DsarConstants.LABEL_RANGE_CUSTOM) {
            viewModel.setSelectedDate(item, customDateModel?.first, customDateModel?.second)
        } else {
            viewModel.setSelectedDate(item, null, null)
        }
    }

    private fun showTransactionHistoryBtmSheet() {
        activity?.supportFragmentManager?.let {
            if (rangePickerBottomSheet == null) {
                rangePickerBottomSheet =
                    DsarHistoryTransactionBottomSheet(requireActivity()) { item, isChecked, customDate ->
                        onItemRangeClicked(item, isChecked, customDate)
                    }
                rangePickerBottomSheet?.setCloseClickListener { viewModel.onTransactionHistoryDeselected() }
            }
            rangePickerBottomSheet?.show(fragmentMgr = it)
        }
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
        val formattedDate = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
            DateUtil.DEFAULT_VIEW_FORMAT,
            searchResult.deadline
        )
        binding?.layoutProgress?.txtProgressDescription?.text = Html.fromHtml(getString(R.string.dsar_progress_description, searchResult.email, formattedDate))
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

    override fun onFragmentBackPressed(): Boolean {
        return if (viewModel.showSummary.value?.isNotEmpty() == true) {
            viewModel.backToFormPage()
            true
        } else {
            super.onFragmentBackPressed()
        }
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
