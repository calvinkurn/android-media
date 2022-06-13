package com.tokopedia.deals.brand_detail.ui.fragment

import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.R
import com.tokopedia.deals.brand_detail.data.Brand
import com.tokopedia.deals.brand_detail.data.Product
import com.tokopedia.deals.brand_detail.di.DealsBrandDetailComponent
import com.tokopedia.deals.brand_detail.ui.activity.DealsBrandDetailActivity
import com.tokopedia.deals.brand_detail.ui.activity.DealsBrandDetailActivity.Companion.EXTRA_SEO_URL
import com.tokopedia.deals.brand_detail.ui.adapter.DealsBrandDetailAdapter
import com.tokopedia.deals.brand_detail.ui.bottomsheet.DealsBrandDetailBottomSheet
import com.tokopedia.deals.brand_detail.ui.viewmodel.DealsBrandDetailViewModel
import com.tokopedia.deals.brand_detail.util.DealsBrandDetailShare
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.bottomsheet.DealsBottomSheetNoInternetConnection
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.databinding.FragmentDealsBrandDetailBinding
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.Math.abs
import java.lang.ref.WeakReference
import java.net.UnknownHostException
import javax.inject.Inject

class DealsBrandDetailFragment : BaseDaggerFragment(), DealsBrandDetailAdapter.DealsBrandDetailCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: DealsBrandDetailViewModel

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: DealsAnalytics

    private var currentLocation: Location? = null

    private var seoUrl = ""

    private val adapterBrandDetail = DealsBrandDetailAdapter(this)

    private var brandDetail = Brand()

    private var binding by autoClearedNullable<FragmentDealsBrandDetailBinding>()

    private lateinit var dealsShareBrandDetail: DealsBrandDetailShare

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsBrandDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        seoUrl = arguments?.getString(EXTRA_SEO_URL, "") ?: ""
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            viewModel = viewModelProvider.get(DealsBrandDetailViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDealsBrandDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLocationUpdated()
        loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLayout()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId ?: "" == R.id.action_share_deals) {
            share(brandDetail)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun clickProduct(position: Int, product: Product) {
        analytics.eventBrandDetailClickProduct(brandDetail.title, position, product)
        RouteManager.route(context, product.appUrl)
    }

    override fun impressionProduct(position: Int, product: Product) {
        analytics.eventBrandDetailImpressionProduct(brandDetail.title, position, product)
    }

    private fun loadData() {
        getBrandDetail()
    }

    private fun showLayout() {
        hideEmptyState()
        showShimmering()
        observeBrandDetail()
    }

    private fun showSuccessLayout(products: List<Product>) {
        hideShimmering()
        hideEmptyState()
        setCollapsingLayout(brandDetail.title)
        setHeaderSection(brandDetail)
        setRVBrandList(products)
    }

    private fun showEmptyLayout(){
        hideShimmering()
        showEmptyState()
    }

    private fun setCollapsingLayout(title: String) {
        binding?.let {
            setHasOptionsMenu(true)
            it.toolbarBrandDetail.apply {
                (activity as DealsBrandDetailActivity).setSupportActionBar(this)
                (activity as DealsBrandDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (activity as DealsBrandDetailActivity).supportActionBar?.title = ""
                setNavigationIcon(ContextCompat.getDrawable(context, com.tokopedia.abstraction.R.drawable.ic_action_back))
            }
            it.collapsingToolbarBrandDetail.title = ""
            it.appBarLayoutBrandDetail.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    context?.let { context ->
                        if(it.toolbarBrandDetail.menu.size() > 0) {
                            var colorInt = 0
                            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                                it.collapsingToolbarBrandDetail.title = title
                                colorInt = ContextCompat.getColor(
                                    context,
                                    com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                                )
                            } else if (verticalOffset == 0) {
                                it.collapsingToolbarBrandDetail.title = ""
                                colorInt = ContextCompat.getColor(
                                    context,
                                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                                )
                            }

                            it.toolbarBrandDetail?.let { toolbar ->
                                setDrawableColorFilter(toolbar.getNavigationIcon(), colorInt)
                                setDrawableColorFilter(toolbar.menu.getItem(0).icon, colorInt)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setHeaderSection(brandDetail: Brand) {
        binding?.let {
            it.imgThumbnailBrandDetail.loadImage(brandDetail.featuredThumbnailImage)
            it.imgIconBrandDetail.loadImage(brandDetail.featuredImage)
            it.tgTitleBrandDetail.text = brandDetail.title
            it.tgDescBrandDetail.apply {
                text = brandDetail.description
                if (lineCount < MAX_LINE_COUNT) {
                    it.tgDescBrandDetailMore.hide()
                }
            }
            it.tgDescBrandDetailMore.setOnClickListener {
                showBottomSheetBrandDescDetail(brandDetail.title, brandDetail.description)
            }
        }
    }

    private fun setRVBrandList(listProduct: List<Product>) {
        binding?.let {
            if (listProduct.size != 0) {
                it.tgDescBrandDetailCount.text = getString(R.string.deals_brand_detail_count,
                        listProduct.size.toString())
            }
            it.rvBrandDetail.apply {
                adapter = adapterBrandDetail
                layoutManager = LinearLayoutManager(context)
            }
        }
        adapterBrandDetail.listProduct = listProduct
        adapterBrandDetail.notifyDataSetChanged()
    }

    private fun observeBrandDetail() {
        observe(viewModel.brandDetail) {
            when (it) {
                is Success -> {
                    brandDetail = it.data.data.brand
                    val products = it.data.data.products
                    if (products.isNotEmpty()) {
                        showSuccessLayout(products)
                    } else {
                        showEmptyLayout()
                    }
                }

                is Fail -> {
                    val throwable = it.throwable
                    if (throwable is UnknownHostException) {
                        context?.let { context ->
                            fragmentManager?.let {
                                DealsBottomSheetNoInternetConnection().showErroNoConnection(context, it,
                                        object : DealsBottomSheetNoInternetConnection.
                                        DealsOnClickBottomSheetNoConnectionListener {
                                            override fun onDismissBottomsheet() {
                                                reLoadData()
                                            }
                                        })
                            }
                        }
                    } else {
                        showToaster(throwable)
                    }
                }
            }
        }
    }

    private fun reLoadData(){
        showShimmering()
        loadData()
    }

    private fun getBrandDetail() {
        viewModel.getBrandDetail(viewModel.createParams(currentLocation?.coordinates.orEmpty(), seoUrl))
    }

    private fun getLocation(): Location? = dealsLocationUtils.getLocation()

    private fun onLocationUpdated() {
        val location = getLocation()
        if (location != null) {
            currentLocation = location
        }
    }

    private fun showBottomSheetBrandDescDetail(title: String, desc: String) {
        fragmentManager?.let { fragmentManager ->
            context?.let {
                DealsBrandDetailBottomSheet().showBottomSheet(it, getString(R.string.deals_brand_detail_title_bottom_sheet, title), desc, fragmentManager)
            }
        }
    }

    private fun share(brandDetail: Brand) {
        activity?.let { activity ->
            val activity = WeakReference<Activity>(activity)
            if(!::dealsShareBrandDetail.isInitialized) dealsShareBrandDetail = DealsBrandDetailShare(activity)
            dealsShareBrandDetail.shareEvent(brandDetail, brandDetail.title, requireContext(), { showShareLoading() }, { hideShareLoading() })
        }
    }

    private fun hideShareLoading() {
        binding?.loaderBrandDetail?.hide()
    }

    private fun showShareLoading() {
        binding?.loaderBrandDetail?.show()
    }

    private fun hideShimmering() {
        binding?.shimmeringBrandDetailDeals?.shimmeringBrandDetail?.hide()
    }

    private fun showShimmering() {
        binding?.shimmeringBrandDetailDeals?.shimmeringBrandDetail?.show()
    }

    private fun showEmptyState(){
        binding?.emptyStateBrandDetailDeals?.containerEmptyStateDeals?.show()
        binding?.emptyStateBrandDetailDeals?.geDealsEmpty?.apply {
            show()
            gravity = Gravity.CENTER
            errorAction.text = getString(R.string.deals_empty_state_button)
            setActionClickListener {
                activity?.finish()
            }
        }
    }

    private fun hideEmptyState(){
        binding?.emptyStateBrandDetailDeals?.let {
            it.geDealsEmpty?.hide()
            it.containerEmptyStateDeals?.hide()
        }
    }

    private fun showToaster(throwable: Throwable) {
        context?.let {
            val errorMessage = ErrorHandler.getErrorMessage(it, throwable)
            binding?.root?.let {
                Toaster.build(it, errorMessage, Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                        getString(R.string.deals_error_reload), View.OnClickListener {
                    reLoadData()
                }).show()
            }
        }
    }

    private fun setDrawableColorFilter(drawable: Drawable?, color: Int) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    companion object {

        private const val MAX_LINE_COUNT = 3

        fun getInstance(seoUrl: String): DealsBrandDetailFragment = DealsBrandDetailFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_SEO_URL, seoUrl)
            }
        }
    }
}