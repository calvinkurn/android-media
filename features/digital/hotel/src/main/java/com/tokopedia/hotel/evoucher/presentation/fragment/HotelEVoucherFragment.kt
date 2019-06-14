package com.tokopedia.hotel.evoucher.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.evoucher.di.HotelEVoucherComponent
import com.tokopedia.hotel.evoucher.presentation.viewmodel.HotelEVoucherViewModel
import com.tokopedia.hotel.evoucher.presentation.widget.HotelSharePdfBottomSheets
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_e_voucher.*
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


/**
 * @author by furqan on 14/05/19
 */
class HotelEVoucherFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var eVoucherViewModel: HotelEVoucherViewModel

    lateinit var orderId: String

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            eVoucherViewModel = viewModelProvider.get(HotelEVoucherViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eVoucherViewModel.orderDetailData.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderData(it.data)
                }
                is Fail -> {
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_ORDER_ID, orderId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_hotel_e_voucher, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = savedInstanceState ?: arguments!!
        orderId = args.getString(EXTRA_ORDER_ID, "")
        eVoucherViewModel.getOrderDetail(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_hotel_order_list_detail), orderId)
    }

    override fun initInjector() = getComponent(HotelEVoucherComponent::class.java).inject(this)

    fun takeScreenshot() {
        val bitmap = getScreenBitmap()
        shareImageUri(saveImage(bitmap))
    }

    private fun getScreenBitmap(): Bitmap? {
        val v = container_root

        v.measure(View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)

        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(0, 0, v.width, v.height)
        v.draw(c)
        return b
    }

    private fun saveImage(bitmap: Bitmap?): Uri? {
        var uri: Uri? = null
        if (bitmap != null) {
            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File(getString(R.string.hotel_share_folder_name, root))
            myDir.mkdirs()
            val currentTime = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.getCurrentCalendar().time)
            val filename = getString(R.string.hotel_share_file_name, currentTime)
            val file = File(myDir, filename)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                uri = Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return uri
    }

    private fun shareImageUri(uri: Uri?) {
        if (uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivity(intent)
        }
    }

    fun shareAsPdf() {
        val shareAsPdfBottomSheets = HotelSharePdfBottomSheets()
        shareAsPdfBottomSheets.show(activity!!.supportFragmentManager, TAG_SHARE_AS_PDF)
    }

    private fun renderData(data: HotelOrderDetail) {

            tv_guest_title.text = data.hotelTransportDetails.guestDetail.title
            tv_guest_name.text = data.hotelTransportDetails.guestDetail.content

            if (data.hotelTransportDetails.propertyDetail.isNotEmpty()) {
                val propertyDetail = data.hotelTransportDetails.propertyDetail[0]

                tv_property_name.text = propertyDetail.propertyInfo.name
                tv_property_address.text = propertyDetail.propertyInfo.address

                rdv_checkin_checkout_date.setRoomDatesFormatted(
                        propertyDetail.checkInOut[0].checkInOut.date,
                        propertyDetail.checkInOut[1].checkInOut.date,
                        propertyDetail.stayLength.content)

                for (i in 1..propertyDetail.propertyInfo.starRating) {
                    container_rating_view.addView(RatingStarView(context!!))
                }

                tv_booking_title.text = propertyDetail.bookingKey.title
                tv_booking_code.text = propertyDetail.bookingKey.content

                if (propertyDetail.room.isNotEmpty()) {
                    tv_room_title.text = propertyDetail.room[0].title
                    tv_room_info.text = propertyDetail.room[0].content

                    var amenitiesString = ""
                    for ((index, item) in propertyDetail.room[0].amenities.withIndex()) {
                        amenitiesString += item.content
                        if (index < propertyDetail.room[0].amenities.size - 1) amenitiesString += ", "
                    }

                    tv_room_facility.text = amenitiesString
                }

                tv_additional_notes.text = propertyDetail.extraInfo

                tv_request_label.text = propertyDetail.specialRequest.title
                tv_request_info.text = propertyDetail.specialRequest.content

            }

            var phoneString = ""
            for ((index, item) in data.hotelTransportDetails.contactInfo.withIndex()) {
                phoneString += item.number
                if (index < data.hotelTransportDetails.contactInfo.size - 1) phoneString += ", "
            }
            tv_property_phone.text = getString(R.string.hotel_e_voucher_phone, phoneString)

    }

    companion object {

        const val TAG_SHARE_AS_PDF = "TAG_SHARE_AS_PDF"
        const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"

        fun getInstance(orderId: String): HotelEVoucherFragment = HotelEVoucherFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_ORDER_ID, orderId)
            }
        }

    }
}