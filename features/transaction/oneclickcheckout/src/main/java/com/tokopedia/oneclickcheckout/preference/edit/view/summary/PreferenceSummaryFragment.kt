package com.tokopedia.oneclickcheckout.preference.edit.view.summary

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditParent
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PreferenceSummaryFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private val viewModel: PreferenceSummaryViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PreferenceSummaryViewModel::class.java]
    }

    private var progressDialog: AlertDialog? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var mainContent: ConstraintLayout? = null
    private var buttonSavePreference: UnifyButton? = null

    private var tvPreferenceName: Typography? = null
    private var tvAddressName: Typography? = null
    private var tvAddressReceiver: Typography? = null
    private var tvAddressDetail: Typography? = null
    private var buttonChangeAddress: Typography? = null

    private var tvShippingName: Typography? = null
    private var tvShippingDuration: Typography? = null
    private var buttonChangeDuration: Typography? = null

    private var ivPayment: ImageView? = null
    private var tvPaymentName: Typography? = null
    private var tvPaymentDetail: Typography? = null
    private var buttonChangePayment: Typography? = null

    private var cbMainPreference: CheckboxUnify? = null
    private var tvMainPreference: Typography? = null

    private var globalError: GlobalError? = null

    companion object {

        private const val ARG_IS_EDIT = "is_edit"
        private const val DEFAULT_PREFERENCE_STATUS = 2

        fun newInstance(isEdit: Boolean = false): PreferenceSummaryFragment {
            val preferenceSummaryFragment = PreferenceSummaryFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            preferenceSummaryFragment.arguments = bundle
            return preferenceSummaryFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preference_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initViews()
        initViewModel()
        getPreferenceDetail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObserver()
        progressDialog?.dismiss()
        swipeRefreshLayout = null
        mainContent = null
        buttonSavePreference = null
        tvPreferenceName = null
        tvAddressName = null
        tvAddressReceiver = null
        tvAddressDetail = null
        buttonChangeAddress = null
        tvShippingName = null
        tvShippingDuration = null
        buttonChangeDuration = null
        ivPayment = null
        tvPaymentName = null
        tvPaymentDetail = null
        buttonChangePayment = null
        cbMainPreference = null
        tvMainPreference = null
        globalError = null
    }

    private fun removeObserver() {
        viewModel.preference.removeObservers(this)
        viewModel.editResult.removeObservers(this)
    }

    private fun getPreferenceDetail() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            viewModel.getPreferenceDetail(parent.getProfileId(), parent.getAddressId(), parent.getShippingId(), parent.getGatewayCode(), parent.getPaymentQuery())
        }
    }

    private fun initViewModel() {
        viewModel.preference.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    buttonSavePreference?.isEnabled = true
                    globalError?.gone()
                    mainContent?.visible()
                    setDataToParent(it.data)
                    setupViews(it.data)
                }
                is OccState.Failed -> {
                    swipeRefreshLayout?.isRefreshing = false
                    buttonSavePreference?.isEnabled = false
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }
                is OccState.Loading -> {
                    swipeRefreshLayout?.isRefreshing = true
                    buttonSavePreference?.isEnabled = false
                }
            }
        })
        viewModel.editResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.Success -> {
                    progressDialog?.dismiss()
                    activity?.setResult(RESULT_OK, Intent().putExtra(PreferenceEditActivity.EXTRA_RESULT_MESSAGE, it.data))
                    activity?.finish()
                }
                is OccState.Failed -> {
                    progressDialog?.dismiss()
                    it.getFailure()?.let { failure ->
                        view?.let { view ->
                            if (failure.throwable is MessageErrorException) {
                                Toaster.build(view, failure.throwable.message
                                        ?: DEFAULT_LOCAL_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                            } else {
                                Toaster.build(view, failure.throwable?.localizedMessage
                                        ?: DEFAULT_LOCAL_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                            }
                        }
                    }
                }
                is OccState.Loading -> {
                    if (progressDialog == null) {
                        progressDialog = AlertDialog.Builder(context!!)
                                .setView(com.tokopedia.purchase_platform.common.R.layout.purchase_platform_progress_dialog_view)
                                .setCancelable(false)
                                .create()
                    }
                    progressDialog?.show()
                }
            }
        })
    }

    private fun setupViews(data: ProfilesItemModel) {
        if (arguments?.getBoolean(ARG_IS_EDIT) == false) {
            val parent = activity
            if (parent is PreferenceEditParent) {
                val preferenceIndex = parent.getPreferenceIndex()
                if (preferenceIndex.isNotEmpty()) {
                    tvPreferenceName?.text = preferenceIndex
                    tvPreferenceName?.visible()
                }
            }
        } else {
            tvPreferenceName?.gone()

            buttonSavePreference?.isEnabled = (viewModel.isDataChanged() || (cbMainPreference?.isVisible ?: false && cbMainPreference?.isChecked ?: false))
        }

        val addressModel = data.addressModel
        tvAddressName?.text = addressModel.addressName
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        if (receiverText.isNotEmpty()) {
            tvAddressReceiver?.text = receiverText
            tvAddressReceiver?.visible()
        } else {
            tvAddressReceiver?.gone()
        }
        tvAddressDetail?.text = addressModel.fullAddress

        val shipmentModel = data.shipmentModel
        tvShippingName?.text = getString(R.string.lbl_shipping_with_name, shipmentModel.serviceName.capitalize())
        tvShippingDuration?.text = getString(R.string.lbl_shipping_duration_prefix, shipmentModel.serviceDuration)

        val paymentModel = data.paymentModel
        ImageHandler.loadImageFitCenter(context, ivPayment, paymentModel.image)
        tvPaymentName?.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail?.text = description
            tvPaymentDetail?.visible()
        } else {
            tvPaymentDetail?.gone()
        }

        val parent = activity
        if (parent is PreferenceEditParent) {
            if (arguments?.getBoolean(ARG_IS_EDIT) == false && !parent.isExtraProfile()) {
                cbMainPreference?.gone()
                tvMainPreference?.gone()
            } else if (data.status == DEFAULT_PREFERENCE_STATUS) {
                cbMainPreference?.gone()
                tvMainPreference?.gone()
            }
        }

        if (cbMainPreference?.isVisible == true) {
            cbMainPreference?.setOnCheckedChangeListener { _, isChecked ->
                buttonSavePreference?.isEnabled = viewModel.isDataChanged() || isChecked
            }
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable?.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            getPreferenceDetail()
        }
        mainContent?.gone()
        globalError?.visible()
    }

    private fun initViews() {
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        mainContent = view?.findViewById(R.id.main_content)
        buttonSavePreference = view?.findViewById(R.id.btn_save)

        tvPreferenceName = view?.findViewById(R.id.tv_preference_name)
        tvAddressName = view?.findViewById(R.id.tv_address_name)
        tvAddressReceiver = view?.findViewById(R.id.tv_address_receiver)
        tvAddressDetail = view?.findViewById(R.id.tv_address_detail)
        buttonChangeAddress = view?.findViewById(R.id.btn_change_address)

        tvShippingName = view?.findViewById(R.id.tv_shipping_name)
        tvShippingDuration = view?.findViewById(R.id.tv_shipping_duration)
        buttonChangeDuration = view?.findViewById(R.id.btn_change_duration)

        ivPayment = view?.findViewById(R.id.iv_payment)
        tvPaymentName = view?.findViewById(R.id.tv_payment_name)
        tvPaymentDetail = view?.findViewById(R.id.tv_payment_detail)
        buttonChangePayment = view?.findViewById(R.id.btn_change_payment)

        cbMainPreference = view?.findViewById(R.id.cb_main_preference)
        tvMainPreference = view?.findViewById(R.id.tv_main_preference)

        globalError = view?.findViewById(R.id.global_error)

        mainContent?.gone()
        globalError?.gone()
        swipeRefreshLayout?.isRefreshing = true

        buttonChangeAddress?.setOnClickListener {
            val parent = activity
            if (parent is PreferenceEditParent) {
                preferenceListAnalytics.eventClickUbahAddressInPreferenceSettingPage()
                parent.addFragment(AddressListFragment.newInstance(true))
            }
        }

        buttonChangeDuration?.setOnClickListener {
            val parent = activity
            if (parent is PreferenceEditParent) {
                preferenceListAnalytics.eventClickUbahShippingInPreferenceSettingPage()
                parent.addFragment(ShippingDurationFragment.newInstance(true))
            }
        }

        buttonChangePayment?.setOnClickListener {
            val parent = activity
            if (parent is PreferenceEditParent) {
                preferenceListAnalytics.eventClickUbahPaymentInPreferenceSettingPage()
                parent.addFragment(PaymentMethodFragment.newInstance(true))
            }
        }

        buttonSavePreference?.setOnClickListener {
            if (viewModel.preference.value is OccState.Success) {
                val parent = activity
                if (parent is PreferenceEditParent) {
                    if (arguments?.getBoolean(ARG_IS_EDIT) == true && parent.getProfileId() > 0) {
                        viewModel.updatePreference(parent.getProfileId(), parent.getAddressId(), parent.getShippingId(), parent.getGatewayCode(), parent.getPaymentQuery(), isDefaultProfileChecked(cbMainPreference), parent.getFromFlow())
                    } else {
                        viewModel.createPreference(parent.getAddressId(), parent.getShippingId(), parent.getGatewayCode(), parent.getPaymentQuery(), isDefaultProfileChecked(cbMainPreference), parent.getFromFlow())
                    }
                    preferenceListAnalytics.eventClickSimpanOnSummaryPurchaseSetting()
                }
            }
        }
    }

    private fun initHeader() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            parent.hideAddButton()
            val profileId = parent.getProfileId()
            if (arguments?.getBoolean(ARG_IS_EDIT) == true && profileId > -1) {
                if (parent.isExtraProfile()) {
                    parent.showDeleteButton()
                    parent.setDeleteButtonOnClickListener {
                        preferenceListAnalytics.eventClickTrashBinInEditPreference()
                        context?.let {
                            if (viewModel.preference.value is OccState.Success) {
                                DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                                    setTitle(getString(R.string.lbl_delete_preference_title))
                                    setDescription(getString(R.string.lbl_delete_preference_desc))
                                    setPrimaryCTAText(getString(R.string.lbl_delete_preference_ok))
                                    setSecondaryCTAText(getString(com.tokopedia.purchase_platform.common.R.string.text_button_negative))
                                    setPrimaryCTAClickListener {
                                        preferenceListAnalytics.eventClickDeletePreferenceFromTrashBin()
                                        dismiss()
                                        viewModel.deletePreference(profileId)
                                    }
                                    setSecondaryCTAClickListener {
                                        dismiss()
                                    }
                                }.show()
                            }
                        }
                    }
                } else {
                    parent.hideDeleteButton()
                }

                parent.setHeaderTitle(getString(R.string.lbl_summary_preference_with_number_title) + " " + parent.getPreferenceIndex())
                parent.hideStepper()
            } else {
                parent.hideDeleteButton()
                parent.setHeaderTitle(getString(R.string.lbl_summary_preference_title))
                parent.setHeaderSubtitle(getString(R.string.lbl_summary_preference_subtitle))
                parent.showStepper()
                parent.setStepperValue(100)
            }
        }
    }

    private fun setDataToParent(data: ProfilesItemModel) {
        val parent = activity
        if (parent is PreferenceEditParent) {
            if (parent.getAddressId() == 0) {
                val addressId = data.addressModel.addressId
                parent.setAddressId(addressId)
                viewModel.setProfileAddressId(addressId)
            }
            if (parent.getShippingId() == 0) {
                val serviceId = data.shipmentModel.serviceId
                parent.setShippingId(serviceId)
                viewModel.setProfileServiceId(serviceId)
            }
            if (parent.getGatewayCode().isEmpty()) {
                val gatewayCode = data.paymentModel.gatewayCode
                parent.setGatewayCode(gatewayCode)
                viewModel.setProfileGatewayCode(gatewayCode)
            }
            if (parent.getPaymentQuery().isEmpty()) {
                val metadata = data.paymentModel.metadata
                parent.setPaymentQuery(metadata)
                viewModel.setProfilePaymentMetadata(metadata)
            }
        }
    }

    private fun isDefaultProfileChecked(checkBox: CheckboxUnify?): Boolean {
        if (checkBox != null) {
            if (checkBox.isVisible) {
                return checkBox.isChecked
            }
            return true
        }
        return false
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }
}