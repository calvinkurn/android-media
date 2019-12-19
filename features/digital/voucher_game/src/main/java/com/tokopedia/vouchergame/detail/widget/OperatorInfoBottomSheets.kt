package com.tokopedia.vouchergame.detail.widget

import androidx.cardview.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.vouchergame.R
import kotlinx.android.synthetic.main.bottom_sheets_voucher_game_operator.*

/**
 * @author by resakemal on 22/08/19
 */
class OperatorInfoBottomSheets: BottomSheets() {

    private lateinit var imageContainer: CardView
    private lateinit var imageView: ImageView
    private lateinit var infoTitle: TextView
    private lateinit var infoDescription: TextView

    var imageUrl: String = ""
    var title: String = ""
    var description: String = ""

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_voucher_game_operator
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
        if (title.isEmpty()) {
            infoTitle.visibility = View.GONE
        } else {
            infoTitle.text = title
        }
        infoDescription.text = description
    }

    override fun title(): String = ""

    override fun state(): BottomSheetsState { return BottomSheetsState.FLEXIBLE }
}