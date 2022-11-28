package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.orUnknown
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSortBinding
import com.tokopedia.play.broadcaster.setup.product.view.model.SortListModel
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SortListViewComponent
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 02/02/22
 */
class ProductSortBottomSheet @Inject constructor(
    private val analytic: PlayBroSetupProductAnalytic,
) : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroProductSortBinding? = null
    private val binding: BottomSheetPlayBroProductSortBinding
        get() = _binding!!

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Any>() },
    )

    private val sortListView by viewComponent { SortListViewComponent(binding.rvSort, eventBus) }

    private var mListener: Listener? = null

    private var mDataSource: DataSource? = null

    private var mSelectedSort: SortUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
        setupAnalytic()
    }

    override fun onStart() {
        super.onStart()
        analytic.viewProductSortingBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
        mDataSource = null
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
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
        mSelectedSort = arguments?.getParcelable(ARGS_SELECTED_SORT)
        setTitle(getString(R.string.play_bro_etalase_sort))
        setAction(getString(R.string.play_label_save)) {
            val selectedSort = mSelectedSort
            if (selectedSort != null) mListener?.onSortChosen(this, selectedSort)
            dismiss()
        }

        setCloseClickListener {
            analytic.clickCloseOnProductSortingBottomSheet()
            dismiss()
        }

        refreshSortList()
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.subscribe().collect {
                when (it) {
                    is SortListViewComponent.Event.OnSelected -> {
                        mSelectedSort = it.sort.sort
                        refreshSortList()
                    }
                }
            }
        }
    }

    private fun setupAnalytic() {
        analytic.setSelectedAccount(mDataSource?.getSelectedAccount().orUnknown())
    }

    private fun refreshSortList() {
        sortListView.setSortList(getSortList())
    }

    private fun getSortList(): List<SortListModel> {
        val selectedSort = mSelectedSort
        return SortUiModel.supportedSortList.map {
            SortListModel(
                sort = it,
                isSelected = it == selectedSort,
            )
        }
    }

    companion object {
        private const val TAG = "ProductSortBottomSheet"

        private const val ARGS_SELECTED_SORT = "selected_sort_id"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            selected: SortUiModel? = null,
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
            if (selected == null) newArgs.remove(ARGS_SELECTED_SORT)
            else newArgs.putParcelable(ARGS_SELECTED_SORT, selected)
            return bottomSheet.apply {
                arguments = newArgs
            }
        }
    }

    interface Listener {
        fun onSortChosen(bottomSheet: ProductSortBottomSheet, item: SortUiModel)
    }

    interface DataSource {
        fun getSelectedAccount(): ContentAccountUiModel
    }
}
