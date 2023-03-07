package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetUnificationTabBinding
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.UnificationTabAdapter

/**
 * Created by @ilhamsuaib on 20/07/22.
 */

class UnificationTabBottomSheet : BaseBottomSheet<ShcBottomSheetUnificationTabBinding>() {

    companion object {
        private const val TAG = "UnificationTabBottomSheet"
        fun createInstance(): UnificationTabBottomSheet {
            return UnificationTabBottomSheet().apply {
                clearContentPadding = true
            }
        }
    }

    private var onTabSelected: (UnificationTabUiModel) -> Unit = {}
    private var items: List<UnificationTabUiModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetUnificationTabBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {
        setTitle(root.context.getString(R.string.shc_select_analysis))
        setupRecyclerView()
    }

    fun setOnTabItemSelected(action: (UnificationTabUiModel) -> Unit): UnificationTabBottomSheet {
        this.onTabSelected = action
        return this
    }

    fun setItems(items: List<UnificationTabUiModel>): UnificationTabBottomSheet {
        this.items = items
        return this
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved || isVisible) {
            //can't show the bottom sheet on state saved or if bottom sheet already shown
            return
        }

        show(fm, TAG)
    }

    private fun setupRecyclerView() {
        val tabAdapter = UnificationTabAdapter(items)
        tabAdapter.setOnItemSelectedListener(onTabSelected)
        binding?.rvShcUnification?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = tabAdapter
        }
    }
}