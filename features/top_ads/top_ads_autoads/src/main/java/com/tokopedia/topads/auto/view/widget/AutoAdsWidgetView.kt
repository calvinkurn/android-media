package com.tokopedia.topads.auto.view.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.constant.TopAdsWidgetStatus
import com.tokopedia.topads.auto.view.activity.SettingBudgetAdsActivity

/**
 * Author errysuprayogi on 14,May,2019
 */
class AutoAdsWidgetView : CardView {

    private lateinit var statusAdsContainer: View
    private lateinit var activeStatus: TextView

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        useCompatPadding = true
        radius = resources.getDimension(R.dimen.dp_8)
        View.inflate(context, R.layout.layout_auto_ads_widget, this)
        statusAdsContainer = findViewById(R.id.status_ads_container)
        activeStatus = findViewById(R.id.active_status)

        setupListener()
    }

    private fun setupListener() {
        statusAdsContainer.setOnClickListener {
            context.startActivity(Intent(context, SettingBudgetAdsActivity::class.java))
        }
    }

    fun setStatusAds(status : Int){
        when(status){
            TopAdsWidgetStatus.STATUS_ACTIVE -> setActive()
            TopAdsWidgetStatus.STATUS_INACTIVE -> setInActive()
            TopAdsWidgetStatus.STATUS_IN_PROGRESS -> setInProgress()
        }
    }

    private fun setInProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Inprogress)
        } else{
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_inprogress)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_300))
        }
        activeStatus.setText(R.string.ads_inprogress)
    }

    private fun setInActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Inactive)
        } else{
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_deactive)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.grey))
        }
        activeStatus.setText(R.string.ads_deactive)
    }

    private fun setActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Active)
        } else{
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_active)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
        activeStatus.setText(R.string.ads_active)
    }

}
