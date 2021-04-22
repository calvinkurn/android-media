package com.tokopedia.hotel.evoucher.presentation.fragment

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.common.util.HotelGqlMutation
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.evoucher.di.HotelEVoucherComponent
import com.tokopedia.hotel.evoucher.presentation.adapter.HotelEVoucherCancellationPoliciesAdapter
import com.tokopedia.hotel.evoucher.presentation.viewmodel.HotelEVoucherViewModel
import com.tokopedia.hotel.evoucher.presentation.widget.HotelSharePdfBottomSheets
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
import kotlinx.android.synthetic.main.fragment_hotel_e_voucher.*
import java.io.File
import java.io.File.separator
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject


/**
 * @author by furqan on 14/05/19
 */
class HotelEVoucherFragment : HotelBaseFragment(), HotelSharePdfBottomSheets.SharePdfBottomSheetsListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var eVoucherViewModel: HotelEVoucherViewModel

    lateinit var orderId: String
    lateinit var cancellationPoliciesAdapter: HotelEVoucherCancellationPoliciesAdapter

    lateinit var progressDialog: ProgressDialog
    private lateinit var shareAsPdfBottomSheets: HotelSharePdfBottomSheets

    private var mUri: Uri? = null
    private val permissionChecker = PermissionCheckerHelper()

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            eVoucherViewModel = viewModelProvider.get(HotelEVoucherViewModel::class.java)
        }

        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eVoucherViewModel.orderDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderData(it.data)
                }
                is Fail -> {
                }
            }
        })

        eVoucherViewModel.sharePdfData.observe(viewLifecycleOwner, Observer {
            progressDialog.dismiss()
            shareAsPdfBottomSheets.dismiss()
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

        val args = savedInstanceState ?: arguments
        orderId = args?.getString(EXTRA_ORDER_ID) ?: ""
        eVoucherViewModel.getOrderDetail(HotelGqlQuery.ORDER_DETAILS, orderId)

    }

    override fun initInjector() = getComponent(HotelEVoucherComponent::class.java).inject(this)

    fun takeScreenshot(isShare: Boolean) {
        val bitmap = getScreenBitmap()
        saveImage(bitmap, isShare)
    }

    private fun showToastMessage(uri: Uri?) {
        if (uri != null) {
            view?.let {
                Toaster.build(
                        it,
                        getString(R.string.hotel_save_as_image_success),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_NORMAL
                ).show()
            }
        }
    }


    private fun getScreenBitmap(): Bitmap? {
        val v = container_root

        v.measure(View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        /**Stretch out layout to fit root view (because, footer is sticky) */
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)

        val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)
        return b
    }

    private fun saveImage(bitmap: Bitmap?, isShare: Boolean) {
        if (bitmap != null) {

            /**Reset layout to origin*/
            container_root.requestLayout()

            permissionChecker.checkPermission(this,
                    PERMISSION_WRITE_EXTERNAL_STORAGE,
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            context?.let {
                                permissionChecker.onPermissionDenied(it, permissionText)
                            }
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            context?.let {
                                permissionChecker.onNeverAskAgain(it, permissionText)
                            }
                        }

                        override fun onPermissionGranted() {
                            context?.let {
                                saveImage(bitmap, it, FILENAME, isShare)
                            }
                        }
                    })
        }
    }

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String, isShare: Boolean) {
        val currentTime = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.getCurrentCalendar().time)
        val filename = getString(R.string.hotel_share_file_name, currentTime)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = contentValues(filename)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            mUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            mUri?.let {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(it))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(it, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, filename)
            saveImageToStream(bitmap, FileOutputStream(file))

            val values = contentValues(filename)
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            mUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }

        if (isShare) {
            shareImageUri(mUri)
        } else {
            showToastMessage(mUri)
        }
    }

    private fun contentValues(filename: String) : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun removeImageFromStorage(uri: Uri?) {
        if (uri != null) {
            context?.contentResolver?.delete(uri, null, null)
            this.mUri = null
        }
    }

    private fun shareImageUri(uri: Uri?) {
        if (uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivityForResult(intent, SHARE_IMG_REQUEST_CODE)
        }
    }

    fun shareAsPdf() {
        shareAsPdfBottomSheets = HotelSharePdfBottomSheets()
        shareAsPdfBottomSheets.listener = this
        activity?.let {
            shareAsPdfBottomSheets.show(it.supportFragmentManager, TAG_SHARE_AS_PDF)
        }
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

            rdv_checkin_checkout_date.setRoomCheckTimes(
                    getString(R.string.hotel_order_detail_day_and_time, propertyDetail.checkInOut[0].checkInOut.day,
                            propertyDetail.checkInOut[0].checkInOut.time),
                    getString(R.string.hotel_order_detail_day_and_time, propertyDetail.checkInOut[1].checkInOut.day,
                            propertyDetail.checkInOut[1].checkInOut.time)
            )

            for (i in 1..propertyDetail.propertyInfo.starRating) {
                context?.run {
                    container_rating_view.addView(RatingStarView(this))
                }
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
                if (amenitiesString.isEmpty()) tv_room_facility.hide() else tv_room_facility.show()
            }

            if (propertyDetail.specialRequest.content.isEmpty()) {
                tv_request_label.hide()
                tv_request_info.hide()
                hotel_detail_seperator.hide()
            } else {
                tv_request_label.text = propertyDetail.specialRequest.title
                tv_request_info.text = propertyDetail.specialRequest.content
                hotel_detail_seperator.show()
            }

            if (propertyDetail.extraInfo.content.isEmpty() && propertyDetail.specialRequest.content.isEmpty()) hotel_detail_seperator.hide()
        }

        var phoneString = ""
        for ((index, item) in data.hotelTransportDetails.contactInfo.withIndex()) {
            phoneString += item.number
            if (index < data.hotelTransportDetails.contactInfo.size - 1) phoneString += ", "
        }
        if (phoneString.isNotEmpty()) {
            tv_property_phone.text = getString(R.string.hotel_e_voucher_phone, phoneString)
        } else {
            tv_property_phone.hide()
        }

        if (data.hotelTransportDetails.cancellation.cancellationPolicies.isNotEmpty()) {
            renderCancellationPolicies(data.hotelTransportDetails.cancellation.cancellationPolicies)
        }
    }

    private fun renderCancellationPolicies(cancellationList: List<HotelTransportDetail.Cancellation.CancellationPolicy>) {
        cancellationPoliciesAdapter = HotelEVoucherCancellationPoliciesAdapter(cancellationList)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_cancellation_policies.layoutManager = layoutManager
        rv_cancellation_policies.setHasFixedSize(true)
        rv_cancellation_policies.isNestedScrollingEnabled = false
        rv_cancellation_policies.adapter = cancellationPoliciesAdapter
    }

    override fun onErrorRetryClicked() {
        eVoucherViewModel.getOrderDetail(HotelGqlQuery.ORDER_DETAILS, orderId)
    }

    override fun sendPdf(emailList: MutableList<String>) {
        progressDialog.show()
        eVoucherViewModel.sendPdf(HotelGqlMutation.SHARE_PDF_NOTIFICATION, emailList, orderId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            SHARE_IMG_REQUEST_CODE -> {
                removeImageFromStorage(mUri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionChecker.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }

    companion object {

        const val TAG_SHARE_AS_PDF = "TAG_SHARE_AS_PDF"
        const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
        const val SHARE_IMG_REQUEST_CODE = 4532
        const val FILENAME = "Tokopedia"

        fun getInstance(orderId: String): HotelEVoucherFragment = HotelEVoucherFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_ORDER_ID, orderId)
            }
        }

    }
}