package com.tokopedia.gamification.giftbox.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.tokopedia.gamification.R
import kotlinx.android.synthetic.main.activity_gift_launcher.*

class GiftLauncherActivity:AppCompatActivity() {

    companion object{
        var uiType = UiType.COUPON_POINTS
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift_launcher)
        btnShowCouponAndPoints.setOnClickListener {
            uiType = UiType.COUPON_POINTS
            openGiftBoxActivity()
        }
        btnShowPoints.setOnClickListener {
            uiType = UiType.POINTS
            openGiftBoxActivity()
        }
    }

    fun openGiftBoxActivity() {
        startActivity(Intent(this, GiftBoxDailyActivity::class.java))
    }

    fun openGiftBoxTapTapActivity(v: View) {
        startActivity(Intent(this, GiftBoxTapTapActivity::class.java))
    }

    enum class UiType{
        COUPON_POINTS, POINTS
    }
}