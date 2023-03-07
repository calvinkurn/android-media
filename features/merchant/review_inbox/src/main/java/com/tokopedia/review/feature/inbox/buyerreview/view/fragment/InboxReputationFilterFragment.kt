package com.tokopedia.review.feature.inbox.buyerreview.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationFilterActivity
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationFilterAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.HeaderOptionUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.OptionUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 8/21/17.
 */
class InboxReputationFilterFragment : BaseDaggerFragment(),
    InboxReputationFilterAdapter.FilterListener, InboxReputationFilterActivity.ResetListener {

    companion object {
        private const val FILTER_ALL_TIME: String = "1"
        private const val FILTER_LAST_WEEK: String = "2"
        private const val FILTER_THIS_MONTH: String = "3"
        private const val FILTER_LAST_3_MONTH: String = "4"
        private const val FILTER_NO_SCORE: String = "1"
        private const val FILTER_GIVEN_SCORE: String = "2"
        const val SELECTED_TIME_FILTER: String = "SELECTED_TIME_FILTER"
        const val SELECTED_SCORE_FILTER: String = "SELECTED_SCORE_FILTER"

        fun createInstance(timeFilter: String?, statusFilter: String?, tab: Int): Fragment {
            val fragment = InboxReputationFilterFragment()
            val bundle = Bundle()
            bundle.putString(SELECTED_TIME_FILTER, timeFilter)
            bundle.putString(SELECTED_SCORE_FILTER, statusFilter)
            bundle.putInt(InboxReputationFragment.PARAM_TAB, tab)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var list: RecyclerView? = null
    private var saveButton: UnifyButton? = null
    private var adapter: InboxReputationFilterAdapter? = null
    private var listOption: ArrayList<OptionUiModel> = arrayListOf()
    private var timeFilter = ""
    private var timeFilterName = ""
    private var scoreFilter = ""

    @kotlin.jvm.JvmField
    @Inject
    var reputationTracking: ReputationTracking? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            timeFilter = it.getString(SELECTED_TIME_FILTER, "")
            scoreFilter = it.getString(SELECTED_SCORE_FILTER, "")
        }
        arguments?.let {
            timeFilter = it.getString(SELECTED_TIME_FILTER, "")
            scoreFilter = it.getString(SELECTED_SCORE_FILTER, "")
        }
        initData()
    }

    override fun initInjector() {
        val baseAppComponent: BaseAppComponent =
            (requireContext().applicationContext as BaseMainApplication).baseAppComponent
        val reputationComponent: DaggerReputationComponent = DaggerReputationComponent
            .builder()
            .baseAppComponent(baseAppComponent)
            .build() as DaggerReputationComponent
        reputationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        val parentView: View = inflater.inflate(
            R.layout.fragment_inbox_reputation_filter, container,
            false
        )
        list = parentView.findViewById<View>(R.id.list) as RecyclerView?
        saveButton = parentView.findViewById<View>(R.id.save_button) as UnifyButton?
        prepareView()
        return parentView
    }

    private fun prepareView() {
        list?.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = InboxReputationFilterAdapter.createInstance(
                this@InboxReputationFilterFragment,
                listOption
            )
        }
        saveButton?.setOnClickListener {
            val data = Intent()
            data.putExtra(SELECTED_TIME_FILTER, timeFilter)
            data.putExtra(SELECTED_SCORE_FILTER, scoreFilter)
            activity?.apply {
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }

    private fun initData() {
        listOption = createListOption()
        setSelected(listOption)
    }

    override fun getScreenName(): String {
        return AppScreen.SCREEN_INBOX_REPUTATION_FILTER
    }

    private fun createListOption(): ArrayList<OptionUiModel> {
        val list: ArrayList<OptionUiModel> = ArrayList()
        list.add(HeaderOptionUiModel(getString(R.string.filter_time)))
        list.add(
            OptionUiModel(
                getString(R.string.filter_all_time),
                GetInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_ALL_TIME, list.size
            )
        )
        list.add(
            OptionUiModel(
                getString(R.string.filter_last_7_days),
                GetInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_WEEK, list.size
            )
        )
        list.add(
            OptionUiModel(
                getString(R.string.filter_this_month),
                GetInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_THIS_MONTH, list.size
            )
        )
        list.add(
            OptionUiModel(
                getString(R.string.filter_last_3_month),
                GetInboxReputationUseCase.PARAM_TIME_FILTER,
                FILTER_LAST_3_MONTH,
                list.size
            )
        )
        if (arguments?.getInt(InboxReputationFragment.PARAM_TAB) ==
            ReviewInboxConstants.TAB_BUYER_REVIEW
        ) {
            list.add(HeaderOptionUiModel(getString(R.string.filter_status)))
            list.add(
                OptionUiModel(
                    getString(R.string.filter_given_reputation),
                    GetInboxReputationUseCase.PARAM_SCORE_FILTER,
                    FILTER_GIVEN_SCORE,
                    list.size
                )
            )
            list.add(
                OptionUiModel(
                    getString(R.string.filter_no_reputation),
                    GetInboxReputationUseCase.PARAM_SCORE_FILTER,
                    FILTER_NO_SCORE,
                    list.size
                )
            )
        }
        return list
    }

    private fun setSelected(listOption: ArrayList<OptionUiModel>) {
        arguments?.let {
            if ((it.getString(SELECTED_TIME_FILTER, "") != ""
                        || it.getString(SELECTED_SCORE_FILTER, "") != "")
            ) {
                for (optionUiModel: OptionUiModel in listOption) {
                    if (((optionUiModel.key ==
                                GetInboxReputationUseCase.PARAM_TIME_FILTER) && (optionUiModel.value ==
                                it.getString(SELECTED_TIME_FILTER)))
                    ) {
                        optionUiModel.isSelected = true
                    }
                    if (((optionUiModel.key ==
                                GetInboxReputationUseCase.PARAM_SCORE_FILTER) && (optionUiModel.value ==
                                it.getString(SELECTED_SCORE_FILTER)))
                    ) {
                        optionUiModel.isSelected = true
                    }
                }
            }
        }
    }

    override fun resetFilter() {
        adapter?.resetFilter()
        timeFilter = ""
        timeFilterName = ""
        scoreFilter = ""
    }

    override fun onFilterSelected(optionUiModel: OptionUiModel?) {
        optionUiModel?.let {
            if ((it.key == GetInboxReputationUseCase.PARAM_TIME_FILTER)) {
                timeFilter = it.value
                timeFilterName = it.name
            } else if ((it.key == GetInboxReputationUseCase.PARAM_SCORE_FILTER)) {
                scoreFilter = it.value
            }
        }
    }

    override fun onFilterUnselected(optionUiModel: OptionUiModel?) {
        optionUiModel?.let {
            if ((it.key == GetInboxReputationUseCase.PARAM_TIME_FILTER)) {
                timeFilter = ""
                timeFilterName = ""
            } else if ((it.key == GetInboxReputationUseCase.PARAM_SCORE_FILTER)) {
                scoreFilter = ""
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SELECTED_TIME_FILTER, timeFilter)
        outState.putString(SELECTED_SCORE_FILTER, scoreFilter)
    }
}