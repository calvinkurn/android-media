package com.tokopedia.hotel.roomlist.presentation.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import com.tokopedia.hotel.roomlist.widget.ChipAdapter
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_room_list.*
import kotlinx.android.synthetic.main.layout_sticky_hotel_date_and_guest.*
import kotlinx.android.synthetic.main.widget_filter_chip_recycler_view.view.*
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListFragment: BaseListFragment<HotelRoom, RoomListTypeFactory>(),
        ImageViewPager.ImageViewPagerListener, ChipAdapter.OnClickListener,
        HotelRoomAndGuestBottomSheets.HotelGuestListener {

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

        getRoomList(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        roomListViewModel.roomListResult.observe(this, android.arch.lifecycle.Observer { when (it) {
            is Success -> {
                if (!roomListViewModel.isFilter) roomListViewModel.roomList = it.data
                renderList(it.data)
            }
            is Fail -> { }
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

        initView()
        renderRoomAndGuestView()
        renderDate()
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

                outRect.left = if (itemPosition == 0) 8 else 0
                outRect.right = if (itemCount > 0 && itemPosition == itemCount - 1) 8 else 0
            }
        })

        hotel_room_and_guest_layout.setOnClickListener { onGuestInfoClicked() }
        hotel_date_layout.setOnClickListener { onDateClicked() }
    }

    fun getRoomList(fromCloud: Boolean = true) {
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

    override fun onImageClicked(position: Int) {

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
        date_text_view.text = "${hotelRoomListPageModel.checkInDateFmt} - ${hotelRoomListPageModel.checkOutDateFmt}"
    }

    companion object {
        val FREE_BREAKFAST = R.string.hotel_room_list_filter_free_breakfast
        val FREE_CANCELABLE = R.string.hotel_room_list_filter_free_cancelable
        val PAY_IN_HOTEL = R.string.hotel_room_list_filter_pay_in_hotel

        const val ARG_PROPERTY_ID = "arg_property_id"
        const val ARG_CHECK_IN = "arg_check_in"
        const val ARG_CHECK_OUT = "arg_check_out"
        const val ARG_TOTAL_ROOM = "arg_total_room"
        const val ARG_TOTAL_ADULT = "arg_total_adult"
        const val ARG_TOTAL_CHILDREN = "arg_total_children"

        fun createInstance(propertyId: Int = 0, checkIn: String = "", checkOut: String = "",
                           totalAdult: Int = 0, totalChildren: Int = 0, totalRoom: Int = 0): HotelRoomListFragment {

            return HotelRoomListFragment().also {
                it.arguments = Bundle().apply {
                    putInt(ARG_PROPERTY_ID, propertyId)
                    putString(ARG_CHECK_IN, checkIn)
                    putString(ARG_CHECK_OUT, checkOut)
                    putInt(ARG_TOTAL_ROOM, totalRoom)
                    putInt(ARG_TOTAL_ADULT, totalAdult)
                    putInt(ARG_TOTAL_CHILDREN, totalChildren)
                }
            }
        }

        val TAG_GUEST_INFO = "guestHotelInfo"

        const val EXTRA_ROOM_DATA = "extra_room_data"
        const val EXTRA_HOTEL_ROOM_LIST_MODEL = "extra_room_list_model"
    }

}