package com.tokopedia.hotel.destination.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_ID
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_NAME
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_TYPE
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationListener
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationTypeFactory
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.hotel.destination.view.viewmodel.Loaded
import com.tokopedia.hotel.destination.view.viewmodel.Shimmering
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_hotel_destination.*
import javax.inject.Inject

/**
 * @author by jessica on 27/03/19
 */

class HotelSearchDestinationFragment: BaseListFragment<SearchDestination, SearchDestinationTypeFactory>(),
SearchDestinationListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        destinationViewModel.searchDestination.observe(this, android.arch.lifecycle.Observer { when (it) {
            is Loaded -> {
                when (it.data) {
                    is Success -> {
                        isLoadingInitialData = true
                        renderList(it.data.data, false)
                    }
                    is Fail -> {
                        showGetListError(it.data.throwable)
                    }
                }
            }
            is Shimmering -> {
                adapter.clearAllNonDataElement()
                adapter.addElement(loadingModel)
            }
        } })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_search_destination, container, false)
        return view
    }

    override fun getAdapterTypeFactory(): SearchDestinationTypeFactory {
        return SearchDestinationTypeFactory(this)
    }

    override fun onItemClicked(searchDestination: SearchDestination) {
        val intent = Intent()
        intent.putExtra(HOTEL_DESTINATION_NAME, searchDestination.name)
        intent.putExtra(HOTEL_DESTINATION_ID, searchDestination.id)
        intent.putExtra(HOTEL_DESTINATION_TYPE, searchDestination.type)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelDestinationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        isLoadingInitialData = true
        onSearchQueryChange(getFilterText())
    }

    override fun getFilterText(): String {
        return (activity as HotelDestinationActivity).search_input_view.searchText
    }

    fun onSearchQueryChange(keyword: String) {
        destinationViewModel.getHotelSearchDestination(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_destination_search), keyword)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        var emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_no_match_search_suggestion
        emptyModel.title = getString(R.string.hotel_destination_empty_view_holder_title)
        emptyModel.content = getString(R.string.hotel_destination_empty_view_holder_subtitle)

        return emptyModel
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        adapter.errorNetworkModel.iconDrawableRes = ErrorHandlerHotel.getErrorImage(throwable)
        adapter.errorNetworkModel.errorMessage = ErrorHandlerHotel.getErrorTitle(context, throwable)
        adapter.errorNetworkModel.subErrorMessage = ErrorHandlerHotel.getErrorMessage(context, throwable)
        adapter.errorNetworkModel.onRetryListener = this
        adapter.showErrorNetwork()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = false

}