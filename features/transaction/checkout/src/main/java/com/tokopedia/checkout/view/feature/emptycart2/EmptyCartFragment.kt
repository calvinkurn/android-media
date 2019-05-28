package com.tokopedia.checkout.view.feature.emptycart2

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.checkout.view.feature.emptycart2.di.DaggerEmptyCartComponent
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView
import com.tokopedia.checkout.view.feature.emptycart2.adapter.EmptyCartAdapter
import com.tokopedia.checkout.view.feature.emptycart2.adapter.EmptyCartAdapterTypeFactory
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.EmptyCartPlaceholderUiModel
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.PromoUiModel
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecentViewUiModel
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.WishlistUiModel
import com.tokopedia.checkout.view.feature.emptycart2.viewholder.PromoViewHolder
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.PromoViewModel
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.RecentViewViewModel
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.RecommendationViewModel
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.WishlistViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import kotlinx.android.synthetic.main.fragment_empty_cart_2.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-05-17.
 */

class EmptyCartFragment : BaseListFragment<Visitable<*>, EmptyCartAdapterTypeFactory>(), ActionListener {

    private var toolbar: View? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var promoViewModel: PromoViewModel
    lateinit var wishlistViewModel: WishlistViewModel
    lateinit var recentViewViewModel: RecentViewViewModel
    lateinit var recommendationViewModel: RecommendationViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var adapter: EmptyCartAdapter

    companion object {
        val ARG_AUTO_APPLY_MESSAGE = "ARG_AUTO_APPLY_MESSAGE"
        val ARG_AUTO_APPLY_STATE = "ARG_AUTO_APPLY_STATE"
        val ARG_AUTO_APPLY_TITLE = "ARG_AUTO_APPLY_TITLE"
        val ARG_AUTO_APPLY_PROMO_CODE = "ARG_AUTO_APPLY_PROMO_CODE"

        val REQUEST_CODE_ROUTE_WISHLIST = 123
        val REQUEST_CODE_ROUTE_RECENT_VIEW = 321

        fun newInstance(autoApplyMessage: String?, args: String?, state: String?, titleDesc: String?, promoCode: String?): EmptyCartFragment {
            val emptyCartFragment = EmptyCartFragment()
            val bundle = Bundle()
            bundle.putString(EmptyCartFragment::class.java.simpleName, args)
            bundle.putString(ARG_AUTO_APPLY_MESSAGE, autoApplyMessage)
            bundle.putString(ARG_AUTO_APPLY_STATE, state)
            bundle.putString(ARG_AUTO_APPLY_TITLE, titleDesc)
            bundle.putString(ARG_AUTO_APPLY_PROMO_CODE, promoCode)
            emptyCartFragment.arguments = bundle

            return emptyCartFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            promoViewModel = viewModelProvider.get(PromoViewModel::class.java)
            wishlistViewModel = viewModelProvider.get(WishlistViewModel::class.java)
            recentViewViewModel = viewModelProvider.get(RecentViewViewModel::class.java)
            recommendationViewModel = viewModelProvider.get(RecommendationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_empty_cart_2, container, false)
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))

        setupToolbar(view)

        adapter.clearAllElements()
        notifyDataSetChanged()

