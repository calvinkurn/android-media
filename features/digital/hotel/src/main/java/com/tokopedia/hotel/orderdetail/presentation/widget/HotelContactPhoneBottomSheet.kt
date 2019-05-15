package com.tokopedia.hotel.orderdetail.presentation.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter

/**
 * @author by jessica on 15/05/19
 */

class HotelContactPhoneBottomSheet: BottomSheets(){

    private lateinit var recyclerView: RecyclerView

    lateinit var contactList: List<HotelTransportDetail.ContactInfo>
    lateinit var listener: ContactAdapter.OnClickCallListener

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_contact_phone

    override fun initView(view: View) {
        view.minimumHeight = 0
        recyclerView = view.findViewById(R.id.recycler_view)

        val contactAdapter = ContactAdapter(contactList, listener)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = contactAdapter
    }

    override fun title(): String = "Kontak Hotel"

}