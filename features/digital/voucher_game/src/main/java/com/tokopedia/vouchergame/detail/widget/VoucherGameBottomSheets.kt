package com.tokopedia.vouchergame.detail.widget

import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.vouchergame.R

/**
 * @author by resakemal on 22/08/19
 */
class VoucherGameBottomSheets: BottomSheets() {

    private lateinit var imageContainer: CardView
    private lateinit var imageView: ImageView
    private lateinit var infoTitle: TextView
    private lateinit var infoDescription: TextView

    var imageUrl: String = ""
    var title: String = ""
    var description: String = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_voucher_game
    }

    override fun initView(view: View) {
        with (view) {
            imageContainer = findViewById(R.id.image_container)
            imageView = findViewById(R.id.info_image)
            infoTitle = findViewById(R.id.info_title)
            infoDescription = findViewById(R.id.info_desc)
        }

        if (imageUrl.isEmpty()) {
            imageContainer.visibility = View.GONE
        } else {
            imageContainer.visibility = View.VISIBLE
            ImageHandler.LoadImage(imageView, imageUrl)
        }
        infoTitle.text = title
        infoDescription.text = description
    }

    override fun title(): String = ""
}