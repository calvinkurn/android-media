package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetOptOutConfirmationBinding
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.UnknownHostException
import javax.inject.Inject

class OptInConfirmationBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomSheetOptOutConfirmationBinding>()

    private var listener: OptInConfirmationListener? = null

    private var isPmPro: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(PowerMerchantSubscriptionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isFullpage = true
        binding = BottomSheetOptOutConfirmationBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observePmActivationStatus()
    }

    private fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    fun setOptInListener(optInConfirmationListener: OptInConfirmationListener) {
        this.listener = optInConfirmationListener
    }

    private fun observePmActivationStatus() {
        mViewModel.pmActivationStatus.observeOnce(viewLifecycleOwner) {
            hideLoadingState()
            when (it) {
                is Success -> {
                    listener?.setOptInConfirmationSuccess(it.data, isPmPro)
                    dismiss()
                }
                is Fail -> {
                    setOnPmActivationFail(it.throwable)
                    logToCrashlytic(PowerMerchantErrorLogger.PM_ACTIVATION_ERROR, it.throwable)
                }
            }
        }
    }

    private fun setupViews() {
        isPmPro = arguments?.getBoolean(ARGS_IS_PM_PRO).orFalse()
        val (title, desc, btnTitle) = if (isPmPro) {
            Triple(
                getString(com.tokopedia.power_merchant.subscribe.R.string.opt_out_confirmation_pm_pro_title),
                getString(com.tokopedia.power_merchant.subscribe.R.string.opt_out_confirmation_pm_pro_desc),
                getString(com.tokopedia.power_merchant.subscribe.R.string.opt_out_confirmation_pm_pro_btn_title)
            )
        } else {
            Triple(
                getString(com.tokopedia.power_merchant.subscribe.R.string.opt_out_confirmation_pm_title),
                getString(com.tokopedia.power_merchant.subscribe.R.string.opt_out_confirmation_pm_desc),
                getString(com.tokopedia.power_merchant.subscribe.R.string.opt_out_confirmation_pm_btn_title)
            )
        }
        binding?.run {
            ivOptOutConfirmation.loadImage(Constant.Image.IL_PM_OPT_OUT_CONFIRMATION)
            tvOptOutConfirmationTitle.text = title
            tvOptOutConfirmationDesc.text = desc
            btnOptOutConfirmation.text = btnTitle
        }

        setButtonToFetchOptIn()
    }

    private fun setButtonToFetchOptIn() {
        binding?.btnOptOutConfirmation?.setOnClickListener {
            showLoadingState()
            mViewModel.submitPMActivation()
        }
    }

    private fun showLoadingState() = binding?.run {
        btnOptOutConfirmation.isLoading = true
    }

    private fun hideLoadingState() = binding?.run {
        btnOptOutConfirmation.isLoading = false
    }

    private fun setOnPmActivationFail(throwable: Throwable) {
        val actionText = context?.getString(R.string.pm_try_again).orEmpty()
        showErrorToaster(getErrorMessage(throwable), actionText)
    }

    private fun getErrorMessage(t: Throwable): String {
        return when (t) {
            is UnknownHostException -> context?.getString(R.string.pm_network_error_message).orEmpty()
            else -> context?.getString(R.string.pm_system_error_message).orEmpty()
        }
    }

    private fun showErrorToaster(message: String, actionText: String) {
        view?.rootView?.run {
            Toaster.toasterCustomBottomHeight =
                context.resources.getDimensionPixelSize(R.dimen.pm_spacing_100dp)
            Toaster.build(
                this,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                actionText
            ).show()
        }
    }

    private fun logToCrashlytic(message: String, throwable: Throwable) {
        PowerMerchantErrorLogger.logToCrashlytic(message, throwable)
    }

    companion object {

        private const val ARGS_IS_PM_PRO = "arg_is_pm_pro"
        private const val TAG = "OptInConfirmationBottomSheet"
        private const val PEAK_HEIGHT = 1.75

        fun newInstance(isPmPro: Boolean): OptInConfirmationBottomSheet {
            return OptInConfirmationBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(ARGS_IS_PM_PRO, isPmPro)
                }
            }
        }
    }

    interface OptInConfirmationListener {
        fun setOptInConfirmationSuccess(pmActivationStatusUiModel: PMActivationStatusUiModel, isPmPro: Boolean)
    }
}
