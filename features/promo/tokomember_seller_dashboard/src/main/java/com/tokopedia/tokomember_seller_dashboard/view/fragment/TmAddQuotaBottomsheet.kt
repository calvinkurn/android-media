package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_TYPE
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_layout_add_quota.*
import javax.inject.Inject

class TmAddQuotaBottomsheet: BottomSheetUnify() {

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

        arguments?.getString(BUNDLE_VOUCHER_ID)?.let {
            voucherId = it
        }
        arguments?.getString(BUNDLE_VOUCHER_TYPE)?.let {
            voucherType = it
        }
        arguments?.getInt(BUNDLE_VOUCHER_QUOTA)?.let {
            voucherQuota = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
        tmCouponViewModel.getInitialCouponData("update", voucherType)
        observeViewModel()
    }

    private fun observeViewModel() {
        tmCouponViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    it.data.getInitiateVoucherPage?.data?.token?.let { it1 ->
                        token = it1
                    }
                }
                is Fail ->{

                }
            }
        })

        tmCouponViewModel.tmCouponQuotaUpdateLiveData.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    if(it.data.data?.merchantPromotionUpdateMVQuota?.data?.status == "success"){
                        tmCouponListRefreshCallback.refreshCouponList()
                        dismiss()
                    }
                }
                is Fail ->{
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
                if(textFieldQuota.editText.text.toString().toIntOrZero() <= voucherQuota){
                    textFieldQuota.textInputLayout.error = "Minimum kuota: $voucherQuota"
                }
                else{
                    textFieldQuota.textInputLayout.error = ""
                }
            }
        })
        btnContinue.setOnClickListener {
            if(token.isEmpty()){
                tmCouponViewModel.getInitialCouponData("update", voucherType)
            }
            else {
                tmCouponViewModel.updateQuota(
                    textFieldQuota.editText.text.toString().toIntOrZero(),
                    voucherId.toIntOrZero(),
                    token
                )
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
            tmCouponListRefreshCallback: TmCouponListRefreshCallback
        ){
            val bundle = Bundle()
            bundle.putString(BUNDLE_VOUCHER_ID, voucherId)
            bundle.putInt(BUNDLE_VOUCHER_QUOTA, currentQuota)
            bundle.putString(BUNDLE_VOUCHER_TYPE, couponType)
            this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
            val tokomemberIntroBottomsheet = TmAddQuotaBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}