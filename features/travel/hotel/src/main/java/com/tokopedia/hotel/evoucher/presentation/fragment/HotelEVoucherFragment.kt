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
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.MutationSharePdfNotification
import com.tokopedia.hotel.common.util.QueryHotelOrderDetail
import com.tokopedia.hotel.databinding.FragmentHotelEVoucherBinding
import com.tokopedia.hotel.evoucher.di.HotelEVoucherComponent
import com.tokopedia.hotel.evoucher.presentation.adapter.HotelEVoucherCancellationPoliciesAdapter
import com.tokopedia.hotel.evoucher.presentation.viewmodel.HotelEVoucherViewModel
import com.tokopedia.hotel.evoucher.presentation.widget.HotelSharePdfBottomSheets
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
import java.io.File
import java.io.File.separator
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
    private var binding by autoClearedNullable<FragmentHotelEVoucherBinding>()

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
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
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
                    showErrorView(it.throwable)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelEVoucherBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = savedInstanceState ?: arguments
        orderId = args?.getString(EXTRA_ORDER_ID) ?: ""
        eVoucherViewModel.getOrderDetail(QueryHotelOrderDetail(), orderId)

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

    fun showErrorView(error: Throwable){
        binding?.containerError?.root?.visible()
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(this, error,
                    { onErrorRetryClicked() }, it
                )
            }
        }
    }

    private fun getScreenBitmap(): Bitmap? {
        val v = binding?.containerRoot

        v?.measure(View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        /**Stretch out layout to fit root view (because, footer is sticky) */
        v?.layout(0, 0, v.measuredWidth, v.measuredHeight)

        val b = Bitmap.createBitmap(v?.measuredWidth ?: 0, v?.measuredHeight ?: 0, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v?.draw(c)
        return b
    }

    private fun saveImage(bitmap: Bitmap?, isShare: Boolean) {
        if (bitmap != null) {

            /**Reset layout to origin*/
            binding?.containerRoot?.requestLayout()

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
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
                            saveImageEVoucher(bitmap, isShare)
                        }
                    })
            } else {
                saveImageEVoucher(bitmap, isShare)
            }
        }
    }

    private fun saveImageEVoucher(bitmap: Bitmap, isShare: Boolean) {
        context?.let {
            saveImage(bitmap, it, FILENAME, isShare)
        }
    }

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String, isShare: Boolean) {
        val currentTime = DateUtil.getCurrentCalendar().time.toString(DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z)
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
            val directory = File(requireContext().getExternalFilesDir(null)?.absoluteFile.toString() + separator + folderName)
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
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / CONVERT_TIME_MILLIS);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, BITMAP_QUALITY, outputStream)
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

        binding?.tvGuestTitle?.text = data.hotelTransportDetails.guestDetail.title
        binding?.tvGuestName?.text = data.hotelTransportDetails.guestDetail.content

        if (data.hotelTransportDetails.propertyDetail.isNotEmpty()) {
            val propertyDetail = data.hotelTransportDetails.propertyDetail[0]

            binding?.tvPropertyName?.text = propertyDetail.propertyInfo.name
            binding?.tvPropertyAddress?.text = propertyDetail.propertyInfo.address

            binding?.rdvCheckinCheckoutDate?.setRoomDatesFormatted(
                    propertyDetail.checkInOut[0].checkInOut.date,
                    propertyDetail.checkInOut[1].checkInOut.date,
                    propertyDetail.stayLength.content)

            binding?.rdvCheckinCheckoutDate?.setRoomCheckTimes(
                    getString(R.string.hotel_order_detail_day_and_time, propertyDetail.checkInOut[0].checkInOut.day,
                            propertyDetail.checkInOut[0].checkInOut.time),
                    getString(R.string.hotel_order_detail_day_and_time, propertyDetail.checkInOut[1].checkInOut.day,
                            propertyDetail.checkInOut[1].checkInOut.time)
            )

            for (i in 1..propertyDetail.propertyInfo.starRating) {
                context?.run {
                    binding?.containerRatingView?.addView(RatingStarView(this))
                }
            }

            binding?.tvBookingTitle?.text = propertyDetail.bookingKey.title
            binding?.tvBookingCode?.text = propertyDetail.bookingKey.content

            if (propertyDetail.room.isNotEmpty()) {
                binding?.tvRoomTitle?.text = propertyDetail.room[0].title
                binding?.tvRoomInfo?.text = propertyDetail.room[0].content

                var amenitiesString = ""
                for ((index, item) in propertyDetail.room[0].amenities.withIndex()) {
                    amenitiesString += item.content
                    if (index < propertyDetail.room[0].amenities.size - 1) amenitiesString += ", "
                }

                binding?.tvRoomFacility?.text = amenitiesString
                if (amenitiesString.isEmpty()) binding?.tvRoomFacility?.hide() else binding?.tvRoomFacility?.show()
            }

            if (propertyDetail.specialRequest.content.isEmpty()) {
                binding?.tvRequestLabel?.hide()
                binding?.tvRequestInfo?.hide()
                binding?.hotelDetailSeperator?.hide()
            } else {
                binding?.tvRequestLabel?.text = propertyDetail.specialRequest.title
                binding?.tvRequestInfo?.text = propertyDetail.specialRequest.content
                binding?.hotelDetailSeperator?.show()
            }

            if (propertyDetail.extraInfo.content.isEmpty() && propertyDetail.specialRequest.content.isEmpty()) binding?.hotelDetailSeperator?.hide()

            if(data.hotelTransportDetails.tickerContactHotel.isNotEmpty() && data.hotelTransportDetails.contactInfo.isNotEmpty()) {
                binding?.tvOrderDetailNha?.show()
                binding?.tvOrderDetailNha?.text = data.hotelTransportDetails.tickerContactHotel
            } else {
                binding?.tvOrderDetailNha?.gone()
            }

            if(data.hotelTransportDetails.contactInfo.isNotEmpty()){
                val telNum: String = (data.hotelTransportDetails.contactInfo.firstOrNull()?.number ?: 0).toString()
                binding?.btnNhaPhone?.setDrawable(
                    getIconUnifyDrawable(requireContext(), IconUnify.CALL, ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                )
                binding?.btnNhaPhone?.setTextColor(
                    ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                )
                binding?.btnNhaPhone?.text = telNum
                binding?.btnNhaPhone?.setOnClickListener {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:$telNum")
                    startActivity(callIntent)
                }
            }
        }

        var phoneString = ""
        for ((index, item) in data.hotelTransportDetails.contactInfo.withIndex()) {
            phoneString += item.number
            if (index < data.hotelTransportDetails.contactInfo.size - 1) phoneString += ", "
        }
        if (phoneString.isNotEmpty()) {
            binding?.tvPropertyPhone?.text = getString(R.string.hotel_e_voucher_phone, phoneString)
        } else {
            binding?.tvPropertyPhone?.hide()
        }

        if (data.hotelTransportDetails.cancellation.cancellationPolicies.isNotEmpty()) {
            renderCancellationPolicies(data.hotelTransportDetails.cancellation.cancellationPolicies)
        }

        if (data.agent.logo.isNotEmpty()){
            binding?.ivDynamicLogo?.loadImage(data.agent.logo){
                listener(
                    onSuccess = { _, _ ->
                        binding?.ivDynamicLogo?.show()
                    },
                    onError = {
                        binding?.ivDynamicLogo?.hide()
                    }
                )
            }
        }
    }

    private fun renderCancellationPolicies(cancellationList: List<HotelTransportDetail.Cancellation.CancellationPolicy>) {
        cancellationPoliciesAdapter = HotelEVoucherCancellationPoliciesAdapter(cancellationList)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.rvCancellationPolicies?.layoutManager = layoutManager
        binding?.rvCancellationPolicies?.setHasFixedSize(true)
        binding?.rvCancellationPolicies?.isNestedScrollingEnabled = false
        binding?.rvCancellationPolicies?.adapter = cancellationPoliciesAdapter
    }

    override fun onErrorRetryClicked() {
        binding?.let {
            it.containerError.root.hide()
        }
        eVoucherViewModel.getOrderDetail(QueryHotelOrderDetail(), orderId)
    }

    override fun sendPdf(emailList: MutableList<String>) {
        progressDialog.show()
        eVoucherViewModel.sendPdf(MutationSharePdfNotification(), emailList, orderId)
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
        const val CONVERT_TIME_MILLIS = 1000
        const val BITMAP_QUALITY = 100

        fun getInstance(orderId: String): HotelEVoucherFragment = HotelEVoucherFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_ORDER_ID, orderId)
            }
        }

    }
}
