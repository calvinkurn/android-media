package com.tokopedia.mvcwidget.views

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.mvcwidget.*
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.htmltags.HtmlUtil

class MvcFollowViewContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val layout = R.layout.mvc_tokomember_follow_container
    var oneActionView: MvcTokomemberFollowOneActionView
    var twoActionView: MvcTokomemberFollowTwoActionsView
    private var divider: View

    init {
        View.inflate(context, layout, this)
        oneActionView = findViewById(R.id.one_action_view)
        twoActionView = findViewById(R.id.two_action_view)
        divider = findViewById(R.id.divider)
        orientation = VERTICAL
    }

    fun setData(followWidget: FollowWidget, widgetImpression: WidgetImpression, shopId: String, @MvcSource source: Int) {
        divider.visibility = View.GONE

        if (followWidget.isShown == true) {
            followWidget.type?.let {
                when (it) {
                    FollowWidgetType.FIRST_FOLLOW -> {
                        oneActionView.visibility = View.VISIBLE
                        twoActionView.visibility = View.GONE
                        oneActionView.setData(followWidget)
                        divider.visibility = View.VISIBLE

                        if (!widgetImpression.sentFollowWidgetImpression) {
                            Tracker.viewWidgetImpression(FollowWidgetType.FIRST_FOLLOW, shopId, UserSession(context).userId, source)
                            widgetImpression.sentFollowWidgetImpression = true
                        }
                    }
                    FollowWidgetType.MEMBERSHIP_OPEN -> {
                        twoActionView.visibility = View.VISIBLE
                        oneActionView.visibility = View.GONE
                        twoActionView.setData(followWidget, shopId, source)
                        divider.visibility = View.VISIBLE

                        if (!widgetImpression.sentJadiMemberImpression) {
                            Tracker.viewWidgetImpression(FollowWidgetType.MEMBERSHIP_OPEN, shopId, UserSession(context).userId, source)
                            widgetImpression.sentJadiMemberImpression = true
                        }
                    }
                }
            }
        }
    }

}

class MvcTokomemberFollowOneActionView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FollowCardView(context, attrs, defStyleAttr) {

    private val layout = R.layout.mvc_tokomember_view
    private var tvTitle: TextView
    var tvBtn: TextView
    var ll_btn: View

    init {
        View.inflate(context, layout, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvBtn = findViewById(R.id.btn_action_mvc_view)
        ll_btn = findViewById(R.id.ll_btn)
        radius = dpToPx(8)
        type = TYPE_SMALL
    }

    fun setData(followWidget: FollowWidget) {
        followWidget.content?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvTitle.text = Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
            } else {
                tvTitle.text = Html.fromHtml(it)
            }
        }
    }
}

class MvcTokomemberFollowTwoActionsView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FollowCardView(context, attrs, defStyleAttr) {

    private val layout = R.layout.mvc_tokomember_follow
    private var tvTitle: Typography
    private var icon: AppCompatImageView
    private var btnFirst: UnifyButton
    var btnSecond: UnifyButton

    init {
        View.inflate(context, layout, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvTitle.setWeight(Typography.REGULAR)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 12.toPx().toFloat())
        icon = findViewById(R.id.image_icon)
        btnFirst = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        radius = dpToPx(8)
        type = TYPE_LARGE
    }

    fun setData(followWidget: FollowWidget, shopId: String, @MvcSource mvcSource: Int) {
        val t = followWidget.content ?: ""
        followWidget.content?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvTitle.text = HtmlUtil.fromHtml(t).trim()
            } else {
                tvTitle.text = Html.fromHtml(t).trim()
            }
        }
        if (!followWidget.iconURL.isNullOrEmpty()) {
            Glide.with(icon)
                    .load(followWidget.iconURL)
                    .into(icon)
        }
        btnFirst.setOnClickListener {

            if (context is AppCompatActivity) {
                showTokomemberBottomSheet(followWidget, context as AppCompatActivity, shopId, mvcSource)
            }
        }
    }

    fun showTokomemberBottomSheet(followWidget: FollowWidget, activity: AppCompatActivity, shopId: String, @MvcSource mvcSource: Int) {
        if (followWidget.membershipHowTo.isNullOrEmpty()) {
            return
        }
        Tracker.clickCekInfoButton(shopId, UserSession(context).userId, mvcSource)
        val bottomsheet = BottomSheetUnify()
        bottomsheet.setTitle(context.getString(R.string.mvc_tentang_toko_member))
        bottomsheet.isSkipCollapseState = true

        val child = View.inflate(activity, R.layout.mvc_tokomember_bm, null)
        val itemViewList = arrayListOf<View>()
        followWidget.membershipHowTo.forEach { memberShipItem ->
            if (memberShipItem != null) {
                val item = View.inflate(activity, R.layout.mvc_carousel_item, null)
                val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                item.layoutParams = param
                val image = item.findViewById<AppCompatImageView>(R.id.image)
                val tvMessage = item.findViewById<Typography>(R.id.tvMessage)
                tvMessage.text = memberShipItem.description
                if (!memberShipItem.imageURL.isNullOrEmpty()) {
                    Glide.with(image)
                            .load(memberShipItem.imageURL)
                            .into(image)
                }
                itemViewList.add(item)
            }
        }
        val caroRef = child.findViewById<CarouselUnify>(R.id.carousel)
        val cta = child.findViewById<View>(R.id.btn)
        cta.setOnClickListener {
            Tracker.clickDaftarJadiMember(shopId,UserSession(context).userId,mvcSource)
            bottomsheet.dismiss()
        }
        caroRef.apply {
            slideToShow = 1f
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = false
            autoplay = false
            itemViewList.forEach { itemView ->
                addItem(itemView)
            }
        }
        bottomsheet.setChild(child)
        bottomsheet.clearContentPadding = true
        bottomsheet.setShowListener {
            Tracker.viewTokomemberBottomSheet(shopId,UserSession(context).userId,mvcSource)
        }
        bottomsheet.show(activity.supportFragmentManager, "btm_mvc_tokomember")

    }
}