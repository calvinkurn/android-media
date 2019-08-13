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
    private lateinit var imgStamp1: ImageView
    private lateinit var imgStamp2: ImageView
    private lateinit var imgStamp3: ImageView
    private lateinit var imgStamp4: ImageView
    private lateinit var imgStamp5: ImageView

    private lateinit var circleStamp1: FrameLayout
    private lateinit var circleStamp2: FrameLayout
    private lateinit var circleStamp3: FrameLayout
    private lateinit var circleStamp4: FrameLayout
    private lateinit var circleStamp5: FrameLayout

    lateinit var line1: View
    lateinit var line2: View
    lateinit var line3: View
    lateinit var line4: View
    lateinit var btnClaim: Button
    lateinit var membershipCard: CardView

    private var listImage = mutableListOf<ImageView>()
    private var listLine = mutableListOf<View>()
    private var listCircleStamp = mutableListOf<FrameLayout>()

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
        if (dataSize <= 1) {
            val layoutParams = membershipCard.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        setTargetProgress(item.targetProgress)
        setCurrentStamp(item.currentProgress, item.iconURL)
        setButtonClaim(item.actionButton)
    }

    private fun setTargetProgress(targetProgress: Int) {
        listCircleStamp.forEachIndexed { index, _ ->
            if (index <= targetProgress - 1) {
                listCircleStamp[index].visibility = View.VISIBLE
                if (index != listCircleStamp.size - 1) {
                    listLine[index].visibility = View.VISIBLE
                }
            } else {
                listCircleStamp[index].visibility = View.INVISIBLE
                if (index != listCircleStamp.size - 1) {
                    listLine[index].visibility = View.INVISIBLE
                }
            }
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
        if (currentProgress == 0) {
            hideAllImageStamp()
        } else {
            listImage.forEachIndexed { index, imageView ->
                if (index <= currentProgress - 1) {
                    imageView.visibility = View.VISIBLE
                    renderCircleImageStamp(imageView, iconUrl)
                } else {
                    imageView.visibility = View.GONE
                }
            }
        }
    }

    private fun renderCircleImageStamp(imageView: ImageView, iconUrl: String) {
        ImageHandler.loadImageCircle2(context, imageView, iconUrl)
    }

    private fun init(attrs: AttributeSet? = null) {
        val inflatedView = View.inflate(context, R.layout.item_membership_stamp, this)
        findView(inflatedView)
    }

    private fun hideAllImageStamp() {
        listImage.forEach {
            it.visibility = View.GONE
        }
    }

    private fun findView(view: View) {
        membershipCard = view.findViewById(R.id.membership_card)
        btnClaim = view.findViewById(R.id.btn_claim)

        txtTitle = view.findViewById(R.id.title_coupon)
        imgStamp1 = view.findViewById(R.id.img_stamp_1)
        imgStamp2 = view.findViewById(R.id.img_stamp_2)
        imgStamp3 = view.findViewById(R.id.img_stamp_3)
        imgStamp4 = view.findViewById(R.id.img_stamp_4)
        imgStamp5 = view.findViewById(R.id.img_stamp_5)

        circleStamp1 = view.findViewById(R.id.circle_stamp_1)
        circleStamp2 = view.findViewById(R.id.circle_stamp_2)
        circleStamp3 = view.findViewById(R.id.circle_stamp_3)
        circleStamp4 = view.findViewById(R.id.circle_stamp_4)
        circleStamp5 = view.findViewById(R.id.circle_stamp_5)

        line1 = view.findViewById(R.id.line1)
        line2 = view.findViewById(R.id.line2)
        line3 = view.findViewById(R.id.line3)
        line4 = view.findViewById(R.id.line4)

        listLine = mutableListOf(line1, line2, line3, line4)
        listImage = mutableListOf(imgStamp1, imgStamp2, imgStamp3, imgStamp4, imgStamp5)
        listCircleStamp = mutableListOf(circleStamp1, circleStamp2, circleStamp3, circleStamp4, circleStamp5)

    }
}