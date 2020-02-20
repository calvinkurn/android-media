package com.tokopedia.vouchergame.detail.widget

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.vouchergame.R

/**
 * @author by resakemal on 17/09/19
 */
class ProductDetailBottomSheets: BottomSheets() {

    private lateinit var detailTitle: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailUrl: TextView

    var title: String = ""
    var subtitle: String = ""
    var description: String = ""
    var urlLabel: String = ""
    var url: String = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_voucher_game_product
    }

    override fun initView(view: View) {
        with (view) {
            detailTitle = findViewById(R.id.detail_title)
            detailDescription = findViewById(R.id.detail_desc)
            detailUrl = findViewById(R.id.detail_url)
        }

        if(subtitle.isNotEmpty()) detailTitle.text = subtitle
        detailDescription.text = MethodChecker.fromHtml(description)
        if (url.isEmpty()) {
            detailUrl.visibility = View.GONE
        } else {
            if (urlLabel.isNotEmpty()) detailUrl.text = urlLabel
            detailUrl.setOnClickListener { RouteManager.route(context, url) }
        }
    }

    override fun title(): String { return title }

    override fun state(): BottomSheetsState { return BottomSheetsState.FLEXIBLE }
}