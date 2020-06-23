package com.tokopedia.play.broadcaster.view.fragment.edit

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEditProductViewModel
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class SimpleEditProductBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : BottomSheetDialogFragment() {

    private lateinit var btnAction: UnifyButton
    private lateinit var vDragArea: View
    private lateinit var tvChooseOver: TextView
    private lateinit var tvSelectedProductTitle: TextView
    private lateinit var rvSelectedProduct: RecyclerView

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var viewModel: PlayEditProductViewModel

    private var mListener: Listener? = null

    private val selectableProductAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
            viewModel.selectProduct(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {

        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayEditProductViewModel::class.java)
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSelectedProducts()
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
        }

    }

    private fun setupView(view: View) {
        rvSelectedProduct.adapter = selectableProductAdapter
        rvSelectedProduct.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))

        btnAction.setOnClickListener { mListener?.onSaveEditedProductList(viewModel.getDataStore()) }
        tvChooseOver.setOnClickListener { mListener?.onChooseOver() }

        setSelectedProductList(viewModel.selectedProducts)
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
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.peekHeight = (MAX_HEIGHT_MULTIPLIER * maxHeight()).toInt()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            isCancelable = false
        }
    }

    private fun maxHeight(): Int = getScreenHeight()

    /**
     * Observe
     */
    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            updateTitle(it.size)
            btnAction.isEnabled = it.size != selectableProductAdapter.itemCount
        })
    }

    companion object {
        private const val MAX_HEIGHT_MULTIPLIER = 0.6

        private const val TAG = "Simple Edit Product BottomSheet"
    }

    interface Listener {

        fun onChooseOver()
        fun onSaveEditedProductList(dataStore: PlayBroadcastSetupDataStore)
    }
}