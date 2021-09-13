package com.tokopedia.review.feature.inbox.buyerreview.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 8/21/17.
 */
class InboxReputationFilterFragment constructor() : BaseDaggerFragment(),
    InboxReputationFilterAdapter.FilterListener, InboxReputationFilterActivity.ResetListener {
    var list: RecyclerView? = null
    var saveButton: Button? = null
    var adapter: InboxReputationFilterAdapter? = null
    var listOption: ArrayList<OptionUiModel>? = null
    var timeFilter: String? = null
    var timeFilterName: String? = null
    var scoreFilter: String? = null

    @kotlin.jvm.JvmField
    @Inject
    var reputationTracking: ReputationTracking? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            timeFilter = getArguments()!!.getString(SELECTED_TIME_FILTER, "")
            scoreFilter = getArguments()!!.getString(SELECTED_SCORE_FILTER, "")
        } else if (savedInstanceState != null) {
            timeFilter = savedInstanceState.getString(SELECTED_TIME_FILTER, "")
            scoreFilter = savedInstanceState.getString(SELECTED_SCORE_FILTER, "")
        }
        initData()
    }

    override fun initInjector() {
        val baseAppComponent: BaseAppComponent =
            (requireContext().getApplicationContext() as BaseMainApplication).getBaseAppComponent()
        val reputationComponent: DaggerReputationComponent = DaggerReputationComponent
            .builder()
            .baseAppComponent(baseAppComponent)
            .build() as DaggerReputationComponent
        reputationComponent.inject(this)
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setRetainInstance(true)
        val parentView: View = inflater.inflate(
            R.layout.fragment_inbox_reputation_filter, container,
            false
        )
        list = parentView.findViewById<View>(R.id.list) as RecyclerView?
        saveButton = parentView.findViewById<View>(R.id.save_button) as Button?
        prepareView()
        return parentView
    }

    private fun prepareView() {
        list!!.setLayoutManager(
            LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL, false
            )
        )
        adapter =
            InboxReputationFilterAdapter.Companion.createInstance(getContext(), this, listOption)
        list!!.setAdapter(adapter)
        saveButton!!.setOnClickListener(View.OnClickListener({ view: View? ->
            val data: Intent = Intent()
            data.putExtra(SELECTED_TIME_FILTER, timeFilter)
            data.putExtra(SELECTED_SCORE_FILTER, scoreFilter)
            getActivity()!!.setResult(Activity.RESULT_OK, data)
            getActivity()!!.finish()
        }))
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
                GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER, FILTER_ALL_TIME, list.size
            )
        )
        list.add(
            OptionUiModel(
                getString(R.string.filter_last_7_days),
                GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER, FILTER_LAST_WEEK, list.size
            )
        )
        list.add(
            OptionUiModel(
                getString(R.string.filter_this_month),
                GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER, FILTER_THIS_MONTH, list.size
            )
        )
        list.add(
            OptionUiModel(
                getString(R.string.filter_last_3_month),
                GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER,
                FILTER_LAST_3_MONTH,
                list.size
            )
        )
        if ((getArguments() != null
                    && getArguments()!!.getInt(InboxReputationFragment.Companion.PARAM_TAB) ==
                    ReviewInboxConstants.TAB_BUYER_REVIEW)
        ) {
            list.add(HeaderOptionUiModel(getString(R.string.filter_status)))
            list.add(
                OptionUiModel(
                    getString(R.string.filter_given_reputation),
                    GetInboxReputationUseCase.Companion.PARAM_SCORE_FILTER, FILTER_GIVEN_SCORE, list
                        .size
                )
            )
            list.add(
                OptionUiModel(
                    getString(R.string.filter_no_reputation),
                    GetInboxReputationUseCase.Companion.PARAM_SCORE_FILTER,
                    FILTER_NO_SCORE,
                    list.size
                )
            )
        }
        return list
    }

    private fun setSelected(listOption: ArrayList<OptionUiModel>?) {
        if ((!(getArguments()!!.getString(SELECTED_TIME_FILTER, "") == "")
                    || !(getArguments()!!.getString(SELECTED_SCORE_FILTER, "") == ""))
        ) {
            for (optionUiModel: OptionUiModel in listOption!!) {
                if (((optionUiModel.getKey() ==
                            GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER) && (optionUiModel.getValue() ==
                            getArguments()!!.getString(SELECTED_TIME_FILTER)))
                ) {
                    optionUiModel.setSelected(true)
                }
                if (((optionUiModel.getKey() ==
                            GetInboxReputationUseCase.Companion.PARAM_SCORE_FILTER) && (optionUiModel.getValue() ==
                            getArguments()!!.getString(SELECTED_SCORE_FILTER)))
                ) {
                    optionUiModel.setSelected(true)
                }
            }
        }
    }

    public override fun resetFilter() {
        adapter!!.resetFilter()
        timeFilter = ""
        timeFilterName = ""
        scoreFilter = ""
    }

    public override fun onFilterSelected(optionUiModel: OptionUiModel) {
        if ((optionUiModel.getKey() == GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER)) {
            timeFilter = optionUiModel.getValue()
            timeFilterName = optionUiModel.getName()
        } else if ((optionUiModel.getKey() == GetInboxReputationUseCase.Companion.PARAM_SCORE_FILTER)) {
            scoreFilter = optionUiModel.getValue()
        }
    }

    public override fun onFilterUnselected(optionUiModel: OptionUiModel) {
        if ((optionUiModel.getKey() == GetInboxReputationUseCase.Companion.PARAM_TIME_FILTER)) {
            timeFilter = ""
            timeFilterName = ""
        } else if ((optionUiModel.getKey() == GetInboxReputationUseCase.Companion.PARAM_SCORE_FILTER)) {
            scoreFilter = ""
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SELECTED_TIME_FILTER, timeFilter)
        outState.putString(SELECTED_SCORE_FILTER, scoreFilter)
    }

    companion object {
        private val FILTER_ALL_TIME: String = "1"
        private val FILTER_LAST_WEEK: String = "2"
        private val FILTER_THIS_MONTH: String = "3"
        private val FILTER_LAST_3_MONTH: String = "4"
        private val FILTER_NO_SCORE: String = "1"
        private val FILTER_GIVEN_SCORE: String = "2"
        val SELECTED_TIME_FILTER: String = "SELECTED_TIME_FILTER"
        val SELECTED_SCORE_FILTER: String = "SELECTED_SCORE_FILTER"
        fun createInstance(timeFilter: String?, statusFilter: String?, tab: Int): Fragment {
            val fragment: InboxReputationFilterFragment = InboxReputationFilterFragment()
            val bundle: Bundle = Bundle()
            bundle.putString(SELECTED_TIME_FILTER, timeFilter)
            bundle.putString(SELECTED_SCORE_FILTER, statusFilter)
            bundle.putInt(InboxReputationFragment.Companion.PARAM_TAB, tab)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}