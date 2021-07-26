package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.GetImeiBS
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.ShowSessionIdBs
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.tradein.viewmodel.TradeInInitialPriceViewModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.*
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.btn_continue
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.iv_back
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.btn_session_id
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.product_name
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.product_price
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.progress_bar_layout
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.tv_final_amt
import javax.inject.Inject

class TradeInInitialPriceFragment : BaseViewModelFragment<TradeInInitialPriceViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    var collapseFlag = false

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics
    private lateinit var tradeInInitialPriceViewModel: TradeInInitialPriceViewModel
    private lateinit var tradeinHomeViewModel: TradeInHomeViewModel

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
            DaggerTradeInComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelProvider)
            tradeinHomeViewModel = viewModelProvider.get(TradeInHomeViewModel::class.java)
        }
        //setUpObservers()
        init()
    }

    private fun init() {
        arguments?.apply {
            handleEligibility(getString(EXTRA_MAX_PRICE, "-"), getBoolean(EXTRA_IS_ELIGIBLE, false), getString(EXTRA_NOT_ELIGIBLE_MESSAGE, ""))
        }

        tradeInInitialPriceViewModel.checkAndroid10(getTradeInDeviceId())
        tradeinHomeViewModel.tradeInParams.apply {
            product_name.text = productName
            product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(newPrice, true)
            ImageHandler.LoadImage(product_image, productImage)
        }
        tv_final_amt.text = getString(R.string.tradein_final_price, tradeinHomeViewModel.finalPrice)
        tv_final_amt_2.text = tradeinHomeViewModel.finalPrice
        model_value.text = StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString()

        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
        initCollapse()

        btn_continue.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val getImeiBS = GetImeiBS.newInstance(tradeinHomeViewModel)
                fragmentManager?.let { fm -> getImeiBS.show(fm, "") }
            } else {
                tradeinHomeViewModel.onInitialPriceClick(null)
            }
        }

        //triggering sessionid bottomsheet
        btn_session_id.setOnClickListener(View.OnClickListener {
            val showSidBs = ShowSessionIdBs.newInstance(tradeinHomeViewModel.xSessionId)
            fragmentManager?.let { fm -> showSidBs.show(fm, "") }
        })

    }

    private fun initCollapse() {
        iv_collapse.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_chevron_normal))
        collapse_view.setOnClickListener {
            if (collapseFlag) {
                iv_collapse.rotation = 0f
                collapse(parent_collapse)
                collapseFlag = !collapseFlag
            } else {
                iv_collapse.rotation = 180f
                expand(parent_collapse)
                collapseFlag = !collapseFlag
            }
        }
    }

    fun handleEligibility(maxPrice: String, isElligibleForTradeIn: Boolean, notElligibleMessage: String) {
        setMaxPrice(maxPrice)
        if (isElligibleForTradeIn) {
            setNonEligibleView()
        } else {
            setNotElligibleState(notElligibleMessage)
        }
    }

    private fun getTradeInDeviceId(): String? {
        return TradeInUtils.getDeviceId(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tradein_initial_price_fragment, container, false)
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInInitialPriceViewModel> {
        return TradeInInitialPriceViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        tradeInInitialPriceViewModel = viewModel as TradeInInitialPriceViewModel
    }

    fun setNotElligibleState(notEligibleText: String) {
        progress_bar_layout.hide()
        btn_continue.isEnabled = false
        phone_valid_ticker.show()
        phone_valid_ticker.setTextDescription(notEligibleText)
        tv_info_prize.hide()
        iv_info_price.hide()
    }

    private fun setMaxPrice(maxPrice: String) {
        tv_old_product_price.text = getString(R.string.tradein_minus, maxPrice)
    }

    private fun setNonEligibleView() {
        progress_bar_layout.hide()
        btn_continue.isEnabled = true
    }

    fun expand(v: View) {
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                v.layoutParams.height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 0.5 dp/ms
        a.duration = ((targetHeight / v.context.resources.displayMetrics.density) * 0.5).toLong()
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Collapse speed of 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
    }

    companion object {
        private const val EXTRA_MAX_PRICE = "EXTRA_MAX_PRICE"
        private const val EXTRA_IS_ELIGIBLE = "EXTRA_IS_ELIGIBLE"
        private const val EXTRA_NOT_ELIGIBLE_MESSAGE = "EXTRA_NOT_ELIGIBLE_MESSAGE"

        fun getFragmentInstance(maxPrice: String, isEligible: Boolean, notEligibleMessage: String): Fragment {
            return TradeInInitialPriceFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_MAX_PRICE, maxPrice)
                    putString(EXTRA_NOT_ELIGIBLE_MESSAGE, notEligibleMessage)
                    putBoolean(EXTRA_IS_ELIGIBLE, isEligible)
                }
            }
        }
    }
}
