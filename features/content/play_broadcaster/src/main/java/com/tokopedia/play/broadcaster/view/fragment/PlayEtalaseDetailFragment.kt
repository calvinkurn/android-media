package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.ResultState
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
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

    private val etalaseId: Long
        get() = arguments?.getLong(EXTRA_ETALASE_ID) ?: throw IllegalStateException("etalaseId must be set")

    private lateinit var tvInfo: TextView
    private lateinit var rvProduct: RecyclerView

    private val selectableProductAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
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

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun isRootFragment(): Boolean {
        return false
    }

    override fun refresh() {
        selectableProductAdapter.notifyDataSetChanged()
    }

    override fun getScreenName(): String = "Etalase Detail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetCoordinator.showBottomAction(true)
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
    }

    private fun initView(view: View) {
        with(view) {
            tvInfo = findViewById(R.id.tv_info)
            rvProduct = findViewById(R.id.rv_product)
        }
    }

    private fun setupView(view: View) {
        rvProduct.layoutManager = GridLayoutManager(rvProduct.context, 2, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return if (selectableProductAdapter.getItem(position) == ProductLoadingUiModel) 2
                    else 1
                }
            }
        }
        rvProduct.adapter = selectableProductAdapter
        rvProduct.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
        scrollListener = object : EndlessRecyclerViewScrollListener(rvProduct.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.loadCurrentEtalaseProducts(etalaseId, page)
            }
        }
        scrollListener.loadMoreNextPage()
        rvProduct.addOnScrollListener(scrollListener)
    }

    private fun observeProductsInSelectedEtalase() {
        viewModel.observableSelectedEtalase.observe(viewLifecycleOwner, Observer {

            when (it.state) {
                is ResultState.Success -> {
                    selectableProductAdapter.setItemsAndAnimateChanges(it.currentValue.productMap.values.flatten())
                    bottomSheetCoordinator.setupTitle(it.currentValue.name)
                    tvInfo.text = getString(R.string.play_product_select_max_info, viewModel.maxProduct)
                    scrollListener.setHasNextPage(it.currentValue.stillHasProduct)
                    scrollListener.updateStateAfterGetData()
                }
                ResultState.Loading -> {
                    selectableProductAdapter.setItemsAndAnimateChanges(it.currentValue.productMap.values.flatten() + ProductLoadingUiModel)
                }
                is ResultState.Fail -> {
                    selectableProductAdapter.setItemsAndAnimateChanges(it.currentValue.productMap.values.flatten())
                    scrollListener.setHasNextPage(it.currentValue.stillHasProduct)
                    scrollListener.updateStateAfterGetData()
                }
            }
        })
    }

    companion object {

        const val EXTRA_ETALASE_ID = "etalase_id"
    }
}