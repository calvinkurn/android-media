package com.tokopedia.hotel.orderdetail.presentation.widget

import android.app.Dialog
import android.os.Bundle
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

    lateinit var contactAdapter: ContactAdapter

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_contact_phone

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)

        contactAdapter = ContactAdapter(contactList, listener)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = contactAdapter
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }


    override fun title(): String = getString(R.string.hotel_order_detail_contact_phone_sheet_title)
}