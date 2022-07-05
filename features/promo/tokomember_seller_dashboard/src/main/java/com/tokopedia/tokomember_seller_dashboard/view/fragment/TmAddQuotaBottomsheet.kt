package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_MAX_CASHBACK
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
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
                    view?.let { it1 -> Toaster.build(it1, it.error?.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
                }
            }
        })

        tmCouponViewModel.tmCouponQuotaUpdateLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    if(it.data?.merchantPromotionUpdateMVQuota?.status == 200) {
                        tmCouponListRefreshCallback.refreshCouponList()
                        dismiss()
                    }
                    else{
                        view?.let { it1 -> it.data?.merchantPromotionUpdateMVQuota?.message?.let { it2 ->
                            Toaster.build(it1,
                                it2, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                        } }
                    }

                }
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.ERROR ->{
                    view?.let { it1 -> Toaster.build(it1, "Something went wrong", Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFieldQuota.editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                tvCouponBenefit.text = "Rp" + CurrencyFormatHelper.convertToRupiah((maxCashback.times(textFieldQuota.editText.text.toString().toIntOrZero()).toString()))
                if(textFieldQuota.editText.text.toString().toIntOrZero() <= voucherQuota){
                    textFieldQuota.isInputError = true
                    textFieldQuota.setMessage("Kuota harus lebih dari $voucherQuota")
                }
                else{
                    textFieldQuota.isInputError = false
                    textFieldQuota.setMessage("")
                }
            }
        })
        btnContinue.setOnClickListener {
            if(token.isEmpty()){
                tmCouponViewModel.getInitialCouponData("update", voucherType.lowercase())
            }
            else {
                val quota = textFieldQuota.editText.text.toString().toIntOrZero()
                if(quota >= voucherQuota) {
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

        fun show(
            childFragmentManager: FragmentManager,
            voucherId: String,
            currentQuota: Int,
            couponType: String,
            tmCouponListRefreshCallback: TmCouponListRefreshCallback,
            maxCashback: Int
        ){
            val bundle = Bundle()
            bundle.putString(BUNDLE_VOUCHER_ID, voucherId)
            bundle.putInt(BUNDLE_VOUCHER_QUOTA, currentQuota)
            bundle.putString(BUNDLE_VOUCHER_TYPE, couponType)
            bundle.putInt(BUNDLE_VOUCHER_MAX_CASHBACK, maxCashback)
            this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
            val tokomemberIntroBottomsheet = TmAddQuotaBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}