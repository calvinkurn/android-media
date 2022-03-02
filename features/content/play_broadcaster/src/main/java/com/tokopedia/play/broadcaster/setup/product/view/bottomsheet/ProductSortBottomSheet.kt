package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSortBinding
import com.tokopedia.play.broadcaster.setup.product.view.model.SortListModel
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SortListViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 02/02/22
 */
class ProductSortBottomSheet @Inject constructor() : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroProductSortBinding? = null
    private val binding: BottomSheetPlayBroProductSortBinding
        get() = _binding!!

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Any>() },
    )

    private val sortListView by viewComponent { SortListViewComponent(binding.rvSort, eventBus) }

    private var mListener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroProductSortBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        setTitle(getString(R.string.play_bro_etalase_sort))
        setAction(getString(R.string.play_label_save)) {
            val selectedId = requireArguments().getInt(ARGS_SELECTED_SORT_ID, -1)
            val selectedSort = SortUiModel.getSortById(selectedId)
            if (selectedSort != null) mListener?.onSortChosen(this, selectedSort)
            dismiss()
        }

        refreshSortList()
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.subscribe().collect {
                when (it) {
                    is SortListViewComponent.Event.OnSelected -> {
                        requireArguments().putInt(ARGS_SELECTED_SORT_ID, it.sort.id)
                        refreshSortList()
                    }
                }
            }
        }
    }

    private fun refreshSortList() {
        sortListView.setSortList(getSortList())
    }

    private fun getSortList(): List<SortListModel> {
        return SortUiModel.supportedSortList.map {
            SortListModel(
                id = it.id,
                text = it.text,
                isSelected = it.id == arguments?.getInt(ARGS_SELECTED_SORT_ID, -1),
            )
        }
    }

    companion object {
        private const val TAG = "ProductSortBottomSheet"

        private const val ARGS_SELECTED_SORT_ID = "selected_sort_id"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            selectedId: Int? = null,
        ): ProductSortBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductSortBottomSheet
            val bottomSheet = if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    ProductSortBottomSheet::class.java.name
                ) as ProductSortBottomSheet
            }
            val newArgs = bottomSheet.arguments ?: Bundle()
            if (selectedId == null) newArgs.remove(ARGS_SELECTED_SORT_ID)
            else newArgs.putInt(ARGS_SELECTED_SORT_ID, selectedId)
            return bottomSheet.apply {
                arguments = newArgs
            }
        }
    }

    interface Listener {
        fun onSortChosen(bottomSheet: ProductSortBottomSheet, item: SortUiModel)
    }
}