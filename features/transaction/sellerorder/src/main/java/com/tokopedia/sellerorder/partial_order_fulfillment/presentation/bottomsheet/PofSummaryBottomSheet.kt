package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.BottomSheetPofSummaryBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapter
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactoryImpl
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class PofSummaryBottomSheet : BottomSheetUnify(), PofAdapterTypeFactory.Listener {

    companion object {
        val TAG = PofSummaryBottomSheet::class.java.name
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PofAdapter(PofAdapterTypeFactoryImpl(this))
    }

    private var binding by viewBinding(BottomSheetPofSummaryBinding::bind)

    init {
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetPofSummaryBinding.inflate(inflater).also { setChild(it.root) }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
        setupRecyclerView()
    }

    override fun onEvent(event: UiEvent) {
        // noop
    }

    private fun setupBottomSheet() {
        setTitle(getString(R.string.som_pof_summary_title))
    }

    private fun setupRecyclerView() {
        binding?.rvPofSummary?.layoutManager = LinearLayoutManager(context)
        binding?.rvPofSummary?.adapter = adapter
    }

    fun updateUi(items: List<PofVisitable>) {
        adapter.update(items)
    }
}
