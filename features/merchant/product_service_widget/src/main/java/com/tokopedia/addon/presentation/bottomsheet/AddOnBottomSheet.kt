package com.tokopedia.addon.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.addon.di.DaggerAddOnComponent
import com.tokopedia.addon.presentation.viewmodel.AddOnViewModel
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetAddonBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AddOnBottomSheet(private val addOnId: String) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: AddOnViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<BottomsheetAddonBinding>()
    //private val titleTips by lazy { binding?.layoutContent?.titleTips }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        clearContentPadding = true
        overlayClickDismiss = true
        binding = BottomsheetAddonBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.gifting_title_bottomsheet))
        initInjector()

        observeGetAddOnByProduct()

        viewModel.getAddOn(addOnId)
    }

    private fun initInjector() {
        DaggerAddOnComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeGetAddOnByProduct() {
        viewModel.getAddOnResult.observe(viewLifecycleOwner) {

        }
    }
}
