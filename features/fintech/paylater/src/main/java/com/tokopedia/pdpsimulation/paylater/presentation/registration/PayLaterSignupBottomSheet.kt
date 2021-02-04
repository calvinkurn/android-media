package com.tokopedia.pdpsimulation.paylater.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterPartnerStepDetails
import com.tokopedia.pdpsimulation.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.pdpsimulation.paylater.mapper.ProcessingApplicationPartnerType
import com.tokopedia.pdpsimulation.paylater.mapper.RegisterStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.mapper.UsageStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.base_list_bottomsheet_widget.*
import javax.inject.Inject

class PayLaterSignupBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var component: PdpSimulationComponent? = null

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

    private val childLayoutRes = R.layout.base_list_bottomsheet_widget
    private var payLaterDataList: ArrayList<PayLaterItemProductData> = arrayListOf()
    private var payLaterApplicationStatusList: ArrayList<PayLaterApplicationDetail> = arrayListOf()
    private var pdpSimulationCallback: PdpSimulationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun initInjector() {
        component = PdpSimulationComponent::class.java.cast((activity as (HasComponent<PdpSimulationComponent>)).component)
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
        baseList.adapter = PayLaterPaymentMethodAdapter(payLaterDataList, payLaterApplicationStatusList) { payLaterData, payLaterApplicationStatus ->
            openBottomSheet(payLaterData, payLaterApplicationStatus)
            dismiss()
        }
        baseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDefaultParams() {
        setTitle(DIALOG_TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun onPayLaterApplicationLoadingFail(throwable: Throwable) {
        payLaterViewModel.let {
            this.payLaterDataList = it.getPayLaterOptions()
            initAdapter()
        }
    }

    private fun onPayLaterApplicationStatusLoaded(data: UserCreditApplicationStatus) {
        payLaterViewModel.let {
            this.payLaterDataList = it.getPayLaterOptions()
            this.payLaterApplicationStatusList = data.applicationDetailList ?: arrayListOf()
            initAdapter()
        }
    }

    private fun openBottomSheet(
            productItemData: PayLaterItemProductData,
            partnerApplicationDetail: PayLaterApplicationDetail?,
    ) {
        val bundle = Bundle()
        productItemData.let { data ->
            bundle.putString(PayLaterActionStepsBottomSheet.ACTION_URL, data.actionWebUrl)
            when (PayLaterPartnerTypeMapper.getPayLaterPartnerType(data, partnerApplicationDetail)) {
                is RegisterStepsPartnerType ->
                    openActionBottomSheet(
                            bundle,
                            data.partnerApplyDetails,
                            "${context?.getString(R.string.pay_later_how_to_register)} ${data.partnerName}")

                is UsageStepsPartnerType ->
                    openActionBottomSheet(
                            bundle,
                            data.partnerUsageDetails,
                            "${context?.getString(R.string.pay_later_how_to_use)} ${data.partnerName}")

                is ProcessingApplicationPartnerType ->
                    openVerificationBottomSheet(bundle, partnerApplicationDetail)
            }
        }
    }

    private fun openActionBottomSheet(bundle: Bundle, partnerData: PayLaterPartnerStepDetails?, title: String) {
        bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, partnerData)
        bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, title)
        pdpSimulationCallback?.openBottomSheet(bundle, PayLaterActionStepsBottomSheet::class.java)
    }

    private fun openVerificationBottomSheet(bundle: Bundle, partnerApplicationDetail: PayLaterApplicationDetail?) {
        bundle.putParcelable(PayLaterVerificationBottomSheet.APPLICATION_STATUS, partnerApplicationDetail)
        pdpSimulationCallback?.openBottomSheet(bundle, PayLaterVerificationBottomSheet::class.java)
    }

    companion object {
        private const val DIALOG_TITLE = "Mau daftar PayLater apa?"
        const val PAY_LATER_PARTNER_DATA = "payLaterPartnerData"
        const val PAY_LATER_APPLICATION_DATA = "payLaterApplicationData"

        const val TAG = "PL_TAG"
        fun show(bundle: Bundle, pdpSimulationCallback: PdpSimulationCallback, childFragmentManager: FragmentManager) {
            val fragment = PayLaterSignupBottomSheet()
            fragment.arguments = bundle
            fragment.pdpSimulationCallback = pdpSimulationCallback
            fragment.show(childFragmentManager, TAG)
        }
    }
}