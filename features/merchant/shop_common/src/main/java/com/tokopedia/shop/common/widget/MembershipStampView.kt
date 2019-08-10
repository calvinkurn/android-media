package com.tokopedia.shop.common.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.stampprogress.ActionButton
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipQuests

class MembershipStampView : FrameLayout {

    lateinit var txtTitle: TextView
    lateinit var imgStamp1: ImageView
    lateinit var imgStamp2: ImageView
    lateinit var imgStamp3: ImageView
    lateinit var imgStamp4: ImageView
    lateinit var imgStamp5: ImageView
    lateinit var btnClaim: Button
    lateinit var membershipCard: CardView
    private var listImage = mutableListOf<ImageView>()

    companion object {
        private const val TOTAL_TEMP = 5
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setMembershipModel(item: MembershipQuests, dataSize: Int) {
        txtTitle.text = item.title
        if(dataSize<=1) {
            val layoutParams = membershipCard.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        setTargetProgress(item.targetProgress)
        setCurrentStamp(item.currentProgress, item.iconURL)
        setButtonClaim(item.actionButton)
    }

    private fun setTargetProgress(targetProgress: Int) {
        (targetProgress..TOTAL_TEMP).forEachIndexed { index, it ->
            listImage[index + 1].visibility = View.INVISIBLE
        }
    }

    private fun setButtonClaim(actionButton: ActionButton) {
        if (actionButton.isShown) {
            btnClaim.text = actionButton.text
            btnClaim.visibility = View.VISIBLE
        } else {
            btnClaim.visibility = View.GONE
        }
    }

    private fun setCurrentStamp(currentProgress: Int, iconUrl: String) {
        (0 until currentProgress).forEachIndexed { index, _ ->
            listImage[index].visibility = View.VISIBLE
            renderCircleImageStamp(listImage[index], iconUrl)
        }
    }

    private fun renderCircleImageStamp(imageView: ImageView, iconUrl: String) {
        ImageHandler.loadImageCircle2(context, imageView, iconUrl)
    }

    private fun init(attrs: AttributeSet? = null) {
        val inflatedView = View.inflate(context, R.layout.item_membership_stamp, this)
        findView(inflatedView)
    }

    private fun findView(view: View) {
        membershipCard = view.findViewById(R.id.membership_card)
        txtTitle = view.findViewById(R.id.title_coupon)
        imgStamp1 = view.findViewById(R.id.img_stamp_1)
        imgStamp2 = view.findViewById(R.id.img_stamp_2)
        imgStamp3 = view.findViewById(R.id.img_stamp_3)
        imgStamp4 = view.findViewById(R.id.img_stamp_4)
        imgStamp5 = view.findViewById(R.id.img_stamp_5)
        btnClaim = view.findViewById(R.id.btn_claim)
        listImage = mutableListOf(imgStamp1, imgStamp2, imgStamp3, imgStamp4, imgStamp5)

    }
}