package com.tokopedia.gifting.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gifting.di.DaggerGiftingComponent
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class GiftingBottomSheet(private val productId: Long) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: GiftingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setTitle("Cara atur bingkisan")
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
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

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context, R.layout.bottomsheet_gifting, null)
        setChild(contentView)
    }
}