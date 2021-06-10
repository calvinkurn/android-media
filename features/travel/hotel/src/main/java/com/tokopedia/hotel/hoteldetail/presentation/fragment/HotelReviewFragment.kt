package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelReviewActivity
import com.tokopedia.hotel.hoteldetail.presentation.adapter.ReviewAdapterTypeFactory
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReviewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_review.*
import javax.inject.Inject

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewFragment : BaseListFragment<HotelReview, ReviewAdapterTypeFactory>(), FilterChipAdapter.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var param: HotelReviewParam = HotelReviewParam()
    lateinit var reviewViewModel: HotelReviewViewModel

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
            param.propertyId = it.getLong(ARG_PROPERTY_ID, 0)
        }

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        reviewViewModel = viewModelProvider.get(HotelReviewViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reviewViewModel.reviewResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> onErrorGetResult(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_review, container, false)
        return view
    }

    override fun getSwipeRefreshLayoutResourceId() = 0
    override fun getRecyclerViewResourceId() = R.id.recycler_view

    fun onSuccessGetResult(reviews: HotelReview.ReviewData) {
        showHotelMetaReview(true)
        super.renderList(reviews.reviewList, reviews.hasNext)
        review_point_text_view.text = reviews.averageScoreReview.toString()
        review_headline_text.text = reviews.headline
        review_total_count_text.text = getString(R.string.hotel_review_total_review, reviews.totalReview.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HotelReviewActivity).updateTitle(screenName)
        initFilterView()
        initSwitch()
    }

    fun initSwitch() {
        indonesia_review_switch.isChecked = true
        indonesia_review_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            param.filterByCountry = if (isChecked) COUNTRY_ID else COUNTRY_ALL
            //add param to get Indo
            loadInitialData()
        }
    }

    fun initFilterView() {
        filter_recycler_view.listener = this
        filter_recycler_view.setItem(arrayListOf(getString(R.string.hotel_review_filter_first_rank),
                getString(R.string.hotel_review_filter_second_rank),
                getString(R.string.hotel_review_filter_third_rank)),
                com.tokopedia.unifyprinciples.R.color.Unify_G300)
        filter_recycler_view.selectOnlyOneChip(true)

        //initially select recent search chip
        filter_recycler_view.selectChipByPosition(0)
    }

    private fun onErrorGetResult(throwable: Throwable) {
        super.showGetListError(throwable)
    }

    override fun getAdapterTypeFactory(): ReviewAdapterTypeFactory = ReviewAdapterTypeFactory()

    override fun onItemClicked(t: HotelReview?) {
    }

    override fun getScreenName(): String = getString(R.string.hotel_review_title)

    override fun initInjector() {
        getComponent(HotelDetailComponent::class.java).inject(this)
    }

    fun showHotelMetaReview(visible: Boolean) {
        app_bar_layout.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun loadData(page: Int) {
        if (isFirstTime) {
            showHotelMetaReview(false)
            isFirstTime = false
        }
        param.page = page - 1
        reviewViewModel.getReview(HotelGqlQuery.PROPERTY_REVIEW, param)

    }

    override fun onChipClickListener(string: String, isSelected: Boolean) {
        if (isSelected) {
            when (string) {
                getString(R.string.hotel_review_filter_first_rank) -> {
                    param.filterByRank = 1
                }
                getString(R.string.hotel_review_filter_second_rank) -> {
                    param.filterByRank = 2
                }
                getString(R.string.hotel_review_filter_third_rank) -> {
                    param.filterByRank = 3
                }
            }
        } else param.filterByRank = 0

        loadInitialData()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        var emptyModel = EmptyModel()
        
        if (indonesia_review_switch.isChecked) {
            emptyModel.urlRes = getString(R.string.hotel_url_no_indonesian_review)
            emptyModel.title = getString(R.string.hotel_review_indonesia_not_found_title)
            emptyModel.content = getString(R.string.hotel_review_indonesia_not_found_subtitle)
        } else {
            emptyModel.urlRes = getString(R.string.hotel_url_no_review)
            emptyModel.title = getString(R.string.hotel_review_filter_review_not_found_title)
            emptyModel.content = getString(R.string.hotel_review_filter_review_not_found_subtitle)
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

        fun createInstance(propertyId: Long): HotelReviewFragment {
            return HotelReviewFragment().also {
                it.arguments = Bundle().apply {
                    putLong(ARG_PROPERTY_ID, propertyId)
                }
            }
        }
    }

}