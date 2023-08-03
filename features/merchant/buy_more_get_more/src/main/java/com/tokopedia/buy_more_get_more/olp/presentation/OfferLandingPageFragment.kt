package com.tokopedia.buy_more_get_more.olp.presentation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.FragmentOfferLandingPageBinding
import com.tokopedia.buy_more_get_more.olp.di.component.DaggerBuyMoreGetMoreComponent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactoryImpl
import com.tokopedia.buy_more_get_more.olp.utils.BundleConstant
import com.tokopedia.buy_more_get_more.olp.utils.DataEndlessScrollListener
import com.tokopedia.buy_more_get_more.sort.activity.ShopProductSortActivity
import com.tokopedia.buy_more_get_more.sort.listener.ProductSortListener
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

class OfferLandingPageFragment :
    BaseListFragment<Visitable<*>, AdapterTypeFactory>(),
    ProductSortListener,
    HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        @JvmStatic
        fun newInstance(shopId: String) = OfferLandingPageFragment().apply {
            arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_SHOP_ID, shopId)
            }
        }

        private const val REQUEST_CODE_SORT = 308
    }

    private var binding by autoClearedNullable<FragmentOfferLandingPageBinding>()
    private val olpAdapter: OlpAdapter?
        get() = adapter as? OlpAdapter

    private val localCacheModel by lazy {
        context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private val olpAdapterTypeFactory by lazy {
        OlpAdapterTypeFactoryImpl(this)
    }
    private var sortId = ""
    private var sortName = ""

    @Inject
    lateinit var viewModel: OfferLandingPageViewModel
    private val shopId by lazy { arguments?.getString(BundleConstant.BUNDLE_SHOP_ID).orEmpty() }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerBuyMoreGetMoreComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): AdapterTypeFactory {
        return olpAdapterTypeFactory
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfferLandingPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservables()
        getRecyclerView(view)?.let {
            it.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        }
        viewModel.getOfferingIndo(listOf(0), shopId, localCacheModel)
    }

    private fun setupObservables() {
        viewModel.offeringInfo.observe(viewLifecycleOwner) { offerInfoForBuyer ->
            hideLoading()
            setupContent(offerInfoForBuyer)
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
        }
    }

    private fun setupHeader(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        setStatusBarColor()
        setupToolbar(offerInfoForBuyer)
    }

    private fun setupContent(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        setupHeader(offerInfoForBuyer)
        olpAdapter?.submitList(
            newList = listOf(
                generateDummyOfferingData(), // pass offering data
                OfferProductSortingUiModel() // pass product count
            )
        )
        olpAdapter?.setProductListData(generateDummyProductData()) // pass product list data
    }

    private fun setupToolbar(offerInfoForBuyer: OfferInfoForBuyerUiModel) {
        binding?.apply {
            val colorBackground = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            header.apply {
                headerSubTitle = offerInfoForBuyer.offerings.firstOrNull()?.offerName
                    ?: "Offering name" // update this with real data
                addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_cart)
                    .apply { setOnClickListener { } }
                addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_menu_hamburger)
                    .apply { setOnClickListener { } }
                setNavigationOnClickListener { activity?.finish() }
                setBackgroundColor(colorBackground)
            }
        }
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.isDarkMode() == true) {
                activity?.window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        activity?.let {
            it.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            WindowCompat.getInsetsController(it.window, it.window.decorView).apply {
                isAppearanceLightStatusBars = false
            }
            it.window.statusBarColor =
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        }
    }

    private fun renderSortFilter(sortId: String, sortName: String) {
        olpAdapter?.changeSelectedSortFilter(sortId, sortName)
        // update product list data
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_SORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    sortName = data?.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    renderSortFilter(sortId, sortName)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSortChipClicked() {
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, sortId)
            startActivityForResult(intent, REQUEST_CODE_SORT)
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_olp
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return OlpAdapter(olpAdapterTypeFactory)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object :
            DataEndlessScrollListener(getRecyclerView(view)?.layoutManager, olpAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                Log.d("Masuk", "load more") // load more data
            }
        }
    }

    private fun generateDummyOfferingData(): OfferInfoForBuyerUiModel {
        return OfferInfoForBuyerUiModel(
            offerings = listOf(
                OfferInfoForBuyerUiModel.Offering(
                    tierList = listOf(
                        OfferInfoForBuyerUiModel.Offering.Tier(
                            rules = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Rule(value = 3)),
                            benefits = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Benefit(value = 20))
                        ),
                        OfferInfoForBuyerUiModel.Offering.Tier(
                            rules = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Rule(value = 5)),
                            benefits = listOf(OfferInfoForBuyerUiModel.Offering.Tier.Benefit(value = 15))
                        )
                    )
                )
            )
        )
    }

    private fun generateDummyProductData(): List<OfferProductListUiModel.Product> {
        val products: MutableList<OfferProductListUiModel.Product> = mutableListOf()
        for (product in 1..50) {
            products.add(
                OfferProductListUiModel.Product(
                    name = "Product $product",
                    imageUrl = "https://images.tokopedia.net/img/cache/200-square/attachment/2018/8/9/3127195/3127195_e5b3e074-c897-4cf0-9ced-5572d0538e7c.jpg",
                    price = "Rp.20.000",
                    rating = "4.5",
                    soldCount = 80,
                    campaign = OfferProductListUiModel.Product.Campaign(
                        discountedPrice = "Rp.10.000",
                        originalPrice = "Rp.15.000",
                        discountedPercentage = "50%"
                    )
                )
            )
        }
        return products
    }
}
