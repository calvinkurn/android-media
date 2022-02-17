package com.tokopedia.shop_widget.mvc_locked_to_product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.MvcLockedToProductSortListBottomsheetBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.di.component.DaggerMvcLockedToProductComponent
import com.tokopedia.shop_widget.mvc_locked_to_product.di.module.MvcLockedToProductModule
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductSortListBottomSheetAdapter
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductSortListBottomSheetTypeFactory
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductSortViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel.MvcLockedToProductSortListBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class MvcLockedToProductSortListBottomSheet : BottomSheetUnify(),
    MvcLockedToProductSortViewHolder.MvcLockedToProductSortViewHolderListener {

    companion object {
        private val TAG = MvcLockedToProductSortListBottomSheet::class.java.simpleName
        private const val DEFAULT_MARGIN_AND_PADDING_VALUE = 16
        fun createInstance() = MvcLockedToProductSortListBottomSheet()
    }

    interface Callback {
        fun onApplySort(mvcLockedToProductSortUiModel: MvcLockedToProductSortUiModel)
    }

    private var viewModel: MvcLockedToProductSortListBottomSheetViewModel? = null
    private var selectedSortData: MvcLockedToProductSortUiModel? = null
    private var viewBinding by autoClearedNullable<MvcLockedToProductSortListBottomsheetBinding>()
    private var callback: Callback? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapterSortList by lazy {
        MvcLockedToProductSortListBottomSheetAdapter(
            typeFactory = MvcLockedToProductSortListBottomSheetTypeFactory(
                this
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(MvcLockedToProductSortListBottomSheetViewModel::class.java)
    }

    private fun initInjector() {
        activity?.run {
            DaggerMvcLockedToProductComponent
                .builder()
                .mvcLockedToProductModule(MvcLockedToProductModule())
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this@MvcLockedToProductSortListBottomSheet)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheetChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        configSubmitButton()
        initRecyclerView()
        getSortListData()
        observeSortListData()
    }

    private fun setupView() {
        bottomSheetWrapper.setPadding(
            0,
            DEFAULT_MARGIN_AND_PADDING_VALUE.toPx(),
            0,
            0
        )
        (bottomSheetHeader.layoutParams as? LinearLayout.LayoutParams)?.setMargins(
            DEFAULT_MARGIN_AND_PADDING_VALUE.toPx(),
            0,
            DEFAULT_MARGIN_AND_PADDING_VALUE.toPx(),
            DEFAULT_MARGIN_AND_PADDING_VALUE.toPx()
        )
    }

    fun show(
        manager: FragmentManager,
        mvcLockedToProductSortUiModel: MvcLockedToProductSortUiModel?,
        callback: Callback
    ) {
        selectedSortData = mvcLockedToProductSortUiModel
        this.callback = callback
        show(manager, TAG)
    }

    private fun configSubmitButton() {
        viewBinding?.buttonSubmitSort?.setOnClickListener {
            selectedSortData?.let {
                callback?.onApplySort(it)
                dismiss()
            }
        }
    }

    private fun observeSortListData() {
        viewModel?.sortListLiveData?.observe(viewLifecycleOwner, {
            hideLoaderUnify()
            when (it) {
                is Success -> {
                    setSortListData(it.data)
                    checkSelectedSort()
                }
                else -> {

                }
            }
        })
    }

    private fun checkSelectedSort() {
        val isSelectedSortAvailable = adapterSortList.isSelectedSortAvailable()
        if (isSelectedSortAvailable) {
            enableButtonSubmitSort()
        }
    }

    private fun setSortListData(data: List<MvcLockedToProductSortUiModel>) {
        adapterSortList.addSortListData(data)
    }

    private fun hideLoaderUnify() {
        viewBinding?.loaderUnify?.hide()
    }

    private fun getSortListData() {
        selectedSortData?.let {
            viewModel?.getSortListData(it)
        }
    }

    private fun setupBottomSheetChildView() {
        viewBinding =
            MvcLockedToProductSortListBottomsheetBinding.inflate(LayoutInflater.from(context))
                .apply {
                    setTitle(getString(R.string.mvc_locked_to_product_bottom_sheet_title))
                    setChild(this.root)
                    setCloseClickListener {
                        dismiss()
                    }
                }
    }

    private fun initRecyclerView() {
        viewBinding?.rvSortList?.apply {
            adapter = adapterSortList
            itemAnimator = null
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onSortItemClicked(uiModel: MvcLockedToProductSortUiModel) {
        val oldSelectedData = selectedSortData
        selectedSortData = uiModel
        adapterSortList.setSelectedSortItem(oldSelectedData, false)
        adapterSortList.setSelectedSortItem(selectedSortData, true)
        enableButtonSubmitSort()
    }

    private fun enableButtonSubmitSort() {
        viewBinding?.buttonSubmitSort?.isEnabled = true
    }
}
