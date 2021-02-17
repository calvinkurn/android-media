package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListButton
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListItem
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx

class BmPrizeDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val LAYOUT = R.layout.gami_prize_detail
    var image: AppCompatImageView
    var imageClose: AppCompatImageView? = null
    var prizeListItemContainer: LinearLayout
    var btn: GreenGradientButton
    var userId: String? = ""

    init {
        View.inflate(context, LAYOUT, this)
        orientation = LinearLayout.VERTICAL
        image = findViewById(R.id.image)
        imageClose = findViewById(R.id.image_close)
        prizeListItemContainer = findViewById(R.id.prizeListItemContainer)
        btn = findViewById(R.id.greenBtn)
    }

    fun setData(prizDetailList: List<PrizeDetailListItem?>?, prizeDetailListButton: PrizeDetailListButton?) {
        if (!prizDetailList.isNullOrEmpty()) {
            val bigItem = prizDetailList.find { it?.isSpecial == true }
            if (bigItem != null && !bigItem.imageURL.isNullOrEmpty()) {
                Glide.with(image)
                        .load(bigItem.imageURL)
                        .into(image)
            } else {
                image.visibility = View.GONE
            }

            prizDetailList.forEach { item ->
                if (item != null) {
                    if (!item.text.isNullOrEmpty() && item.isSpecial == false) {
                        val itemView = PrizeDetailListItemView(context)
                        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        lp.topMargin = dpToPx(12).toInt()
                        itemView.layoutParams = lp
                        prizeListItemContainer.addView(itemView)
                        itemView.setData(item)
                    }
                }
            }

            btn.setText(prizeDetailListButton?.text)
            btn.setOnClickListener {
                if (!prizeDetailListButton?.applink.isNullOrEmpty()) {
                    RouteManager.route(context, prizeDetailListButton!!.applink)
                }
                GtmEvents.clickInfoButtonFromDialog(userId)
            }
        }
    }
}