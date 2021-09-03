package com.tokopedia.deals.brand_detail.ui.fragment

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.R
import com.tokopedia.deals.brand_detail.data.Brand
import com.tokopedia.deals.brand_detail.di.DealsBrandDetailComponent
import com.tokopedia.deals.brand_detail.ui.activity.DealsBrandDetailActivity
import com.tokopedia.deals.brand_detail.ui.activity.DealsBrandDetailActivity.Companion.EXTRA_SEO_URL
import com.tokopedia.deals.brand_detail.ui.viewmodel.DealsBrandDetailViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.databinding.FragmentDealsBrandDetailBinding
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.Math.abs
import javax.inject.Inject

class DealsBrandDetailFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: DealsBrandDetailViewModel

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @Inject
    lateinit var userSession: UserSessionInterface

    private var currentLocation: Location? = null

    private var seoUrl = ""

    private var binding by autoClearedNullable<FragmentDealsBrandDetailBinding>()

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

    private fun loadData() {
        getBrandDetail()
    }

    private fun showLayout() {
        observeBrandDetail()
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
                    val toolbar = it.toolbarBrandDetail
                    if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                        it.collapsingToolbarBrandDetail.title = title
                        toolbar.menu.getItem(0).setIcon(com.tokopedia.deals.R.drawable.ic_deals_revamp_share_black)
                        context?.let {
                            setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                        }
                    } else if (verticalOffset == 0) {
                        it.collapsingToolbarBrandDetail.title = ""
                        toolbar.menu.getItem(0).setIcon(com.tokopedia.deals.R.drawable.ic_deals_revamp_share_white)
                        context?.let {
                            setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                        }
                    }
                }
            })
        }
    }

    private fun setHeaderSection(brandDetail: Brand){
        binding?.let {
            it.imgThumbnailBrandDetail.loadImage(brandDetail.featuredThumbnailImage)
            it.imgIconBrandDetail.loadImage(brandDetail.featuredImage)
        }
    }

    private fun observeBrandDetail() {
        observe(viewModel.brandDetail) {
            when (it) {
                is Success -> {
                    val brandDetail = it.data.data.brand
                    setCollapsingLayout(brandDetail.title)
                    setHeaderSection(brandDetail)
                }

                is Fail -> {

                }
            }
        }
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

    fun setDrawableColorFilter(drawable: Drawable?, color: Int) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId ?: "" == R.id.action_share_deals) {
            //shareLink()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun getInstance(seoUrl: String): DealsBrandDetailFragment = DealsBrandDetailFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_SEO_URL, seoUrl)
            }
        }
    }
}