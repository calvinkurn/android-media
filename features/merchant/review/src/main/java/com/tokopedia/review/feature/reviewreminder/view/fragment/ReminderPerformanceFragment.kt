package com.tokopedia.review.feature.reviewreminder.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStats
import com.tokopedia.review.feature.reviewreminder.di.component.DaggerReviewReminderComponent
import com.tokopedia.review.feature.reviewreminder.view.viewmodel.ReminderPerformanceViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReminderPerformanceFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ReminderPerformanceViewModel? = null
    private var textPeriod: Typography? = null
    private var textNumberChat: Typography? = null
    private var textLastSent: Typography? = null
    private var textIncrementChat: Typography? = null
    private var textReviewPercentage: Typography? = null
    private var layoutLastSent: View? = null
    private var iconInformation: IconUnify? = null
    private var coachMarkItems: ArrayList<CoachMark2Item>? = null
    private var coachMarkInformation: CoachMark2? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReminderPerformanceViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reminder_performance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textPeriod = view.findViewById(R.id.text_period)
        textNumberChat = view.findViewById(R.id.text_number_of_chat)
        textLastSent = view.findViewById(R.id.text_last_sent)
        textIncrementChat = view.findViewById(R.id.text_increment_chat)
        textReviewPercentage = view.findViewById(R.id.text_review_percentage)
        layoutLastSent = view.findViewById(R.id.layout_last_sent)
        iconInformation = view.findViewById(R.id.icon_information)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        initView()
        setupViewinteraction()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onPause() {
        super.onPause()
        coachMarkInformation?.hideCoachMark()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val component = activity?.run {
            DaggerReviewReminderComponent.builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
        component?.inject(this)
    }

    private fun initView() {
        coachMarkItems = arrayListOf(
                CoachMark2Item(
                        iconInformation as View,
                        "",
                        getString(R.string.review_reminder_performance_coachmark)
                )
        )
        coachMarkInformation = CoachMark2(requireContext()).apply {
            simpleCloseIcon?.visibility = View.GONE
            simpleDescriptionView?.gravity = Gravity.CENTER
            isFocusable = true
        }
    }

    private fun setupViewinteraction() {
        iconInformation?.setOnClickListener {
            coachMarkInformation?.run {
                if (isShowing) dismiss()
                else coachMarkItems?.let { showCoachMark(it) }
            }
        }

        swipeRefreshLayout?.setOnRefreshListener {
            fetchData()
        }
    }

    private fun observeViewModel() {
        viewModel?.getReminderStats()?.observe(viewLifecycleOwner, observerReminderStats)
    }

    private val observerReminderStats = Observer<Result<ProductrevGetReminderStats>> { result ->
        if (result is Success) {
            val productrevGetReminderStats = result.data
            textPeriod?.text = productrevGetReminderStats.timeRange
            textNumberChat?.text = getString(R.string.review_reminder_postfix_chat, productrevGetReminderStats.totalReminderStats)
            textIncrementChat?.text = productrevGetReminderStats.lastReminderStats
            textReviewPercentage?.text = productrevGetReminderStats.reviewPercentage

            productrevGetReminderStats.lastReminderTime.let {
                if (it.isNotBlank()) {
                    textLastSent?.text = it
                    layoutLastSent?.visibility = View.VISIBLE
                }
            }
        } else if (result is Fail) {
            view?.let {
                Toaster.build(
                        it,
                        getString(R.string.review_reminder_snackbar_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR,
                        "Refresh",
                        View.OnClickListener {
                            fetchData()
                        }).show()
            }
        }
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun fetchData() {
        viewModel?.fetchReminderStats()
    }
}