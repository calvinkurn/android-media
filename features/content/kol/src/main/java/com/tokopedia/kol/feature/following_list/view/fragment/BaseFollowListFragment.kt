package com.tokopedia.kol.feature.following_list.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.following_list.di.DaggerKolFollowingListComponent
import com.tokopedia.kol.feature.following_list.view.activity.KolFollowingListActivity
import com.tokopedia.kol.feature.following_list.view.adapter.KolFollowingAdapter
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingListEmptyListener
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-10-22
 */
abstract class BaseFollowListFragment : BaseDaggerFragment(), KolFollowingList.View {

    private lateinit var rvItem: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var isCanLoadMore: Boolean = false
    private var cursor: String? = null
    private var emptyApplink: String? = null
    private var userId: Int = 0

    private lateinit var adapter: KolFollowingAdapter
    private lateinit var emptyState: View
    private lateinit var emptyButton: Button
    private var openFollowerPage: Boolean? = false

    abstract var presenter: KolFollowingList.Presenter

    private val recyclerViewListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                if (isCanLoadMore && visibleItemCount + firstVisiblesItems >= totalItemCount) {
                    adapter.addBottomLoading()
                    isCanLoadMore = false
                    presenter.getKolLoadMore(userId, cursor)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            userId = arguments!!.getInt(KolFollowingListActivity.ARGS_USER_ID)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(state: Bundle?) {
        super.onViewStateRestored(state)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_kol_following_list, container, false)
        rvItem = parentView.findViewById(R.id.rv_item)
        progressBar = parentView.findViewById(R.id.progress_bar)
        emptyState = parentView.findViewById(R.id.view_empty_state)
        emptyButton = parentView.findViewById(R.id.btn_empty)
        presenter.attachView(this)
        return parentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openFollowerPage = arguments!!.getBoolean(KolFollowingListActivity.ARGS_OPEN_FOLLOWER, false)
        initView()
        initViewListener()
    }

    private fun initView() {
        adapter = KolFollowingAdapter(activity, this)
        rvItem.apply {
            visibility = View.GONE
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@BaseFollowListFragment.adapter
            addOnScrollListener(recyclerViewListener)
        }
        emptyState.visibility = View.GONE
        showLoading()
        presenter.getKolFollowingList(userId)
    }

    private fun initViewListener() {
        emptyButton.setOnClickListener {
            if (!TextUtils.isEmpty(emptyApplink) && activity!!.application is ApplinkRouter) {
                val applinkRouter = activity!!.application as ApplinkRouter
                applinkRouter.goToApplinkActivity(activity, emptyApplink)
            }
        }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onSuccessGetKolFollowingList(viewModel: KolFollowingResultViewModel) {
        rvItem.visibility = View.VISIBLE
        updateView(viewModel)
        updateParams(viewModel)
    }

    override fun onSuccessGetKolFollowingListEmptyState() {
        emptyState.visibility = View.VISIBLE
        if (activity is KolFollowingListEmptyListener) {
            (activity as KolFollowingListEmptyListener).onFollowingEmpty()
        }
    }

    private fun updateView(model: KolFollowingResultViewModel) {
        adapter.itemList = model.kolFollowingViewModelList
        if (model.kolFollowingViewModelList == null || model.kolFollowingViewModelList.size == 0) {
            emptyButton.text = model.buttonText
            emptyApplink = model.buttonApplink

            if (activity is KolFollowingListEmptyListener) {
                (activity as KolFollowingListEmptyListener).onFollowingEmpty()
            }
        } else {
            if (activity is KolFollowingListEmptyListener) {
                (activity as KolFollowingListEmptyListener).onFollowingNotEmpty()
            }
        }
    }

    private fun updateParams(viewModel: KolFollowingResultViewModel) {
        this.isCanLoadMore = viewModel.isCanLoadMore
        this.cursor = viewModel.lastCursor
    }

    override fun onErrorGetKolFollowingList(error: String) {
        NetworkErrorHelper.showEmptyState(activity, view, error) { initView() }
    }

    override fun onSuccessLoadMoreKolFollowingList(itemList: KolFollowingResultViewModel) {
        adapter.removeBottomLoading()
        adapter.itemList.addAll(itemList.kolFollowingViewModelList)
        adapter.notifyDataSetChanged()
        updateParams(itemList)
    }

    override fun onErrorLoadMoreKolFollowingList(error: String) {
        adapter.removeBottomLoading()
        NetworkErrorHelper.showSnackbar(activity, error)
    }

    override fun onListItemClicked(item: KolFollowingViewModel) {
        if (RouteManager.isSupportApplink(context, item.profileApplink) && !item.profileApplink.contains("m.tokopedia.com")) {
            RouteManager.route(context, item.profileApplink)
        }
    }

    override fun isOpenFollowerPage(): Boolean {
        return openFollowerPage!!
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
