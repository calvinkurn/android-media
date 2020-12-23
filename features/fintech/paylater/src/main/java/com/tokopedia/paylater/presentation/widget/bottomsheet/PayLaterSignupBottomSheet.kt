package com.tokopedia.paylater.presentation.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.presentation.adapter.PayLaterPaymentMethodAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.paylater_signup_bottomsheet_widget.*
import javax.inject.Inject

class PayLaterSignupBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var component: PayLaterComponent? = null

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(parentFragment!!, viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.paylater_signup_bottomsheet_widget
    private var payLaterDataList: ArrayList<PayLaterItemProductData> = arrayListOf()
    private var payLaterApplicationStatusList: ArrayList<PayLaterApplicationDetail> = arrayListOf()
    private var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun initInjector() {
        component = PayLaterComponent::class.java.cast((activity as (HasComponent<PayLaterComponent>)).component)
        component?.inject(this) ?: dismiss()
    }


    private fun getArgumentData() {
        arguments?.let {
            payLaterDataList = it.getParcelableArrayList(PAY_LATER_PARTNER_DATA) ?: arrayListOf()
            payLaterApplicationStatusList = it.getParcelableArrayList(PAY_LATER_APPLICATION_DATA)
                    ?: arrayListOf()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //initAdapter()
        payLaterViewModel.let { vm ->
            vm.payLaterApplicationStatusResultLiveData.observe(viewLifecycleOwner, {
                when (it) {
                    is Success -> onPayLaterApplicationStatusLoaded(it.data)
                    is Fail -> onPayLaterApplicationLoadingFail(it.throwable)
                }
            })

        }
    }


    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        rvPayLaterPaymentMethods.adapter = PayLaterPaymentMethodAdapter(payLaterDataList, payLaterApplicationStatusList) { payLaterData, payLaterApplicationStatus ->
            listener?.onPayLaterSignupClicked(payLaterData, payLaterApplicationStatus)
            dismiss()
            //openBottomSheet(payLaterData, payLaterApplicationStatus)
        }
        rvPayLaterPaymentMethods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDefaultParams() {
        setTitle(DIALOG_TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    fun setActionListener(listener: Listener) {
        this.listener = listener
    }

    private fun onPayLaterApplicationLoadingFail(throwable: Throwable) {
        payLaterViewModel.let {
            val payLaterProductList = ArrayList<PayLaterItemProductData>()
            payLaterProductList.addAll(it.getPayLaterOptions())
            // @Todo remove below lines
            payLaterProductList.add(payLaterProductList[0])
            payLaterProductList.add(payLaterProductList[0])
            this.payLaterDataList = payLaterProductList
            initAdapter()
        }
    }

    private fun onPayLaterApplicationStatusLoaded(data: UserCreditApplicationStatus) {
        payLaterViewModel.let {

            val payLaterProductList = ArrayList<PayLaterItemProductData>()
            payLaterProductList.addAll(it.getPayLaterOptions())
            // @Todo remove below lines
            payLaterProductList.add(payLaterProductList[0])
            payLaterProductList.add(payLaterProductList[0])
            this.payLaterDataList = payLaterProductList
            this.payLaterApplicationStatusList = data.applicationDetailList
            initAdapter()
        }
    }


    companion object {
        private const val DIALOG_TITLE = "Mau daftar PayLater apa?"
        const val PAY_LATER_PARTNER_DATA = "payLaterPartnerData"
        const val PAY_LATER_APPLICATION_DATA = "payLaterApplicationData"

        const val TAG = "FT_TAG"
        fun getInstance(bundle: Bundle): PayLaterSignupBottomSheet {
            return PayLaterSignupBottomSheet().apply {
                arguments = bundle
            }
        }
    }

    interface Listener {
        fun onPayLaterSignupClicked(productItemData: PayLaterItemProductData, partnerApplicationDetail: PayLaterApplicationDetail?)
    }
}