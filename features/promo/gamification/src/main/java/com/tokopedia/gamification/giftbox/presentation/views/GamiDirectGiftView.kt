package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListButton
import com.tokopedia.gamification.giftbox.data.entities.PrizeDetailListItem
import com.tokopedia.gamification.giftbox.data.entities.PrizeListItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class GamiDirectGiftView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GiftBoxGlowingView(context, attrs, defStyleAttr) {

    val LAYOUT = R.layout.gami_direct_gift_view
    lateinit var image: AppCompatImageView
    lateinit var tvTitle: Typography
    lateinit var tvMessage: Typography
    lateinit var greenBtn: GreenGradientButton
    var userId: String? = ""

    init {
        View.inflate(context, LAYOUT, this)
        initViews()
    }

    private fun initViews() {
        image = findViewById(R.id.image)
        tvTitle = findViewById(R.id.tvTitle)
        tvMessage = findViewById(R.id.tvMessage)
        greenBtn = findViewById(R.id.greenBtn)
    }

    fun setData(prizeList: List<PrizeListItem>?,
                bottomSheetButtonText: String?,
                prizeDetailList: List<PrizeDetailListItem?>?,
                prizeDetailListButton: PrizeDetailListButton?,
                userId: String?) {
        this.userId = userId

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
                    GtmEvents.clickRewardDetail(this.userId)
                }
            }
        } else {
            // TODO Rahul ???? maybe hide this ~ ask first
        }
    }

    private fun showBmPrizeDetails(prizeDetailList: List<PrizeDetailListItem?>?, prizeDetailListButton: PrizeDetailListButton?) {
        val isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false
        val itemView = getDialogChildView(prizeDetailList, prizeDetailListButton)
        if (isTablet) {
            showTabletDialog(itemView)
        } else {
            showUnifyDialog(itemView)
        }

    }

    fun getDialogChildView(prizeDetailList: List<PrizeDetailListItem?>?, prizeDetailListButton: PrizeDetailListButton?): BmPrizeDetailView {
        val itemView = BmPrizeDetailView(context)
        itemView.setData(prizeDetailList, prizeDetailListButton)
        itemView.userId = userId
        return itemView
    }

    private fun showUnifyDialog(itemView: BmPrizeDetailView) {
        val bmUnify = BottomSheetUnify()
        bmUnify.setChild(itemView)
        bmUnify.setTitle(context.getString(R.string.gami_hadiah_yang_mungkin_didapat))
        bmUnify.show((context as AppCompatActivity).supportFragmentManager, "gami_prize_detail")
    }

    private fun showTabletDialog(itemView: BmPrizeDetailView) {
        val dialog = DialogUnify(context, 0, 0)
        dialog.setUnlockVersion()
        dialog.setChild(itemView)
        val width = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_rv_coupons_width) ?: 390.toPx().toFloat() + 20.toPx()
        dialog.dialogMaxWidth = width.toInt()
        dialog.setOverlayClose(false)
        dialog.setOnShowListener {
            itemView.imageClose?.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}