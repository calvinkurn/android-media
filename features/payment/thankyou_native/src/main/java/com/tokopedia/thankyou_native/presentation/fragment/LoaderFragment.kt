package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.Invalid
import com.tokopedia.thankyou_native.data.mapper.PaymentStatusMapper
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.activity.ARG_MERCHANT
import com.tokopedia.thankyou_native.presentation.activity.ARG_PAYMENT_ID
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_loader.*
import java.util.zip.ZipInputStream
import javax.inject.Inject

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            if (it.containsKey(ARG_PAYMENT_ID) && it.containsKey(ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(it.getLong(ARG_PAYMENT_ID),
                        it.getString(ARG_MERCHANT, ""))
            }
        }
    }

    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(this, Observer {
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
            callback?.onThankYouPageDataLoaded(thanksPageData)
        }
    }

    private fun showLoaderView() {
        tvWaitForMinute.visible()
        tvProcessingPayment.visible()
        lottieAnimationView.visible()
        if (lottieTask == null)
            lottieTask = prepareLoaderLottieTask()
        addLottieAnimationToView()
    }

    private fun hideLoaderView() {
        lottieAnimationView.gone()
        tvWaitForMinute.hide()
        tvProcessingPayment.hide()
    }

    private fun prepareLoaderLottieTask(): LottieTask<LottieComposition>? {
        val lottieFileZipStream = ZipInputStream(context!!.assets.open(LOADER_JSON_ZIP_FILE))
        return LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
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
        const val LOADER_JSON_ZIP_FILE = "thanks_payment_data_loader.zip"
        fun getLoaderFragmentInstance(bundle: Bundle): LoaderFragment = LoaderFragment().apply {
            arguments = bundle
        }
    }

}

