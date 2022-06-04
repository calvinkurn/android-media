package com.tokopedia.recentview.view.fragment

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
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.recentview.R
import com.tokopedia.recentview.analytics.RecentViewTracking
import com.tokopedia.recentview.di.DaggerRecentViewComponent
import com.tokopedia.recentview.view.adapter.RecentViewDetailAdapter
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactoryImpl
import com.tokopedia.recentview.view.listener.RecentView
import com.tokopedia.recentview.view.presenter.RecentViewViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

/**
 * @author by Lukas on 15/7/2020.
 */

@ExperimentalCoroutinesApi
class RecentViewFragment : BaseDaggerFragment(), RecentView.View {
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: RecentViewDetailAdapter
    private var layoutManager: LinearLayoutManager? = null

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
            recentViewDetailProductDataResp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        if (it.data.isNotEmpty()) {
                            val visitableList: ArrayList<Visitable<*>> = ArrayList(it.data)
                            RecentViewTracking.trackEventImpressionOnProductRecentView(activity, it.data)
                            onSuccessGetRecentView(visitableList)
                        } else {
                            onEmptyGetRecentView()
                        }
                    }
                    is Fail -> {
                        onErrorGetRecentView(ErrorHandler.getErrorMessage(context, it.throwable))
                    }
                }
            })

            addWishlistResponse.observe(lifecycleOwner, Observer {
                if(it is Success) onSuccessAddWishlist(it.data)
                else onErrorAddWishList(com.tokopedia.network.utils.ErrorHandler.getErrorMessage (context, (it as Fail).throwable))
            })

            removeWishlistResponse.observe(lifecycleOwner, Observer {
                if(it is Success) onSuccessRemoveWishlist(it.data)
                else onErrorRemoveWishlist(com.tokopedia.network.utils.ErrorHandler.getErrorMessage (context, (it as Fail).throwable))
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
        recyclerView = parentView.findViewById<View>(R.id.rv_recent_view_page) as RecyclerView
        prepareView()
        return parentView
    }

    private fun prepareView() {
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(recyclerView?.context, DividerItemDecoration.VERTICAL)
        context?.let { context ->
            val drawable = ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.bg_line_separator)
            drawable?.let { dividerItemDecoration.setDrawable(it) }
        }
        recyclerView?.addItemDecoration(dividerItemDecoration)
        recyclerView?.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewModel.getRecentView()
        RecentViewTracking.trackEventOpenScreen(context)
    }

    fun onWishlistClicked(productId: Int, isWishlist: Boolean) {
        showLoadingProgress()

        var isUsingV2 = false
        context?.let {
            if (WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(it)) isUsingV2 = true
        }
        if (!isWishlist) {
            if (isUsingV2) {
                viewModel.addToWishlistV2(productId.toString(), object : WishlistV2ActionListener {
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        context?.let { context ->
                            val errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable)
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, v)
                            }
                        }
                    }

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        context?.let { context ->
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
                            }
                        }
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {}
                })
            } else viewModel.addToWishlist(productId.toString())
        } else {
            if (isUsingV2) {
                viewModel.removeFromWishlistV2(productId.toString(), object : WishlistV2ActionListener{
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {}
                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {}

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                        context?.let { context ->
                            val errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable)
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, v)
                            }
                        }
                    }

                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {
                        context?.let { context ->
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(result, context, v)
                            }
                        }
                    }

                })
            } else viewModel.removeFromWishlist(productId.toString())
        }
    }

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun showLoadingProgress() {}

    override fun onErrorGetRecentView(errorMessage: String) {
        adapter.dismissLoading()
        if (activity != null && view != null) NetworkErrorHelper.showEmptyState(activity, view,
                errorMessage) {
            showLoading()
            viewModel.getRecentView()
        }
    }

    private fun onSuccessGetRecentView(recentViewProductViewModels: ArrayList<Visitable<*>>) {
        adapter.dismissLoading()
        adapter.addList(recentViewProductViewModels)
        adapter.notifyDataSetChanged()
    }

    override fun onEmptyGetRecentView() {
        adapter.dismissLoading()
        adapter.showEmpty()
    }

    override fun sendRecentViewClickTracking(element: RecentViewDetailProductDataModel) {
        activity?.let {
            RecentViewTracking.trackEventClickOnProductRecentView(it, element)
        }
    }

    private fun onErrorAddWishList(errorMessage: String) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun onSuccessAddWishlist(productID: String) {
        dismissLoadingProgress()
        for (i in adapter.list.indices) {
            if (adapter.list[i] is RecentViewDetailProductDataModel) {
                val feedDetailViewModel = adapter.list[i] as RecentViewDetailProductDataModel
                if (productID == feedDetailViewModel.productId) {
                    feedDetailViewModel.isWishlist = true
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        val msg = getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
        val ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, ctaText, View.OnClickListener { goToWishList() }).show()
        }
    }

    private fun goToWishList() {
        context?.let { context ->
            val intent = RouteManager.getIntent(context, ApplinkConst.NEW_WISHLIST)
            startActivity(intent)
        }
    }

    private fun onErrorRemoveWishlist(errorMessage: String) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun onSuccessRemoveWishlist(productID: String) {
        dismissLoadingProgress()
        for (i in adapter.list.indices) {
            if (adapter.list[i] is RecentViewDetailProductDataModel) {
                val feedDetailViewModel = adapter.list[i] as RecentViewDetailProductDataModel
                if (productID == feedDetailViewModel.productId) {
                    feedDetailViewModel.isWishlist = true
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
        val msg = getString(com.tokopedia.wishlist_common.R.string.on_success_remove_from_wishlist_msg)
        val ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_remove_from_wishlist)
        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, ctaText).show()
        }
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    override fun dismissLoadingProgress() {}

    companion object {
        const val DEFAULT_VALUE_NONE_OTHER = "none / other"
        fun createInstance(): RecentViewFragment {
            return RecentViewFragment()
        }
    }
}