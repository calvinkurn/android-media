package com.tokopedia.epharmacy.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.databinding.EpharmacyItemMasterMiniConsultationBsBinding
import com.tokopedia.epharmacy.databinding.EpharmacyMasterMiniConsultationBottomSheetBinding
import com.tokopedia.epharmacy.viewmodel.MiniConsultationMasterBsViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class MiniConsultationMasterBottomSheetInfo : BottomSheetUnify() {

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var binding by viewBinding(EpharmacyItemMasterMiniConsultationBsBinding::bind)

    companion object {
        private const val TICKER_DATA = "tickerData"
        private const val TICKER_ID = "tickerId"
        fun newInstance(
        ): MiniConsultationMasterBottomSheetInfo {
            return MiniConsultationMasterBottomSheetInfo().apply {
                arguments = Bundle().apply {}
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), viewModelFactory).get(MiniConsultationMasterBsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = EpharmacyItemMasterMiniConsultationBsBinding.inflate(
            inflater, container, false
        ).apply { binding = this }.root
        setChild(view)
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        arguments?.let {}
        setData()
        setCloseClickListener {
            dismiss()
        }
    }

    private fun setData() {

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        initInject()
        return super.onCreateDialog(savedInstanceState)
    }
}