        renderPromo()
        renderEmptyCartPlaceholder()
        renderRecentView()
        renderWishlist()
        renderRecommendation()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        promoViewModel.clearPromoResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessClearPromo()
                is Fail -> onErrorClearPromo(RuntimeException())
            }
        })
        wishlistViewModel.wishlistData.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetWishlist(it.data)
                is Fail -> {
                }
            }
        })
        recentViewViewModel.recentViewData.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetRecentView(it.data)
                is Fail -> {
                }
            }
        })
        recommendationViewModel.recommendationData.observe(this, Observer {
            onSuccessLoadRecommendation()
        })
    }

    override fun onDestroyView() {
        promoViewModel.clearPromoResult.removeObservers(this)
        promoViewModel.unsubscribeSubscription()
        wishlistViewModel.wishlistData.removeObservers(this)
        wishlistViewModel.unsubscribeSubscription()
        recentViewViewModel.recentViewData.removeObservers(this)
        recentViewViewModel.unsubscribeSubscription()
        recommendationViewModel.recommendationData.removeObservers(this)
        recommendationViewModel.unsubscribeSubscription()

        super.onDestroyView()
    }

    private fun notifyItemInserted(position: Int) {
        if (recycler_view.isComputingLayout) {
            recycler_view.post { adapter.notifyItemInserted(position) }
        } else {
            adapter.notifyItemInserted(position)
        }
    }

    private fun notifyItemRemoved(position: Int) {
        if (recycler_view.isComputingLayout) {
            recycler_view.post { adapter.notifyItemRemoved(position) }
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    private fun notifyItemChanged(position: Int) {
        if (recycler_view.isComputingLayout) {
            recycler_view.post { adapter.notifyItemChanged(position) }
        } else {
            adapter.notifyItemChanged(position)
        }
    }

    private fun notifyDataSetChanged() {
        if (recycler_view.isComputingLayout) {
            recycler_view.post { adapter.notifyDataSetChanged() }
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById<View>(R.id.recycler_view) as RecyclerView
    }

    override fun getAdapterTypeFactory(): EmptyCartAdapterTypeFactory {
        return EmptyCartAdapterTypeFactory(this, getItemWidth())
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No op
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, EmptyCartAdapterTypeFactory> {
        adapter = EmptyCartAdapter(adapterTypeFactory)
        return adapter
    }

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerEmptyCartComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun loadData(page: Int) {

    }

    private fun setupToolbar(view: View) {
        val appbar = view.findViewById<Toolbar>(R.id.toolbar)
        val statusBarBackground = view.findViewById<View>(R.id.status_bar_bg)
        statusBarBackground.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(activity)
        val args = arguments?.getString(EmptyCartFragment::class.java.simpleName)
        if (args != null && !args.isEmpty()) {
            toolbar = toolbarRemoveView()
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarBackground.visibility = View.INVISIBLE
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBarBackground.visibility = View.VISIBLE
            } else {
                statusBarBackground.visibility = View.GONE
            }
        } else {
            toolbar = toolbarRemoveWithBackView()
            statusBarBackground.visibility = View.GONE
        }
        appbar.addView(toolbar)
        (activity as AppCompatActivity).setSupportActionBar(appbar)
        setVisibilityRemoveButton(false)
    }

    private fun toolbarRemoveWithBackView(): ToolbarRemoveWithBackView {
        val toolbar = ToolbarRemoveWithBackView(activity!!)
        toolbar.navigateUp(activity)
        toolbar.setTitle(getString(R.string.cart))
        return toolbar
    }

    private fun toolbarRemoveView(): ToolbarRemoveView {
        val toolbar = ToolbarRemoveView(activity!!)
        toolbar.setTitle(getString(R.string.cart))
        return toolbar
    }

    private fun setVisibilityRemoveButton(state: Boolean) {
        if (toolbar != null) {
            if (toolbar is ToolbarRemoveView) {
                (toolbar as ToolbarRemoveView).setVisibilityRemove(state)
            } else if (toolbar is ToolbarRemoveWithBackView) {
                (toolbar as ToolbarRemoveWithBackView).setVisibilityRemove(state)
            }
        }
    }

    private fun showLoadingDialog() {
        progressDialog.show()
    }

    private fun hideLoadingDialog() {
        progressDialog.hide()
    }

    private fun showSnackBarError(message: String) {
        NetworkErrorHelper.showRedSnackbar(activity, message)
    }

    private fun renderPromo() {
        if (arguments?.getString(ARG_AUTO_APPLY_MESSAGE) != null) {
            val promoUiModel = PromoUiModel()
            promoUiModel.messageSuccess = arguments?.getString(ARG_AUTO_APPLY_MESSAGE) ?: ""
            promoUiModel.code = arguments?.getString(ARG_AUTO_APPLY_PROMO_CODE) ?: ""
            promoUiModel.state = arguments?.getString(ARG_AUTO_APPLY_STATE) ?: ""
            promoUiModel.titleDescription = arguments?.getString(ARG_AUTO_APPLY_TITLE) ?: ""

            adapter.addElement(0, promoUiModel)
            notifyItemChanged(0)
        }
    }

    override fun onClearPromo(promoCode: String) {
        showLoadingDialog()
        promoViewModel.clearCacheAutoApplyStack(promoCode)
    }

    private fun onSuccessClearPromo() {
        hideLoadingDialog()
        if (adapter.getItemViewType(0) == PromoViewHolder.LAYOUT) {
            adapter.removeElement(0)
            notifyItemRemoved(0)
        }
    }

    private fun onErrorClearPromo(e: Throwable) {
        hideLoadingDialog()
        showSnackBarError(ErrorHandler.getErrorMessage(activity, e))
    }

    private fun renderEmptyCartPlaceholder() {
        val uiModel = EmptyCartPlaceholderUiModel()
        adapter.addElement(uiModel)
        notifyItemChanged(adapter.getIndexOf(uiModel))
    }

    override fun onClickShopNow() {
        RouteManager.route(activity, ApplinkConst.HOME)
    }

    private fun renderWishlist() {
        wishlistViewModel.getWishlist()
    }

    private fun getItemWidth(): Int {
        val displayMetrics = activity?.resources?.displayMetrics
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics?.widthPixels
        return (deviceWidth?.div(2.0f))?.toDouble()?.toInt() ?: 0
    }

    override fun onItemWishListClicked(wishlist: Wishlist, position: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, wishlist.id);
        startActivityForResult(intent, REQUEST_CODE_ROUTE_WISHLIST)
    }

    override fun onShowAllWishlist() {
        val intent = RouteManager.getIntent(context, ApplinkConst.WISHLIST);
        startActivityForResult(intent, REQUEST_CODE_ROUTE_WISHLIST)
    }

    private fun onSuccessGetWishlist(wishlistData: MutableList<Wishlist>) {
        val wishlistUiModel = WishlistUiModel()
        wishlistUiModel.wishlistItems = wishlistData
        if (adapter.itemCount >= 3) {
            adapter.addElement(wishlistUiModel)
        } else {
            adapter.addElement(EmptyCartAdapter.DEFAULT_POSITION_WISHLIST, wishlistUiModel)
        }
        notifyItemInserted(adapter.getIndexOf(wishlistUiModel))
    }

    private fun renderRecentView() {
        recentViewViewModel.getRecentView()
    }

    private fun onSuccessGetRecentView(recentViewdata: MutableList<RecentView>) {
        val recentViewUiModel = RecentViewUiModel()
        recentViewUiModel.recentViewItems = recentViewdata
        if (adapter.itemCount >= 2) {
            adapter.addElement(recentViewUiModel)
        } else {
            adapter.addElement(EmptyCartAdapter.DEFAULT_POSITION_RECENT_VIEW, recentViewUiModel)
        }
        notifyItemInserted(adapter.getIndexOf(recentViewUiModel))
    }

    override fun onItemRecentViewClicked(recentView: RecentView, position: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, recentView.productId);
        startActivityForResult(intent, REQUEST_CODE_ROUTE_RECENT_VIEW)
    }

    override fun onShowAllRecentView() {
        val intent = RouteManager.getIntent(context, ApplinkConst.RECENT_VIEW);
        startActivityForResult(intent, REQUEST_CODE_ROUTE_RECENT_VIEW)
    }

    private fun renderRecommendation() {
        recommendationViewModel.getRecommendation()
    }

    private fun onSuccessLoadRecommendation() {
        val recommendationUiModel = recommendationViewModel.recommendationData.value
        if (adapter.getIndexOf(recommendationUiModel) == -1) {
            if (adapter.itemCount >= 4) {
                adapter.addElement(recommendationUiModel)
            } else {
                adapter.addElement(EmptyCartAdapter.DEFAULT_POSITION_RECOMMENDATION, recommendationUiModel)
            }
            notifyItemInserted(adapter.getIndexOf(recommendationUiModel))
        } else {
            notifyItemChanged(adapter.getIndexOf(recommendationUiModel))
        }
        endlessRecyclerViewScrollListener.updateStateAfterGetData()
    }

    private fun onErrorLoadRecommendation(e: Throwable) {

    }

    override fun onLoadMoreRecommendation() {
        recommendationViewModel.getRecommendation()
    }
}