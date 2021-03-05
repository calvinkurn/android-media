package com.tokopedia.shop.score.performance.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.adapter.*
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class ShopPerformancePageFragment: BaseListFragment<Visitable<*>, ShopPerformanceAdapterTypeFactory>(),
        ShopPerformanceListener, ItemShopPerformanceListener, ItemCurrentStatusPowerMerchantListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: ShopPerformanceViewModel

    private val shopPerformanceAdapterTypeFactory by lazy {
        ShopPerformanceAdapterTypeFactory(this, this)
    }

    private val shopPerformanceAdapter by lazy { ShopPerformanceAdapter(shopPerformanceAdapterTypeFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_performance, container, false)
    }

    override fun onDestroy() {
        viewModel.shopInfoLevel.removeObservers(this)
        viewModel.shopPerformanceDetail.removeObservers(this)
        super.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun loadInitialData() {
        super.loadInitialData()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ShopPerformanceAdapterTypeFactory> {
        return shopPerformanceAdapter
    }

    override fun getAdapterTypeFactory(): ShopPerformanceAdapterTypeFactory {
        return shopPerformanceAdapterTypeFactory
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvShopPerformance)
    }

    override fun onTooltipLevelClicked(level: Int) {
        TODO("Not yet implemented")
    }

    override fun onTooltipScoreClicked() {
        TODO("Not yet implemented")
    }

    override fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String) {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): ShopPerformancePageFragment {
            return ShopPerformancePageFragment()
        }
    }

    override fun onItemClickedCurrentStatus() {
        TODO("Not yet implemented")
    }
}