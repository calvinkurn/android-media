package com.tokopedia.hotel.evoucher.presentation.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import kotlinx.android.synthetic.main.fragment_hotel_e_voucher.*
import java.io.File
import java.io.FileOutputStream


/**
 * @author by furqan on 14/05/19
 */
class HotelEVoucherFragment : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_hotel_e_voucher, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        for (i in 1..5) {
            container_rating_view.addView(RatingStarView(context!!))
        }
    }

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
            val myDir = File("$root/screenshots")
            myDir.mkdirs()
            val currentTime = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.getCurrentCalendar().time)
            val fname = "Tokopedia_Hotel_$currentTime.png"
            val file = File(myDir, fname)
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

    companion object {

        fun getInstance(): HotelEVoucherFragment = HotelEVoucherFragment()

    }
}