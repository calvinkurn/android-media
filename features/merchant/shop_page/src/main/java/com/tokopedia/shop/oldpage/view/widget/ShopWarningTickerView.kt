package com.tokopedia.shop.oldpage.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.PorterDuff
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import kotlinx.android.synthetic.main.partial_shop_info_ticker.view.*

class ShopWarningTickerView: FrameLayout {
    private val view: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.partial_shop_info_ticker, this, false)
    }

    constructor(context: Context):super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet):super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int)
            :super(context, attrs, defStyleAttr){
        init()
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int)
            :super(context, attrs, defStyleAttr, defStyleRes){
        init()
    }

    private fun init(){
        this.addView(this.view)
        view.buttonAction.setOnClickListener { this.view.visibility = View.GONE }
    }

    fun setTitle(titleString: String) {
        if (!TextUtils.isEmpty(titleString)) {
            view.title.text = titleString
            view.title.visibility = View.VISIBLE
        } else {
            view.title.visibility = View.GONE
        }
    }

    fun setDescription(descriptionString: String) {
        if (!TextUtils.isEmpty(descriptionString)) {
            view.description.text = MethodChecker.fromHtml(descriptionString)
            view.description.visibility = View.VISIBLE
        } else {
            view.description.visibility = View.GONE
        }
    }

    /* it has to be called after setDescription */
    fun setAction(listener: ClickableSpan?) {
        listener?.run {
            val infoLanjut = view.context.getString(R.string.shop_page_header_shop_see_more_info)
            val start = view.description.text.length+1
            val text = "${view.description.text} $infoLanjut"
            val end = start + infoLanjut.length

            val spannableString = SpannableString(text)
            spannableString.setSpan(this, start, end, 0)
            view.description.text = spannableString

            view.description.movementMethod = LinkMovementMethod.getInstance()

        }
    }

    fun setTickerColor(color: Int) {
        if (color == 0) {
            view.baseTicker.background.clearColorFilter()
            view.buttonAction.clearColorFilter()
        } else {
            view.baseTicker.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            view.buttonAction.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}