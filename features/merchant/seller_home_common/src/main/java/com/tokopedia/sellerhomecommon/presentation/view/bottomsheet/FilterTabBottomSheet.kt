package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetFilterTabBinding
import com.tokopedia.sellerhomecommon.presentation.model.FilterTabUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.FilterTabAdapter

class FilterTabBottomSheet : BaseBottomSheet<ShcBottomSheetFilterTabBinding>(),
    FilterTabAdapter.Listener {

    companion object {
        private const val TITLE_KEY = "title"
        private const val FILTERS_KEY = "filters"

        private const val TAG = "FilterTabBottomSheet"

        fun createInstance(
            title: String,
            uiModels: List<FilterTabUiModel>
        ): FilterTabBottomSheet {
            return FilterTabBottomSheet().apply {
                clearContentPadding = true
                arguments = Bundle().apply {
                    putString(TITLE_KEY, title)
                    putParcelableArrayList(FILTERS_KEY, ArrayList(uiModels))
                }
            }
        }
    }

    private val title by lazy {
        arguments?.getString(TITLE_KEY).orEmpty()
    }

    private var onFilterTabSelected: ((FilterTabUiModel) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetFilterTabBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() {
        setTitle(title)
        setupRecyclerView()
    }

    override fun onFilterTabSelected(uiModel: FilterTabUiModel) {
        onFilterTabSelected?.invoke(uiModel)
        dismiss()
    }

    fun setFilterTabSelectListener(onClick: (FilterTabUiModel) -> Unit) {
        onFilterTabSelected = onClick
    }

    fun show(fm: FragmentManager) {
        if (!fm.isStateSaved) {
            show(fm, TAG)
        }
    }

    private fun setupRecyclerView() {
        binding?.rvShcFilterTabBottomSheet?.run {
            val filters = arguments?.getParcelableArrayList<FilterTabUiModel>(FILTERS_KEY)?.toList().orEmpty()
            adapter = FilterTabAdapter(filters, this@FilterTabBottomSheet)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }
}
