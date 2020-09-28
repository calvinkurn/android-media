package com.tokopedia.tradein.view.viewcontrollers.activity

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.viewmodel.TradeInInfoViewModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_activity_tradein_info.*
import javax.inject.Inject

const val tradeInTNCSegment = "tradein_tnc"
const val blackMarketSegment = "tradein_black_market"

class TradeInInfoActivity : BaseViewModelActivity<TradeInInfoViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics

    private lateinit var viewModel: TradeInInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setObserver()
        handleIntent()
    }

    private fun setObserver() {
        viewModel.tncInfoLiveData.observe(this, Observer {
            if (it != null) {
                linear_layout.removeAllViews()
                if (it.fetchTickerAndTnC.type == 0)
                    tradeInAnalytics.viewEducationalTNC()
                else
                    tradeInAnalytics.viewEducationalBlackMarket()
                for (tnc in it.fetchTickerAndTnC.tnC) {
                    addView(tnc)
                }
            }
        })
    }

    private fun addView(tnc: String) {
        val typography: Typography = Typography(this).apply {
            setWeight(Typography.REGULAR)
            setType(Typography.BODY_2)
            setPadding(dpToPx(1).toInt(),
                    dpToPx(1).toInt(),
                    dpToPx( 1).toInt(),
                    dpToPx(12).toInt())
            text = tnc
        }
        linear_layout.addView(typography)
    }

    private fun init() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        root_view.minimumHeight = dm.heightPixels.minus(50)
        root_view.minimumWidth = dm.widthPixels
        close_button.setOnClickListener {
            this.finish()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_activity_tradein_info
    }

    private fun handleIntent() {
        if (intent.data?.lastPathSegment == tradeInTNCSegment) {
            heading.text = getString(R.string.tradein_terms_and_conditions)
            tradeInAnalytics.clickEducationalTNC()
            viewModel.getTNC(0)
        } else if (intent.data?.lastPathSegment == blackMarketSegment) {
            heading.text = getString(R.string.tradein_black_market)
            tradeInAnalytics.clickEducationalBlackMarket()
            viewModel.getTNC(1)
        }
    }

    override fun initInject() {
        DaggerTradeInComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInInfoViewModel> {
        return TradeInInfoViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInInfoViewModel
    }
}