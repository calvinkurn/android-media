package com.tokopedia.topads.auto.view.widget

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.internal.TopAdsWidgetStatus
import com.tokopedia.topads.auto.view.activity.SettingBudgetAdsActivity
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.view.factory.AutoAdsWidgetViewModelFactory
import com.tokopedia.topads.auto.view.viewmodel.AutoAdsWidgetViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Author errysuprayogi on 14,May,2019
 */
class AutoAdsWidgetView : CardView {

    private lateinit var statusAdsContainer: View
    private lateinit var activeStatus: TextView
    private lateinit var dailyUsageStatus: TextView
    private lateinit var dailyBudgetStatus: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var widgetViewModel: AutoAdsWidgetViewModel

    @Inject
    lateinit var factory: AutoAdsWidgetViewModelFactory
    @Inject
    lateinit var userSession: UserSessionInterface

    constructor(context: Context) : super(context) {
        initView(context)
        setupListener()
        renderUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
        setupListener()
        renderUI()
    }

    private fun getComponent(context: Context): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build()

    private fun initView(context: Context) {
        getComponent(context).inject(this)
        useCompatPadding = true
        radius = resources.getDimension(R.dimen.dp_8)
        View.inflate(context, R.layout.layout_auto_ads_widget, this)
        statusAdsContainer = findViewById(R.id.status_ads_container)
        activeStatus = findViewById(R.id.active_status)
        dailyBudgetStatus = findViewById(R.id.daily_budget_status)
        dailyUsageStatus = findViewById(R.id.daily_usage_status)
        progressBar = findViewById(R.id.progress_bar)
        setupListener()
    }

    private fun setupListener() {
        statusAdsContainer.setOnClickListener {
            context.startActivity(Intent(context, SettingBudgetAdsActivity::class.java))
        }
    }

    private fun renderUI() {
        widgetViewModel = ViewModelProviders.of(context as BaseSimpleActivity, factory).get(AutoAdsWidgetViewModel::class.java)
        widgetViewModel.getAutoAdsStatus(userSession.shopId.toInt())
        widgetViewModel.autoAdsData.observe(context as BaseSimpleActivity, Observer {
            if (it!!.status != 0) {
                setStatusAds(it!!.status)
                visibility = View.VISIBLE
                dailyBudgetStatus.text = String.format(context.getString(R.string.anggaran_harian_status), it!!.dailyBudget)
                dailyUsageStatus.text = String.format(context.getString(R.string.terpakai), it!!.dailyUsage)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(it!!.dailyUsage, true)
                } else {
                    progressBar.progress = it!!.dailyUsage
                }
            }
        })
    }

    fun setStatusAds(status: Int) {
        when (status) {
            TopAdsWidgetStatus.STATUS_ACTIVE -> setActive()
            TopAdsWidgetStatus.STATUS_INACTIVE -> setInActive()
            TopAdsWidgetStatus.STATUS_IN_PROGRESS_ACTIVE -> setInProgress()
            TopAdsWidgetStatus.STATUS_IN_PROGRESS_AUTOMANAGE -> setInProgress()
            TopAdsWidgetStatus.STATUS_IN_PROGRESS_INACTIVE -> setInProgress()
            TopAdsWidgetStatus.STATUS_NOT_DELIVERED -> setNotDelivered()
            else -> setInActive()
        }
    }

    private fun setNotDelivered() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Inprogress)
        } else {
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_deactive)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.grey))
        }
        activeStatus.setText(R.string.ads_not_delivered)
    }

    private fun setInProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Inprogress)
        } else {
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_inprogress)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_300))
        }
        activeStatus.setText(R.string.ads_inprogress)
    }

    private fun setInActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Inactive)
        } else {
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_deactive)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.grey))
        }
        activeStatus.setText(R.string.ads_deactive)
    }

    private fun setActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeStatus.setTextAppearance(R.style.AutoAdsText_Active)
        } else {
            activeStatus.setBackgroundResource(R.drawable.bg_autoads_active)
            activeStatus.setTextColor(ContextCompat.getColor(context, R.color.true_green))
        }
        activeStatus.setText(R.string.ads_active)
    }

}
