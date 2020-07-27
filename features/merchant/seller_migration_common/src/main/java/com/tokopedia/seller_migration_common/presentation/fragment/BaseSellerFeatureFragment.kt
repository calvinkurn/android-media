package com.tokopedia.seller_migration_common.presentation.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.seller_migration_common.presentation.StaticDataProvider
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureAdapterTypeFactory

abstract class BaseSellerFeatureFragment(private val listener: RecyclerViewListener, private val staticDataProvider: StaticDataProvider) : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>() {

    override fun onItemClicked(t: Visitable<*>?) {/* noop */}
    override fun initInjector() {/* noop */}
    override fun loadData(page: Int) {/* noop */}

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory = SellerFeatureAdapterTypeFactory()
    override fun isLoadMoreEnabledByDefault(): Boolean = false
    override fun getScreenName(): String = ""
    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(context, 2)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getRecyclerView(view).apply {
            layoutManager = recyclerViewLayoutManager
            viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // all recyclerview item inflated
                    listener.onRecyclerViewBindFinished()
                    getRecyclerView(view).viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun loadInitialData() {
        renderList(staticDataProvider.getData())
    }

    interface RecyclerViewListener {
        fun onRecyclerViewBindFinished()
    }
}