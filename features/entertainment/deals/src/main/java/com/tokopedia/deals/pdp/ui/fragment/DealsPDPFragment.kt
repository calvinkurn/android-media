package com.tokopedia.deals.pdp.ui.fragment

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.databinding.FragmentDealsDetailBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.deals.pdp.widget.WidgetDealsPDPCarousel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class DealsPDPFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var productId: String = ""
    private var displayName: String = ""
    private var binding by autoClearedNullable<FragmentDealsDetailBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DealsPDPViewModel::class.java)
    }

    private var progressBar: LoaderUnify? = null
    private var progressBarLayout: FrameLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var toolbar: Toolbar? = null
    private var menuPDP: Menu? = null
    private var tgDealsDetail: Typography? = null
    private var clHeader: ConstraintLayout? = null
    private var imageCarousel: WidgetDealsPDPCarousel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = arguments?.getString(EXTRA_PRODUCT_ID, "") ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeFlowData()
        callPDP()
    }

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    private fun setupUi() {
        view?.apply {
            progressBar = binding?.progressBar
            progressBarLayout = binding?.progressBarLayout
            appBarLayout = binding?.appBarLayout
            collapsingToolbarLayout = binding?.collapsingToolbar
            toolbar = binding?.toolbar
            tgDealsDetail = binding?.subView?.tgDealDetails
            clHeader = binding?.clHeader
            imageCarousel = binding?.carouselDealsPdp

            updateCollapsingToolbar()
        }
    }

    private fun observeFlowData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowPDP.collect {
                when (it) {
                    is Success -> {
                        hideLoading()
                        showPDPData(it.data.eventProductDetail.productDetailData)
                    }

                    is Fail -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun callPDP() {
        showLoading()
        viewModel.setPDP(productId)
    }

    private fun showLoading() {
        progressBar?.show()
        progressBarLayout?.show()
        imageCarousel?.shimmering()
    }

    private fun hideLoading() {
        progressBar?.hide()
        progressBarLayout?.hide()
    }

    private fun showPDPData(data: ProductDetailData) {
        displayName = data.displayName
        showShareButton()
        showPDPOptionsMenu()
        showHeader()
        showImageCarousel(data)
    }

    private fun showHeader() {
        clHeader?.show()
        collapsingToolbarLayout?.title = displayName
        tgDealsDetail?.text = displayName
        tgDealsDetail?.show()
    }

    private fun showImageCarousel(data: ProductDetailData) {
        imageCarousel?.setImages(viewModel.productImagesMapper(data))
        imageCarousel?.buildView()
    }

    private fun showPDPOptionsMenu() {
        appBarLayout?.let { mainAppBarLayout ->
            setHasOptionsMenu(true)
            setToolbar()
            mainAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                context?.let { context ->
                    val menuSize = toolbar?.menu?.size() ?: Int.ZERO
                    if(menuSize > Int.ZERO) {
                        var colorInt = Int.ZERO
                        val appVerticalOffset = Math.abs(verticalOffset)
                        val difference =
                            mainAppBarLayout.totalScrollRange - (toolbar?.height ?: Int.ZERO)
                        if (appVerticalOffset >= difference) {
                            if (displayName.isNotEmpty()) {
                                collapsingToolbarLayout?.title = displayName
                            }
                            colorInt = ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N400
                            )
                        } else {
                            collapsingToolbarLayout?.title = ""
                            colorInt = ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N0
                            )
                        }

                        toolbar?.let { toolbar ->
                            setDrawableColorFilter(
                                toolbar.navigationIcon,
                                colorInt
                            )
                            setDrawableColorFilter(
                                toolbar.menu.getItem(Int.ZERO)?.icon,
                                colorInt
                            )
                        }
                    }
                }
            })
        }
    }

    private fun setToolbar() {
        toolbar?.let { toolbar ->
            (activity as DealsPDPActivity).setSupportActionBar(toolbar)
            (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as DealsPDPActivity).supportActionBar?.title = ""
            context?.let {
               toolbar.navigationIcon = ContextCompat.getDrawable(it, com.tokopedia.abstraction.R.drawable.ic_action_back)
            }
        }
    }

    private fun updateCollapsingToolbar() {
        context?.let {
            collapsingToolbarLayout?.setExpandedTitleColor(it.resources.getColor(android.R.color.transparent))
            collapsingToolbarLayout?.title = ""
        }
    }

    private fun setDrawableColorFilter(drawable: Drawable?, color: Int) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    private fun showShareButton() {
        if (menuPDP != null) {
            val item = menuPDP?.findItem(com.tokopedia.deals.R.id.action_menu_share)
            item?.setVisible(true)
        }
    }

    companion object {

        fun createInstance(productId: String?): DealsPDPFragment {
            val fragment = DealsPDPFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}