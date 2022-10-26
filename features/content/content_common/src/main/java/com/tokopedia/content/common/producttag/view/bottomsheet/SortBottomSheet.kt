package com.tokopedia.content.common.producttag.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.BottomSheetSortBinding
import com.tokopedia.content.common.producttag.view.adapter.SortAdapter
import com.tokopedia.content.common.producttag.view.decoration.SortItemDecoration
import com.tokopedia.content.common.producttag.view.uimodel.SortUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 20, 2022
 */
class SortBottomSheet @Inject constructor() : BottomSheetUnify() {

    private var _binding: BottomSheetSortBinding? = null
    private val binding: BottomSheetSortBinding
        get() = _binding!!

    private var mListener: Listener? = null
    private var mSortList = mutableListOf<SortUiModel>()
    private val adapter: SortAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        SortAdapter(
            onSelected = { selectedSort ->
                val newList = mSortList.map { sort ->
                    sort.copy(isSelected = selectedSort.isSame(sort))
                }

                setData(newList)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetSortBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
    }

    private fun setupView() {
        setTitle(getString(R.string.cc_product_tag_sort_label))
        setAction(getString(R.string.cc_product_tag_save)) {
            mSortList.firstOrNull { it.isSelected }?.let { sort ->
                mListener?.onSortSelected(sort)
            }
            dismiss()
        }

        binding.rvSortProduct.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSortProduct.addItemDecoration(SortItemDecoration(requireContext()))
        binding.rvSortProduct.adapter = adapter

        adapter.setItemsAndAnimateChanges(mSortList)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(sortList: List<SortUiModel>) {
        mSortList.clear()
        mSortList.addAll(sortList)

        if(isAdded) adapter.setItemsAndAnimateChanges(mSortList)
    }

    fun showNow(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "SortBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): SortBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? SortBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                SortBottomSheet::class.java.name
            ) as SortBottomSheet
        }
    }

    interface Listener {
        fun onSortSelected(sort: SortUiModel)
    }
}