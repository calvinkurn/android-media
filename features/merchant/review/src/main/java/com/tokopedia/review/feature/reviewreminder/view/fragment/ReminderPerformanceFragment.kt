package com.tokopedia.review.feature.reviewreminder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderStats
import com.tokopedia.review.feature.reviewreminder.di.component.DaggerReviewReminderComponent
import com.tokopedia.review.feature.reviewreminder.view.viewmodel.ReminderPerformanceViewModel
import com.tokopedia.unifyprinciples.Typography
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

        observeViewModel()
        fetchData()
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

    private fun observeViewModel() {
        viewModel?.getReminderStats()?.observe(viewLifecycleOwner, observerReminderStats)
    }

    private val observerReminderStats = Observer { productrevGetReminderStats: ProductrevGetReminderStats ->
        textPeriod?.text = productrevGetReminderStats.timeRange
        textNumberChat?.text = getString(R.string.review_reminder_postfix_chat, productrevGetReminderStats.totalReminderStats)
        textLastSent?.text = productrevGetReminderStats.lastReminderTime
        textIncrementChat?.text = productrevGetReminderStats.lastReminderStats
        textReviewPercentage?.text = productrevGetReminderStats.reviewPercentage
    }

    private fun fetchData() {
        viewModel?.fetchReminderStats()
    }
}