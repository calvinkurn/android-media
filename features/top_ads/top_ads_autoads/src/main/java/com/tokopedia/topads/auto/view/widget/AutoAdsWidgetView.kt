package com.tokopedia.topads.auto.view.widget

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.internal.TopAdsWidgetStatus
import com.tokopedia.topads.auto.view.activity.SettingBudgetAdsActivity
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.view.activity.DailyBudgetActivity
import com.tokopedia.topads.auto.view.factory.AutoAdsWidgetViewModelFactory
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment
import com.tokopedia.topads.auto.view.viewmodel.AutoAdsWidgetViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Author errysuprayogi on 14,May,2019
 */
class AutoAdsWidgetView : CardView {

    private lateinit var statusAdsContainer: View
    private lateinit var btnArrow: View
    private lateinit var progressAdsContainer: View
    private lateinit var activeStatus: TextView
    private lateinit var dailyUsageStatus: TextView
    private lateinit var dailyBudgetStatus: TextView
    private lateinit var subTitle: TextView
    private lateinit var startAdsBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var widgetViewModel: AutoAdsWidgetViewModel
    private var activeListener: ActiveListener? = null

    @Inject
    lateinit var factory: AutoAdsWidgetViewModelFactory
    @Inject
    lateinit var userSession: UserSessionInterface

    constructor(context: Context) : super(context) {
        initView(context)
        renderUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
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
        progressAdsContainer = findViewById(R.id.container_progress_status)
        activeStatus = findViewById(R.id.active_status)
        subTitle = findViewById(R.id.subtitle)
        dailyBudgetStatus = findViewById(R.id.daily_budget_status)
        dailyUsageStatus = findViewById(R.id.daily_usage_status)
        progressBar = findViewById(R.id.progress_bar)
        startAdsBtn = findViewById(R.id.button_start_autoads)
        btnArrow = findViewById(R.id.btn_arrow)
    }

    fun fetchData() {
        widgetViewModel.getAutoAdsStatus(userSession.shopId.toInt())
    }

    private fun renderUI() {
        widgetViewModel = ViewModelProviders.of(context as BaseSimpleActivity, factory).get(AutoAdsWidgetViewModel::class.java)
        widgetViewModel.autoAdsData.observe(context as BaseSimpleActivity, Observer {
            if (it!!.status != 0) {
                visibility = View.VISIBLE
                setStatusAds(it!!.status, it!!.dailyBudget)
                dailyBudgetStatus.text = String.format(context.getString(R.string.anggaran_harian_status), it!!.dailyBudget.toDouble())
                dailyUsageStatus.text = String.format(context.getString(R.string.terpakai), it!!.dailyUsage.toDouble())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(it!!.dailyUsage, true)
                } else {
                    progressBar.progress = it!!.dailyUsage
                }
            }
        })
    }

    fun setStatusAds(status: Int, budget: Int) {
        when (status) {
            TopAdsWidgetStatus.STATUS_ACTIVE -> setActive(budget)
            TopAdsWidgetStatus.STATUS_INACTIVE -> setInActive(budget)
            TopAdsWidgetStatus.STATUS_IN_PROGRESS_ACTIVE -> setInProgress()
            TopAdsWidgetStatus.STATUS_IN_PROGRESS_AUTOMANAGE -> setInProgress()
            TopAdsWidgetStatus.STATUS_IN_PROGRESS_INACTIVE -> setInProgress()
            TopAdsWidgetStatus.STATUS_NOT_DELIVERED -> setNotDelivered(budget)
            else -> setInActive(budget)
        }
    }

    private fun setNotDelivered(budget: Int) {
        activeStatus.setBackgroundResource(R.drawable.bg_autoads_deactive)
        activeStatus.setTextColor(ContextCompat.getColor(context, R.color.grey))
        activeStatus.setText(R.string.ads_not_delivered)
        subTitle.setText(R.string.saatnya_beriklan_mudah_dan_efektif)
        startAdsBtn.visibility = View.GONE
        progressAdsContainer.visibility = View.GONE
        btnArrow.visibility = View.VISIBLE
        activeListener?.onActive()
        statusAdsContainer.setOnClickListener {
            val intent = Intent(context, SettingBudgetAdsActivity::class.java)
            intent.putExtra(DailyBudgetFragment.KEY_DAILY_BUDGET, budget)
            context.startActivity(intent)
        }
    }

    private fun setInProgress() {
        activeStatus.setBackgroundResource(R.drawable.bg_autoads_inprogress)
        activeStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_300))
        activeStatus.setText(R.string.ads_inprogress)
        subTitle.setText(R.string.akan_aktif_dalam_beberapa_menit)
        startAdsBtn.visibility = View.GONE
        progressAdsContainer.visibility = View.GONE
        btnArrow.visibility = View.GONE
        activeListener?.onInActive()
    }

    private fun setInActive(budget: Int) {
        activeStatus.setBackgroundResource(R.drawable.bg_autoads_deactive)
        activeStatus.setTextColor(ContextCompat.getColor(context, R.color.grey))
        activeStatus.setText(R.string.ads_deactive)
        subTitle.setText(R.string.saatnya_beriklan_mudah_dan_efektif)
        startAdsBtn.visibility = View.VISIBLE
        progressAdsContainer.visibility = View.GONE
        btnArrow.visibility = View.GONE
        activeListener?.onInActive()
        statusAdsContainer.setOnClickListener(null)
        startAdsBtn.setOnClickListener {
            val intent = Intent(context, DailyBudgetActivity::class.java)
            intent.putExtra(DailyBudgetFragment.KEY_DAILY_BUDGET, budget)
            context.startActivity(intent)
        }
    }

    private fun setActive(budget: Int) {
        activeStatus.setBackgroundResource(R.drawable.bg_autoads_active)
        activeStatus.setTextColor(ContextCompat.getColor(context, R.color.true_green))
        activeStatus.setText(R.string.ads_active)
        subTitle.visibility = View.GONE
        startAdsBtn.visibility = View.GONE
        progressAdsContainer.visibility = View.VISIBLE
        btnArrow.visibility = View.VISIBLE
        activeListener?.onActive()
        statusAdsContainer.setOnClickListener {
            val intent = Intent(context, SettingBudgetAdsActivity::class.java)
            intent.putExtra(DailyBudgetFragment.KEY_DAILY_BUDGET, budget)
            context.startActivity(intent)
        }
    }

    fun setActiveListener(activeListener: ActiveListener) {
        this.activeListener = activeListener
    }

    interface ActiveListener {
        fun onActive()
        fun onInActive()
    }
}
