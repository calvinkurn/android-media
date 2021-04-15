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
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.util.extension.productEtalaseEmpty
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.contract.ProductSetupListener
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.BottomActionViewComponent
import com.tokopedia.play.broadcaster.view.partial.SelectedProductPageViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 27/05/20
 */
class PlayEtalaseDetailFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatchers,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseSetupFragment() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private lateinit var viewModel: PlayEtalasePickerViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private val etalaseId: String
        get() = arguments?.getString(EXTRA_ETALASE_ID) ?: throw IllegalStateException("etalaseId must be set")

    private lateinit var tvInfo: TextView
    private lateinit var rvProduct: RecyclerView
    private lateinit var errorEmptyProduct: GlobalError
    private lateinit var bottomSheetHeader: PlayBottomSheetHeader

    private val selectedProductPageView by viewComponent {
        SelectedProductPageViewComponent(view as ViewGroup, object : SelectedProductPageViewComponent.Listener {
            override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
                viewModel.selectProduct(productId, isSelected)
                onSelectedProductChanged()
            }
        })
    }

    private val bottomActionView by viewComponent {
        BottomActionViewComponent(it, object : BottomActionViewComponent.Listener {
            override fun onInventoryIconClicked() {
                showSelectedProductPage()
                analytic.clickSelectedProductIcon()
            }

            override fun onNextButtonClicked() {
                uploadProduct()
                analytic.clickContinueOnProductBottomSheet()
            }
        })
    }

    private var shouldLoadFirst = true

    private var mListener: ProductSetupListener? = null

    private var toasterBottomMargin = 0

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

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    override fun onInterceptBackPressed(): Boolean {
        return if (selectedProductPageView.isShown()) {
            selectedProductPageView.hide()
            true
        } else false
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
    }

    private fun setupView(view: View) {
        tvInfo.text = viewModel.maxProductDesc

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
                analytic.clickProductCard(productId.toString(), isSelected)
            }

            override fun onProductSelectError(reason: Throwable) {
                showToaster(
                        message = reason.localizedMessage,
                        actionLabel = getString(R.string.play_ok)
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
        viewModel.uploadProduct()
    }

    private fun onSelectedProductChanged() {
        selectableProductAdapter.notifyDataSetChanged()
    }

    private fun showSelectedProductPage() {
        if (selectedProductPageView.isShown()) {
            selectedProductPageView.hide()
        } else {
            selectedProductPageView.setSelectedProductList(viewModel.selectedProductList)
            selectedProductPageView.show()
        }
    }

    private fun showToaster(message: String, type: Int = Toaster.TYPE_NORMAL, duration: Int = Toaster.LENGTH_SHORT, actionLabel: String = "") {
        if (toasterBottomMargin == 0) {
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            toasterBottomMargin = bottomActionView.rootView.height + offset8
        }

        view?.showToaster(
                message = message,
                type = type,
                duration = duration,
                actionLabel = actionLabel,
                bottomMargin = toasterBottomMargin
        )
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

    private fun onUploadSuccess() {
        scope.launch {
            val error = mListener?.onProductSetupFinished(listOf(bottomActionView.getButtonView()), dataStoreViewModel.getDataStore())
            if (error != null) {
                yield()
                onUploadFailed(error)
            }
        }
    }

    private fun onUploadFailed(e: Throwable?) {
        bottomActionView.setLoading(false)
        e?.localizedMessage?.let {
            errMessage -> showToaster(errMessage, type = Toaster.TYPE_ERROR)
        }
    }

    /**
     * Observe
     */
    private fun observeProductsInSelectedEtalase() {
        viewModel.observableSelectedEtalase.observe(viewLifecycleOwner, Observer {
            bottomSheetHeader.setHeader(getString(R.string.play_etalase_detail_header, it.currentValue.name, it.currentValue.totalProduct), isRoot = false)
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

                    analytic.viewErrorProduct(it.state.error.localizedMessage)
                    showProductEmptyError(flattenValues.isEmpty())
                }
            }
        })
    }

    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            bottomActionView.setupBottomActionWithProducts(it)
            selectedProductPageView.onSelectedProductsUpdated(it)
        })
    }

    private fun observeUploadProduct() {
        viewModel.observableUploadProductEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> bottomActionView.setLoading(true)
                is NetworkResult.Fail -> onUploadFailed(it.error)
                is NetworkResult.Success -> {
                    val data = it.data.getContentIfNotHandled()
                    if (data != null) onUploadSuccess()
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