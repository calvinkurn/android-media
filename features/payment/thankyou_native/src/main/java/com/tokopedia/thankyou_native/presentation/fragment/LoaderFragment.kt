package com.tokopedia.thankyou_native.presentation.fragment

import android.animation.Animator
import android.animation.Animator.AnimatorListener
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
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.InstantPaymentPage
import com.tokopedia.thankyou_native.data.mapper.Invalid
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentStatusMapper
import com.tokopedia.thankyou_native.data.mapper.ProcessingPaymentPage
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.activity.ARG_MERCHANT
import com.tokopedia.thankyou_native.presentation.activity.ARG_PAYMENT_ID
import com.tokopedia.thankyou_native.presentation.activity.IS_V2
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_loader.*
import java.util.zip.ZipInputStream
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class LoaderFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    var lottieTask: LottieTask<LottieComposition>? = null

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
        showSuccessLottie()
        (activity as ThankYouPageActivity).findViewById<LottieAnimationView>(R.id.lottieSuccess).hide()
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
            if (it.containsKey(ARG_PAYMENT_ID) && it.containsKey(ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(
                    it.getString(ARG_PAYMENT_ID, ""),
                    it.getString(ARG_MERCHANT, "")
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
        hideLoaderView()
        if (PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus) == Invalid) {
            callback?.onInvalidThankYouPage()
            return
        } else {
            if (!IS_V2) {
                callback?.onThankYouPageDataLoaded(thanksPageData)
                return
            }
            if (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType) == InstantPaymentPage) {
                val lottie = (activity as ThankYouPageActivity).findViewById<LottieAnimationView>(R.id.lottieSuccess)
                lottie.visible()
                lottie.playAnimation()
                lottie.addAnimatorListener(object: AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
//                    (activity as ThankYouPageActivity).findViewById<LottieAnimationView>(R.id.lottieSuccess).animate().alpha(0f).setDuration(UnifyMotion.T5).start()
                        callback?.onThankYouPageDataLoaded(thanksPageData)
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
            } else {
                val header = (activity as ThankYouPageActivity).findViewById<ImageView>(R.id.header_background)
                if (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType) == ProcessingPaymentPage) {
                    context?.let {
                        header.setColorFilter(ContextCompat.getColor(it, unifyprinciplesR.color.Unify_TN50))
                    }
                }
                header.animate().alpha(1f).setDuration(UnifyMotion.T5).setInterpolator(UnifyMotion.EASE_OUT).start()
                callback?.onThankYouPageDataLoaded(thanksPageData)
            }
        }
    }

    private fun showLoaderView() {
        loaderAnimation.showWithCondition(IS_V2)
        if (IS_V2) return

        tvWaitForMinute.visible()
        tvProcessingPayment.visible()
        lottieAnimationView.visible()
        if (lottieTask == null)
            lottieTask = prepareLoaderLottieTask()
        addLottieAnimationToView()
    }

    private fun showSuccessLottie() {
        if (!IS_V2) return

        lottieTask = prepareSuccessLottieTask()
        addLottieAnimationToView()
    }

    private fun hideLoaderView() {
        lottieAnimationView.gone()
        tvWaitForMinute.hide()
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

    private fun prepareLoaderLottieTask(): LottieTask<LottieComposition>? {
        return try {
            val lottieFileZipStream =
                ZipInputStream(requireContext().assets.open(LOADER_JSON_ZIP_FILE))
            LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        } catch (ignore: IllegalStateException) {
            null
        }
    }

    private fun prepareSuccessLottieTask(): LottieTask<LottieComposition>? {
        return try {
            val lottieFileZipStream =
                ZipInputStream(requireContext().assets.open(SUCCESS_JSON_ZIP_FILE))
            LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        } catch (ignore: IllegalStateException) {
            null
        }
    }

    private fun addLottieAnimationToView() {
        lottieTask?.addListener { result: LottieComposition? ->
            result?.let {
                lottieAnimationView?.setComposition(result)
                lottieAnimationView?.repeatCount = LottieDrawable.INFINITE
                lottieAnimationView?.repeatMode = LottieDrawable.RESTART
                lottieAnimationView?.playAnimation()
            }
        }
    }

    companion object {
        const val RPC_ERROR_STR = "rpc error:"
        const val DELAY_MILLIS = 2000L
        const val VIBRATION_MILLIS = 150L
        const val LOADER_JSON_ZIP_FILE = "thanks_payment_data_loader.zip"
        const val SUCCESS_JSON_ZIP_FILE = "thanks_success.zip"
        fun getLoaderFragmentInstance(bundle: Bundle): LoaderFragment = LoaderFragment().apply {
            arguments = bundle
        }
    }

}

