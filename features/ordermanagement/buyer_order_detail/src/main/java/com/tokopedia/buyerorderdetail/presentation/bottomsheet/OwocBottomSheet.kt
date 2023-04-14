package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.OwocBottomsheetBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocSectionGroupAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocSectionGroupListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OwocBottomSheet: BottomSheetUnify(), OwocSectionGroupListener {

    private var binding by autoClearedNullable<OwocBottomsheetBinding>()

    private val navigator: BuyerOrderDetailNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BuyerOrderDetailNavigator(requireActivity(), this)
    }

    private val typeFactory: OwocSectionGroupTypeFactoryImpl by lazy(LazyThreadSafetyMode.NONE) {
        OwocSectionGroupTypeFactoryImpl(navigator, this)
    }

    private val owocSectionGroupAdapter: OwocSectionGroupAdapter by lazy(LazyThreadSafetyMode.NONE) {
        OwocSectionGroupAdapter(typeFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        binding = OwocBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onErrorActionClicked() {
        TODO("Not yet implemented")
    }

    private fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerBuyerOrderDetailComponent.builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    private fun setupRecyclerView() {
        binding?.rvOwocList?.run {
            if (adapter != owocSectionGroupAdapter) {
                layoutManager = LinearLayoutManager(context)
                adapter = owocSectionGroupAdapter
            }
        }
    }

    companion object {
        fun newInstance(): OwocBottomSheet {
            return OwocBottomSheet()
        }
    }
}
