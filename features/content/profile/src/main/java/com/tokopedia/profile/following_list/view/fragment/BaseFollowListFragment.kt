package com.tokopedia.profile.following_list.view.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.profile.R
import com.tokopedia.profile.following_list.view.activity.FollowingListActivity
import com.tokopedia.profile.following_list.view.adapter.FollowingAdapter
import com.tokopedia.profile.following_list.view.listener.FollowingList
import com.tokopedia.profile.following_list.view.listener.FollowingListEmptyListener
import com.tokopedia.profile.following_list.view.viewmodel.FollowingResultViewModel
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel

/**
 * Created by jegul on 2019-10-22
 */
abstract class BaseFollowListFragment<I: FollowingViewModel, T : FollowingResultViewModel<I>> : BaseDaggerFragment(), FollowingList.View<I, T> {

    private lateinit var rvItem: RecyclerView
    private lateinit var progressBar: ProgressBar

    protected var isCanLoadMore: Boolean = false
    protected var cursor: String? = null
    protected var emptyApplink: String? = null
    private var userId: Int = 0

    protected lateinit var adapter: FollowingAdapter
    private lateinit var emptyState: View
    protected lateinit var emptyButton: Button
    private var openFollowerPage: Boolean? = false

    abstract var presenter: FollowingList.Presenter<I, T>

    abstract fun updateParams(viewModel: T)
    abstract fun onViewUpdated(viewModel: T)

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
                    presenter.getFollowingListLoadMore(userId, cursor)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            userId = arguments!!.getInt(FollowingListActivity.ARGS_USER_ID)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_following_list, container, false)
        rvItem = parentView.findViewById(R.id.rv_item)
        progressBar = parentView.findViewById(R.id.progress_bar)
        emptyState = parentView.findViewById(R.id.view_empty_state)
        emptyButton = parentView.findViewById(R.id.btn_empty)
        presenter.attachView(this)
        return parentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openFollowerPage = arguments!!.getBoolean(FollowingListActivity.ARGS_OPEN_FOLLOWER, false)
        initView()
        initViewListener()
    }

    private fun initView() {
        adapter = FollowingAdapter(activity, this)
        rvItem.apply {
            visibility = View.GONE
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@BaseFollowListFragment.adapter
            addOnScrollListener(recyclerViewListener)
        }
        emptyState.visibility = View.GONE
        showLoading()
        presenter.getFollowingList(userId)
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

    override fun onSuccessGetKolFollowingList(viewModel: T) {
        rvItem.visibility = View.VISIBLE
        updateView(viewModel)
        updateParams(viewModel)
    }

    override fun onSuccessGetKolFollowingListEmptyState() {
        emptyState.visibility = View.VISIBLE
        if (activity is FollowingListEmptyListener) {
            (activity as FollowingListEmptyListener).onFollowingEmpty()
        }
    }

    override fun onErrorGetKolFollowingList(error: String) {
        NetworkErrorHelper.showEmptyState(activity, view, error) { initView() }
    }

    override fun onSuccessLoadMoreKolFollowingList(itemList: T) {
        adapter.removeBottomLoading()
        adapter.itemList.addAll(itemList.followingViewModelList)
        adapter.notifyDataSetChanged()
        updateParams(itemList)
    }

    override fun onErrorLoadMoreKolFollowingList(error: String) {
        adapter.removeBottomLoading()
        NetworkErrorHelper.showSnackbar(activity, error)
    }

    override fun isOpenFollowerPage(): Boolean {
        return openFollowerPage!!
    }

    override fun onUnfollowShopButtonClicked(model: FollowingViewModel) {
    }

    override fun onSuccessUnfollowShop(model: FollowingViewModel) {
    }

    override fun onErrorUnfollowShop(errorMessage: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun updateView(viewModel: T) {
        adapter.itemList = viewModel.followingViewModelList
        onViewUpdated(viewModel)
    }
}
