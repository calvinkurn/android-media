package com.tokopedia.shop.showcase.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.showcase.di.component.DaggerShopPageShowcaseComponent
import com.tokopedia.shop.showcase.di.component.ShopPageShowcaseComponent
import com.tokopedia.shop.showcase.presentation.adapter.ShopPageFeaturedShowcaseAdapter
import com.tokopedia.shop.showcase.presentation.adapter.ShopPageShowcaseListAdapter
import com.tokopedia.shop.showcase.presentation.viewmodel.ShopPageShowcaseViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseFragment : BaseDaggerFragment(), HasComponent<ShopPageShowcaseComponent> {

    companion object {

        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_SHOP_REF = "SHOP_REF"
        private const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        private const val DEFAULT_SHOP_ID = "0"
        private const val VIEW_LOADING = 1
        private const val VIEW_CONTENT = 2
        private const val VIEW_ERROR = 3
        private const val SHOWCASE_REQUEST_CODE = 201

        @JvmStatic
        fun createInstance(
                shopId: String,
                shopRef: String,
                shopAttribution: String?
        ): ShopPageShowcaseFragment = ShopPageShowcaseFragment().apply {
            // set arguments bundle
            arguments = Bundle().apply {
                putString(KEY_SHOP_ID, shopId)
                putString(KEY_SHOP_REF, shopRef)
                putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
            }
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val shopPageShowcaseViewModel: ShopPageShowcaseViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPageShowcaseViewModel::class.java)
    }

    private var showcaseShimmerView: LinearLayout? = null
    private var featuredShowcaseRv: RecyclerView? = null
    private var allShowcaseRv: RecyclerView? = null
    private var featuredShowcaseAdapter: ShopPageFeaturedShowcaseAdapter? = null
    private var allShowcaseListAdapter: ShopPageShowcaseListAdapter? = null
    private var tvFeaturedShowcaseTitle: Typography? = null
    private var tvAllShowcaseTitle: Typography? = null
    private var icShowcaseSearch: IconUnify? = null
    private var shopId: String = DEFAULT_SHOP_ID
    private var shopRef: String = ""
    private var shopAttribution: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_page_showcase, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        observeLiveData()
        loadShowcaseData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            SHOWCASE_REQUEST_CODE -> {
                // get data from shop showcase list
                data?.let {

                    val showcaseId = it.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    val isReloadShowcaseData = it.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)

                    startActivity(ShopProductListResultActivity.createIntent(
                            activity,
                            shopId,
                            "",
                            showcaseId,
                            shopAttribution,
                            "",
                            shopRef
                    ).apply {
                        putExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isReloadShowcaseData)
                    })

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun getComponent(): ShopPageShowcaseComponent? {
        return activity?.run {
            DaggerShopPageShowcaseComponent
                    .builder()
                    .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun loadShowcaseData() {
        setViewState(VIEW_LOADING)
        shopPageShowcaseViewModel.getShowcaseList(shopId)
    }

    private fun observeLiveData() {

        // showcase list live data
        shopPageShowcaseViewModel.etalaseList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setViewState(VIEW_CONTENT)
                    featuredShowcaseAdapter?.updateFeaturedShowcaseDataset(it.data)
                    allShowcaseListAdapter?.updateShowcaseList(it.data)
                }
                is Fail -> { }
            }
        })

    }

    private fun setViewState(viewState: Int) {
        when(viewState) {
            VIEW_LOADING -> {
                showcaseShimmerView?.visible()
                featuredShowcaseRv?.hide()
                tvFeaturedShowcaseTitle?.hide()
                tvAllShowcaseTitle?.hide()
                icShowcaseSearch?.hide()
            }
            VIEW_CONTENT -> {
                showcaseShimmerView?.hide()
                featuredShowcaseRv?.visible()
                allShowcaseRv?.visible()
                tvFeaturedShowcaseTitle?.visible()
                tvAllShowcaseTitle?.visible()
                icShowcaseSearch?.visible()
            }
        }
    }

    private fun initListener() {

        // search showcase icon on click listener
        icShowcaseSearch?.setOnClickListener {
            goToShopShowcaseList()
        }
    }

    private fun initView(view: View?) {
        view?.let {
            showcaseShimmerView = it.findViewById(R.id.showcase_loading_shimmer)
            featuredShowcaseRv = it.findViewById(R.id.rvFeaturedShowcase)
            allShowcaseRv = it.findViewById(R.id.rvAllShowcase)
            tvFeaturedShowcaseTitle = it.findViewById(R.id.tvFeaturedTitle)
            tvAllShowcaseTitle = it.findViewById(R.id.tvAllShowcaseTitle)
            icShowcaseSearch = it.findViewById(R.id.icSearchShowcase)

            // init recyclerview for featured and all showcase
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        featuredShowcaseAdapter = ShopPageFeaturedShowcaseAdapter()
        allShowcaseListAdapter = ShopPageShowcaseListAdapter()

        val featuredShowcaseLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )

        val allShowcaseLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
        )

        // init featured showcase recyclerview
        featuredShowcaseRv?.apply {
            setHasFixedSize(true)
            layoutManager = featuredShowcaseLayoutManager
            adapter = featuredShowcaseAdapter
        }

        // ini all showcase recyclerview
        allShowcaseRv?.apply {
            setHasFixedSize(true)
            layoutManager = allShowcaseLayoutManager
            adapter = allShowcaseListAdapter
        }
    }

    private fun getIntentData() {
        arguments?.let { args ->
            shopId = args.getString(KEY_SHOP_ID, DEFAULT_SHOP_ID)
            shopRef = args.getString(KEY_SHOP_REF, "")
            shopAttribution = args.getString(KEY_SHOP_ATTRIBUTION, "")
        }
    }

    private fun goToShopShowcaseList() {
        context?.let { ctx ->
            RouteManager.getIntent(ctx, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST).apply {
                val showcaseListBundle = Bundle().apply {
                    putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
                    putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
                    putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, false)
                    putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
                }
                putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, showcaseListBundle)
                startActivityForResult(this, SHOWCASE_REQUEST_CODE)
            }
        }
    }

}