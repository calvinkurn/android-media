package com.tokopedia.gamification.giftbox.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.applink.RouteManager

import com.tokopedia.gamification.R
import kotlinx.android.synthetic.main.activity_gift_launcher.*

class GiftLauncherActivity:AppCompatActivity() {

    companion object{
        var uiType = UiType.COUPON_POINTS
        var iS_STAGING = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift_launcher)
        btnShowCouponAndPoints.setOnClickListener {
            iS_STAGING = false
            uiType = UiType.COUPON_POINTS
            openGiftBoxActivity()
        }
        btnShowPoints.setOnClickListener {
            iS_STAGING = false
            uiType = UiType.POINTS
            openGiftBoxActivity()
        }
    }

    fun openGiftBoxActivity() {
        iS_STAGING = false
        startActivity(Intent(this, GiftBoxDailyActivity::class.java))
    }

    fun openGiftBoxTapTapActivity(v: View) {
        iS_STAGING = false
        startActivity(Intent(this, GiftBoxTapTapActivity::class.java))
    }

    fun openWithStagingApi(v:View){
        iS_STAGING = true
        startActivity(Intent(this, GiftBoxDailyActivity::class.java))
    }

    fun launchStaticPage(v:View){
        val url = "https://76.staging-feature.tokopedia.com/kejutan-ramadan-ekstra/pre-giftbox-update"
        RouteManager.route(this, url)
    }

    fun launchDigital(v:View){
        val url = "tokopedia://digital/form?category_id=1"
        RouteManager.route(this, url)
    }

    enum class UiType{
        COUPON_POINTS, POINTS
    }
}