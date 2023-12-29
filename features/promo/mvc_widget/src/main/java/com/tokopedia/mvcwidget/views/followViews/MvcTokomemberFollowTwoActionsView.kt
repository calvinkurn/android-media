package com.tokopedia.mvcwidget.views.followViews

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.views.FollowCardView
import com.tokopedia.mvcwidget.views.TYPE_LARGE
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class MvcTokomemberFollowTwoActionsView @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FollowCardView(context, attrs, defStyleAttr) {

    private val layout = R.layout.mvc_tokomember_follow
    private var tvTitle: Typography
    private var icon: AppCompatImageView
    private var btnFirst: UnifyButton
    private var collapsableContainer: LinearLayout
    private var iconBackground: AppCompatImageView
    var iconBackgroundContainer: FrameLayout

    var tvList: Typography
    private var tvSubTitle: Typography
    var btnSecond: UnifyButton
    var containerContent: ConstraintLayout

    init {
        View.inflate(context, layout, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvTitle.setWeight(Typography.REGULAR)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 12.toPx().toFloat())
        icon = findViewById(R.id.image_icon)
        btnFirst = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        collapsableContainer = findViewById(R.id.container_collapsable)
        tvList = findViewById(R.id.tvList)
        tvSubTitle = findViewById(R.id.tvSubTitle)
        iconBackground = findViewById(R.id.iconBackground)
        containerContent = findViewById(R.id.container_content)
        iconBackgroundContainer = findViewById(R.id.fm_image)

        if (context.isDarkMode()) {
            iconBackground.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
        }
        radius = dpToPx(8)
        type = TYPE_LARGE
    }

    fun setData(followWidget: FollowWidget, shopId: String, @MvcSource mvcSource: Int, mvcTracker: MvcTracker?) {
        val t = followWidget.content ?: ""
        val st = followWidget.contentDetails ?: ""

        followWidget.content?.let {
            tvTitle.text = returnTextFromHtml(t)
        }

        if (!followWidget.contentDetails.isNullOrEmpty()) {
            val s = returnTextFromHtml(st)
            val minTransLabel = "{{MinimumTransaction}}"
            val indexMinTransLabel = s.indexOf(minTransLabel)
            if (indexMinTransLabel >= 0) {
                val highLightColor =
                    context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_YN500)
                val sb = SpannableStringBuilder(s).replace(
                    indexMinTransLabel,
                    indexMinTransLabel + minTransLabel.length,
                    followWidget.membershipMinimumTransactionLabel
                )
                sb.setSpan(
                    ForegroundColorSpan(highLightColor),
                    indexMinTransLabel,
                    indexMinTransLabel + followWidget.membershipMinimumTransactionLabel?.length!!,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvSubTitle.text = sb
            } else {
                tvSubTitle.text = returnTextFromHtml(st)
            }
        }

        when (followWidget.type) {
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                collapsableContainer.visibility = View.GONE
                containerContent.visibility = View.VISIBLE
                tvSubTitle.visibility = View.GONE
                setAdjustableMargin()
                btnSecond.text = context.resources.getString(R.string.mvc_jadi_member)
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                collapsableContainer.visibility = View.VISIBLE
                containerContent.visibility = View.GONE
                btnSecond.text = context.resources.getString(R.string.mvc_mulai_belanja)

                if (mvcSource == MvcSource.PDP || mvcSource == MvcSource.REWARDS) {
                    btnSecond.visibility = View.GONE
                }
            }
        }

        if (!followWidget.iconURL.isNullOrEmpty()) {
            Glide.with(icon)
                .load(followWidget.iconURL)
                .into(icon)
        }
        btnFirst.setOnClickListener {
            if (context is AppCompatActivity) {
                showTokomemberBottomSheet(followWidget, context as AppCompatActivity, shopId, mvcSource, mvcTracker)
            }
            when (followWidget.type) {
                FollowWidgetType.MEMBERSHIP_OPEN -> {
                    mvcTracker?.clickCekInfoButton(FollowWidgetType.MEMBERSHIP_OPEN, shopId, UserSession(context).userId, mvcSource, btnFirst.text?.toString())
                }
                FollowWidgetType.MEMBERSHIP_CLOSE -> {
                    mvcTracker?.clickCekInfoButtonClose(FollowWidgetType.MEMBERSHIP_CLOSE, shopId, UserSession(context).userId, mvcSource, btnFirst.text?.toString())
                }
            }
        }
    }

    fun showTokomemberBottomSheet(followWidget: FollowWidget, activity: AppCompatActivity, shopId: String, @MvcSource mvcSource: Int, mvcTracker: MvcTracker?) {
        if (followWidget.membershipHowTo.isNullOrEmpty()) {
            return
        }
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
        val cta = child.findViewById<UnifyButton>(R.id.btn)

        when (followWidget.type) {
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                cta.setOnClickListener {
                    mvcTracker?.clickDaftarJadiMember(shopId, UserSession(context).userId, mvcSource)
                    bottomsheet.dismiss()
                }
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                if (mvcSource == MvcSource.PDP) {
                    cta.visibility = View.GONE
                } else {
                    cta.text = context.resources.getString(R.string.mvc_mulai_belanja)
                    cta.setOnClickListener {
                        RouteManager.route(
                            context,
                            ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT,
                            shopId
                        )
                        mvcTracker?.clickDaftarJadiMember(
                            shopId,
                            UserSession(context).userId,
                            mvcSource
                        )
                    }
                }
            }
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
            mvcTracker?.viewTokomemberBottomSheet(shopId, UserSession(context).userId, mvcSource)
        }
        bottomsheet.show(activity.supportFragmentManager, "btm_mvc_tokomember")
    }

    private fun returnTextFromHtml(t: String): CharSequence {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HtmlUtil.fromHtml(t).trim()
        } else {
            Html.fromHtml(t).trim()
        }
    }

    private fun setAdjustableMargin() {
        val marginLayoutParams = containerContent.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.bottomMargin =
            containerContent.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
        containerContent.layoutParams = marginLayoutParams
    }
}
