package com.tokopedia.shop.pageheader.presentation

import android.os.Bundle
import android.view.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.error
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.di.DaggerShopPageComponent
import com.tokopedia.shop.pageheader.di.ShopPageComponent
import com.tokopedia.shop.pageheader.di.ShopPageModule
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPagePagerAdapter
import com.tokopedia.shop.pageheader.presentation.viewmodel.ShopPageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.shop_page_main.*
import javax.inject.Inject

class ShopPageFragment :
        BaseDaggerFragment(),
        HasComponent<ShopPageComponent>
{

    @Inject
    lateinit var viewModel: ShopPageViewModel

    private val tabAdapter by lazy {
        ShopPagePagerAdapter(context, childFragmentManager, viewModel.shopInfoResult)
    }

    companion object {
        @JvmStatic
        fun initInstance(bundle: Bundle?) = ShopPageFragment().apply { arguments = bundle }
    }

    override fun getComponent() = activity?.run {
        DaggerShopPageComponent
                .builder()
                .shopPageModule(ShopPageModule())
                .shopComponent(ShopComponentInstance.getComponent(application))
                .build()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.shop_page_main, container, false)

    override fun onDestroy() {
        viewModel.shopInfoResult.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isVisible) {
            startObserveLiveDatas(this)
            viewModel.fetchData()
            initViews()
        }
    }

    private fun initViews() {
        shop_page_main_status_bar.visibility = View.GONE
        shop_page_main_toolbar.inflateMenu(R.menu.menu_shop_page_main)
        shop_page_main_viewpager.adapter = tabAdapter
        shop_page_main_tab_layout.apply {
            setupWithViewPager(shop_page_main_viewpager)
            tabRippleColor = null

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                    tabAdapter.handleSelectedTab(tab, true)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tabAdapter.handleSelectedTab(tab, false)
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    tabAdapter.handleSelectedTab(tab, true)
                }
            })

            for (i in 0 until tabCount) {
                getTabAt(i)?.customView = tabAdapter.getTabView(i)
            }
        }
    }

    private fun bindDataToHeaderView(shopInfo: ShopInfo) {
        shop_page_main_profile_name.text = shopInfo.shopCore.name

        ImageHandler.loadRoundedImage(
                shop_page_main_profile_image,
                shopInfo.shopAssets.avatar,
                32.dpToPx(resources.displayMetrics).toFloat(),
                -1,
                -1
        )

        ImageHandler.loadImageFitCenter(
                context,
                shop_page_main_profile_background,
                shopInfo.shopAssets.cover
        )
    }

    private fun networkFailureHandler(throwable: Throwable) {
        view?.run { Toaster.error(ErrorHandler.getErrorMessage(context, throwable)) }
    }

    private fun startObserveLiveDatas(owner: LifecycleOwner) {
        viewModel.shopInfoResult.observe(owner, Observer { result ->
            when (result) {
                is Success -> { bindDataToHeaderView(result.data) }
                is Fail -> { networkFailureHandler(result.throwable) }
            }
        })
    }
}
