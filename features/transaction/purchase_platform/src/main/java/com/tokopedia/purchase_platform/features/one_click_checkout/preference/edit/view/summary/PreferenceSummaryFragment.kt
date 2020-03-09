package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceData
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address.AddressListFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_preference_summary.*
import kotlinx.android.synthetic.main.fragment_preference_summary.global_error
import kotlinx.android.synthetic.main.fragment_preference_summary.main_content
import kotlinx.android.synthetic.main.fragment_preference_summary.swipe_refresh_layout
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PreferenceSummaryFragment : BaseDaggerFragment() {

    companion object {

        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): PreferenceSummaryFragment {
            val preferenceSummaryFragment = PreferenceSummaryFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            preferenceSummaryFragment.arguments = bundle
            return preferenceSummaryFragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PreferenceSummaryViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PreferenceSummaryViewModel::class.java]
    }

    private var progressDialog: AlertDialog? = null

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
    }

    private fun removeObserver() {
        viewModel.preference.removeObservers(this)
        viewModel.editResult.removeObservers(this)
    }

    private fun getPreferenceDetail() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            viewModel.getPreferenceDetail(parent.profileId, parent.addressId, parent.shippingId, parent.gatewayCode, parent.paymentQuery)
        }
    }

    private fun initViewModel() {
        viewModel.preference.observe(this, Observer {
            when (it) {
                is OccState.Success -> {
                    swipe_refresh_layout.isRefreshing = false
                    btn_save.isEnabled = true
                    global_error.gone()
                    main_content.visible()
                    setupViews(it.data)
                }
                is OccState.Fail -> {
                    swipe_refresh_layout.isRefreshing = false
                    btn_save.isEnabled = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }
                else -> {
                    swipe_refresh_layout.isRefreshing = true
                    btn_save.isEnabled = false
                }
            }
        })
        viewModel.editResult.observe(this, Observer {
            when (it) {
                is OccState.Success -> {
                    progressDialog?.dismiss()
                    activity?.setResult(RESULT_OK, Intent().putExtra(PreferenceEditActivity.EXTRA_RESULT_MESSAGE, it.data))
                    activity?.finish()
                }
                is OccState.Fail -> {
                    progressDialog?.dismiss()
                    if (!it.isConsumed) {
                        view?.let { view ->
                            if (it.throwable != null) {
                                if (it.throwable is MessageErrorException) {
                                    Toaster.make(view, it.throwable.message
                                            ?: "Failed", type = Toaster.TYPE_ERROR)
                                } else {
                                    Toaster.make(view, it.throwable.localizedMessage
                                            ?: "Failed", type = Toaster.TYPE_ERROR)
                                }
                            } else {
                                Toaster.make(view, "Failed", type = Toaster.TYPE_ERROR)
                            }
                            viewModel.consumeEditResultFail()
                        }
                    }
                }
                else -> {
                    if (progressDialog == null) {
                        progressDialog = AlertDialog.Builder(context!!)
                                .setView(R.layout.purchase_platform_progress_dialog_view)
                                .setCancelable(false)
                                .create()
                    }
                    progressDialog?.show()
                }
            }
        })
    }

    private fun setupViews(data: GetPreferenceData) {
        if (arguments?.getBoolean(ARG_IS_EDIT) == false) {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                val preferenceIndex = parent.preferenceIndex
                if (preferenceIndex > 0) {
                    tv_preference_name.text = getString(R.string.preference_number, preferenceIndex)
                    tv_preference_name.visible()
                }
            }
        } else {
            tv_preference_name.gone()
        }

        val addressModel = data.addressModel
        tv_address_name.text = addressModel?.addressName ?: ""
        val receiverName = addressModel?.receiverName
        val phone = addressModel?.phone
        var receiverText = ""
        if (receiverName != null) {
            receiverText = "- $receiverName"
            if (phone != null) {
                receiverText = "$receiverText($phone)"
            }
        }
        if (receiverText.isNotEmpty()) {
            tv_address_receiver.text = receiverText
            tv_address_receiver.visible()
        } else {
            tv_address_receiver.gone()
        }
        tv_address_detail.text = addressModel?.fullAddress ?: ""

        val shipmentModel = data.shipmentModel
        tv_shipping_name.text = shipmentModel?.serviceName ?: ""
        tv_shipping_duration.text = shipmentModel?.serviceDuration ?: ""

        val paymentModel = data.paymentModel
        ImageHandler.loadImageFitCenter(context, iv_payment, paymentModel?.image)
        tv_payment_name.text = paymentModel?.gatewayName ?: ""
        val description = paymentModel?.description
        if (description != null && description.isNotBlank()) {
            tv_payment_detail.text = description
            tv_payment_detail.visible()
        } else {
            tv_payment_detail.gone()
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.make(it, "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        global_error.setType(type)
        global_error.setActionClickListener {
            getPreferenceDetail()
        }
        main_content.gone()
        global_error.visible()
    }

    private fun initViews() {
        main_content.gone()
        global_error.gone()
        swipe_refresh_layout.isRefreshing = true

        btn_change_address.setOnClickListener {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.addFragment(AddressListFragment.newInstance(true))
            }
        }

        btn_change_duration.setOnClickListener {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.addFragment(ShippingDurationFragment.newInstance(true))
            }
        }

        btn_change_payment.setOnClickListener {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.addFragment(PaymentMethodFragment.newInstance(true))
            }
        }

        btn_save.setOnClickListener {
            if (viewModel.preference.value is OccState.Success) {
                val parent = activity
                if (parent is PreferenceEditActivity) {
                    if (arguments?.getBoolean(ARG_IS_EDIT) == true && parent.profileId > 0) {
                        viewModel.updatePreference(parent.profileId, parent.addressId, parent.shippingId, parent.gatewayCode, parent.paymentQuery)
                    } else {
                        viewModel.createPreference(parent.addressId, parent.shippingId, parent.gatewayCode, parent.paymentQuery)
                    }
                }
            }
        }
    }

    private fun initHeader() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.hideAddButton()
            if (arguments?.getBoolean(ARG_IS_EDIT) == true && parent.profileId > -1) {
                parent.showDeleteButton()
                parent.setDeleteButtonOnClickListener {
                    context?.let {
                        if (viewModel.preference.value is OccState.Success) {
                            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                                setTitle(getString(R.string.lbl_delete_preference_title))
                                setDescription(getString(R.string.lbl_delete_preference_desc))
                                setPrimaryCTAText(getString(R.string.lbl_delete_preference_ok))
                                setSecondaryCTAText(getString(R.string.text_button_negative))
                                setPrimaryCTAClickListener {
                                    dismiss()
                                    viewModel.deletePreference()
                                }
                                setSecondaryCTAClickListener {
                                    dismiss()
                                }
                            }.show()
                        }
                    }
                }
                parent.setHeaderTitle(getString(R.string.lbl_summary_preference_with_number_title, parent.preferenceIndex))
                parent.hideStepper()
            } else {
                parent.hideDeleteButton()
                parent.setHeaderTitle(getString(R.string.lbl_summary_preference_title))
                parent.setHeaderSubtitle(getString(R.string.lbl_summary_preference_subtitle))
                parent.showStepper()
                parent.setStepperValue(100, true)
            }
        }
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

}