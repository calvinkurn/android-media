package com.tokopedia.gamification.giftbox.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.gamification.R

import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxNonLoginFragment
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment

class GiftBoxTapTapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift_box_daily)
        if (savedInstanceState == null) {
            showGiftBoxFragment()
        }
    }

    fun showGiftBoxFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fm, GiftBoxTapTapFragment())
            .commit()
    }

    fun showNonLoginFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fm, GiftBoxNonLoginFragment())
            .commit()
    }
}