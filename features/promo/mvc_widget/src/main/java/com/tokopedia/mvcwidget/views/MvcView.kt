package com.tokopedia.mvcwidget.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.user.session.UserSession
import java.lang.ref.WeakReference
import java.util.*


/*
* 1. It has internal Padding of 6dp to render its shadows
* */
class MvcView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    companion object{
        const val REQUEST_CODE = 121
        const val RESULT_CODE_OK = 1
    }

    lateinit var imageChevron: AppCompatImageView
    lateinit var imageCouponBg: AppCompatImageView
    lateinit var mvcTextContainerFirst: MvcTextContainer
    lateinit var mvcTextContainerSecond: MvcTextContainer
    lateinit var mvcContainer: View

    lateinit var mvcAnimationHandler: MvcAnimationHandler

    var shopId: String = ""

    @MvcSource
    var source: Int = MvcSource.SHOP

    init {
        View.inflate(context, R.layout.mvc_entry_view, this)
        initViews()
        setClicks()

        mvcAnimationHandler = MvcAnimationHandler(WeakReference(mvcTextContainerFirst),WeakReference(mvcTextContainerSecond))
    }

    private fun initViews() {
        imageChevron = this.findViewById(R.id.image_chevron)
        mvcContainer = this.findViewById(R.id.mvc_container)
        mvcTextContainerFirst = this.findViewById(R.id.mvc_text_container_first)
        mvcTextContainerSecond = this.findViewById(R.id.mvc_text_container_second)
    }

    private fun setClicks() {
        mvcContainer.setOnClickListener {
            if (context is AppCompatActivity){
                (context as AppCompatActivity).startActivityForResult(TransParentActivity.getIntent(context, shopId, this.source), REQUEST_CODE)
            }else{
                (context).startActivity(TransParentActivity.getIntent(context, shopId, this.source))
            }
            Tracker.userClickEntryPoints(shopId, UserSession(context).userId, this.source)
        }
    }

    fun setData(mvcData: MvcData, shopId: String, @MvcSource source: Int) {
        this.source = source
        this.shopId = shopId
        setMVCData(mvcData.animatedInfoList)
    }

    private fun setMVCData(animatedInfos: List<AnimatedInfos?>?) {

        if (!animatedInfos.isNullOrEmpty()) {
            if(animatedInfos.size == 1){
                val animatedInfo = animatedInfos.first()
                animatedInfo?.let {
                    mvcTextContainerFirst.setData(it.title ?: "", it.subTitle ?: "", it.iconURL ?: "")
                }
            }else{
                    mvcAnimationHandler.animatedInfoList = animatedInfos
                    mvcAnimationHandler.startTimer()
            }

        }
    }

}