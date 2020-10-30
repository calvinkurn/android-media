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
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.tradein.viewmodel.TradeInInitialPriceViewModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.*
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
        //no_imei_value.text = getTradeInDeviceId()
        model_value.text = StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString()
//        typography_imei_help.setOnClickListener {
//            val tradeInImeiHelpBottomSheet = newInstance()
//            tradeInImeiHelpBottomSheet.show(childFragmentManager, "")
//            tradeInAnalytics.clickInitialPriceImeiBottomSheet()
//        }
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
    }

    private fun initCollapse() {

        val drawbleExpand = resources.getDrawable(com.tokopedia.analyticsdebugger.R.drawable.ic_expand_more_black_24dp)
        val drawableCollase = resources.getDrawable(com.tokopedia.analyticsdebugger.R.drawable.ic_expand_less_black_24dp)
        iv_collapse.setOnClickListener {
            if (collapseFlag) {
                iv_collapse.setImageDrawable(drawbleExpand)
                collapse(parent_collapse)
                collapseFlag = !collapseFlag
            } else {
                iv_collapse.setImageDrawable(drawableCollase)
                expand(parent_collapse)
                collapseFlag = !collapseFlag
            }
        }
    }

    fun handleEligibility(maxPrice: String, isElligibleForTradeIn: Boolean, notElligibleMessage: String) {
        setMaxPrice(maxPrice)
        if (isElligibleForTradeIn) {
            isElligible()
        } else {
            notElligible(notElligibleMessage)
        }
    }

    /* private fun setUpObservers() {
         tradeInInitialPriceViewModel.imeiStateLiveData.observe(viewLifecycleOwner, Observer { showImei: Boolean ->
             if (showImei) {
                 handleImei()
             } else {
                 imei_view.hide()
                 btn_continue.setOnClickListener {
                     tradeinHomeViewModel.onInitialPriceClick(null)
                 }
             }
         })
     }*/

    private fun getTradeInDeviceId(): String? {
        return TradeInUtils.getDeviceId(context)
    }

    /* private fun handleImei() {
         imei_view.show()
         no_imei.hide()
         no_imei_value.hide()
         edit_text_imei.setOnFocusChangeListener { _, hasFocus ->
             if (hasFocus)
                 tradeInAnalytics.clickInitialPriceInputImei()
         }
         btn_continue.setOnClickListener {
             when {
                 edit_text_imei.text.length == 15 -> {
                     tradeinHomeViewModel.onInitialPriceClick(edit_text_imei.text.toString())
                 }
                 edit_text_imei.text.isEmpty() -> {
                     tradeInAnalytics.clickInitialPriceImeiNoInput()
                     typography_imei_description.text = getString(R.string.enter_the_imei_number_text)
                     typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
                 }
                 else -> {
                     tradeInAnalytics.clickInitialPriceImeiWrongInput()
                     typography_imei_description.text = getString(R.string.wrong_imei_string)
                     typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
                 }
             }
         }
     }*/

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

    fun notElligible(notEligibleText: String) {
        progress_bar_layout.hide()
        btn_continue.isEnabled = false
        phone_valid_ticker.show()
        phone_valid_ticker.setTextDescription(notEligibleText)
    }

    private fun setMaxPrice(maxPrice: String) {
        tv_old_product_price.text = getString(R.string.tradein_minus, maxPrice)
//        val spannableString = SpannableString(getString(R.string.tradein_save_upto, maxPrice))
//        val start = 18
//        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, start + maxPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//        save_upto.text = spannableString
    }

    private fun isElligible() {
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