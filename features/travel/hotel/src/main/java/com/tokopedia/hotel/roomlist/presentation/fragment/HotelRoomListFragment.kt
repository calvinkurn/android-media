package com.tokopedia.hotel.roomlist.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.widget.filterchips.FilterChipAdapter
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelErrorException
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.MutationAddToCart
import com.tokopedia.hotel.common.util.QueryHotelPropertyRoomList
import com.tokopedia.hotel.databinding.FragmentHotelRoomListBinding
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
import com.tokopedia.imagepreviewslider.presentation.view.ImagePreviewViewer
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.resources.common.R as resourcescommonR

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListFragment : BaseListFragment<HotelRoom, RoomListTypeFactory>(), FilterChipAdapter.OnClickListener,
        HotelRoomAndGuestBottomSheets.HotelGuestListener, BaseEmptyViewHolder.Callback,
        RoomListViewHolder.OnClickBookListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var roomListViewModel: HotelRoomListViewModel

    private var binding by autoClearedNullable<FragmentHotelRoomListBinding>()

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    var hotelRoomListPageModel = HotelRoomListPageModel()
    var roomList: List<HotelRoom> = listOf()

    var firstTime = true

    lateinit var progressDialog: ProgressDialog

    private lateinit var remoteConfig: RemoteConfig

    private var imagePreviewViewer: ImagePreviewViewer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
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
            hotelRoomListPageModel.checkInDateFmt = hotelRoomListPageModel.checkIn.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)
            hotelRoomListPageModel.checkOutDateFmt = hotelRoomListPageModel.checkOut.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)
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
                                    getString(resourcescommonR.string.general_label_ok)).show()
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
        binding = FragmentHotelRoomListBinding.inflate(inflater, container, false)
        return binding?.root
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
        binding?.let {
            it.filterRecyclerView.listener = this
            it.filterRecyclerView.setItem(arrayListOf(getString(R.string.hotel_room_list_filter_free_breakfast),
                getString(R.string.hotel_room_list_filter_free_cancelable)),
                unifyprinciplesR.color.Unify_GN300)

            it.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)

                    val itemPosition = parent.getChildLayoutPosition(view)
                    val itemCount = state.getItemCount()
                    outRect.bottom = if (itemCount > ITEM_COUNT_NUll && itemPosition == itemCount - ITEM_COUNT_NOT_EMPTY) ITEM_COUNT_FULL else ITEM_COUNT_NUll
                }
            })

            it.hotelDateAndGuest.hotelRoomAndGuestLayout.setOnClickListener { onGuestInfoClicked() }
            it.hotelDateAndGuest.hotelDateLayout.setOnClickListener { onDateClicked() }
        }
    }

    private fun showFilterRecyclerView(show: Boolean) {
        binding?.filterRecyclerView?.visibility = if (show) View.VISIBLE else View.GONE
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
        hotelRoomListPageModel.checkIn = newCheckInDate.toString(DateUtil.YYYY_MM_DD)
        hotelRoomListPageModel.checkInDateFmt = newCheckInDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)

        if (newCheckInDate >= hotelRoomListPageModel.checkOut.toDate(DateUtil.YYYY_MM_DD)) {
            val tomorrow = newCheckInDate.addTimeToSpesificDate(Calendar.DATE, 1)
            hotelRoomListPageModel.checkOut = tomorrow.toString(DateUtil.YYYY_MM_DD)
            hotelRoomListPageModel.checkOutDateFmt = tomorrow.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        }
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        hotelRoomListPageModel.checkOut = newCheckOutDate.toString(DateUtil.YYYY_MM_DD)
        hotelRoomListPageModel.checkOutDateFmt = newCheckOutDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
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
            roomListViewModel.getRoomList(QueryHotelPropertyRoomList(),
                    hotelRoomListPageModel, false)
        } else roomListViewModel.getRoomList(QueryHotelPropertyRoomList(),
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
        val minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY
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
        binding?.let {
            it.hotelDateAndGuest.totalRoomTextView.text = hotelRoomListPageModel.room.toString()
            it.hotelDateAndGuest.totalGuestTextView.text = hotelRoomListPageModel.adult.toString()
        }
    }

    fun renderDate() {
        binding?.hotelDateAndGuest?.dateTextView?.text = getString(R.string.hotel_room_list_check_in_check_out_date,
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
        binding?.filterRecyclerView?.onResetChip()
        roomListViewModel.clearFilter()
    }

    override fun onClickBookListener(room: HotelRoom) {
        progressDialog.show()
        val hotelAddCartParam = mapToAddCartParam(hotelRoomListPageModel, room)
        trackingHotelUtil.hotelChooseRoom(context, room, hotelAddCartParam, ROOM_LIST_SCREEN_NAME)
        if (userSessionInterface.isLoggedIn) {
            roomListViewModel.addToCart(MutationAddToCart(), hotelAddCartParam)
        } else {
            navigateToLoginPage()
        }
    }

    override fun onPhotoClickListener(imageView: ImageView, room: HotelRoom, imageUrls: List<String>, position: Int) {
        trackingHotelUtil.hotelClickRoomListPhoto(context, room.additionalPropertyInfo.propertyId, room.roomId,
                room.roomPrice.priceAmount.roundToLong().toString(), ROOM_LIST_SCREEN_NAME)
        if (imagePreviewViewer == null) imagePreviewViewer = ImagePreviewViewer()
        imagePreviewViewer?.startImagePreviewViewer(room.roomInfo.name, imageView, imageUrls, requireContext(), position)
    }

    private fun navigateToLoginPage() {
        if (activity != null) {
            progressDialog.dismiss()
            context?.let { startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQ_CODE_LOGIN) }
        }
    }

    private fun navigateToAddEmailPage() {
        RouteManager.route(context, ApplinkConstInternalUserPlatform.ADD_EMAIL)
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        adapter.errorNetworkModel.iconDrawableRes = ErrorHandlerHotel.getErrorImage(throwable)
        adapter.errorNetworkModel.errorMessage = ErrorHandlerHotel.getErrorTitle(context, throwable)
        adapter.errorNetworkModel.subErrorMessage = ErrorHandler.getErrorMessage(context, throwable)
        adapter.errorNetworkModel.onRetryListener = this
        adapter.showErrorNetwork()
    }

    override fun showGetListError(throwable: Throwable?) {
        binding?.containerError?.root?.visible()
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(this, throwable,
                    { onRetryClicked() }, it
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

    private fun navigateToAddPhonePage() {
        RouteManager.route(requireContext(), ApplinkConstInternalUserPlatform.ADD_PHONE)
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

    override fun onDestroy() {
        super.onDestroy()
        imagePreviewViewer = null
    }

    companion object {
        const val DELAY_ROOM_LIST_SHIMMERING: Long = 500

        const val RESULT_ROOM_DETAIL = 102
        const val REQ_CODE_LOGIN = 1345

        const val ITEM_COUNT_NUll = 0
        const val ITEM_COUNT_NOT_EMPTY = 1
        const val ITEM_COUNT_FULL = 20

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
