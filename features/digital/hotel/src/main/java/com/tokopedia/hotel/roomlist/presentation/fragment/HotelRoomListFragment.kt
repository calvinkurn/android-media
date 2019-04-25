package com.tokopedia.hotel.roomlist.presentation.fragment

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
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import com.tokopedia.hotel.roomlist.widget.ChipAdapter
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_room_list.*
import kotlinx.android.synthetic.main.layout_sticky_hotel_date_and_guest.*
import kotlinx.android.synthetic.main.widget_filter_chip_recycler_view.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListFragment: BaseListFragment<HotelRoom, RoomListTypeFactory>(), ChipAdapter.OnClickListener,
        HotelRoomAndGuestBottomSheets.HotelGuestListener, BaseEmptyViewHolder.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var roomListViewModel: HotelRoomListViewModel

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager

    var hotelRoomListPageModel = HotelRoomListPageModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saveInstanceCacheManager = SaveInstanceCacheManager(activity!!, savedInstanceState)

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

        roomListViewModel.roomListResult.observe(this, android.arch.lifecycle.Observer { when (it) {
            is Success -> {
                if (!roomListViewModel.isFilter) {
                    roomListViewModel.roomList = it.data
                    showFilterRecyclerView(it.data.size > 0)
                } else showFilterRecyclerView(true)
                loadInitialData()
                renderList(it.data)
            }
            is Fail -> {
                showGetListError(it.throwable)
                showFilterRecyclerView(false)
            }
        } })
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

        getRoomList(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_HOTEL_ROOM_LIST_MODEL, hotelRoomListPageModel)
    }

    fun initView() {
        filter_recycler_view.listener = this
        filter_recycler_view.setItem(arrayListOf(getString(PAY_IN_HOTEL),
                getString(FREE_BREAKFAST), getString(FREE_CANCELABLE)),
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

        recycler_view.addItemDecoration(object: RecyclerView.ItemDecoration() {
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

    fun getRoomList(fromCloud: Boolean = true) {
        showFilterRecyclerView(false)
        roomListViewModel.getRoomList(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list), hotelRoomListPageModel, fromCloud)
    }

    fun onGuestInfoClicked() {
        val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
        hotelRoomAndGuestBottomSheets.listener = this
        hotelRoomAndGuestBottomSheets.roomCount = hotelRoomListPageModel.room
        hotelRoomAndGuestBottomSheets.adultCount = hotelRoomListPageModel.adult
        hotelRoomAndGuestBottomSheets.childCount = hotelRoomListPageModel.child
        hotelRoomAndGuestBottomSheets.show(activity!!.supportFragmentManager, TAG_GUEST_INFO)
    }

    fun onDateClicked() {
        configAndRenderCheckInDate()
    }

    private fun configAndRenderCheckInDate() {
        val minDate = TravelDateUtil.removeTime(TravelDateUtil.addTimeToSpesificDate(
                TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1))

        val maxDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time,
                Calendar.YEAR, 1)
        val maxDateCalendar = TravelDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_SEC_IN_DAY)
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_MIN_SEC_IN_DAY)

        val selectedDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelRoomListPageModel.checkIn)

        renderTravelCalendar(selectedDate, minDate, maxDateCalendar.time, getString(R.string.hotel_check_in_calendar_title), TAG_CALENDAR_CHECK_IN)
    }

    private fun configAndRenderCheckOutDate() {
        val minDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.stringToDate(
                TravelDateUtil.YYYY_MM_DD, hotelRoomListPageModel.checkIn), Calendar.DATE, 1)

        val maxDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD,
                hotelRoomListPageModel.checkIn), Calendar.DATE, MAX_SELECTION_DATE)
        val maxDateCalendar = TravelDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_SEC_IN_DAY)
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_MIN_SEC_IN_DAY)

        val selectedDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelRoomListPageModel.checkOut)

        renderTravelCalendar(selectedDate, minDate, maxDateCalendar.time, getString(R.string.hotel_check_out_calendar_title), TAG_CALENDAR_CHECK_OUT)
    }

    private fun renderTravelCalendar(selectedDate: Date, minDate: Date, maxDate: Date, title: String, calendarTag: String) {
        val travelCalendarBottomSheet = TravelCalendarBottomSheet.Builder()
                .setMinDate(minDate)
                .setMaxDate(maxDate)
                .setSelectedDate(selectedDate)
                .setShowHoliday(true)
                .setTitle(title)
                .build()
        travelCalendarBottomSheet.setListener(object : TravelCalendarBottomSheet.ActionListener {
            override fun onClickDate(dateSelected: Date) {
                val calendarSelected = Calendar.getInstance()
                calendarSelected.time = dateSelected
                if (calendarTag == TAG_CALENDAR_CHECK_IN) {
                    onCheckInDateChanged(dateSelected)
                    configAndRenderCheckOutDate()
                } else if (calendarTag == TAG_CALENDAR_CHECK_OUT) {
                    onCheckOutDateChanged(dateSelected)
                }
            }
        })
        travelCalendarBottomSheet.show(activity!!.supportFragmentManager, calendarTag)
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
        getRoomList(true)
    }

    override fun getAdapterTypeFactory(): RoomListTypeFactory {
        return RoomListTypeFactory(this)
    }

    override fun onItemClicked(room: HotelRoom) {
        saveInstanceCacheManager.put(EXTRA_ROOM_DATA, room)
    }

    override fun getScreenName(): String = "Room List"

    override fun initInjector() {
        getComponent(HotelRoomListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {

    }

    override fun onChipClickListener(string: String) {
        when (string) {
            getString(FREE_BREAKFAST) -> { roomListViewModel.clickFilter(clickFreeBreakfast = true) }
            getString(FREE_CANCELABLE) -> { roomListViewModel.clickFilter(clickFreeCancelable = true) }
            getString(PAY_IN_HOTEL) -> { roomListViewModel.clickFilter(clickPayInHotel = true) }
        }
    }

    override fun onSaveGuest(room: Int, adult: Int, child: Int) {
        hotelRoomListPageModel.room = room
        hotelRoomListPageModel.adult = adult
        hotelRoomListPageModel.child = child

        renderRoomAndGuestView()
        getRoomList(true)
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

    override fun onEmptyContentItemTextClicked() { }

    override fun onEmptyButtonClicked() {
        //DELETE FILTER
        filter_recycler_view.resetChipSelected()
        roomListViewModel.clearFilter()
    }

    companion object {
        val FREE_BREAKFAST = R.string.hotel_room_list_filter_free_breakfast
        val FREE_CANCELABLE = R.string.hotel_room_list_filter_free_cancelable
        val PAY_IN_HOTEL = R.string.hotel_room_list_filter_pay_in_hotel

        const val ARG_PROPERTY_ID = "arg_property_id"
        const val ARG_PROPERTY_NAME = "arg_property_name"
        const val ARG_CHECK_IN = "arg_check_in"
        const val ARG_CHECK_OUT = "arg_check_out"
        const val ARG_TOTAL_ROOM = "arg_total_room"
        const val ARG_TOTAL_ADULT = "arg_total_adult"
        const val ARG_TOTAL_CHILDREN = "arg_total_children"
        const val TAG_GUEST_INFO = "guestHotelInfo"
        const val EXTRA_ROOM_DATA = "extra_room_data"
        const val EXTRA_HOTEL_ROOM_LIST_MODEL = "extra_room_list_model"

        val TAG_CALENDAR_CHECK_IN = "calendarHotelCheckIn"
        val TAG_CALENDAR_CHECK_OUT = "calendarHotelCheckOut"
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)
        val MAX_SELECTION_DATE = 30
        val DEFAULT_LAST_HOUR_IN_DAY = 23
        val DEFAULT_LAST_MIN_SEC_IN_DAY = 59

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