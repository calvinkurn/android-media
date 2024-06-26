package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListItem
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

class PrizeDetailListItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val LAYOUT = R.layout.gami_prize_list_item
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: Typography

    init {
        View.inflate(context, LAYOUT, this)
        orientation = LinearLayout.HORIZONTAL
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
    }

    fun setData(prizeDetailListItem: PrizeDetailListItem) {
        if (!prizeDetailListItem.imageURL.isNullOrEmpty()) {
            image.loadImage(prizeDetailListItem.imageURL)
        }
        tvTitle.text = prizeDetailListItem.text
    }
}
