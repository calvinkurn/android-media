package com.tokopedia.product.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.util.CenterLayoutManager
import com.tokopedia.product.detail.databinding.DynamicProductDetailFragmentBinding
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.dynamicadapter.ProductDetailAdapter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * Created by Yehezkiel on 05/01/21
 */
abstract class BaseProductDetailFragment<T : Visitable<*>, F : AdapterTypeFactory> : BaseDaggerFragment() {

    var productAdapter: ProductDetailAdapter? = null
    var productDaggerComponent: ProductDetailComponent? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var rvPdp: RecyclerView? = null
    private var swipeToRefresh: SwipeRefreshLayout? = null

    protected abstract fun createAdapterInstance(): ProductDetailAdapter

    protected abstract fun loadData(forceRefresh: Boolean = false)

    protected abstract fun observeData()

    protected var binding by autoClearedNullable<DynamicProductDetailFragmentBinding>()

    open fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
        setHasOptionsMenu(true)
        productAdapter = createAdapterInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DynamicProductDetailFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeLayout(view)
        setupRecyclerView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    fun submitInitialList(visitables: List<DynamicPdpDataModel>) {
        hideSwipeLoading()

        rvPdp?.post {
            productAdapter?.submitList(visitables)
        }
    }

    fun submitList(visitables: List<DynamicPdpDataModel>) {
        rvPdp?.post {
            productAdapter?.submitList(visitables)
        }
    }

    fun showLoading() {
        productAdapter?.showLoading()
    }

    fun renderPageError(errorModel: PageErrorDataModel) {
        context?.let { ctx ->
            productAdapter?.showError(errorModel)
            swipeToRefresh?.let {
                it.isEnabled = false
            }
            binding?.partialLayoutButtonAction?.baseBtnAction?.gone()
        }
    }

    fun getViewHolderByPosition(position: Int): RecyclerView.ViewHolder? {
        if (position == -1) {
            return null
        }
        return rvPdp?.findViewHolderForAdapterPosition(position)
    }

    fun <T : DynamicPdpDataModel> getComponentPosition(data: T?): Int {
        return if (data != null) {
            productAdapter?.currentList?.indexOf(data) ?: RecyclerView.NO_POSITION
        } else {
            RecyclerView.NO_POSITION
        }
    }

    fun <T : DynamicPdpDataModel> getComponentPositionBeforeUpdate(data: T?): Int {
        return if (data != null) {
            productAdapter?.currentList?.indexOfFirst {
                it.name() == data.name()
            } ?: RecyclerView.NO_POSITION
        } else {
            RecyclerView.NO_POSITION
        }
    }

    fun scrollToPosition(position: Int) {
        if (position >= 0) {
            getRecyclerView()?.post {
                try {
                    getRecyclerView()?.smoothScrollToPosition(position)
                } catch (_: Throwable) { }
            }
        }
    }

    protected fun getRecyclerView(): RecyclerView? {
        return rvPdp
    }

    private fun setupSwipeLayout(view: View) {
        swipeToRefresh = view.findViewById<View>(R.id.swipeRefreshPdp) as SwipeRefreshLayout
        swipeToRefresh?.let {
            it.setOnRefreshListener { onSwipeRefresh() }
        }
    }

    private fun hideSwipeLoading() {
        swipeToRefresh?.let {
            it.isEnabled = true
            it.isRefreshing = false
        }
    }

    private fun setupRecyclerView(view: View) {
        rvPdp = view.findViewById(R.id.rv_pdp)
        rvPdp?.isNestedScrollingEnabled = false
        rvPdp?.layoutManager = CenterLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        rvPdp?.itemAnimator = null
        showLoading()

        rvPdp?.adapter = productAdapter
    }
}