package com.tokopedia.hotel.roomlist.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelErrorException
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelGqlMutation
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity
import com.tokopedia.hotel.roomdetail.presentation.fragment.HotelRoomDetailFragment
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomDetailModel
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity.Companion.ROOM_LIST_SCREEN_NAME
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_hotel_room_list.*
import kotlinx.android.synthetic.main.layout_sticky_hotel_date_and_guest.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListFragment : BaseListFragment<HotelRoom, RoomListTypeFactory>(), FilterChipAdapter.OnClickListener,
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

    private lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            roomListViewModel = viewModelProvider.get(HotelRoomListViewModel::class.java)
        }

        arguments?.let {
            hotelRoomListPageModel.propertyId = it.getLong(ARG_PROPERTY_ID, 0)
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
            hotelRoomListPageModel.destinationName = it.getString(ARG_DESTINATION_NAME, "")
            hotelRoomListPageModel.destinationType = it.getString(ARG_DESTINATION_TYPE, "")
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        roomListViewModel.roomListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    if (!roomListViewModel.isFilter) {
                        roomListViewModel.roomList = it.data
                        showFilterRecyclerView(it.data.size > 0)
                    } else showFilterRecyclerView(true)

                    clearAllData()
                    roomList = it.data
                    renderList(roomList, false)
                    if (firstTime) {
                        firstTime = false
                        trackingHotelUtil.hotelViewRoomList(context, hotelRoomListPageModel.propertyId,
                                hotelRoomListPageModel, it.data, ROOM_LIST_SCREEN_NAME)
                    }
                }
                is Fail -> {
                    showGetListError(it.throwable)
                    showFilterRecyclerView(false)
                }
            }
        })

        roomListViewModel.addCartResponseResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            progressDialog.dismiss()
            when (it) {
                is Success -> {
                    context?.run {
                        startActivity(HotelBookingActivity.getCallingIntent(this, it.data.response.cartId))
                    }
                }
                is Fail -> {
                    when {
                        ErrorHandlerHotel.isPhoneNotVerfiedError(it.throwable) -> navigateToAddPhonePage()
                        ErrorHandlerHotel.isGetFailedRoomError(it.throwable) -> showFailedGetRoomErrorDialog((it.throwable as HotelErrorException).message)
                        ErrorHandlerHotel.isEmailNotVerifiedError(it.throwable) -> navigateToAddEmailPage()
                        else -> view?.let { v ->
                            Toaster.build(v, ErrorHandler.getErrorMessage(activity, it.throwable), Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                        }
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESULT_ROOM_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                loadInitialData()
            }
            REQ_CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                loadInitialData()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_room_list, container, false)
        return view
    }

    override fun getSwipeRefreshLayoutResourceId() = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_ROOM_LIST_MODEL)) {
            hotelRoomListPageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_ROOM_LIST_MODEL)
                    ?: HotelRoomListPageModel()
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
                com.tokopedia.unifyprinciples.R.color.Unify_G300)

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

    private fun showFilterRecyclerView(show: Boolean) {
        filter_recycler_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun onGuestInfoClicked() {
        val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
        hotelRoomAndGuestBottomSheets.listener = this
        hotelRoomAndGuestBottomSheets.roomCount = hotelRoomListPageModel.room
        hotelRoomAndGuestBottomSheets.adultCount = hotelRoomListPageModel.adult
        activity?.let {
            hotelRoomAndGuestBottomSheets.show(it.supportFragmentManager, TAG_GUEST_INFO)
        }
    }

    private fun onDateClicked() {
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
        val position = roomList.indexOf(room)
        trackingHotelUtil.hotelClickRoomDetails(context, room, hotelRoomListPageModel, position, ROOM_LIST_SCREEN_NAME)
        if (room.available) {
            val objectId = System.currentTimeMillis().toString()
            context?.run {
                SaveInstanceCacheManager(this, objectId).apply {
                    val addCartParam = mapToAddCartParam(hotelRoomListPageModel, room)
                    put(HotelRoomDetailFragment.EXTRA_ROOM_DATA, HotelRoomDetailModel(room, addCartParam))
                }
                startActivityForResult(HotelRoomDetailActivity.getCallingIntent(this, objectId, position), RESULT_ROOM_DETAIL)
            }
        }
    }

    private fun mapToAddCartParam(hotelRoomListPageModel: HotelRoomListPageModel, room: HotelRoom): HotelAddCartParam {
        return HotelAddCartParam("", hotelRoomListPageModel.checkIn,
                hotelRoomListPageModel.checkOut, hotelRoomListPageModel.propertyId,
                listOf(HotelAddCartParam.Room(roomId = room.roomId, numOfRooms = room.roomQtyReqiured)),
                hotelRoomListPageModel.room, hotelRoomListPageModel.adult, hotelRoomListPageModel.destinationType,
                hotelRoomListPageModel.destinationName)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        showFilterRecyclerView(false)
        if (firstTime) {
            roomListViewModel.getRoomList(HotelGqlQuery.PROPERTY_ROOM_LIST,
                    hotelRoomListPageModel, false)
        } else roomListViewModel.getRoomList(HotelGqlQuery.PROPERTY_ROOM_LIST,
                hotelRoomListPageModel, true)
    }

    override fun onChipClickListener(string: String, isSelected: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            clearAllData()
            showLoading()

            delay(DELAY_ROOM_LIST_SHIMMERING)

            when (string) {
                getString(R.string.hotel_room_list_filter_free_breakfast) -> {
                    roomListViewModel.clickFilter(clickFreeBreakfast = true)
                }
                getString(R.string.hotel_room_list_filter_free_cancelable) -> {
                    roomListViewModel.clickFilter(clickFreeCancelable = true)
                }
            }
        }
    }

    private fun openCalendarDialog(selectedDate: Date? = null) {
        var minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY
        if (!(remoteConfig.getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_BOOK_FOR_TODAY, true))) minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_PLUS_1_DAY

        val hotelCalendarDialog = SelectionRangeCalendarWidget.getInstance(hotelRoomListPageModel.checkIn,
                hotelRoomListPageModel.checkOut, SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED_ONE_MONTH.toLong(),
                getString(R.string.hotel_min_date_label), getString(R.string.hotel_max_date_label), minSelectDateFromToday)

        hotelCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                onCheckInDateChanged(dateIn)
                onCheckOutDateChanged(dateOut)
            }
        }
        hotelCalendarDialog.listenerMaxRange = object : SelectionRangeCalendarWidget.OnNotifyMaxRange {
            override fun onNotifyMax() {
                Toast.makeText(context, R.string.hotel_calendar_error_max_range, Toast.LENGTH_SHORT).show()
            }
        }
        fragmentManager?.let { hotelCalendarDialog.show(it, "test") }
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
        emptyModel.urlRes = getString(R.string.hotel_url_empty_room_listing)
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
        val hotelAddCartParam = mapToAddCartParam(hotelRoomListPageModel, room)
        trackingHotelUtil.hotelChooseRoom(context, room, hotelAddCartParam, ROOM_LIST_SCREEN_NAME)
        if (userSessionInterface.isLoggedIn) {
            roomListViewModel.addToCart(HotelGqlMutation.ADD_TO_CART,
                    hotelAddCartParam)
        } else {
            navigateToLoginPage()
        }
    }

    override fun onPhotoClickListener(room: HotelRoom) {
        trackingHotelUtil.hotelClickRoomListPhoto(context, room.additionalPropertyInfo.propertyId, room.roomId,
                room.roomPrice.priceAmount.roundToLong().toString(), ROOM_LIST_SCREEN_NAME)
    }

    private fun navigateToLoginPage() {
        if (activity != null) {
            progressDialog.dismiss()
            context?.let { startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQ_CODE_LOGIN) }
        }
    }

    private fun navigateToAddEmailPage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.ADD_EMAIL)
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        adapter.errorNetworkModel.iconDrawableRes = ErrorHandlerHotel.getErrorImage(throwable)
        adapter.errorNetworkModel.errorMessage = ErrorHandlerHotel.getErrorTitle(context, throwable)
        adapter.errorNetworkModel.subErrorMessage = ErrorHandlerHotel.getErrorMessage(context, throwable)
        adapter.errorNetworkModel.onRetryListener = this
        adapter.showErrorNetwork()
    }

    private fun navigateToAddPhonePage() {
        RouteManager.route(requireContext(), ApplinkConstInternalGlobal.ADD_PHONE)
    }

    private fun showFailedGetRoomErrorDialog(message: String) {
        val dialog = DialogUnify(activity as AppCompatActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
        dialog.setTitle(getString(R.string.hotel_room_list_failed_get_room_availability_error_title))
        dialog.setDescription(message)
        dialog.setImageDrawable(R.drawable.ic_hotel_room_error_refresh)
        dialog.setPrimaryCTAText(getString(R.string.hotel_room_list_failed_get_room_availability_cta_title))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            loadInitialData()
        }
        dialog.setCancelable(false)
        dialog.setOverlayClose(false)
        dialog.show()
    }

    companion object {
        const val DELAY_ROOM_LIST_SHIMMERING: Long = 500

        const val RESULT_ROOM_DETAIL = 102
        const val REQ_CODE_LOGIN = 1345

        const val ARG_PROPERTY_ID = "arg_property_id"
        const val ARG_PROPERTY_NAME = "arg_property_name"
        const val ARG_CHECK_IN = "arg_check_in"
        const val ARG_CHECK_OUT = "arg_check_out"
        const val ARG_TOTAL_ROOM = "arg_total_room"
        const val ARG_TOTAL_ADULT = "arg_total_adult"
        const val ARG_TOTAL_CHILDREN = "arg_total_children"
        const val ARG_DESTINATION_TYPE = "arg_destination_type"
        const val ARG_DESTINATION_NAME = "arg_destination_name"
        const val TAG_GUEST_INFO = "guestHotelInfo"
        const val EXTRA_HOTEL_ROOM_LIST_MODEL = "extra_room_list_model"

        fun createInstance(propertyId: Long = 0, propertyName: String = "", checkIn: String = "", checkOut: String = "",
                           totalAdult: Int = 0, totalChildren: Int = 0, totalRoom: Int = 0,
                           destinationType: String, destinationName: String): HotelRoomListFragment {

            return HotelRoomListFragment().also {
                it.arguments = Bundle().apply {
                    putLong(ARG_PROPERTY_ID, propertyId)
                    putString(ARG_PROPERTY_NAME, propertyName)
                    putString(ARG_CHECK_IN, checkIn)
                    putString(ARG_CHECK_OUT, checkOut)
                    putInt(ARG_TOTAL_ROOM, totalRoom)
                    putInt(ARG_TOTAL_ADULT, totalAdult)
                    putInt(ARG_TOTAL_CHILDREN, totalChildren)
                    putString(ARG_DESTINATION_TYPE, destinationType)
                    putString(ARG_DESTINATION_NAME, destinationName)
                }
            }
        }
    }

}