package com.tokopedia.epharmacy.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.epharmacy.databinding.EpharmacyMasterMiniConsultationBottomSheetBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.params.GetMiniConsultationBottomSheetParams
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse
import com.tokopedia.epharmacy.ui.adapter.EpharmacyMiniConsultationStepsAdapter
import com.tokopedia.epharmacy.viewmodel.MiniConsultationMasterBsViewModel
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MiniConsultationMasterBottomSheetInfo : BottomSheetUnify() {

    private var binding by autoClearedNullable<EpharmacyMasterMiniConsultationBottomSheetBinding>()
    private val miniConsultationAdapter: EpharmacyMiniConsultationStepsAdapter by lazy {
        EpharmacyMiniConsultationStepsAdapter(mutableListOf())
    }

    companion object {
        private const val DATA_TYPE = "data_type"
        private const val ENABLER_NAME = "enabler_name"
        fun newInstance(dataType: String, enabler: String
        ): MiniConsultationMasterBottomSheetInfo {
            return MiniConsultationMasterBottomSheetInfo().apply {
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                arguments = Bundle().apply {
                    putString(DATA_TYPE, dataType)
                    putString(ENABLER_NAME, enabler)
                }
            }
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val viewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(
                MiniConsultationMasterBsViewModel::class.java
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyMasterMiniConsultationBottomSheetBinding.inflate(
            inflater, container, false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initRecyclerView()
        setupObservers()
    }

    private fun init(){
        binding?.let {
            with(it) {
                shimmerParent.parentShimmerView.show()
                bottomSheetParent.invisible()
                viewModel?.getEPharmacyMiniConsultationDetail(requestParams())
                closeIcon.setOnClickListener {
                    closeBottomSheet()
                }
                setOnDismissListener {
                    activity?.finish()
                }
            }
        }
    }

    private fun requestParams(): GetMiniConsultationBottomSheetParams {
        val dataType = arguments?.getString(DATA_TYPE)
        val enabler = arguments?.getString(ENABLER_NAME)
        if(dataType != null && enabler != null) {
            return GetMiniConsultationBottomSheetParams(
                dataType = dataType,
                enablerName = enabler
            )
        }
        else{
            closeBottomSheet()
            return GetMiniConsultationBottomSheetParams()
        }
    }

    private fun initRecyclerView() {
        binding?.stepListRv?.apply {
            adapter = miniConsultationAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel?.miniConsultationLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val bottomSheetData = it.data.data
                    setupBottomSheetUiData(bottomSheetData)
                }
                is Fail -> {
                    onFailMiniConsultationData(it)
                }
            }
        }
    }

    private fun setupBottomSheetUiData(bottomSheetData: EPharmacyMiniConsultationMasterResponse.EPharmacyMiniConsultationData?) {
        binding?.let {
            with(it) {
                shimmerParent.parentShimmerView.invisible()
                bottomSheetParent.show()
                headingTitle.text = bottomSheetData?.infoTitle.toEmptyStringIfNull()
                paraSubtitle.text = bottomSheetData?.infoText.toEmptyStringIfNull()
                headingSubtitle.text = bottomSheetData?.stepTitle.toEmptyStringIfNull()
            }
        }
        bottomSheetData?.steps?.let { miniConsultationAdapter.setStepList(it) }
    }


    private fun onFailMiniConsultationData(miniConsultationMasterResponseFail: Fail) {

    }

    private fun closeBottomSheet(){
        dismiss()
        activity?.finish()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): EPharmacyComponent =
        DaggerEPharmacyComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
}
