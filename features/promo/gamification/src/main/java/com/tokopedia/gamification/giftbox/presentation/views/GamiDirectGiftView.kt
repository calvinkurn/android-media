package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListButton
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListItem
import com.tokopedia.gamification.giftbox.data.entities.PrizeListItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class GamiDirectGiftView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GiftBoxGlowingView(context, attrs, defStyleAttr) {

    val LAYOUT = R.layout.gami_direct_gift_view
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: Typography
    lateinit var tvMessage: Typography
    lateinit var greenBtn: GreenGradientButton

    init {
        View.inflate(context, LAYOUT, this)
        initViews()
    }

    private fun initViews() {
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
        tvMessage = findViewById(R.id.tvMessage)
        greenBtn = findViewById(R.id.greenBtn)
//        btnContainer = findViewById(R.id.btnContainer)
    }

    fun setData(prizeList: List<PrizeListItem>?,
                bottomSheetButtonText: String?,
                prizeDetailList: List<PrizeDetailListItem?>?,
                prizeDetailListButton: PrizeDetailListButton?) {

        val prizeItem = prizeList?.find { it -> it.isSpecial }
        if (prizeItem != null) {
            prizeItem.let { item ->
                Glide.with(image)
                        .load(item.imageURL)
                        .into(image)

                val tvList = arrayListOf(tvTitle, tvMessage)
                item.text?.forEachIndexed { index, text ->
                    if (index < tvList.size) {
                        tvList[index].text = text
                    }
                }

                greenBtn.setText(bottomSheetButtonText)
                greenBtn.setOnClickListener {
                    showBmPrizeDetails(prizeDetailList, prizeDetailListButton)
                }
            }
        } else {
            // TODO Rahul ???? maybe hide this ~ ask first
        }
    }

    private fun showBmPrizeDetails(prizeDetailList: List<PrizeDetailListItem?>?, prizeDetailListButton: PrizeDetailListButton?) {
        if (context is AppCompatActivity) {
            val bmUnify = BottomSheetUnify()
            val itemView = BmPrizeDetailView(context)
            itemView.setData(prizeDetailList, prizeDetailListButton)
            bmUnify.setChild(itemView)
            bmUnify.setTitle("Hadiah yang Mungkin Didapat")
            bmUnify.show((context as AppCompatActivity).supportFragmentManager, "gami_prize_detail")
        }
    }
}