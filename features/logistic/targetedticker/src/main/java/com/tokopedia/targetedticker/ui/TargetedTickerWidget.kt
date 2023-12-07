package com.tokopedia.targetedticker.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.targetedticker.databinding.WidgetTargetedTickerBinding
import com.tokopedia.targetedticker.di.DaggerTargetedTickerComponent
import com.tokopedia.targetedticker.domain.TargetedTickerHelper.renderTargetedTickerView
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by irpan on 09/10/23.
 */
class TargetedTickerWidget : LinearLayout {

    var ticker: Ticker? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var onSubmitSuccessListener: (TickerModel) -> TickerModel = { item -> item }
    private var onSubmitErrorListener: (Throwable) -> TickerModel? = { item -> null }

    private val binding = WidgetTargetedTickerBinding.inflate(LayoutInflater.from(context), this)

    private val viewModel: TargetedTickerViewModel? by lazy {
        findViewTreeViewModelStoreOwner()?.let { viewModelOwner ->
            ViewModelProvider(viewModelOwner, viewModelFactory)[TargetedTickerViewModel::class.java]
        }
    }

    private fun setupView(attributeSet: AttributeSet? = null) {
        initInjector()
        binding.apply {
            ticker = this.tickerTargeted
        }
    }

    private fun observeTickerState() {

        findViewTreeLifecycleOwner()?.let { treeLifecycleOwner ->

            viewModel?.tickerState?.observe(treeLifecycleOwner) { tickerResult ->
                when (tickerResult) {
                    is Success -> {
                        val data = onSubmitSuccessListener(tickerResult.data)
                        showTicker(data)
                    }

                    is Fail -> {
                        val data = onSubmitErrorListener(tickerResult.throwable)
                        if (data != null) {
                            showTicker(data)
                        } else {
                            hideTicker()
                        }
                    }
                }
            }
        }
    }

    private fun showTicker(tickerItem: TickerModel) {
        context?.let {
            binding.tickerTargeted.renderTargetedTickerView(
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
        binding.tickerTargeted.gone()
    }

    private fun initInjector() {
        context?.let {
            DaggerTargetedTickerComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build().inject(this)
        }
    }

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setupView(attributeSet)
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attributeSet, defStyleAttr) {
        setupView(attributeSet)
    }

    /**
     * Public funtion can be used for targeted ticker widget
     */

    /*
    Call this to load and show targeted ticker data
    adjust the param to set param when hit gql

     */
    fun loadAndShow(params: TargetedTickerParamModel) {
        observeTickerState()
        viewModel?.getTargetedTicker(params)
    }

    // without target, page only
    fun loadAndShow(page: String) {
        observeTickerState()
        viewModel?.getTargetedTicker(
            TargetedTickerParamModel(
                page = page
            )
        )
    }

    /*
    Call this to get listener and adjust the ticker list if you want to add ticker data from backend
     */
    fun setResultListener(
        onSuccess: ((TickerModel) -> TickerModel)? = null,
        onError: ((Throwable) -> TickerModel?)? = null
    ) {
        if (onSuccess != null) {
            onSubmitSuccessListener = onSuccess
        }

        if (onError != null) {
            onSubmitErrorListener = onError
        }
    }

    fun setTickerShape(tickerShape: Int) {
        ticker?.tickerShape = tickerShape
    }

    /**
     * end
     */
}
