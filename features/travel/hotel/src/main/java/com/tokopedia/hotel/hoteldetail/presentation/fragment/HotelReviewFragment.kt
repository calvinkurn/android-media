package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.QueryHotelPropertyReview
import com.tokopedia.hotel.databinding.FragmentHotelReviewBinding
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelReviewActivity
import com.tokopedia.hotel.hoteldetail.presentation.adapter.ReviewAdapterTypeFactory
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReviewViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewFragment : BaseListFragment<HotelReview, ReviewAdapterTypeFactory>(), FilterChipAdapter.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var param: HotelReviewParam = HotelReviewParam()
    lateinit var reviewViewModel: HotelReviewViewModel
    private var binding by autoClearedNullable<FragmentHotelReviewBinding>()

    var isFirstTime = true

    init {
        param.rows = PARAM_ROWS
        param.sortBy = PARAM_SORT_BY_TIME
        param.sortType = PARAM_SORT_TYPE_DESC
        param.filterByCountry = COUNTRY_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param.propertyId = it.getLong(ARG_PROPERTY_ID, 0L)
        }

        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        reviewViewModel = viewModelProvider.get(HotelReviewViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reviewViewModel.reviewResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onSuccessGetResult(it.data)
                    is Fail -> onErrorGetResult(it.throwable)
                }
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelReviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getSwipeRefreshLayoutResourceId() = 0
    override fun getRecyclerViewResourceId() = R.id.recycler_view

    fun onSuccessGetResult(reviews: HotelReview.ReviewData) {
        showHotelMetaReview(true)
        super.renderList(reviews.reviewList, reviews.hasNext)

        binding?.let {
            it.reviewPointTextView.text = reviews.averageScoreReview.toString()
            it.reviewHeadlineText.text = reviews.headline
            it.reviewTotalCountText.text = getString(R.string.hotel_review_total_review, reviews.totalReview.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HotelReviewActivity).updateTitle(screenName)
        initFilterView()
        initSwitch()
    }

    fun initSwitch() {
        binding?.let {
            it.indonesiaReviewSwitch.isChecked = true
            it.indonesiaReviewSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                param.filterByCountry = if (isChecked) COUNTRY_ID else COUNTRY_ALL
                // add param to get Indo
                loadInitialData()
            }
        }
    }

    fun initFilterView() {
        binding?.let {
            it.filterRecyclerView.listener = this
            it.filterRecyclerView.setItem(
                arrayListOf(
                    getString(R.string.hotel_review_filter_first_rank),
                    getString(R.string.hotel_review_filter_second_rank),
                    getString(R.string.hotel_review_filter_third_rank)
                ),
                com.tokopedia.unifyprinciples.R.color.Unify_GN300
            )
            it.filterRecyclerView.selectOnlyOneChip(true)
            // initially select recent search chip
            it.filterRecyclerView.selectChipByPosition(0)
        }
    }

    private fun onErrorGetResult(throwable: Throwable) {
        binding?.containerError?.root?.visible()
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(
                    this,
                    throwable,
                    { onRetryClicked() },
                    it
                )
            }
        }
    }

    override fun onRetryClicked() {
        binding?.let {
            it.containerError.root.hide()
        }
        super.onRetryClicked()
    }

    override fun getAdapterTypeFactory(): ReviewAdapterTypeFactory = ReviewAdapterTypeFactory()

    override fun onItemClicked(t: HotelReview?) {
    }

    override fun getScreenName(): String = getString(R.string.hotel_review_title)

    override fun initInjector() {
        getComponent(HotelDetailComponent::class.java).inject(this)
    }

    fun showHotelMetaReview(visible: Boolean) {
        binding?.appBarLayout?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun loadData(page: Int) {
        if (isFirstTime) {
            showHotelMetaReview(false)
            isFirstTime = false
        }
        param.page = page - 1
        reviewViewModel.getReview(QueryHotelPropertyReview(), param)
    }

    override fun onChipClickListener(string: String, isSelected: Boolean) {
        if (isSelected) {
            when (string) {
                getString(R.string.hotel_review_filter_first_rank) -> {
                    param.filterByRank = FILTER_RANK_FIRST
                }
                getString(R.string.hotel_review_filter_second_rank) -> {
                    param.filterByRank = FILTER_RANK_SECOND
                }
                getString(R.string.hotel_review_filter_third_rank) -> {
                    param.filterByRank = FILTER_RANK_THIRD
                }
            }
        } else {
            param.filterByRank = FILTER_RANK_ZERO
        }

        loadInitialData()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        var emptyModel = EmptyModel()

        binding?.let {
            if (it.indonesiaReviewSwitch.isChecked) {
                emptyModel.urlRes = getString(R.string.hotel_url_no_indonesian_review)
                emptyModel.title = getString(R.string.hotel_review_indonesia_not_found_title)
                emptyModel.content = getString(R.string.hotel_review_indonesia_not_found_subtitle)
            } else {
                emptyModel.urlRes = getString(R.string.hotel_url_no_review)
                emptyModel.title = getString(R.string.hotel_review_filter_review_not_found_title)
                emptyModel.content = getString(R.string.hotel_review_filter_review_not_found_subtitle)
            }
        }
        return emptyModel
    }

    companion object {

        const val ARG_PROPERTY_ID = "arg_property_id"

        const val PARAM_ROWS = 11
        const val PARAM_SORT_BY_TIME = "create_time"
        const val PARAM_SORT_TYPE_DESC = "desc"

        const val COUNTRY_ID = "id"
        const val COUNTRY_ALL = "all"

        const val FILTER_RANK_ZERO = 0
        const val FILTER_RANK_FIRST = 1
        const val FILTER_RANK_SECOND = 2
        const val FILTER_RANK_THIRD = 3

        fun createInstance(propertyId: Long): HotelReviewFragment {
            return HotelReviewFragment().also {
                it.arguments = Bundle().apply {
                    putLong(ARG_PROPERTY_ID, propertyId)
                }
            }
        }
    }
}
