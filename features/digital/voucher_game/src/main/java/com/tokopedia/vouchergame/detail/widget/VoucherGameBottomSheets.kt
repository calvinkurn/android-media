package com.tokopedia.vouchergame.detail.widget

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.vouchergame.R
import kotlinx.android.synthetic.main.bottom_sheets_voucher_game.*

/**
 * @author by resakemal on 22/08/19
 */
class VoucherGameBottomSheets: BottomSheets() {

    var imageUrl: String = ""
    var title: String = ""
    var description: String = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_voucher_game
    }

    override fun initView(view: View?) {
        ImageHandler.loadImageWithoutPlaceholder(info_image, imageUrl)
        info_title.text = title
        info_desc.text = description
    }
}