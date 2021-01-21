package com.tokopedia.mvcwidget.views

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil

/*
* Pending
* 1. Setting green image
* 2. Shadow background
* */
class MvcView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var tvTitle: Typography
    lateinit var tvSubTitle: Typography
    lateinit var imageChevron: AppCompatImageView
    lateinit var imageCoupon: AppCompatImageView
    var shopId: String = ""

    init {
        View.inflate(context, R.layout.mvc_entry_view, this)
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(64).toInt())
        this.layoutParams = lp
        val startPadding = dpToPx(16).toInt()
        val topPadding = dpToPx(8).toInt()
        setPadding(startPadding, topPadding, startPadding, 0)
        initViews()
        setClicks()
    }

    private fun initViews() {
        tvTitle = this.findViewById(R.id.tvTitle)
        tvSubTitle = this.findViewById(R.id.tvSubTitle)
        imageChevron = this.findViewById(R.id.image_chevron)
        imageCoupon = this.findViewById(R.id.image_coupon)
    }

    private fun setClicks() {
        imageChevron.setOnClickListener {
            val bottomSheet = BottomSheetUnify()
            bottomSheet.setTitle("Daftar Kupon Toko")
            val childView = MvcDetailView(context)
            bottomSheet.setChild(childView)
            bottomSheet.show((context as AppCompatActivity).supportFragmentManager, "BottomSheet Tag")
            childView.show(shopId, )
            bottomSheet.setShowListener {
                val imageMargin = dpToPx(20).toInt()
                bottomSheet.bottomSheetWrapper.setPadding(0, dpToPx(20).toInt(), 0, 0)
                bottomSheet.bottomSheetClose.setImageResource(R.drawable.mvc_dialog_close)
                bottomSheet.bottomSheetClose.setMargin(imageMargin, 0, dpToPx(20).toInt(), 0)
            }

        }
    }

    fun setData(mvcData: MvcData, shopId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTitle.text = HtmlUtil.fromHtml(mvcData.title).trim()
        } else {
            tvTitle.text = Html.fromHtml(mvcData.title).trim()
        }

        tvSubTitle.text = mvcData.subTitle
        this.shopId = shopId

//        Glide.with(imageCoupon)
//                .load(mvcData.imageUrl)
//                .dontAnimate()
//                .into(imageCoupon)
    }

}