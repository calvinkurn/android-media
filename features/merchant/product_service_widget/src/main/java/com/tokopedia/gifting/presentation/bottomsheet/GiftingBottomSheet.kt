package com.tokopedia.gifting.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gifting.di.DaggerGiftingComponent
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class GiftingBottomSheet: BottomSheetUnify() {

    @Inject
    lateinit var viewModel: GiftingViewModel

    init {
        setTitle("werwer")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInjector()
        viewModel.dummy()
    }

    private fun initInjector() {
        DaggerGiftingComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}