package com.tokopedia.tokopedianow.buyercomm.presentation.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.buyercomm.di.component.DaggerBuyerCommunicationComponent
import com.tokopedia.tokopedianow.buyercomm.presentation.viewmodel.TokoNowBuyerCommunicationViewModel
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowBuyerCommunicationBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import javax.inject.Inject

class TokoNowBuyerCommunicationBottomSheet: BottomSheetUnify() {

    companion object {
        fun newInstance(
            data: BuyerCommunicationData?
        ): TokoNowBuyerCommunicationBottomSheet {
            return TokoNowBuyerCommunicationBottomSheet().apply {
                this.data = data
            }
        }

        private val TAG = TokoNowBuyerCommunicationBottomSheet::class.java.simpleName
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowBuyerCommunicationBinding>()

    private var data: BuyerCommunicationData? = null

    @Inject
    lateinit var viewModel: TokoNowBuyerCommunicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        observeLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(data)
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.finish()
        super.onDismiss(dialog)
    }

    private fun initBottomSheet() {
        binding = BottomsheetTokopedianowBuyerCommunicationBinding
            .inflate(LayoutInflater.from(context))
        setTitle(getString(R.string.tokopedianow_buyer_communication_title))
        setChild(binding?.root)
        isFullpage = false
    }

    private fun observeLiveData() {
        observe(viewModel.buyerCommunicationData) {

        }
    }

    private fun injectDependencies() {
        DaggerBuyerCommunicationComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}
