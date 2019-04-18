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
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.di.HotelRoomListComponent
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import com.tokopedia.hotel.roomlist.widget.ChipAdapter
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_room_list.view.*
import kotlinx.android.synthetic.main.widget_filter_chip_recycler_view.view.*
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListFragment: BaseListFragment<RoomListModel, RoomListTypeFactory>(),
        ImageViewPager.ImageViewPagerListener, ChipAdapter.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var roomListViewModel: HotelRoomListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            roomListViewModel = viewModelProvider.get(HotelRoomListViewModel::class.java)
        }

        roomListViewModel.initRoomListParam(2103, "2019-04-14", "2019-04-16", 2, 2, 1 )
        roomListViewModel.getRoomList(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list),
                GraphqlHelper.loadRawString(resources, R.raw.dummy_hotel_room_list))

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        roomListViewModel.roomListResult.observe(this, android.arch.lifecycle.Observer { when (it) {
            is Success -> renderList(it.data)
            is Fail -> { }
        } })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_room_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.filter_recycler_view.listener = this
        view.filter_recycler_view.setItem(arrayListOf("Text Panjangggg", "Text1 ini juga panjang kook", "Ini Text Panjang Banget"))
        view.filter_recycler_view.chip_recycler_view.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)

                val itemPosition = parent.getChildLayoutPosition(view)
                val itemCount = state.getItemCount()

                outRect.left = if (itemPosition == 0) 8 else 0
                outRect.right = if (itemCount > 0 && itemPosition == itemCount - 1) 8 else 0
            }
        })
    }

    override fun getAdapterTypeFactory(): RoomListTypeFactory {
        return RoomListTypeFactory(this)
    }

    override fun onItemClicked(t: RoomListModel?) {
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

    }
}