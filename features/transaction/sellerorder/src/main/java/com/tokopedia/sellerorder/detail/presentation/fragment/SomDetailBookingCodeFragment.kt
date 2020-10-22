package com.tokopedia.sellerorder.detail.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_COPY_BOOKING_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BARCODE_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_CODE
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailBookingCodeMessageAdapter
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_som_booking_code.*

/**
 * Created by fwidjaja on 2019-11-27.
 */
class SomDetailBookingCodeFragment: BaseDaggerFragment() {
    private var bookingCode = ""
    private var barcodeType: String = ""
    private lateinit var somBookingCodeMsgAdapter: SomDetailBookingCodeMessageAdapter
    private val CONST_INCREASE_DP = 50
    private val CONST_REDUCE_DP = -50
    private val API_BARCODE_TYPE128 = "128b"
    private val API_BARCODE_TYPE39 = "39"
    private val API_BARCODE_TYPE93 = "93"
    val BARCODE_WIDTH = 256
    val BARCODE_HEIGHT = 61

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailBookingCodeFragment {
            return SomDetailBookingCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_BOOKING_CODE, bundle.getString(PARAM_BOOKING_CODE))
                    putString(PARAM_BARCODE_TYPE, bundle.getString(PARAM_BARCODE_TYPE))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            bookingCode = arguments?.getString(PARAM_BOOKING_CODE).toString()
            barcodeType = arguments?.getString(PARAM_BARCODE_TYPE).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_booking_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        initListeners()
    }

    private fun initLayout() {
        booking_code?.text = bookingCode
        somBookingCodeMsgAdapter = SomDetailBookingCodeMessageAdapter()
        rv_message?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = somBookingCodeMsgAdapter
        }
        somBookingCodeMsgAdapter.listMessage.add(getString(R.string.online_booking_msg_1))
        somBookingCodeMsgAdapter.listMessage.add(getString(R.string.online_booking_msg_2))
        somBookingCodeMsgAdapter.notifyDataSetChanged()
        generateBarcode(bookingCode, barcodeType)?.let { showBarcode(it) }
    }

    private fun initListeners() {
        ll_code?.setOnClickListener { copyCode() }
        text_tap_barcode?.setOnClickListener { zoomBarcode() }
        card_barcode?.setOnClickListener { zoomBarcode() }
    }

    private fun copyCode() {
        val code = booking_code?.text.toString().trim { it <= ' ' }
        activity?.let {
            val clipboardManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText(LABEL_COPY_BOOKING_CODE, code))
            showCommonToaster(getString(R.string.booking_code_copied))
        }
    }

    private fun zoomBarcode() {
        card_barcode?.isClickable = false
        changeBarcodeSize(CONST_INCREASE_DP)
        filter_view?.visibility = View.VISIBLE
        filter_view?.setOnClickListener { view ->
            view.visibility = View.GONE
            changeBarcodeSize(CONST_REDUCE_DP)
            card_barcode?.isClickable = true
        }
    }

    private fun changeBarcodeSize(dp: Int) {
        val displayMetrics = context?.resources?.displayMetrics
        val params = barcode_img?.layoutParams
        var widthHeight = dp
        displayMetrics?.let {
            widthHeight = dp.dpToPx(it)
        }
        params?.width = barcode_img?.width?.plus(widthHeight)
        params?.height = barcode_img?.height?.plus(widthHeight)
        barcode_img?.layoutParams = params
    }

    private fun showCommonToaster(message: String) {
        val toasterCommon = Toaster
        view?.let { v ->
            toasterCommon.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    private fun showBarcode(bitmap: Bitmap) {
        barcode_img?.setImageBitmap(bitmap)
    }

    private fun generateBarcode(code: String, type: String): Bitmap? {
        var bitmap: Bitmap? = null
        var format: BarcodeFormat? = null
        when (type) {
            API_BARCODE_TYPE128 -> format = BarcodeFormat.CODE_128
            API_BARCODE_TYPE39 -> format = BarcodeFormat.CODE_39
            API_BARCODE_TYPE93 -> format = BarcodeFormat.CODE_93
        }
        if (format != null) {
            try {
                val multiFormatWriter = MultiFormatWriter()
                val bitMatrix = multiFormatWriter
                        .encode(code, format, BARCODE_WIDTH, BARCODE_HEIGHT)
                val barcodeEncoder = BarcodeEncoder()
                bitmap = barcodeEncoder.createBitmap(bitMatrix)
            } catch (e: WriterException) {
                e.printStackTrace()
            }

        }
        return bitmap
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}
}