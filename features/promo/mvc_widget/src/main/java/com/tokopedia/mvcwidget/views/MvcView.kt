package com.tokopedia.mvcwidget.views

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.htmltags.HtmlUtil
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay


/*
* 1. It has internal Padding of 6dp to render its shadows
* 2. isMainContainerSetFitsSystemWindows must be true if activity/fragment layout is setFitsSystemWindows(false) or setFitsSystemWindows = false
* */
class MvcView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var tvTitle: Typography
    lateinit var tvSubTitle: Typography
    lateinit var imageChevron: AppCompatImageView
    lateinit var imageCoupon: AppCompatImageView
    lateinit var mvcContainer: View
    lateinit var mvcAnimContainer: ConstraintLayout
    var shopId: String = ""
    var isMainContainerSetFitsSystemWindows = false
    @MvcSource
    var source: Int = MvcSource.SHOP
    var iconListRunnable: Runnable? = null

    init {
        View.inflate(context, R.layout.mvc_entry_view, this)
        initViews()
        setClicks()
    }

    private fun initViews() {
        tvTitle = this.findViewById(R.id.tvTitle)
        tvSubTitle = this.findViewById(R.id.tvSubTitle)
        imageChevron = this.findViewById(R.id.image_chevron)
        imageCoupon = this.findViewById(R.id.image_coupon)
        mvcContainer = this.findViewById(R.id.mvc_container)
        mvcAnimContainer = this.findViewById(R.id.mvcAnimViewContainer)
    }

    private fun setClicks() {
        mvcContainer.setOnClickListener {
            context.startActivity(TransParentActivity.getIntent(context, shopId, this.source))
            Tracker.userClickEntryPoints(shopId,UserSession(context).userId,this.source)
        }
    }

    fun setData(mvcData: MvcData, shopId: String, isMainContainerSetFitsSystemWindows: Boolean = false, @MvcSource source: Int) {
        this.source = source
        this.isMainContainerSetFitsSystemWindows = isMainContainerSetFitsSystemWindows
        this.shopId = shopId

        val iconList = mvcData.animatedInfos
        if (iconList.size > 1) {
            animateView(iconList)
        } else {
            setMVCData(iconList[0].title?: "", iconList[0].subTitle?: "", iconList[0].iconURL?: "")
        }
    }

    private fun animateView(iconList: List<AnimatedInfos>) {

        iconListRunnable = object : Runnable {
            var currentIndex = 0
            var updateInterval = 2000L

            override fun run() {
                val title = iconList[currentIndex].title ?: ""
                val subTitle = iconList[currentIndex].subTitle ?: ""
                val iconUrl = iconList[currentIndex].iconURL ?: ""

                setMVCData(title, subTitle, iconUrl)
                translateView(mvcAnimContainer)
                currentIndex += 1
                if (currentIndex == iconList.size) {
                    currentIndex = 0
                }

                imageCoupon.postDelayed(this, updateInterval)
            }
        }

        iconListRunnable?.run()
    }

    private fun translateView(view: View){
        view.slideUpFromBottom(completion = {
            view.slideUpFromMiddle()
        })
    }

    private fun setMVCData(titles:String, subTitle:String,imageUrl:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTitle.text = HtmlUtil.fromHtml(titles).trim()
        } else {
            tvTitle.text = Html.fromHtml(titles).trim()
        }
        tvSubTitle.text = subTitle

        if (!(context as Activity).isFinishing) {
            Glide.with(imageCoupon.context)
                .load(imageUrl)
                .dontAnimate()
                .into(imageCoupon)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(iconListRunnable)
    }

}