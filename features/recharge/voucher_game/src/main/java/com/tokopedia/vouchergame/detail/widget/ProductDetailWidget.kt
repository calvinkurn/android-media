package com.tokopedia.vouchergame.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.vouchergame.R

/**
 * @author by resakemal on 17/09/19
 */
class ProductDetailWidget: BaseCustomView {

    private lateinit var detailDescription: TextView
    private lateinit var detailUrl: TextView

    var description: String = ""
    set(value) {
        field = value
        if (::detailDescription.isInitialized) {
            detailDescription.text = MethodChecker.fromHtml(description)
        }
    }

    var url: String = ""
    set(value) {
        field = value
        if (::detailUrl.isInitialized) {
            detailUrl.isVisible = url.isNotEmpty()
        }
    }

    var urlLabel: String = ""
    set(value) {
        field = value
        if (url.isNotEmpty() && urlLabel.isNotEmpty() && ::detailUrl.isInitialized) {
            detailUrl.text = urlLabel
            detailUrl.setOnClickListener { RouteManager.route(context, url) }
        }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_voucher_game_product, this, true)
        with (view) {
            detailDescription = findViewById(R.id.detail_desc)
            detailUrl = findViewById(R.id.detail_url)
        }
    }
}