package com.tokopedia.topads.common.view.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.AutoAdsStatus
import com.tokopedia.topads.common.data.internal.NonDeliveryReason
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.di.DaggerTopAdsCommonComponent
import com.tokopedia.topads.common.di.TopAdsCommonComponent
import com.tokopedia.topads.common.di.module.TopAdsCommonModule
import com.tokopedia.topads.common.view.AutoAdsWidgetViewModelCommon
import com.tokopedia.topads.common.view.sheet.ManualAdsConfirmationCommonSheet
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_common_auto_edit_status_active_widget.view.*
import javax.inject.Inject

/**
 * Created by Pika on 17/9/20.
 */

private const val CLICK_TAMBHA_STOK = "click - tambah stok"
private const val CLICK_TOP_UP_KREDIT = "click - top up kredit"
private const val CLICK_CEK_STATUS = "click - cek status"
private const val CLICK_TINGA_KATLAN = "click - tingkatkan"
private const val CLICK_SETTING_ICON = "click - settings icon"
private const val ENTRY_FROM_DETAIL_SHEET = 2
private const val ENTRY_FROM_EDIT_PAGE = 1

class AutoAdsWidgetCommon(context: Context, attrs: AttributeSet?) : CardUnify(context, attrs) {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var baseLayout: FrameLayout? = null
    private var entryPoint: Int = 0
    private var currentBudget = 0
    private val outOfDailyBudget = 0
    private val outOfCredit = 1
    private val merchantClosed = 2
    private val outOfStock = 3
    private val AUTO_ADS_DISABLED = 111

    private val viewModelProvider by lazy {
        ViewModelProviders.of(context as BaseActivity, viewModelFactory)
    }
    private val widgetViewModel by lazy {
        viewModelProvider.get(AutoAdsWidgetViewModelCommon::class.java)
    }

    init {
        initView(context)
        renderUI()
    }

    companion object {
        private const val TOGGLE_OFF = "toggle_off"
        private const val CHANNEL = "topchat"
        private const val SOURCE = "sellerapp_autoads_creation"
        private const val source = "update_auto_ads"
        private const val TOPUP_LINK = " TopUp Sekarang"
        private const val MANAGE_PRODUCT = " Tambah Stok"
        private const val MERCHANT_SETTING = " Cek Status"
        private const val EDIT_ADS = " Tambah Anggaran"
        private const val MANAGE_PRODUCT_LINK = "tokopedia://seller/product/manage"
        private const val MERCHANT_SETTING_LINK = "tokopedia-android-internal://marketplace/shop-settings-info"

    }

