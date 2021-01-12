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
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListButton
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListItem
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.unifyprinciples.Typography

//TODO rahul cta button should have lower shadow - LATER
class BmPrizeDetailView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val LAYOUT = R.layout.gami_prize_detail
    lateinit var image: AppCompatImageView
    lateinit var prizeListItemContainer: LinearLayout
    lateinit var btnContainer: View
    lateinit var btn: Typography

    init {
        View.inflate(context, LAYOUT, this)
        orientation = LinearLayout.VERTICAL
        image = findViewById(R.id.image)
        prizeListItemContainer = findViewById(R.id.prizeListItemContainer)
        btn = findViewById(R.id.cta)
        btnContainer = findViewById(R.id.btnContainer)
    }

    fun setData(prizDetailList: List<PrizeDetailListItem?>?, prizeDetailListButton: PrizeDetailListButton?) {
        if (!prizDetailList.isNullOrEmpty()) {
            val bigItem = prizDetailList.find { it?.text.isNullOrEmpty() }
            if (bigItem != null && !bigItem.imageURL.isNullOrEmpty()) {
                //todo Rahul check with loading read coupon
                Glide.with(image)
                        .load(bigItem.imageURL)
                        .into(image)
            } else {
                image.visibility = View.GONE
            }

            prizDetailList.forEach { item ->
                if (item != null) {
                    if (!item.text.isNullOrEmpty()) {
                        val itemView = PrizeDetailListItemView(context)
                        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        lp.topMargin = dpToPx(12).toInt()
                        itemView.layoutParams = lp
                        prizeListItemContainer.addView(itemView)
                        itemView.setData(item)
                    }
                }
            }

            btn.text = prizeDetailListButton?.text
            btnContainer.setOnClickListener {
                if (!prizeDetailListButton?.applink.isNullOrEmpty()) {
                    RouteManager.route(context, prizeDetailListButton!!.applink)
                }
            }
        }
    }
}