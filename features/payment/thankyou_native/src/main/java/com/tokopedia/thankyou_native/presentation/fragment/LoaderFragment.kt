package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.InstantPaymentPage
import com.tokopedia.thankyou_native.data.mapper.Invalid
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentStatusMapper
import com.tokopedia.thankyou_native.data.mapper.ProcessingPaymentPage
import com.tokopedia.thankyou_native.data.mapper.WaitingPaymentPage
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.activity.ARG_MERCHANT
import com.tokopedia.thankyou_native.presentation.activity.ARG_PAYMENT_ID
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_activity_thank_you.*
import kotlinx.android.synthetic.main.thank_fragment_loader.*
import java.util.zip.ZipInputStream
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class LoaderFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private var isV2Enabled: Boolean = true

    private val thanksPageDataViewModel: ThanksPageDataViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    private val handler = Handler()

    var callback: ThankYouPageDataLoadCallback? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.thank_fragment_loader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        showLoaderView()
        handler.postDelayed(delayLoadingRunnable, DELAY_MILLIS)
    }

    override fun onDestroyView() {
        handler.removeCallbacks(delayLoadingRunnable)
        super.onDestroyView()
    }

    private val delayLoadingRunnable = Runnable {
        loadThankPageData()
    }

    private fun loadThankPageData() {
        globalError.gone()
        arguments?.let {
            if (it.containsKey(ThankYouBaseFragment.ARG_IS_V2_ENABLED)) {
                isV2Enabled = it.getBoolean(ThankYouBaseFragment.ARG_IS_V2_ENABLED)
            }
            if (it.containsKey(ARG_PAYMENT_ID) && it.containsKey(ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(
                    it.getString(ARG_PAYMENT_ID, ""),
                    it.getString(ARG_MERCHANT, ""),
                    isV2Enabled
                )
            }
        }
    }

    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onThankYouPageDataLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ThankYouPageDataLoadCallback)
            callback = context
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    private fun onThankYouPageDataLoadingFail(throwable: Throwable) {
        hideLoaderView()
        when (throwable) {
            is MessageErrorException -> {
                if (throwable.message?.startsWith(RPC_ERROR_STR) == true) {
                    callback?.onInvalidThankYouPage()
                } else {
                    showGlobalError(GlobalError.SERVER_ERROR, ::loadThankPageData)
                }
            }
            else -> showGlobalError(GlobalError.NO_CONNECTION, ::loadThankPageData)
        }
    }

    private fun showGlobalError(errorType: Int, retryAction: () -> Unit) {
        globalError.visible()
        globalError.setType(errorType)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            showLoaderView()
            retryAction.invoke()
        }
    }

    private fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData) {
        triggerHaptics()
        if (PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus) == Invalid) {
            callback?.onInvalidThankYouPage()
            return
        } else {
            if (!isV2Enabled) {
                callback?.onThankYouPageDataLoaded(thanksPageData)
                return
            }
            callback?.onThankYouPageDataLoaded(thanksPageData)
        }
    }

    private fun showLoaderView() {
        if (isV2Enabled) return

        tvProcessingPayment.visible()
    }

    private fun hideLoaderView() {
        tvProcessingPayment.hide()
        loaderAnimation.hide()
        triggerHaptics()
    }

    private fun triggerHaptics() {
        try {
            val vibrationService = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationService.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATION_MILLIS,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrationService.vibrate(VIBRATION_MILLIS)
            }
        } catch (ignore: Exception) {
        }
    }

    companion object {
        const val RPC_ERROR_STR = "rpc error:"
        const val DELAY_MILLIS = 2000L
        const val VIBRATION_MILLIS = 150L
        const val LOADER_JSON_ZIP_FILE = "thanks_payment_data_loader.zip"
        fun getLoaderFragmentInstance(bundle: Bundle): LoaderFragment = LoaderFragment().apply {
            arguments = bundle
        }
    }

}
