package com.tokopedia.hotel.roomlist.presentation.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.widget.hotelcalendar.HotelCalendarDialog
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity
import com.tokopedia.hotel.roomdetail.presentation.fragment.HotelRoomDetailFragment
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomDetailModel
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import com.tokopedia.hotel.roomlist.widget.ChipAdapter
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_hotel_room_list.*
import kotlinx.android.synthetic.main.layout_sticky_hotel_date_and_guest.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListFragment : BaseListFragment<HotelRoom, RoomListTypeFactory>(), ChipAdapter.OnClickListener,
        HotelRoomAndGuestBottomSheets.HotelGuestListener, BaseEmptyViewHolder.Callback,
        RoomListViewHolder.OnClickBookListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var roomListViewModel: HotelRoomListViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    var hotelRoomListPageModel = HotelRoomListPageModel()
    var roomList: List<HotelRoom> = listOf()

    var firstTime = true

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            roomListViewModel = viewModelProvider.get(HotelRoomListViewModel::class.java)
        }

        arguments?.let {
            hotelRoomListPageModel.propertyId = it.getInt(ARG_PROPERTY_ID, 0)
            hotelRoomListPageModel.propertyName = it.getString(ARG_PROPERTY_NAME, "")
            hotelRoomListPageModel.checkIn = it.getString(ARG_CHECK_IN, "")
            hotelRoomListPageModel.checkOut = it.getString(ARG_CHECK_OUT, "")
            hotelRoomListPageModel.adult = it.getInt(ARG_TOTAL_ADULT, 0)
            hotelRoomListPageModel.child = it.getInt(ARG_TOTAL_CHILDREN, 0)
            hotelRoomListPageModel.room = it.getInt(ARG_TOTAL_ROOM, 0)
            hotelRoomListPageModel.checkInDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                    TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelRoomListPageModel.checkIn))
            hotelRoomListPageModel.checkOutDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                    TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelRoomListPageModel.checkOut))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        roomListViewModel.roomListResult.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Success -> {
                    firstTime = false
                    if (!roomListViewModel.isFilter) {
                        roomListViewModel.roomList = it.data
                        showFilterRecyclerView(it.data.size > 0)
                    } else showFilterRecyclerView(true)
                    clearAllData()
                    trackingHotelUtil.hotelViewRoomList(hotelRoomListPageModel.propertyId)
                    roomList = it.data
                    renderList(roomList, false)
                }
                is Fail -> {
                    showGetListError(it.throwable)
                    showFilterRecyclerView(false)
                }
            }
        })

        roomListViewModel.addCartResponseResult.observe(this, android.arch.lifecycle.Observer {
            progressDialog.dismiss()
            when (it) {
                is Success -> {
                    startActivity(HotelBookingActivity.getCallingIntent(context!!,it.data.response.cartId))
                }
                is Fail -> {
                    NetworkErrorHelper.showRedSnackbar(activity, ErrorHandler.getErrorMessage(activity, it.throwable))
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_room_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_ROOM_LIST_MODEL)) {
            hotelRoomListPageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_ROOM_LIST_MODEL)!!
        }

        (activity as HotelRoomListActivity).updateTitle(hotelRoomListPageModel.propertyName)
        initView()
        renderRoomAndGuestView()
        renderDate()

        loadInitialData()

        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.hotel_progress_dialog_title))
        progressDialog.setCancelable(false)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun callInitialLoadAutomatically(): Boolean = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_HOTEL_ROOM_LIST_MODEL, hotelRoomListPageModel)
    }

    fun initView() {
        filter_recycler_view.listener = this
        filter_recycler_view.setItem(arrayListOf(getString(R.string.hotel_room_list_filter_free_breakfast),
                getString(R.string.hotel_room_list_filter_free_cancelable)),
                R.color.snackbar_border_normal)

        recycler_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                val itemPosition = parent.getChildLayoutPosition(view)
                val itemCount = state.getItemCount()
                outRect.bottom = if (itemCount > 0 && itemPosition == itemCount - 1) 20 else 0
            }
        })

        hotel_room_and_guest_layout.setOnClickListener { onGuestInfoClicked() }
        hotel_date_layout.setOnClickListener { onDateClicked() }
    }

    fun showFilterRecyclerView(show: Boolean) {
        filter_recycler_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onGuestInfoClicked() {
        val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
        hotelRoomAndGuestBottomSheets.listener = this
        hotelRoomAndGuestBottomSheets.roomCount = hotelRoomListPageModel.room
        hotelRoomAndGuestBottomSheets.adultCount = hotelRoomListPageModel.adult
        hotelRoomAndGuestBottomSheets.show(activity!!.supportFragmentManager, TAG_GUEST_INFO)
    }

    fun onDateClicked() {
        configAndRenderCheckInDate()
    }

    private fun configAndRenderCheckInDate() {
        openCalendarDialog()
    }

    private fun onCheckInDateChanged(newCheckInDate: Date) {
        hotelRoomListPageModel.checkIn = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, newCheckInDate)
        hotelRoomListPageModel.checkInDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, newCheckInDate)

        if (newCheckInDate >= TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelRoomListPageModel.checkOut)) {
            val tomorrow = TravelDateUtil.addTimeToSpesificDate(newCheckInDate,
                    Calendar.DATE, 1)
            hotelRoomListPageModel.checkOut = TravelDateUtil.dateToString(
                    TravelDateUtil.YYYY_MM_DD, tomorrow)
            hotelRoomListPageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                    TravelDateUtil.DEFAULT_VIEW_FORMAT, tomorrow)
        }
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        hotelRoomListPageModel.checkOut = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, newCheckOutDate)
        hotelRoomListPageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, newCheckOutDate)
        renderDate()
        loadInitialData()
    }

    override fun getAdapterTypeFactory(): RoomListTypeFactory {
        return RoomListTypeFactory(this, this)
    }

    override fun onItemClicked(room: HotelRoom) {
        trackingHotelUtil.hotelClickRoomDetails(hotelRoomListPageModel.propertyId, room.roomId, room.roomPrice.roomPrice)
        val objectId = System.currentTimeMillis().toString()
        SaveInstanceCacheManager(context!!, objectId).apply {
            val addCartParam = mapToAddCartParam(hotelRoomListPageModel, room)
            put(HotelRoomDetailFragment.EXTRA_ROOM_DATA, HotelRoomDetailModel(room, addCartParam))
        }
        startActivityForResult(HotelRoomDetailActivity.getCallingIntent(context!!, objectId, roomList.indexOf(room)), RESULT_ROOM_DETAIL)
    }

    fun mapToAddCartParam(hotelRoomListPageModel: HotelRoomListPageModel, room: HotelRoom): HotelAddCartParam {
        return HotelAddCartParam("", hotelRoomListPageModel.checkIn,
                hotelRoomListPageModel.checkOut, hotelRoomListPageModel.propertyId,
                listOf(HotelAddCartParam.Room(roomId = room.roomId, numOfRooms = room.roomQtyReqiured)),
                hotelRoomListPageModel.adult)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        showFilterRecyclerView(false)
        if (firstTime) {
            roomListViewModel.getRoomList(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list),
                    hotelRoomListPageModel, false)
        } else roomListViewModel.getRoomList(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list),
                hotelRoomListPageModel, true)
    }

    override fun onChipClickListener(string: String, isSelected: Boolean) {
        when (string) {
            getString(R.string.hotel_room_list_filter_free_breakfast) -> {
                roomListViewModel.clickFilter(clickFreeBreakfast = true)
            }
            getString(R.string.hotel_room_list_filter_free_cancelable) -> {
                roomListViewModel.clickFilter(clickFreeCancelable = true)
            }
        }
    }

    private fun openCalendarDialog(selectedDate: Date? = null) {
        val hotelCalendarDialog = HotelCalendarDialog.getInstance(hotelRoomListPageModel.checkIn, hotelRoomListPageModel.checkOut)
        hotelCalendarDialog.listener = object : HotelCalendarDialog.OnDateClickListener{
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                onCheckInDateChanged(dateIn)
                onCheckOutDateChanged(dateOut)
            }
        }
        hotelCalendarDialog.show(fragmentManager, "test")
    }

    override fun onSaveGuest(room: Int, adult: Int) {
        hotelRoomListPageModel.room = room
        hotelRoomListPageModel.adult = adult

        renderRoomAndGuestView()
        loadInitialData()
    }

    fun renderRoomAndGuestView() {
        total_room_text_view.text = hotelRoomListPageModel.room.toString()
        total_guest_text_view.text = hotelRoomListPageModel.adult.toString()
    }

    fun renderDate() {
        date_text_view.text = getString(R.string.hotel_room_list_check_in_check_out_date,
                hotelRoomListPageModel.checkInDateFmt, hotelRoomListPageModel.checkOutDateFmt)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        var emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_room_listing
        if (roomListViewModel.isFilter) {
            emptyModel.title = getString(R.string.hotel_room_not_found_title_after_filter)
            emptyModel.content = getString(R.string.hotel_room_not_found_subtitle_after_filter)
            emptyModel.buttonTitle = getString(R.string.hotel_room_not_found_button_text)
        } else {
            emptyModel.title = getString(R.string.hotel_room_not_found_title)
            emptyModel.content = getString(R.string.hotel_room_not_found_subtitle)
        }
        return emptyModel
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {
        //DELETE FILTER
        filter_recycler_view.onResetChip()
        roomListViewModel.clearFilter()
    }

    override fun onClickBookListener(room: HotelRoom) {
        progressDialog.show()
        trackingHotelUtil.hotelChooseRoom(room, roomList.indexOf(room))
        if (userSessionInterface.isLoggedIn) {
            roomListViewModel.addToCart(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_add_to_cart),
                    mapToAddCartParam(hotelRoomListPageModel, room))
        } else {
            goToLoginPage()
        }
    }

    override fun onPhotoClickListener(room: HotelRoom) {
        trackingHotelUtil.hotelClickRoomListPhoto(room.additionalPropertyInfo.propertyId, room.roomId, room.roomPrice.roomPrice)
    }

    fun goToLoginPage() {
        if (activity != null) {
            progressDialog.dismiss()
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        adapter.errorNetworkModel.iconDrawableRes = ErrorHandlerHotel.getErrorImage(throwable)
        adapter.errorNetworkModel.errorMessage = ErrorHandlerHotel.getErrorTitle(context, throwable)
        adapter.errorNetworkModel.subErrorMessage = ErrorHandlerHotel.getErrorMessage(context, throwable)
        adapter.errorNetworkModel.onRetryListener = this
        adapter.showErrorNetwork()
    }

    companion object {

        const val RESULT_ROOM_DETAIL = 102

        const val ARG_PROPERTY_ID = "arg_property_id"
        const val ARG_PROPERTY_NAME = "arg_property_name"
        const val ARG_CHECK_IN = "arg_check_in"
        const val ARG_CHECK_OUT = "arg_check_out"
        const val ARG_TOTAL_ROOM = "arg_total_room"
        const val ARG_TOTAL_ADULT = "arg_total_adult"
        const val ARG_TOTAL_CHILDREN = "arg_total_children"
        const val TAG_GUEST_INFO = "guestHotelInfo"
        const val EXTRA_HOTEL_ROOM_LIST_MODEL = "extra_room_list_model"

        fun createInstance(propertyId: Int = 0, propertyName: String = "", checkIn: String = "", checkOut: String = "",
                           totalAdult: Int = 0, totalChildren: Int = 0, totalRoom: Int = 0): HotelRoomListFragment {

            return HotelRoomListFragment().also {
                it.arguments = Bundle().apply {
                    putInt(ARG_PROPERTY_ID, propertyId)
                    putString(ARG_PROPERTY_NAME, propertyName)
                    putString(ARG_CHECK_IN, checkIn)
                    putString(ARG_CHECK_OUT, checkOut)
                    putInt(ARG_TOTAL_ROOM, totalRoom)
                    putInt(ARG_TOTAL_ADULT, totalAdult)
                    putInt(ARG_TOTAL_CHILDREN, totalChildren)
                }
            }
        }
    }

}