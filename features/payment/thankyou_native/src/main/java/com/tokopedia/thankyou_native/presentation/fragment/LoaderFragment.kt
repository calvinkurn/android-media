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
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.Invalid
import com.tokopedia.thankyou_native.helper.PaymentStatusMapper
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.ThankYouPageDataLoadCallback
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_loader.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.zip.ZipInputStream
import javax.inject.Inject

class LoaderFragment : BaseDaggerFragment() {

    val job = Job()
    val mainDispatcher = Dispatchers.Main + job
    val ioDispatcher = Dispatchers.IO

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var thankYouPageAnalytics: ThankYouPageAnalytics

    private lateinit var thanksPageDataViewModel: ThanksPageDataViewModel

    private val handler = Handler()

    var callback: ThankYouPageDataLoadCallback? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_loader, container, false)
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        thanksPageDataViewModel = viewModelProvider.get(ThanksPageDataViewModel::class.java)
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
            if (it.containsKey(ThankYouPageActivity.ARG_PAYMENT_ID) && it.containsKey(ThankYouPageActivity.ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(it.getLong(ThankYouPageActivity.ARG_PAYMENT_ID),
                        it.getString(ThankYouPageActivity.ARG_MERCHANT, ""))
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
                if (throwable.message?.startsWith("rpc error:") == true) {
                    callback?.onInvalidThankYouPage()
                } else {
                    showServerError()
                }
            }
            is UnknownHostException -> showNoConnectionError()
            is SocketTimeoutException -> showNoConnectionError()
            else -> showServerError()
        }
    }

    private fun showNoConnectionError() {
        globalError.visible()
        globalError.setType(GlobalError.NO_CONNECTION)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            showLoaderView()
            loadThankPageData()
        }
    }

    private fun showServerError() {
        globalError.visible()
        globalError.setType(GlobalError.SERVER_ERROR)
        globalError.errorAction.gone()
    }

    private fun onThankYouPageDataLoaded(data: ThanksPageData) {
        hideLoaderView()
        if (PaymentStatusMapper.getPaymentStatusByInt(data.paymentStatus) == Invalid) {
            callback?.onInvalidThankYouPage()
            return
        } else {
            sendThankYouPageAnalytics(data)
            callback?.onThankYouPageDataLoaded(data)
        }
    }

    /**
     *ThanksPageData.pushGtm is used to prevent duplicate firing of event...
     * */
    private fun sendThankYouPageAnalytics(thanksPageData: ThanksPageData) {
        if (thanksPageData.pushGtm)
            thankYouPageAnalytics.sendThankYouPageData(thanksPageData)
        thankYouPageAnalytics.appsFlyerPurchaseEvent(thanksPageData, "MarketPlace")
        thankYouPageAnalytics.sendBranchIOEvent(thanksPageData)
    }

    private fun showLoaderView() {
        tvWaitForMinute.visible()
        tvProcessingPayment.visible()
        lottieAnimationView.visible()
        loadLoaderInputStream()
    }

    private fun hideLoaderView() {
        lottieAnimationView.gone()
        tvWaitForMinute.hide()
        tvProcessingPayment.hide()
    }

    private fun loadLoaderInputStream() {
        CoroutineScope(mainDispatcher).launchCatchError(
                block = {
                    val task = getLottieTask()
                    addLottieAnimationToView(task)
                }, onError = {
            it.printStackTrace()
        })

    }

    private fun addLottieAnimationToView(task: LottieTask<LottieComposition>) {
        task.addListener { result: LottieComposition? ->
            lottieAnimationView?.setComposition(result!!)
            lottieAnimationView.repeatCount = LottieDrawable.INFINITE
            lottieAnimationView?.repeatMode = LottieDrawable.RESTART
            lottieAnimationView.playAnimation()
        }
    }

    private suspend fun getLottieTask(): LottieTask<LottieComposition> = withContext(ioDispatcher) {
        val lottieFileZipStream = ZipInputStream(context!!.assets.open(LOADER_JSON_ZIP_FILE))
        return@withContext LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
    }

    companion object {
        const val DELAY_MILLIS = 2000L
        const val LOADER_JSON_ZIP_FILE = "thanks_payment_data_loader.zip"
        fun getLoaderFragmentInstance(bundle: Bundle): LoaderFragment = LoaderFragment().apply {
            arguments = bundle
        }
    }

}

