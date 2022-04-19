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
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recentview.R
import com.tokopedia.recentview.analytics.RecentViewTracking
import com.tokopedia.recentview.di.DaggerRecentViewComponent
import com.tokopedia.recentview.ext.convertRupiahToInt
import com.tokopedia.recentview.view.adapter.RecentViewDetailAdapter
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactoryImpl
import com.tokopedia.recentview.view.listener.RecentView
import com.tokopedia.recentview.view.presenter.RecentViewViewModel
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
                else onErrorAddWishList((it as Fail).throwable.message ?: "")
            })

            removeWishlistResponse.observe(lifecycleOwner, Observer {
                if(it is Success) onSuccessRemoveWishlist(it.data)
                else onErrorRemoveWishlist((it as Fail).throwable.message ?: "")
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

    fun onWishlistClicked(adapterPosition: Int, productId: Int, isWishlist: Boolean) {
        showLoadingProgress()
        if (!isWishlist) {
            viewModel.addToWishlist(adapterPosition, productId.toString())
        } else {
            viewModel.removeFromWishlist(adapterPosition, productId.toString())
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
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.recent_view_msg_success_add_wishlist))
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