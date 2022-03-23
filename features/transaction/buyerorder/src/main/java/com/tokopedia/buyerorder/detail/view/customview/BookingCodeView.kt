package com.tokopedia.buyerorder.detail.view.customview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.RelativeLayout
import com.tokopedia.buyerorder.databinding.BookingCodeLayoutBinding
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.build

class BookingCodeView : RelativeLayout {

    private var bookingCodeLabel: String? = null
    private var bookingCodeValue: String? = null
    private var position = 0
    private var totalItems = 0

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
        this.bookingCodeValue = bookingCode
        this.position = position
        this.bookingCodeLabel = bookingCodeTitle
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
        val binding = BookingCodeLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        with(binding){
            bookingCodeTitle.text = bookingCodeLabel
            bookingCode.text = bookingCodeValue

            copyCode.setOnClickListener {
                val myClipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip: ClipData = ClipData.newPlainText(TEXT, bookingCodeValue)
                myClipboard.setPrimaryClip(myClip)
                build(
                    this@BookingCodeView,
                    TEXT_COPIED,
                    LENGTH_LONG,
                    TYPE_NORMAL,
                    "Ok",
                    OnClickListener { }
                ).show()
            }
        }
    }

    companion object {
        private const val TEXT_COPIED = "Kode booking telah disalin"
        private const val TEXT = "text"
    }
}