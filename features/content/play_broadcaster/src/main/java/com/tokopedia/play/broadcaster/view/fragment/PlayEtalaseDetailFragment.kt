package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.util.doOnPreDraw
import com.tokopedia.play.broadcaster.util.productEtalaseEmpty
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play.broadcaster.util.scroll.StopFlingScrollListener
import com.tokopedia.play.broadcaster.util.showToaster
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.contract.ProductSetupListener
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.BottomActionPartialView
import com.tokopedia.play.broadcaster.view.partial.SelectedProductPagePartialView
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 27/05/20
 */
class PlayEtalaseDetailFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private val etalaseId: String
        get() = arguments?.getString(EXTRA_ETALASE_ID) ?: throw IllegalStateException("etalaseId must be set")

    private lateinit var tvInfo: TextView
    private lateinit var rvProduct: RecyclerView
    private lateinit var errorEmptyProduct: GlobalError
    private lateinit var bottomSheetHeader: PlayBottomSheetHeader

    private lateinit var selectedProductPage: SelectedProductPagePartialView
    private lateinit var bottomActionView: BottomActionPartialView

    private var shouldLoadFirst = true

    private var mListener: ProductSetupListener? = null

    private lateinit var selectableProductAdapter: ProductSelectableAdapter

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun getScreenName(): String = "Etalase Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_play_etalase_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeProductsInSelectedEtalase()
        observeSelectedProducts()
        observeUploadProduct()
    }

    override fun onInterceptBackPressed(): Boolean {
        return false
    }

    fun setListener(listener: ProductSetupListener) {
        mListener = listener
    }

    private fun initView(view: View) {
        with(view) {
            tvInfo = findViewById(R.id.tv_info)
            rvProduct = findViewById(R.id.rv_product)
            errorEmptyProduct = findViewById(R.id.error_empty_product)
            bottomSheetHeader = findViewById(R.id.bottom_sheet_header)
        }

        selectedProductPage = SelectedProductPagePartialView(view as ViewGroup, object : SelectedProductPagePartialView.Listener {
            override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
                viewModel.selectProduct(productId, isSelected)
                onSelectedProductChanged()
            }
        })

        bottomActionView = BottomActionPartialView(view, object : BottomActionPartialView.Listener {
            override fun onInventoryIconClicked() {
                showSelectedProductPage()
            }

            override fun onNextButtonClicked() {
                uploadProduct()
            }
        })
    }

    private fun setupView(view: View) {
        selectableProductAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
            private var isAlreadyBound = false

            override fun onImageLoaded(position: Int, isSuccess: Boolean) {
                if (!isAlreadyBound) {
                    startPostponedTransition()
                    isAlreadyBound = true
                }
            }

            override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
                viewModel.selectProduct(productId, isSelected)
            }

            override fun onProductSelectError(reason: Throwable) {
                //TODO("Increase distance from bottom")
                Toaster.make(
                        view = requireView(),
                        text = reason.localizedMessage,
                        duration = Toaster.LENGTH_SHORT,
                        actionText = getString(R.string.play_ok)
                )
            }
        })

        rvProduct.layoutManager = GridLayoutManager(rvProduct.context, SPAN_COUNT, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return if (selectableProductAdapter.getItem(position) == ProductLoadingUiModel) SPAN_COUNT
                    else 1
                }
            }
        }
        rvProduct.adapter = selectableProductAdapter
        rvProduct.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
        scrollListener = object : EndlessRecyclerViewScrollListener(rvProduct.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.loadEtalaseProducts(etalaseId, page)
            }
        }

        rvProduct.addOnScrollListener(scrollListener)
        rvProduct.addOnScrollListener(StopFlingScrollListener())

        if (shouldLoadFirst) {
            scrollListener.loadMoreNextPage()
            shouldLoadFirst = false
        }

        bottomSheetHeader.setListener(object : PlayBottomSheetHeader.Listener {
            override fun onBackButtonClicked(view: PlayBottomSheetHeader) {
                bottomSheetCoordinator.goBack()
            }
        })

        errorEmptyProduct.productEtalaseEmpty()
    }

    private fun uploadProduct() {
        viewModel.uploadProduct(bottomSheetCoordinator.channelId)
    }

    private fun onSelectedProductChanged() {
        selectableProductAdapter.notifyDataSetChanged()
    }

    private fun showSelectedProductPage() {
        if (selectedProductPage.isShown) return

        selectedProductPage.setSelectedProductList(viewModel.selectedProductList)
        selectedProductPage.show()
    }

    private fun finishSetupProduct(nextBtnView: View) {
        mListener?.onProductSetupFinished(listOf(nextBtnView), dataStoreViewModel.getDataStore())
    }

    private fun showProductEmptyError(shouldShow: Boolean) {
        if (shouldShow) {
            errorEmptyProduct.show()

            tvInfo.hide()
            rvProduct.hide()
        } else {
            errorEmptyProduct.hide()

            tvInfo.show()
            rvProduct.show()
        }
    }

    /**
     * Observe
     */
    private fun observeProductsInSelectedEtalase() {
        viewModel.observableSelectedEtalase.observe(viewLifecycleOwner, Observer {
            bottomSheetHeader.setHeader(getString(R.string.play_etalase_detail_header, it.currentValue.name, it.currentValue.totalProduct), isRoot = false)
            tvInfo.text = getString(R.string.play_product_select_max_info, viewModel.maxProduct)
            val flattenValues = it.currentValue.productMap.values.flatten()
            when (it.state) {
                is PageResultState.Success -> {
                    showProductEmptyError(flattenValues.isEmpty())
                    selectableProductAdapter.setItemsAndAnimateChanges(flattenValues)

                    scrollListener.setHasNextPage(it.currentValue.stillHasProduct)
                    scrollListener.updateState(true)
                }
                PageResultState.Loading -> {
                    showProductEmptyError(false)
                    selectableProductAdapter.setItemsAndAnimateChanges(flattenValues + ProductLoadingUiModel)
                }
                is PageResultState.Fail -> {
                    selectableProductAdapter.setItemsAndAnimateChanges(flattenValues)

                    startPostponedTransition()

                    scrollListener.setHasNextPage(it.currentValue.stillHasProduct)
                    scrollListener.updateState(false)

                    showProductEmptyError(flattenValues.isEmpty())
                }
            }
        })
    }

    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            bottomActionView.setupBottomActionWithProducts(it)
            selectedProductPage.onSelectedProductsUpdated(it)
        })
    }

    private fun observeUploadProduct() {
        viewModel.observableUploadProductEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> bottomActionView.setLoading(true)
                is NetworkResult.Fail -> {
                    bottomActionView.setLoading(false)
                    it.error.localizedMessage?.let {
                        errMessage -> requireView().showToaster(errMessage, type = Toaster.TYPE_ERROR)
                    }
                }
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) {
                        bottomActionView.setLoading(false)
                        finishSetupProduct(bottomActionView.getButtonView())
                    }
                }
            }
        })
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupEnterTransition()
        setupExitTransition()
        setupReturnTransition()
        setupReenterTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = TransitionSet()
                .addTransition(Slide(Gravity.END))
                .addTransition(Fade(Fade.IN))
                .setDuration(300)

        sharedElementEnterTransition = TransitionSet()
                .addTransition(ChangeTransform())
                .addTransition(ChangeBounds())
                .setDuration(300)
    }

    private fun setupReturnTransition() {
        returnTransition = TransitionSet()
                .addTransition(Slide(Gravity.END))
                .addTransition(Fade(Fade.OUT))
                .setDuration(250)

        sharedElementReturnTransition = TransitionSet()
                .addTransition(ChangeTransform())
                .addTransition(ChangeBounds())
                .setDuration(300)
    }

    private fun setupExitTransition() {
        exitTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.OUT))
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = TransitionSet()
                .addTransition(Slide(Gravity.START))
                .addTransition(Fade(Fade.IN))
                .setDuration(300)
    }

    private fun startPostponedTransition() {
        requireView().doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    companion object {

        const val EXTRA_ETALASE_ID = "etalase_id"

        private const val SPAN_COUNT = 2
    }
}