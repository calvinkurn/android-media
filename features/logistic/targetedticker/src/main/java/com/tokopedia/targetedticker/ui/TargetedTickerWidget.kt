package com.tokopedia.targetedticker.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.targetedticker.domain.TargetedTickerHelper.renderTargetedTickerView
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.targetedticker.databinding.TargetedTickerWidgetBinding
import com.tokopedia.targetedticker.di.DaggerTargetedTickerComponent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by irpan on 09/10/23.
 */
class TargetedTickerWidget : FrameLayout {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var lifecycleOwner: LifecycleOwner? = null

    private var onSubmitSuccessListener: () -> Unit = {}
    private var onSubmitErrorListener: (Throwable) -> Unit = {}
    private var onSubmitLoadingListener: () -> Unit = {}

    private val viewModel: TargetedTickerViewModel? by lazy {
        findViewTreeViewModelStoreOwner().let { viewModelOwner ->
            ViewModelProvider(viewModelOwner!!, viewModelFactory)[TargetedTickerViewModel::class.java]
        }
    }

    private val binding: TargetedTickerWidgetBinding? =
        TargetedTickerWidgetBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setupView(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        setupView(attributeSet)
    }

    private fun setupView(attributeSet: AttributeSet? = null) {
        initInjector()

//        if (attributeSet != null) setAttributes(attributeSet)

        binding?.apply {

        }


    }

    private fun observeTickerState() {
        lifecycleOwner = ViewTreeLifecycleOwner.get(this)

        lifecycleOwner!!.let {
            viewModel?.tickerState?.observe(it) { tickerResult ->
                when (tickerResult) {
                    is Success -> {
                        showTicker(tickerResult.data)
                    }

                    is Fail -> {
                        hideTicker()
                    }
                }
            }
        }
    }

    private fun showTicker(tickerItem: TickerModel) {
        context?.let {
            binding?.tickerTargeted?.renderTargetedTickerView(
                it,
                tickerItem,
                onClickUrl = { url ->
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$url")
                },
                onClickApplink = { applink ->
                    context.startActivity(RouteManager.getIntent(context, applink))
                }
            )
        }
    }

    private fun hideTicker() {
        binding?.tickerTargeted?.gone()
    }

    private fun initInjector() {
        context?.let {
            DaggerTargetedTickerComponent.builder()
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
                ).build().inject(this)
        }
    }


    /**
     * Public funtion can be used for targeted ticker widget
     */


    /*
    Call this to get targeted ticker data
     */
    fun load() {
        observeTickerState()
        viewModel?.getTargetedTicker()
    }


    fun setSubmitResultListener(
        onSuccess: (() -> Unit),
        onError: ((Throwable) -> Unit),
        onLoading: (() -> Unit)
    ) {
        onSubmitSuccessListener = onSuccess
        onSubmitErrorListener = onError
        onSubmitLoadingListener = onLoading
    }


    fun onDestroy() {
        lifecycleOwner = null
    }

    /**
     * end
     */


}
