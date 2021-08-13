package com.tokopedia.vouchergame.detail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.vouchergame.R

/**
 * @author by resakemal on 22/08/19
 */
class OperatorInfoWidget: BaseCustomView {

    private lateinit var imageContainer: CardView
    private lateinit var imageView: ImageView
    private lateinit var infoTitle: TextView
    private lateinit var infoDescription: TextView

    var imageUrl: String = ""
    set(value) {
        field = value
        if (::imageContainer.isInitialized && ::imageView.isInitialized) {
            if (value.isEmpty()) {
                imageContainer.visibility = View.GONE
            } else {
                imageContainer.visibility = View.VISIBLE
                ImageHandler.LoadImage(imageView, imageUrl)
            }
        }
    }

    var title: String = ""
    set(value) {
        field = value
        if (::infoTitle.isInitialized) {
            if (title.isEmpty()) {
                infoTitle.visibility = View.GONE
            } else {
                infoTitle.visibility = View.VISIBLE
                infoTitle.text = title
            }
        }
    }

    var description: String = ""
    set(value) {
        field = value
        infoDescription.text = MethodChecker.fromHtml(value)
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
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_voucher_game_operator, this, true)
        with (view) {
            imageContainer = findViewById(R.id.image_container)
            imageView = findViewById(R.id.info_image)
            infoTitle = findViewById(R.id.info_title)
            infoDescription = findViewById(R.id.info_desc)
        }
    }
}