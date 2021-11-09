package com.tokopedia.review.feature.reputationhistory.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.datepicker.range.view.listener.DatePickerResultListener
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reputationhistory.di.DaggerSellerReputationComponent
import com.tokopedia.review.feature.reputationhistory.di.SellerReputationComponent
import com.tokopedia.review.feature.reputationhistory.view.activity.SellerReputationInfoActivity
import com.tokopedia.review.feature.reputationhistory.view.adapter.ReputationPenaltyAdapterFactory
import com.tokopedia.review.feature.reputationhistory.view.adapter.SellerReputationPenaltyAdapter
import com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder.SellerReputationShopViewHolder
import com.tokopedia.review.feature.reputationhistory.view.helper.GMStatHeaderViewHelper
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationPenaltyUiModel
import com.tokopedia.review.feature.reputationhistory.view.viewmodel.SellerReputationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SellerReputationPenaltyFragment :
    BaseListFragment<Visitable<*>, ReputationPenaltyAdapterFactory>(),
    HasComponent<SellerReputationComponent>, DatePickerResultListener.DatePickerResult,
    SellerReputationShopViewHolder.SellerReputationInfoListener {

    @Inject
    lateinit var sellerReputationViewModel: SellerReputationViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var datePickerResultListener: DatePickerResultListener? = null


    private val reputationPenaltyAdapterFactory by lazy {
        ReputationPenaltyAdapterFactory(this, this)
    }

    private val reputationPenaltyAdapter by lazy {
        SellerReputationPenaltyAdapter(
            reputationPenaltyAdapterFactory
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seller_reputation_penalty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background)
            )
        }
        datePickerResultListener = DatePickerResultListener(this, GMStatHeaderViewHelper.MOVE_TO_SET_DATE)
        observeReputationPenalty()
        observeReputationPenaltyLoadMore()
    }

    override fun getScreenName(): String {
        return AppScreen.SCREEN_SELLER_REP_HISTORY
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): SellerReputationComponent? {
        return activity?.run {
            DaggerSellerReputationComponent
                .builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .build()
        }
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //no op
    }

    override fun loadData(page: Int) {
        sellerReputationViewModel.getReputationPenaltyList(page)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ReputationPenaltyAdapterFactory> {
        return reputationPenaltyAdapter
    }

    override fun getAdapterTypeFactory(): ReputationPenaltyAdapterFactory {
        return reputationPenaltyAdapterFactory
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        datePickerResultListener?.onActivityResult(requestCode, resultCode, data)
    }

    override fun loadInitialData() {
        super.loadInitialData()
        sellerReputationViewModel.getReputationPenaltyRewardMerge()
    }

    override fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = false
        showLoading()
        sellerReputationViewModel.getReputationPenaltyRewardMerge()
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rv_seller_reputation_penalty)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.swipe_refresh_reputation_penalty)
    }

    override fun onDestroy() {
        removeObservers(sellerReputationViewModel.reputationAndPenaltyMerge)
        removeObservers(sellerReputationViewModel.reputationAndPenaltyReward)
        super.onDestroy()
    }

    override fun onDateChoosen(sDate: Long, eDate: Long, lastSelection: Int, selectionType: Int) {
        reputationPenaltyAdapter.apply {
            removeReputationPenaltyListData()
            removeEmptyStateReputationPenalty()
            updateDateFilter(sDate, eDate)
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
        sellerReputationViewModel.setDateFilterReputationPenalty(Pair(sDate, eDate))
    }

    override fun onClickReputationInfo() {
        this@SellerReputationPenaltyFragment.startActivity(
            Intent(
                this@SellerReputationPenaltyFragment.activity,
                SellerReputationInfoActivity::class.java
            )
        )
    }

    private fun observeReputationPenalty() {
        observe(sellerReputationViewModel.reputationAndPenaltyMerge) {
            hideLoading()
            when (it) {
                is Success -> {
                    val baseReputationPenaltyData = it.data.baseSellerReputationList.filterNot { visitable -> visitable is ReputationPenaltyUiModel }
                    val reputationPenaltyList = it.data.baseSellerReputationList.filterIsInstance<ReputationPenaltyUiModel>()
                    reputationPenaltyAdapter.setReputationPenaltyMerge(baseReputationPenaltyData)
                    onSuccessReputationPenaltyList(reputationPenaltyList, it.data.hasNext)
                }
                is Fail -> {
                    showToasterErrorPenalty(it.throwable)
                }
            }
        }
    }

    private fun observeReputationPenaltyLoadMore() {
        observe(sellerReputationViewModel.reputationAndPenaltyReward) {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessReputationPenaltyList(it.data.reputationPenaltyList, it.data.hasNext)
                }
                is Fail -> {
                    showToasterErrorPenalty(it.throwable)
                }
            }
        }
    }

    private fun onSuccessReputationPenaltyList(
        data: List<ReputationPenaltyUiModel>,
        hasNext: String?) {
        val reputationPenaltyList =
            reputationPenaltyAdapter.list.filterIsInstance<ReputationPenaltyUiModel>()
        if (reputationPenaltyList.isEmpty() && data.isEmpty()) {
            reputationPenaltyAdapter.setEmptyStateReputationPenalty()
        } else {
            reputationPenaltyAdapter.updateReputationPenaltyListData(data)
        }
        val hasNextPenalty = hasNext != null
        updateScrollListenerState(hasNextPenalty)
    }

    private fun showToasterErrorPenalty(throwable: Throwable) {
        val messageError = context?.let { ErrorHandler.getErrorMessage(it, throwable) }
        val actionError = getString(R.string.error_action_text_seller_reputation)
        val reputationPenaltyList =
            reputationPenaltyAdapter.list.filterIsInstance<ReputationPenaltyUiModel>()
        view?.run {
            messageError?.let {
                Toaster.build(
                    this,
                    it, duration = Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR,
                    actionText = actionError, clickListener = {
                        if (reputationPenaltyList.isNotEmpty()) {
                            loadData(endlessRecyclerViewScrollListener.currentPage)
                        } else {
                            loadInitialData()
                        }
                    }
                ).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): SellerReputationPenaltyFragment {
            return SellerReputationPenaltyFragment()
        }
    }
}