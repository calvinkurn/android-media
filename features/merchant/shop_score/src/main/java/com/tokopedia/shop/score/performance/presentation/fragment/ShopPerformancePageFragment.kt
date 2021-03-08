package com.tokopedia.shop.score.performance.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.adapter.*
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetPerformanceDetail
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetShopTooltipLevel
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetShopTooltipScore
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class ShopPerformancePageFragment: BaseListFragment<Visitable<*>, ShopPerformanceAdapterTypeFactory>(),
        ShopPerformanceListener, ItemShopPerformanceListener, ItemCurrentStatusPowerMerchantListener,
        ItemPotentialPowerMerchantListener, ItemRecommendationFeatureListener {

    @Inject
    lateinit var viewModel: ShopPerformanceViewModel

    private val shopPerformanceAdapterTypeFactory by lazy {
        ShopPerformanceAdapterTypeFactory(this, this,
                this, this)
    }

    private val shopPerformanceAdapter by lazy { ShopPerformanceAdapter(shopPerformanceAdapterTypeFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_performance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeShopPerformancePage()
    }

    override fun onDestroy() {
        viewModel.shopInfoLevel.removeObservers(this)
        viewModel.shopPerformanceDetail.removeObservers(this)
        viewModel.shopPerformancePage.removeObservers(this)
        super.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun loadInitialData() {
        isLoadingInitialData = true
        shopPerformanceAdapter.clearAllElements()
        shopPerformanceAdapter.showLoading()
        viewModel.getShopPerformancePageDummy()
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

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout {
        return view.findViewById(R.id.shopPerformanceSwipeRefresh)
    }

    override fun onTooltipLevelClicked(level: Int) {
        val bottomSheetShopTooltipLevel = BottomSheetShopTooltipLevel.createInstance(level)
        bottomSheetShopTooltipLevel.show(childFragmentManager)
    }

    override fun onTooltipScoreClicked() {
        val bottomSheetShopTooltipScore = BottomSheetShopTooltipScore()
        bottomSheetShopTooltipScore.show(childFragmentManager)
    }

    override fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String) {
        val bottomSheetDetail = BottomSheetPerformanceDetail.createInstance(titlePerformanceDetail)
        bottomSheetDetail.show(childFragmentManager)
    }

    override fun onItemClickedCurrentStatus() {

    }

    override fun onItemClickedBenefitPotentialPM() {

    }

    override fun onItemClickedRecommendationFeature(appLink: String) {
        TODO("Not yet implemented")
    }

    private fun observeShopPerformancePage() {
        observe(viewModel.shopPerformancePage) {
            shopPerformanceAdapter.hideLoading()
            when(it) {
                is Success -> {
                    shopPerformanceAdapter.setShopPerformanceData(it.data)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): ShopPerformancePageFragment {
            return ShopPerformancePageFragment()
        }
    }
}