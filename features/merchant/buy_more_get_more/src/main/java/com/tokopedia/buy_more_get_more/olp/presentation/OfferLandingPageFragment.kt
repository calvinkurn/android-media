package com.tokopedia.buy_more_get_more.olp.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.databinding.FragmentOfferLandingPageBinding
import com.tokopedia.buy_more_get_more.olp.utils.BundleConstant
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.olp.di.component.DaggerBuyMoreGetMoreComponent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactoryImpl
import javax.inject.Inject

class OfferLandingPageFragment :
    BaseListFragment<Visitable<*>, AdapterTypeFactory>(),
    HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        @JvmStatic
        fun newInstance(shopId: String) = OfferLandingPageFragment().apply {
            arguments = Bundle().apply {
                putString(BundleConstant.BUNDLE_SHOP_ID, shopId)
            }
        }
    }

    private var binding by autoClearedNullable<FragmentOfferLandingPageBinding>()
    private val olpAdapter: OlpAdapter?
        get() = adapter as? OlpAdapter

    private val olpAdapterTypeFactory by lazy {
        OlpAdapterTypeFactoryImpl()
    }

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

    override fun onItemClicked(t: Visitable<*>?) { }

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
        setupHeader()
        setupContent()
        setupObservables()
        viewModel.getOfferingIndo(listOf(0), shopId)
    }

    private fun setupHeader() {
        setupToolbar()
    }

    private fun setupToolbar() {
        setStatusBarColor()
        binding?.apply {
            val colorBackground = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
            header.headerSubTitle = "Offering name" //update this with real data
            header.addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_cart).apply { setOnClickListener { } }
            header.addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_menu_hamburger).apply { setOnClickListener { } }
            header.setNavigationOnClickListener { activity?.finish() }
            header.setBackgroundColor(colorBackground)
        }
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.isDarkMode() == true) {
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.let {
                it.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                WindowCompat.getInsetsController(it.window, it.window.decorView).apply {
                    isAppearanceLightStatusBars = false
                }
                it.window.statusBarColor = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            }
        }
    }

    private fun setupContent() {
        olpAdapter?.submitList(newList = listOf(OfferInfoForBuyerUiModel()))
    }

    private fun setupObservables() {
        viewModel.offeringInfo.observe(viewLifecycleOwner) { offerInfoForBuyer ->
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_olp
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return OlpAdapter(olpAdapterTypeFactory)
    }
}
