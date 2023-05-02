package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_MAX_CASHBACK
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.MAX_QUOTA_LABEL
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.customview.MAX_QUOTA_CHECK
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_layout_add_quota.*
import javax.inject.Inject

class TmAddQuotaBottomsheet: BottomSheetUnify() {

    private var maxCashback = 1
    private var token = ""

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmCouponViewModel: TmCouponViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmCouponViewModel::class.java)
    }

    @Inject
    lateinit var tmTracker: TmTracker
    @Inject
    lateinit var userSession:UserSessionInterface

    private val childLayoutRes = R.layout.tm_layout_add_quota
    private var voucherId = ""
    private var voucherQuota = 0
    private var voucherType = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getInt(BUNDLE_VOUCHER_MAX_CASHBACK)?.let {
            maxCashback = it
        }
        arguments?.getString(BUNDLE_VOUCHER_ID)?.let {
            voucherId = it
        }
        arguments?.getString(BUNDLE_VOUCHER_TYPE)?.let {
            voucherType = it
        }
        arguments?.getInt(BUNDLE_VOUCHER_QUOTA)?.let {
            voucherQuota = it
        }
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setDefaultParams()
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
        observeViewModel()
        if(voucherType.lowercase() == "gratis ongkir"){
            voucherType = "shipping"
        }
        tmCouponViewModel.getInitialCouponData("update", voucherType.lowercase())
    }

    private fun observeViewModel() {
        tmCouponViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    it.data?.getInitiateVoucherPage?.data?.token?.let { it1 ->
                        token = it1
                    }
                }
                TokoLiveDataResult.STATUS.ERROR ->{
                    view?.rootView?.let { it1 -> Toaster.build(it1, it.error?.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
                }
            }
        })

        tmCouponViewModel.tmCouponQuotaUpdateLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    if(it.data?.merchantPromotionUpdateMVQuota?.status == 200) {
                        tmCouponListRefreshCallback.refreshCouponList(ADD_QUOTA)
                        dismiss()
                    }
                    else{
                        view?.rootView?.let { it1 -> it.data?.merchantPromotionUpdateMVQuota?.message?.let { it2 ->
                            Toaster.build(it1,
                                it2, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                        } }
                    }

                }
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.ERROR ->{
                    view?.rootView?.let { it1 -> Toaster.build(it1, "Something went wrong", Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFieldQuota.editText.setText(voucherQuota.toString())
        tvCouponBenefit.text = "Rp" + CurrencyFormatHelper.convertToRupiah((maxCashback.times(voucherQuota).toString()))
        textFieldQuota.editText.addTextChangedListener(object : NumberTextWatcher(textFieldQuota.editText){
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                when {
                    number <= voucherQuota -> {
                        textFieldQuota.isInputError = true
                        textFieldQuota.setMessage("Kuota harus lebih dari $voucherQuota")
                    }
                    number> MAX_QUOTA_CHECK -> {
                        textFieldQuota.isInputError = true
                        textFieldQuota.setMessage(MAX_QUOTA_LABEL)
                    }
                    else -> {
                        textFieldQuota.isInputError = false
                        textFieldQuota.setMessage("")
                    }
                }
                tvCouponBenefit.text = "Rp" + CurrencyFormatHelper.convertToRupiah((maxCashback.times(CurrencyFormatHelper.convertRupiahToInt(number.toString())).toString()))
            }
        })
        btnContinue.setOnClickListener {
            if(token.isEmpty()){
                tmCouponViewModel.getInitialCouponData("update", voucherType.lowercase())
            }
            else {
                val quota = textFieldQuota.editText.text.toString().toIntOrZero()
                if(quota >= voucherQuota) {
                    callingFragment?.let {
                        if (it is TmDashCouponDetailFragment) {
                            tmTracker.clickSimpanQuotaCouponDetail(userSession.shopId)
                        } else {
                            tmTracker.clickAddQuotaCTA(userSession.shopId)
                        }
                    }
                    tmCouponViewModel.updateQuota(
                        quota = quota,
                        voucherId = voucherId.toIntOrZero(),
                        token
                    )
                }
                else{
                    textFieldQuota.textInputLayout.error = "Minimum kuota: $voucherQuota"
                }
            }
        }
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        setTitle("Tambah Kuota")
        setCloseClickListener {
            dismiss()
        }

        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        const val TAG = "TM_ADD_QUOTA_BOTTOM_SHEET"
        lateinit var tmCouponListRefreshCallback: TmCouponListRefreshCallback
        private var callingFragment: Fragment? = null

        fun show(
            childFragmentManager: FragmentManager,
            voucherId: String,
            currentQuota: Int,
            couponType: String,
            tmCouponListRefreshCallback: TmCouponListRefreshCallback,
            fragment: Fragment,
            maxCashback: Int
        ){
            val bundle = Bundle()
            bundle.putString(BUNDLE_VOUCHER_ID, voucherId)
            bundle.putInt(BUNDLE_VOUCHER_QUOTA, currentQuota)
            bundle.putString(BUNDLE_VOUCHER_TYPE, couponType)
            bundle.putInt(BUNDLE_VOUCHER_MAX_CASHBACK, maxCashback)
            this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
            callingFragment = fragment
            val tokomemberIntroBottomsheet = TmAddQuotaBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}
