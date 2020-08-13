package com.tokopedia.recentview.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.recentview.R
import com.tokopedia.recentview.analytics.RecentViewTracking
import com.tokopedia.recentview.di.DaggerRecentViewComponent
import com.tokopedia.recentview.view.adapter.RecentViewDetailAdapter
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactoryImpl
import com.tokopedia.recentview.view.listener.RecentView
import com.tokopedia.recentview.view.presenter.RecentViewPresenter
import com.tokopedia.recentview.view.presenter.RecentViewViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import java.util.*
import javax.inject.Inject

/**
 * @author by Lukas on 15/7/2020.
 */

class RecentViewFragment : BaseDaggerFragment(), RecentView.View, WishListActionListener {
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: RecentViewDetailAdapter
    private var layoutManager: LinearLayoutManager? = null

    @Inject
    lateinit var presenter: RecentViewPresenter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: RecentViewViewModel

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerRecentViewComponent.builder()
                .baseAppComponent(
                        (activity?.application as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(RecentViewViewModel::class.java)
        }
        initVar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        viewModel.run {
            recentViewResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success ->  {
                        if(it.data.isNotEmpty()) {

                            val visitableList: ArrayList<Visitable<*>> = ArrayList(it.data)
                            onSuccessGetRecentView(visitableList)
                            sendRecentViewImpressionTracking(it.data)
                        } else {
                            onEmptyGetRecentView()
                        }
                    }
                    is Fail -> {

                    }
                }
            })
        }
    }

    private fun initVar() {
        layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        val typeFactory: RecentViewTypeFactory = RecentViewTypeFactoryImpl(this)
        adapter = RecentViewDetailAdapter(typeFactory)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val parentView: View = inflater.inflate(R.layout.fragment_recent_view_detail, container, false)
        recyclerView = parentView.findViewById<View>(R.id.list) as RecyclerView
        prepareView()
        presenter?.attachView(this, this)
        return parentView
    }

    private fun prepareView() {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(recyclerView?.context, DividerItemDecoration.VERTICAL)
        context?.let { context ->
            val drawable = ContextCompat.getDrawable(context, com.tokopedia.design.R.drawable.bg_line_separator)
            drawable?.let { dividerItemDecoration.setDrawable(it) }
        }
        recyclerView?.addItemDecoration(dividerItemDecoration)
        recyclerView?.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getRecentView()
    }

    override fun onWishlistClicked(adapterPosition: Int, productId: Int, isWishlist: Boolean) {
        if (!isWishlist) {
            presenter?.addToWishlist(adapterPosition, productId.toString())
        } else {
            presenter?.removeFromWishlist(adapterPosition, productId.toString())
        }
    }

    override fun onGoToProductDetail(productId: String,
                                     productName: String,
                                     productPrice: String,
                                     productImage: String) {
        activity?.startActivity(getProductIntent(productId))
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun showLoadingProgress() {}
    override fun onErrorGetRecentView(errorMessage: String) {
        adapter.dismissLoading()
        if (activity != null && view != null && presenter != null) NetworkErrorHelper.showEmptyState(activity, view,
                errorMessage) { presenter?.getRecentViewProduct() }
    }

    override fun onSuccessGetRecentView(recentViewProductViewModels: ArrayList<Visitable<*>>) {
        adapter.dismissLoading()
        adapter.addList(recentViewProductViewModels)
        adapter.notifyDataSetChanged()
    }

    override fun onEmptyGetRecentView() {
        adapter.dismissLoading()
        adapter.showEmpty()
    }

    override fun sendRecentViewClickTracking(element: RecentViewDetailProductViewModel) {
        RecentViewTracking.trackEventClickOnProductRecentView(activity,
                element.recentViewAsObjectDataLayerForClick
        )
    }

    override fun sendRecentViewImpressionTracking(recentViewModel: List<RecentViewDetailProductViewModel>) {
        RecentViewTracking.trackEventImpressionOnProductRecentView(activity,
                getRecentViewAsDataLayerForImpression(recentViewModel))
    }

    fun getRecentViewAsDataLayerForImpression(recentViewModel: List<RecentViewDetailProductViewModel>): List<Any> {
        val objects: MutableList<Any> = ArrayList()
        for (model in recentViewModel) {
            objects.add(DataLayer.mapOf(
                    "name", model.name,
                    "id", model.productId,
                    "price", CurrencyFormatHelper.convertRupiahToInt(model.price.toString()).toString(),
                    "list", "/recent",
                    "brand", DEFAULT_VALUE_NONE_OTHER,
                    "category", "",
                    "position", model.positionForRecentViewTracking.toString()))
        }
        return objects
    }

    override fun onErrorAddWishList(errorMessage: String, productId: String) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productID: String) {
        dismissLoadingProgress()
        for (i in adapter.list.indices) {
            if (adapter.list[i] is RecentViewProductViewModel) {
                val feedDetailViewModel = adapter.list[i] as RecentViewProductViewModel
                if (productID == feedDetailViewModel.id.toString()) {
                    feedDetailViewModel.wishlist = true
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.recent_view_msg_success_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productID: String) {
        dismissLoadingProgress()
        for (i in adapter.list.indices) {
            if (adapter.list[i] is RecentViewProductViewModel) {
                val feedDetailViewModel = adapter.list[i] as RecentViewProductViewModel
                if (productID == feedDetailViewModel.id.toString()) {
                    feedDetailViewModel.wishlist = true
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.recent_view_msg_success_remove_wishlist))
    }

    override fun dismissLoadingProgress() {}

    companion object {
        const val DEFAULT_VALUE_NONE_OTHER = "none / other"
        fun createInstance(): RecentViewFragment {
            return RecentViewFragment()
        }
    }
}