package com.tokopedia.play.broadcaster.view.fragment.edit

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEditProductViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class SimpleEditProductBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        dispatcher: CoroutineDispatchers,
        private val dialogCustomizer: PlayBroadcastDialogCustomizer
) : BottomSheetDialogFragment() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private lateinit var btnAction: UnifyButton
    private lateinit var vDragArea: View
    private lateinit var tvChooseOver: TextView
    private lateinit var tvSelectedProductTitle: TextView
    private lateinit var rvSelectedProduct: RecyclerView
    private lateinit var coordinatorEdit: CoordinatorLayout
    private lateinit var llBtnContainer: LinearLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var viewModel: PlayEditProductViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private var mListener: Listener? = null

    private var toasterBottomMargin = 0

    private val selectableProductAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: String, isSelected: Boolean) {
            viewModel.selectProduct(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {

        }
    })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayEditProductViewModel::class.java)
        dataStoreViewModel = ViewModelProvider(this, viewModelFactory).get(DataStoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_simple_edit_product, container, false)
        dialog?.let { setupDialog(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun initView(view: View) {
        with(view) {
            btnAction = findViewById(R.id.btn_action)
            vDragArea = findViewById(R.id.v_drag_area)
            tvChooseOver = findViewById(R.id.tv_choose_over)
            tvSelectedProductTitle = findViewById(R.id.tv_selected_product_title)
            rvSelectedProduct = findViewById(R.id.rv_selected_product)
            coordinatorEdit = findViewById(R.id.coordinator_edit)
            llBtnContainer = findViewById(R.id.ll_btn_container)
        }

    }

    private fun setupView(view: View) {
        rvSelectedProduct.adapter = selectableProductAdapter
        rvSelectedProduct.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
        rvSelectedProduct.addOnScrollListener(StopFlingScrollListener())

        btnAction.setOnClickListener {
            if (btnAction.isLoading) return@setOnClickListener
            viewModel.uploadProduct()
        }
        tvChooseOver.setOnClickListener { mListener?.onChooseOver(this@SimpleEditProductBottomSheet) }

        setSelectedProductList(viewModel.selectedProducts)
    }

    private fun setupObserve() {
        observeSelectedProducts()
        observeUploadProduct()
    }

    private fun setSelectedProductList(productList: List<ProductContentUiModel>) {
        updateTitle(productList.size)
        selectableProductAdapter.setItems(productList)
        selectableProductAdapter.notifyDataSetChanged()
    }

    private fun updateTitle(productCount: Int) {
        tvSelectedProductTitle.text = requireContext().getString(R.string.play_selected_products, productCount)
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = (MAX_HEIGHT_MULTIPLIER * maxHeight()).toInt()
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.peekHeight = (MAX_HEIGHT_MULTIPLIER * maxHeight()).toInt()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            isCancelable = true
        }
    }

    private fun maxHeight(): Int = getScreenHeight()

    private fun onUploadSuccess() {
        scope.launch {
            val error = mListener?.onSaveEditedProductList(this@SimpleEditProductBottomSheet, dataStoreViewModel.getDataStore())
            if (error != null) {
                yield()
                onUploadFailed(error)
            }
            else dismiss()
        }
    }

    private fun onUploadFailed(e: Throwable) {
        btnAction.isLoading = false
        showErrorToaster(e)
    }

    private fun showErrorToaster(err: Throwable, duration: Int = Toaster.LENGTH_SHORT) {
        if (toasterBottomMargin == 0) {
            toasterBottomMargin = llBtnContainer.height
        }

        coordinatorEdit.showErrorToaster(
            err = err,
            duration = duration,
            bottomMargin = toasterBottomMargin
        )
    }

    /**
     * Observe
     */
    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner) {
            updateTitle(it.size)
            btnAction.isEnabled = (it.size != selectableProductAdapter.itemCount) && it.isNotEmpty()
        }
    }

    private fun observeUploadProduct() {
        viewModel.observableUploadProductEvent.observe(viewLifecycleOwner) {
            when (it) {
                NetworkResult.Loading -> btnAction.isLoading = true
                is NetworkResult.Fail -> onUploadFailed(it.error)
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) onUploadSuccess()
                }
            }
        }
    }

    companion object {
        private const val MAX_HEIGHT_MULTIPLIER = 0.95

        private const val TAG = "Simple Edit Product BottomSheet"
    }

    interface Listener {

        fun onChooseOver(bottomSheet: BottomSheetDialogFragment)
        suspend fun onSaveEditedProductList(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable?
    }
}