package com.tokopedia.shop_widget.favourite.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.analytic.model.CustomDimensionShopWidget
import com.tokopedia.shop_widget.favourite.analytic.ShopFavouriteListTracker
import com.tokopedia.shop_widget.favourite.di.component.DaggerShopFavouriteComponent
import com.tokopedia.shop_widget.favourite.di.module.ShopFavouriteModule
import com.tokopedia.shop_widget.favourite.view.adapter.ShopFavouriteAdapterTypeFactory
import com.tokopedia.shop_widget.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop_widget.favourite.view.listener.ShopFavouriteListView
import com.tokopedia.shop_widget.favourite.view.model.ShopFollowerUiModel
import com.tokopedia.shop_widget.favourite.view.presenter.ShopFavouriteListPresenter
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created by nathan on 2/5/18.
 */
class ShopFavouriteListFragment : BaseListFragment<ShopFollowerUiModel?, ShopFavouriteAdapterTypeFactory?>(), ShopFavouriteListView, BaseEmptyViewHolder.Callback {

    @JvmField
    @Inject
    var shopFavouriteListPresenter: ShopFavouriteListPresenter? = null
    var shopPageTracking: ShopFavouriteListTracker? = null
    private var shopInfo: ShopInfo? = null
    private var shopId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopPageTracking = ShopFavouriteListTracker(TrackingQueue(requireContext()))
        shopId = requireArguments().getString(ShopFavouriteListActivity.EXTRA_SHOP_ID)
        shopFavouriteListPresenter?.attachView(this)
    }

    override fun loadData(page: Int) {
        if (shopInfo == null) {
            shopFavouriteListPresenter?.getShopInfo(shopId.orEmpty())
        } else {
            shopFavouriteListPresenter?.getShopFavouriteList(shopId.orEmpty(), page)
        }
    }

    override fun getAdapterTypeFactory(): ShopFavouriteAdapterTypeFactory {
        return ShopFavouriteAdapterTypeFactory(this)
    }

    override fun onItemClicked(shopFollowerUiModel: ShopFollowerUiModel?) {
//        val shopProfileIntent = RouteManager.getIntent(activity, ApplinkConst.PROFILE, shopFollowerUiModel?.id)
//        shopProfileIntent?.let { startActivity(it) }
    }

    override fun initInjector() {
        DaggerShopFavouriteComponent
                .builder()
                .shopFavouriteModule(ShopFavouriteModule())
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo?) {
        this.shopInfo = shopInfo
        shopFavouriteListPresenter?.getShopFavouriteList(shopId.orEmpty(), currentPage)
    }

    override fun onErrorToggleFavourite(throwable: Throwable?) {
        if (shopFavouriteListPresenter?.isLoggedIn == false) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
            return
        }
        NetworkErrorHelper.showRedCloseSnackbar(view, ErrorHandler.getErrorMessage(context, throwable))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_USER_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                shopFavouriteListPresenter?.toggleFavouriteShop(shopId.orEmpty())
            }
        }
    }

    override fun onSuccessToggleFavourite(successValue: Boolean) {
        loadInitialData()
        requireActivity().setResult(Activity.RESULT_OK)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_shop_page_tree_empty_state
        if (shopFavouriteListPresenter?.isMyShop(shopId.orEmpty()) == true) {
            emptyModel.title = getString(R.string.shop_product_my_empty_follower_title)
            emptyModel.content = ""
            emptyModel.buttonTitle = ""
        } else {
            emptyModel.title = getString(R.string.shop_product_empty_follower_title)
            emptyModel.content = getString(R.string.shop_product_empty_product_title_desc, shopInfo?.shopCore?.name)
            emptyModel.buttonTitle = getString(R.string.shop_page_label_follow)
            shopInfo?.let{
                shopPageTracking?.impressionFollowFromZeroFollower(CustomDimensionShopWidget.create(it))
            }
        }
        return emptyModel
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getDefaultInitialPage(): Int {
        return DEFAULT_INITIAL_PAGE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (shopFavouriteListPresenter != null) {
            shopFavouriteListPresenter?.detachView()
        }
    }

    override fun onEmptyContentItemTextClicked() {
        // no-op
    }

    override fun onEmptyButtonClicked() {
        shopInfo?.let {
            shopPageTracking?.followFromZeroFollower(CustomDimensionShopWidget.create(it))
            shopFavouriteListPresenter?.toggleFavouriteShop(shopId.orEmpty())
        }
    }

    companion object {
        private const val DEFAULT_INITIAL_PAGE = 1
        private const val REQUEST_CODE_USER_LOGIN = 100
        fun createInstance(shopId: String?): ShopFavouriteListFragment {
            val shopFavouriteListFragment = ShopFavouriteListFragment()
            val bundle = Bundle()
            bundle.putString(ShopFavouriteListActivity.EXTRA_SHOP_ID, shopId)
            shopFavouriteListFragment.arguments = bundle
            return shopFavouriteListFragment
        }
    }
}
