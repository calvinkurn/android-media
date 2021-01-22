package com.tokopedia.mvcwidget.views

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
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
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.R
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
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

    fun setData(followWidget: FollowWidget) {
        divider.visibility = View.GONE

        if (followWidget.isShown == true) {
            followWidget.type?.let {
                when (it) {
                    FollowWidgetType.FIRST_FOLLOW -> {
                        oneActionView.visibility = View.VISIBLE
                        oneActionView.setData(followWidget)
                        divider.visibility = View.VISIBLE
                    }
                    FollowWidgetType.MEMBERSHIP_OPEN -> {
                        twoActionView.visibility = View.VISIBLE
                        twoActionView.setData(followWidget)
                        divider.visibility = View.VISIBLE
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
    var btn: TextView

    init {
        View.inflate(context, layout, this)
        tvTitle = findViewById(R.id.tvTitle)
        btn = findViewById(R.id.btn_action_mvc_view)
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

        btn.setOnClickListener { }
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

    fun setData(followWidget: FollowWidget) {
        //todo Rahul remove this hardcoded text
        val t = "<b>Jadi member toko untuk dapat kupon:</b>" +
                "<ul>" +
                "  <li>Cashback 10% hingga Rp500.000</li>" +
                "  <li>Cashback 10% hingga Rp500.000</li>" +
                "  <li>Cashback 10% hingga Rp500.000</li>" +
                "</ul>"
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
                showTokomemberBottomSheet(followWidget, context as AppCompatActivity)
            }
        }
    }

    fun showTokomemberBottomSheet(followWidget: FollowWidget, activity: AppCompatActivity) {
        if (followWidget.membershipHowTo.isNullOrEmpty()) {
            return
        }
        val bottomsheet = BottomSheetUnify()
        bottomsheet.setTitle("Tentang Toko Member")
        bottomsheet.showCloseIcon = false
        bottomsheet.showKnob = true
        bottomsheet.isDragable = true
        bottomsheet.isHideable = true
        bottomsheet.isSkipCollapseState = true


        val child = View.inflate(activity, R.layout.mvc_tokomember_bm, null)
        val itemViewList = arrayListOf<View>()
        followWidget.membershipHowTo!!.forEach { memberShipItem ->
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
        cta.setOnClickListener { bottomsheet.dismiss() }
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
        bottomsheet.show(activity.supportFragmentManager, "btm_mvc_tokomember")

    }
}