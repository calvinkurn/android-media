package com.tokopedia.sellerorder.detail.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_COPY_BOOKING_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_CODE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_BOOKING_TYPE
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailBookingCodeMessageAdapter
import com.tokopedia.unifycomponents.Toaster
import io.hansel.a.v
import kotlinx.android.synthetic.main.fragment_som_booking_code.*

/**
 * Created by fwidjaja on 2019-11-27.
 */
class SomDetailBookingCodeFragment: BaseDaggerFragment() {
    private var bookingCode = ""
    private lateinit var somBookingCodeMsgAdapter: SomDetailBookingCodeMessageAdapter

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailBookingCodeFragment {
            return SomDetailBookingCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_BOOKING_CODE, bundle.getString(PARAM_BOOKING_CODE))
                    /*putString(PARAM_BOOKING_CODE, bundle.getString(PARAM_BOOKING_CODE))
                    putInt(PARAM_BOOKING_TYPE, bundle.getInt(PARAM_CURR_SHIPMENT_ID))
                    putString(PARAM_CURR_SHIPMENT_NAME, bundle.getString(PARAM_CURR_SHIPMENT_NAME))
                    putString(PARAM_CURR_SHIPMENT_PRODUCT_NAME, bundle.getString(PARAM_CURR_SHIPMENT_PRODUCT_NAME))*/
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            bookingCode = arguments?.getString(PARAM_BOOKING_CODE).toString()
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
        somBookingCodeMsgAdapter = SomDetailBookingCodeMessageAdapter()
        rv_message?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = somBookingCodeMsgAdapter
        }
    }

    private fun initListeners() {
        ll_code?.setOnClickListener { copyCode() }
    }

    private fun copyCode() {
        val code = booking_code?.text.toString().trim { it <= ' ' }
        activity?.let {
            val clipboardManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.primaryClip = ClipData.newPlainText(LABEL_COPY_BOOKING_CODE, code)
            showCommonToaster(getString(R.string.booking_code_copied))
        }
    }

    /*private fun zoomBarcode() {
        card_barcode?.isClickable = true
        changeBarcodeSize(CONST_INCREASE_DP)
        filterView.setVisibility(View.VISIBLE)
        filterView.setOnClickListener({ view ->
            view.setVisibility(View.GONE)
            changeBarcodeSize(CONST_REDUCE_DP)
            cardBarcode.setClickable(true)
        })
    }*/

    /*override fun changeBarcodeSize(dp: Int) {
        val params = barcodeImg.getLayoutParams()
        params.width = barcodeImg.getWidth() + dpToPx(dp)
        params.height = barcodeImg.getHeight() + dpToPx(dp)
        barcodeImg.setLayoutParams(params)
    }*/

    private fun showCommonToaster(message: String) {
        val toasterCommon = Toaster
        view?.let { v ->
            toasterCommon.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}
}