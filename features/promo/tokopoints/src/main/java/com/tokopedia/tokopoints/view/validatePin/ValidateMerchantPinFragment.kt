package com.tokopedia.tokopoints.view.validatePin

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.pin.PinUnify
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Success
import javax.inject.Inject

class ValidateMerchantPinFragment : BaseDaggerFragment(), ValidateMerchantPinContract.View {
    @Inject
    lateinit var factory: ViewModelFactory

    private val mViewModel: ValidateMerchantPinViewModel by lazy { ViewModelProviders.of(this, factory)[ValidateMerchantPinViewModel::class.java] }
    private var mEditPin: PinUnify? = null
    private var mTextError: TextView? = null
    private var mTextInfo: TextView? = null
    private var mValidatePinCallBack: ValidatePinCallBack? = null

    override val activityContext: Context
        get() = requireActivity()

    override val appContext: Context
        get() = requireContext()

    fun setmValidatePinCallBack(mValidatePinCallBack: ValidatePinCallBack) {
        this.mValidatePinCallBack = mValidatePinCallBack
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        return inflater.inflate(R.layout.tp_fragment_validate_merchant_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEditPin = view.findViewById(R.id.et_input_otp)
        mTextError = view.findViewById(R.id.tv_pin_error)
        mTextInfo = view.findViewById(R.id.text_info)
        if (arguments != null) {
            mTextInfo?.setText(arguments!!.getString(CommonConstant.EXTRA_PIN_INFO))
        }
        mEditPin?.focus()
        mEditPin?.pinTextField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length == CommonConstant.PIN_COUNT) {
                    mViewModel.swipeMyCoupon(arguments!!.getString(CommonConstant.EXTRA_COUPON_ID)
                            ?: "", charSequence.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        addObserver()
    }

    private fun addObserver() {
        addSwipeObserver()
    }

    private fun addSwipeObserver() = mViewModel.swipeCouponLiveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Success -> onSuccess(it.data)
                is ErrorMessage -> onError(it.data)
            }
        }
    })

    override fun initInjector() {
        DaggerTokopointBundleComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .tokopointsQueryModule(TokopointsQueryModule(activity as BaseSimpleActivity))
                .build().inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun showLoading() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(R.id.progress_send).visibility = View.VISIBLE
    }

    override fun hideLoading() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(R.id.progress_send).visibility = View.GONE
    }

    override fun onSuccess(couponSwipeUpdate: CouponSwipeUpdate) {
        mValidatePinCallBack?.onSuccess(couponSwipeUpdate)
    }

    override fun onError(error: String) {
        mTextError!!.visibility = View.VISIBLE
        mTextError!!.text = error
    }

    interface ValidatePinCallBack {
        fun onSuccess(couponSwipeUpdate: CouponSwipeUpdate?)
    }

    fun PinUnify.focus() {
        requestFocus()
        // Show keyboard
        val inputMethodManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, 0)
    }

    companion object {
        fun newInstance(extras: Bundle?): ValidateMerchantPinFragment {
            val fragment = ValidateMerchantPinFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
