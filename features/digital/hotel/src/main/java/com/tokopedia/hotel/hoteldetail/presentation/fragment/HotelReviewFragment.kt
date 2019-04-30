package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelReviewActivity
import com.tokopedia.hotel.hoteldetail.presentation.adapter.ReviewAdapterTypeFactory
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReviewViewModel
import com.tokopedia.hotel.roomlist.widget.ChipAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_review.*
import kotlinx.android.synthetic.main.widget_filter_chip_recycler_view.view.*
import javax.inject.Inject

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewFragment: BaseListFragment<HotelReview, ReviewAdapterTypeFactory>(), ChipAdapter.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var param: HotelReviewParam = HotelReviewParam()
    lateinit var reviewViewModel: HotelReviewViewModel

    init {
        param.rows = 5
        param.sortBy = "create_time"
        param.sortType = "asc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param.propertyId = it.getInt(ARG_PROPERTY_ID, 0)
        }

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        reviewViewModel = viewModelProvider.get(HotelReviewViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reviewViewModel.reviewResult.observe(this, Observer {
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

    fun onSuccessGetResult(reviews: HotelReview.ReviewData) {
        super.renderList(reviews.reviewList)
        showHotelMetaReview(reviews.reviewList.size > 0)
        if (currentPage == 1) {
            review_point_text_view.text = reviews.averageScoreReview.toString()
            review_headline_text.text = "Mengesankan"
            review_total_count_text.text = "${reviews.totalReview} Ulasan"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as HotelReviewActivity).updateTitle(screenName)
        initFilterView()
    }

    fun initFilterView() {
        filter_recycler_view.listener = this
        filter_recycler_view.setItem(arrayListOf(RECENT_REVIEW,
                HIGHEST_RATING, LOWEST_RATING),
                R.color.snackbar_border_normal)
        filter_recycler_view.chip_recycler_view.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                val itemPosition = parent.getChildLayoutPosition(view)
                val itemCount = state.getItemCount()

                outRect.left = if (itemPosition == 0) 20 else 0
                outRect.right = if (itemCount > 0 && itemPosition == itemCount - 1) 20 else 0
            }
        })
    }

    private fun onErrorGetResult(throwable: Throwable) {
        super.showGetListError(throwable)
        showHotelMetaReview(false)
    }

    override fun getAdapterTypeFactory(): ReviewAdapterTypeFactory = ReviewAdapterTypeFactory()

    override fun onItemClicked(t: HotelReview?) {
    }

    override fun getScreenName(): String = "Ulasan"

    override fun initInjector() {
        getComponent(HotelDetailComponent::class.java).inject(this)
    }

    fun showHotelMetaReview(visible: Boolean) {
        app_bar_layout.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun loadData(page: Int) {
        showHotelMetaReview(false)
        param.page = page
        reviewViewModel.getReview(GraphqlHelper.loadRawString(resources, R.raw.gql_get_hotel_review), param,
                GraphqlHelper.loadRawString(resources, R.raw.dummy_hotel_review))
    }

    override fun onChipClickListener(string: String) {
        when (string) {
            RECENT_REVIEW -> {}
            HIGHEST_RATING -> {}
            LOWEST_RATING -> {}
        }
    }

    companion object {
        const val RECENT_REVIEW = "Ulasan Terbaru"
        const val HIGHEST_RATING = "Rating Tertinggi"
        const val LOWEST_RATING = "Rating Terendah"

        const val ARG_PROPERTY_ID = "arg_property_id"

        fun createInstance(propertyId: Int): HotelReviewFragment {
            return HotelReviewFragment().also {
                it.arguments = Bundle().apply {
                    putInt(ARG_PROPERTY_ID, propertyId)
                }
            }
        }
    }

}