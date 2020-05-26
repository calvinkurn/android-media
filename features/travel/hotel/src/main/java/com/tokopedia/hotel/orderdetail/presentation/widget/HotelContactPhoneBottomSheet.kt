package com.tokopedia.hotel.orderdetail.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by jessica on 15/05/19
 */

class HotelContactPhoneBottomSheet : BottomSheetUnify() {

    private lateinit var recyclerView: RecyclerView

    lateinit var contactList: List<HotelTransportDetail.ContactInfo>
    lateinit var listener: ContactAdapter.OnClickCallListener

    lateinit var contactAdapter: ContactAdapter

    init {
        isFullpage = false
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_contact_phone, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)

        contactAdapter = ContactAdapter(contactList, listener)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = contactAdapter

        setTitle(getString(R.string.hotel_order_detail_contact_phone_sheet_title))
    }
}