    private fun renderUI() {
        widgetViewModel.autoAdsData.observe(context as BaseActivity, Observer {
            currentBudget = it.dailyBudget
            if (it.status == AutoAdsStatus.STATUS_NOT_DELIVERED) {
                widgetViewModel.getNotDeliveredReason(userSession.shopId)
            } else
                setUiComponent(it.status, it.dailyUsage)
        })
        widgetViewModel.adsDeliveryStatus.observe(context as BaseActivity, Observer {
            if (it.status == 2)
                setUi(it.statusDetail)
        })
        widgetViewModel.autoAdsStatus.observe(context as BaseActivity, Observer {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
                putExtra(TopAdsCommonConstant.TOPADS_MOVE_TO_DASHBOARD, TopAdsCommonConstant.PARAM_PRODUK_IKLAN)
            }
            (context as BaseActivity).setResult(Activity.RESULT_OK)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            (context as BaseActivity).startActivity(intent)
        })
    }

    private fun setUiComponent(status: Int, dailyUsage: Int) {
        when (status) {
            AutoAdsStatus.STATUS_ACTIVE -> setActive(dailyUsage)
            AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE -> setInProgress()
            AutoAdsStatus.STATUS_IN_PROGRESS_AUTOMANAGE -> setInProgress()
            AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE -> setInProgressInactive()
        }
    }

    private fun setUi(statusDetail: String) {
        when (statusDetail) {
            NonDeliveryReason.NO_BALANCE -> setNoBalanceView()
            NonDeliveryReason.OUT_OF_STOCK -> setOutOfStockView()
            NonDeliveryReason.OUT_OF_BUDGET -> setOutOfBudgetView()
            NonDeliveryReason.SHOP_INACTIVE -> setShopInactiveView()
        }
    }

    private fun setShopInactiveView() {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_merchantclosed_widget,
                this,
                false
        )
        getDrwableforNotDeliverd(view)
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
        setSpannable(MERCHANT_SETTING, view, merchantClosed)
        setSwitchAction(view)
    }

    private fun getDrwableforNotDeliverd(view: View) {
        val imgBg = view.findViewById<ConstraintLayout>(R.id.auto_ad_status_image)
        imgBg.background = AppCompatResources.getDrawable(context, R.drawable.topads_common_orange_bg)
    }

    private fun setOutOfBudgetView() {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_outofdaily_budget,
                this,
                false
        )
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
        getDrwableforNotDeliverd(view)
        val desc = view.findViewById<TextView>(R.id.status_desc)
        if (entryPoint == 1) {
            desc.text = resources.getString(R.string.topads_common_autoads_outofbudget_desc_edit)
        } else {
            desc.text = resources.getString(R.string.topads_common_autoads_outofbudget_desc)
            setSpannable(EDIT_ADS, view, outOfDailyBudget)
        }
        setSwitchAction(view)
    }

    private fun setOutOfStockView() {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_outofstock_widget,
                this,
                false
        )
        getDrwableforNotDeliverd(view)
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
        setSpannable(MANAGE_PRODUCT, view, outOfStock)
        setSwitchAction(view)
    }

    private fun setSpannable(moreInfo: String, view: View, status: Int) {
        val desc = view.findViewById<TextView>(R.id.status_desc)
        val spannableText = SpannableString(moreInfo)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                when (status) {
                    outOfCredit -> {
                        RouteManager.route(context, TOPADS_BUY_CREDIT)
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_TOP_UP_KREDIT, "")
                    }
                    outOfStock -> {
                        RouteManager.route(context, MANAGE_PRODUCT_LINK)
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_TAMBHA_STOK, "")
                    }
                    outOfDailyBudget -> {
                        startEditActivity()
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_TINGA_KATLAN, "")
                    }
                    merchantClosed -> {
                        RouteManager.route(context, MERCHANT_SETTING_LINK)
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_CEK_STATUS, "")
                    }
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        desc.movementMethod = LinkMovementMethod.getInstance()
        desc.append(spannableText)
    }

    private fun setSwitchAction(view: View) {
        val switch = view.findViewById<SwitchUnify>(R.id.btn_switch)
        val setting = view.findViewById<ImageView>(R.id.setting)
        setting.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.topads_common_setting))
        switch.isChecked = true
        when (entryPoint) {
            ENTRY_FROM_EDIT_PAGE -> {
                setting.visibility = View.GONE
                switch.visibility = View.VISIBLE
                switch.setOnClickListener {
                    val manual = ManualAdsConfirmationCommonSheet.newInstance()
                    manual.show((view.context as FragmentActivity).supportFragmentManager, "")
                    manual.dismissed = { switch.isChecked = true }
                    manual.manualClick = { switchToManual() }
                }
            }
            ENTRY_FROM_DETAIL_SHEET -> {
                switch.visibility = View.INVISIBLE
                setting.visibility = View.INVISIBLE
            }
            else -> {
                setting.visibility = View.VISIBLE
                switch.visibility = View.INVISIBLE
                setting.setOnClickListener {
                    startEditActivity()
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_SETTING_ICON, "")
                }
            }
        }
    }

    private fun setNoBalanceView() {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_outofcredit_widget,
                this,
                false
        )
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
        getDrwableforNotDeliverd(view)
        val desc = view.findViewById<TextView>(R.id.status_desc)
        view.let {
            if (entryPoint == ENTRY_FROM_EDIT_PAGE)
                desc.text = resources.getString(R.string.topads_common_autoads_outofcredit_desc_edit)
            else
                desc.text = resources.getString(R.string.topads_common_autoads_outofcredit_desc)
        }
        setSpannable(TOPUP_LINK, view, outOfCredit)
        setSwitchAction(view)
    }

    private fun setInProgress() {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_progress_widget,
                this,
                false
        )
        val imgBg = view.findViewById<ConstraintLayout>(R.id.auto_ad_status_image)
        imgBg.background = AppCompatResources.getDrawable(context, R.drawable.topads_common_blue_bg)
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
    }

    private fun setInProgressInactive() {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_progress_widget,
                this,
                false
        )
        val imgBg = view.findViewById<ConstraintLayout>(R.id.auto_ad_status_image)
        imgBg.background = AppCompatResources.getDrawable(context, R.drawable.topads_common_blue_bg)
        view.findViewById<TextView>(R.id.status_desc).text = context.getString(R.string.topads_common_autoads_inprogress_deactivate_desc)
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
    }

    private fun setActive(dailyUsage: Int) {
        val view = LayoutInflater.from(context).inflate(
                R.layout.topads_common_auto_edit_status_active_widget,
                this,
                false
        )
        val imgBg = view.findViewById<ConstraintLayout>(R.id.auto_ad_status_image)
        val drawable = AppCompatResources.getDrawable(context, R.drawable.topads_common_green_bg)
        baseLayout?.removeAllViews()
        baseLayout?.addView(view)
        view.let {
            imgBg.background = drawable
            it.progress_status1.text = "Rp $dailyUsage"
            it.progress_status2.text = String.format(view.context.resources.getString(R.string.topads_dash_group_item_progress_status), currentBudget)
            it.progress_bar.setValue(dailyUsage, true)
            setSwitchAction(view)
        }
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
        (context as BaseActivity).startActivityForResult(intent, AUTO_ADS_DISABLED)
    }

    private fun switchToManual() {
        widgetViewModel.postAutoAds(AutoAdsParam(AutoAdsParam.Input(
                TOGGLE_OFF,
                CHANNEL,
                currentBudget,
                userSession.shopId.toInt(),
                SOURCE))
        )
    }

    fun loadData(fromEdit: Int) {
        this.entryPoint = fromEdit
        widgetViewModel.getAutoAdsStatus(userSession.shopId.toInt())
    }

    private fun initView(context: Context) {
        try {
            getComponent(context).inject(this)
            View.inflate(context, R.layout.topads_common_autoads_edit_base_widget, this)
            baseLayout = findViewById(R.id.base_layout)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getComponent(context: Context): TopAdsCommonComponent = DaggerTopAdsCommonComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).topAdsCommonModule(TopAdsCommonModule(context)).build()
}