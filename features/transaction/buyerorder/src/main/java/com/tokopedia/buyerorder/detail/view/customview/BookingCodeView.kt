package com.tokopedia.buyerorder.detail.view.customview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.buyerorder.R
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.build

class BookingCodeView : RelativeLayout {

    private var bookingCode: String? = null
    private var position = 0
    private var totalItems = 0
    private var bookingCodeTitle: String? = null
    private lateinit var bookingCodeText: TextView
    private lateinit var copyBookingCode: TextView
    private lateinit var bookingCodeTitleView: TextView

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(
        context: Context,
        bookingCode: String,
        position: Int,
        bookingCodeTitle: String,
        totalItems: Int
    ) : super(context) {
        this.bookingCode = bookingCode
        this.position = position
        this.bookingCodeTitle = bookingCodeTitle
        this.totalItems = totalItems
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.booking_code_layout, this)

        bookingCodeText = view.findViewById(R.id.booking_code)
        bookingCodeTitleView = view.findViewById(R.id.booking_code_title)
        copyBookingCode = view.findViewById(R.id.copyCode)
        bookingCodeTitleView.text = bookingCodeTitle
        bookingCodeText.text = bookingCode

        copyBookingCode.setOnClickListener {
            val myClipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip: ClipData = ClipData.newPlainText(TEXT, bookingCode)
            myClipboard.setPrimaryClip(myClip)
            build(
                view,
                TEXT_COPIED,
                LENGTH_LONG,
                TYPE_NORMAL,
                "Ok",
                OnClickListener { }
            ).show()
        }
    }

    companion object {
        private const val TEXT_COPIED = "Kode booking telah disalin"
        private const val TEXT = "text"
    }
}