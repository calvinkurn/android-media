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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageShowcaseTracking
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageListener
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.showcase.di.component.DaggerShopPageShowcaseComponent
import com.tokopedia.shop.showcase.di.component.ShopPageShowcaseComponent
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcase
import com.tokopedia.shop.showcase.presentation.adapter.ShopPageFeaturedShowcaseAdapter
import com.tokopedia.shop.showcase.presentation.adapter.ShopPageShowcaseListAdapter
import com.tokopedia.shop.showcase.presentation.viewmodel.ShopPageShowcaseViewModel
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseFragment : BaseDaggerFragment(),
        HasComponent<ShopPageShowcaseComponent>,
        ShopShowcaseListImageListener {

    companion object {

        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_SHOP_REF = "SHOP_REF"
        private const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        private const val DEFAULT_SHOP_ID = "0"
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

    @Inject
    lateinit var shopPageShowcaseTracking: ShopPageShowcaseTracking

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
    private var localLoadFeaturedShowcase: LocalLoad? = null
    private var localLoadAllShowcase: LocalLoad? = null
    private var globalError: GlobalError? = null

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
        loadShowcaseInitialData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SHOWCASE_REQUEST_CODE -> {
                // get data from shop showcase list
                data?.let {
                    val showcaseId = it.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    val isReloadShowcaseData = it.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)

                    showcaseId?.let { id ->
                        goToShowcaseProductListResult(id, isReloadShowcaseData)
                    }
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

    override fun onShowcaseListItemSelected(showcaseId: String) {
        // open showcase product result list page
        goToShowcaseProductListResult(showcaseId, true)
    }

    private fun initView(view: View?) {
        view?.let {
            showcaseShimmerView = it.findViewById(R.id.showcase_loading_shimmer)
            featuredShowcaseRv = it.findViewById(R.id.rvFeaturedShowcase)
            allShowcaseRv = it.findViewById(R.id.rvAllShowcase)
            tvFeaturedShowcaseTitle = it.findViewById(R.id.tvFeaturedTitle)
            tvAllShowcaseTitle = it.findViewById(R.id.tvAllShowcaseTitle)
            icShowcaseSearch = it.findViewById(R.id.icSearchShowcase)
            localLoadAllShowcase = it.findViewById(R.id.localLoadAllShowcase)
            localLoadFeaturedShowcase = it.findViewById(R.id.localLoadFeaturedShowcase)
            globalError = it.findViewById(R.id.globalError)

            // init recyclerview for featured and all showcase
            initRecyclerView()
        }
    }

    private fun initListener() {

        // search showcase icon on click listener
        icShowcaseSearch?.setOnClickListener {
            goToShopShowcaseList()
        }

        // local load featured showcase listener
        localLoadFeaturedShowcase?.refreshBtn?.setOnClickListener {
            shouldLoadingLocalLoad(localLoadFeaturedShowcase, true)
            reloadFeaturedShowcaseSection()
        }

        // local load all showcase listener
        localLoadAllShowcase?.refreshBtn?.setOnClickListener {
            shouldLoadingLocalLoad(localLoadAllShowcase, true)
            reloadAllShowcaseSection()
        }

        // global error listener
        globalError?.setActionClickListener {
            shouldShowShimmerView(true)
            loadShowcaseInitialData()
        }
    }

    private fun initRecyclerView() {
        featuredShowcaseAdapter = ShopPageFeaturedShowcaseAdapter(this)
        allShowcaseListAdapter = ShopPageShowcaseListAdapter(this)

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

    private fun loadShowcaseInitialData() {
        // load both featured and all showcase data
        shouldShowShimmerView(true)
        shopPageShowcaseViewModel.getShowcasesInitialData(shopId)
    }

    private fun reloadFeaturedShowcaseSection() {
        // load only featured showcase data
        shopPageShowcaseViewModel.getFeaturedShowcaseList(shopId)
    }

    private fun reloadAllShowcaseSection() {
        // load only all showcase data
        shopPageShowcaseViewModel.getShowcaseList(shopId)
    }

    private fun observeLiveData() {

        // observe initial load showcase
        shopPageShowcaseViewModel.showcasesBuyerUiModel.observe(viewLifecycleOwner, Observer { result ->
            shouldShowShimmerView(false)

            when (result) {
                is Success -> {

                    val response = result.data
                    if (response.isFeaturedShowcaseError && response.isAllShowcaseError) {
                        // both showcases section error, show global error
                        showGlobalError()
                    }
                    else {
                        // check for featured showcase section
                        if (response.isFeaturedShowcaseError) {
                            // featured showcase section error, show local load
                            showFeaturedSectionLocalLoad()
                        } else {
                            response.getFeaturedShowcase?.let { featuredShowcaseResponse ->
                                // render featured showcase section
                                renderFeaturedShowcaseSection(featuredShowcaseResponse)
                            }
                        }

                        // check for all showcase section
                        if (response.isAllShowcaseError) {
                            // all showcase section error, show local load
                            showAllShowcaseSectionLocalLoad()
                        } else {
                            // render all showcase section
                            renderAllShowcaseSection(response.allShowcaseList)
                        }
                    }

                }
                is Fail -> {
                    // show global error
                    showGlobalError()
                }
            }
        })

        // observe only featured showcase data
        shopPageShowcaseViewModel.featuredShowcaseList.observe(viewLifecycleOwner, Observer {
            shouldLoadingLocalLoad(localLoadFeaturedShowcase, false)
            when (it) {
                is Success -> {
                    val response = it.data

                    // render featured showcase section
                    renderFeaturedShowcaseSection(response)
                }
                is Fail -> {
                    // featured showcase section error, show local load
                    showFeaturedSectionLocalLoad()
                }
            }
        })

        // observe only all showcase data
        shopPageShowcaseViewModel.showcaseList.observe(viewLifecycleOwner, Observer {
            shouldLoadingLocalLoad(localLoadAllShowcase, false)
            when (it) {
                is Success -> {
                    val response = it.data

                    // render all showcase section
                    renderAllShowcaseSection(response)
                }
                is Fail -> {
                    // all showcase section error, show local load
                    showAllShowcaseSectionLocalLoad()
                }
            }
        })

    }

    private fun renderFeaturedShowcaseSection(featuredShowcaseResponse: GetFeaturedShowcase) {
        // if featured showcase response code is OK (200), but got error message
        // show local load for featured showcase section
        if (featuredShowcaseResponse.error.errorMessage.isNotEmpty()) {
            showFeaturedSectionLocalLoad(title = featuredShowcaseResponse.error.errorMessage)
        } else {
            if (featuredShowcaseResponse.result.isEmpty()) {
                // featured showcase response code is OK (200), but no data
                // hide the section
                hideFeaturedShowcaseSection()
            } else {
                // show featured showcase section
                featuredShowcaseAdapter?.updateFeaturedShowcaseDataset(featuredShowcaseResponse.result)
                shouldShowFeaturedShowcaseLocalLoad(false)
            }
        }
    }

    private fun renderAllShowcaseSection(list: List<ShopEtalaseModel>) {
        if (list.isNotEmpty()) {
            // show all showcase section if data is not empty
            allShowcaseListAdapter?.updateShowcaseList(list)
            shouldShowAllShowcaseLocalLoad(false)
        }
    }

    private fun showFeaturedSectionLocalLoad(title: String = "") {
        setupLocalLoad(localLoad = localLoadFeaturedShowcase, title = title)
        shouldShowFeaturedShowcaseLocalLoad(true)
    }

    private fun showAllShowcaseSectionLocalLoad() {
        setupLocalLoad(localLoad = localLoadAllShowcase)
        shouldShowAllShowcaseLocalLoad(true)
    }

    private fun setupLocalLoad(localLoad: LocalLoad?, title: String = "") {
        localLoad?.localLoadTitle = if (title.isNotEmpty()) {
            title
        } else {
            ErrorHandler.getErrorMessage(context, null)
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

    private fun goToShowcaseProductListResult(showcaseId: String, isNeedToReloadData: Boolean) {
        context?.let { ctx ->
            startActivity(ShopProductListResultActivity.createIntent(
                    ctx,
                    shopId,
                    "",
                    showcaseId,
                    shopAttribution,
                    "",
                    shopRef
            ).apply {
                putExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData)
            })
        }
    }

    private fun hideFeaturedShowcaseSection() {
        tvFeaturedShowcaseTitle?.hide()
        featuredShowcaseRv?.hide()
        localLoadFeaturedShowcase?.hide()
    }

    private fun shouldLoadingLocalLoad(localLoad: LocalLoad?, isLoading: Boolean) {
        localLoad?.progressState = isLoading
    }

    private fun shouldShowShimmerView(isShow: Boolean) {
        if (isShow) {
            showcaseShimmerView?.show()
            tvFeaturedShowcaseTitle?.hide()
            featuredShowcaseRv?.hide()
            localLoadFeaturedShowcase?.hide()
            tvAllShowcaseTitle?.hide()
            allShowcaseRv?.hide()
            globalError?.hide()
        } else {
            showcaseShimmerView?.hide()
            globalError?.hide()
        }
    }

    private fun shouldShowAllShowcaseLocalLoad(isShow: Boolean) {
        tvAllShowcaseTitle?.show()
        icShowcaseSearch?.show()
        if (isShow) {
            allShowcaseRv?.hide()
            localLoadAllShowcase?.show()
        } else {
            allShowcaseRv?.show()
            localLoadAllShowcase?.hide()
        }
    }

    private fun shouldShowFeaturedShowcaseLocalLoad(isShow: Boolean) {
        tvFeaturedShowcaseTitle?.show()
        if (isShow) {
            featuredShowcaseRv?.hide()
            localLoadFeaturedShowcase?.show()
        } else {
            localLoadFeaturedShowcase?.hide()
            featuredShowcaseRv?.show()
        }
    }

    private fun showGlobalError() {
        globalError?.visible()
        showcaseShimmerView?.hide()
        featuredShowcaseRv?.hide()
        allShowcaseRv?.hide()
        tvFeaturedShowcaseTitle?.hide()
        tvAllShowcaseTitle?.hide()
        icShowcaseSearch?.hide()
        localLoadAllShowcase?.hide()
        localLoadFeaturedShowcase?.hide()
    }

}