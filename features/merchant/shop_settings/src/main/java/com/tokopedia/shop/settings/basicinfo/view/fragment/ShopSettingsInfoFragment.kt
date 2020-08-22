@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataQuery
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsInfoViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_settings_info.*
import kotlinx.android.synthetic.main.partial_shop_settings_info_basic.*
import kotlinx.android.synthetic.main.partial_shop_settings_info_status.*
import java.util.*
import javax.inject.Inject

class ShopSettingsInfoFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    @Inject
    lateinit var shopSettingsInfoViewModel: ShopSettingsInfoViewModel

    private var needReload: Boolean = false
    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var shopId: String = "0"     // 67726 for testing

    private var progressDialog: ProgressDialog? = null

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(context!!)
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_settings_info, container, false)
        return view
    }

    private fun showShopStatusManageMenu() {
        context?.let { ctx ->
            shopBasicDataModel?.let { shopBasicDataModel ->
                val menus = Menus(ctx)
                menus.setTitle(getString(R.string.shop_settings_manage_status))

                val itemMenusList = ArrayList<Menus.ItemMenus>()
                if (shopBasicDataModel.isOpen == true) {
                    if (StringUtils.isEmptyNumber(shopBasicDataModel.closeSchedule)) {
                        itemMenusList.add(Menus.ItemMenus(getString(R.string.schedule_your_shop_close)))
                    } else {
                        itemMenusList.add(Menus.ItemMenus(getString(R.string.change_schedule)))
                        itemMenusList.add(Menus.ItemMenus(getString(R.string.remove_schedule)))
                    }
                    itemMenusList.add(Menus.ItemMenus(getString(R.string.label_close_shop_now)))
                } else {
                    itemMenusList.add(Menus.ItemMenus(getString(R.string.change_schedule)))
                    itemMenusList.add(Menus.ItemMenus(getString(R.string.label_open_shop_now)))
                }
                menus.itemMenuList = itemMenusList
                menus.setOnItemMenuClickListener { itemMenus, _ ->
                    onItemMenuClicked(itemMenus.title)
                    menus.dismiss()
                }
                menus.show()
            }
        }
    }

    fun onItemMenuClicked(itemMenuTitle: String) {
        when {
            itemMenuTitle.equals(getString(R.string.schedule_your_shop_close), ignoreCase = true) -> {
                val intent = ShopEditScheduleActivity.createIntent(context!!, shopBasicDataModel!!,
                        getString(R.string.schedule_shop_close), false)
                startActivityForResult(intent, REQUEST_EDIT_SCHEDULE)
            }
            itemMenuTitle.equals(getString(R.string.label_close_shop_now), ignoreCase = true) -> {
                val intent = ShopEditScheduleActivity.createIntent(context!!, shopBasicDataModel!!,
                        getString(R.string.label_close_shop_now), true)
                startActivityForResult(intent, REQUEST_EDIT_SCHEDULE)
            }
            itemMenuTitle.equals(getString(R.string.change_schedule), ignoreCase = true) -> {
                val intent = ShopEditScheduleActivity.createIntent(context!!, shopBasicDataModel!!,
                        getString(R.string.change_schedule), false)
                startActivityForResult(intent, REQUEST_EDIT_SCHEDULE)
            }
            itemMenuTitle.equals(getString(R.string.remove_schedule), ignoreCase = true) -> {
                activity?.let { it ->
                    Dialog(it, Dialog.Type.PROMINANCE).apply {
                        setTitle(getString(R.string.remove_schedule))
                        setDesc(getString(R.string.remove_schedule_message))
                        setBtnOk(getString(R.string.action_delete))
                        setBtnCancel(getString(com.tokopedia.design.R.string.label_cancel))
                        setOnOkClickListener {
                            //remove schedule
                            showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
                            shopSettingsInfoViewModel.updateShopSchedule(
                                    action = if (shopBasicDataModel!!.isClosed) ShopScheduleActionDef.CLOSED else ShopScheduleActionDef.OPEN,
                                    closeNow = false,
                                    closeStart = "",
                                    closeEnd = "",
                                    closeNote = ""
                            )
                            dismiss()
                        }
                        setOnCancelClickListener { dismiss() }
                        show()
                    }
                }
            }
            itemMenuTitle.equals(getString(R.string.label_open_shop_now), ignoreCase = true) -> {
                // open now
                showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
                shopSettingsInfoViewModel.updateShopSchedule(
                        action = ShopScheduleActionDef.OPEN,
                        closeNow = false,
                        closeStart = "",
                        closeEnd = "",
                        closeNote = ""
                )
            }
        }
    }

    fun showLoading() {
        viewContent.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        viewContent.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnChangeShopInfo.setOnClickListener {
            val intent = ShopEditBasicInfoActivity.createIntent(context!!, shopBasicDataModel)
            startActivityForResult(intent, REQUEST_EDIT_BASIC_INFO)
        }

        vgShopStatusContainer.setOnClickListener { showShopStatusManageMenu() }
        loadShopBasicData()
        shopSettingsInfoViewModel.validateOsMerchantType(shopId.toInt())

        observeShopBasicData()
        observeShopStatus()
        observeOsMerchantData()
        observeUpdateScheduleData()
    }

    private fun observeUpdateScheduleData() {
        shopSettingsInfoViewModel.updateScheduleResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessUpdateShopSchedule(it.data)
                is Fail -> onErrorUpdateShopSchedule(it.throwable)
            }
        })
    }

    private fun observeShopStatus() {
        shopSettingsInfoViewModel.shopStatusData.observe(this, Observer {
            when (it) {
                is Success -> {
                    val shopStatusData = it.data.result.data
                    userSession.setIsGoldMerchant(!(shopStatusData.isRegularMerchantOrPending()
                            ?: true))

                    if (shopStatusData.isRegularMerchantOrPending()) {
                        showRegularMerchantMembership(shopStatusData)
                    } else {
                        showPowerMerchant(shopStatusData)
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.make(view, getString(R.string.error_get_shop_status), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                    }
                }
            }
        })
    }

    private fun observeShopBasicData() {
        shopSettingsInfoViewModel.shopBasicData.observe(this, Observer {
            when (it) {
                is Success -> {
                    hideLoading()
                    val shopBasicData = it.data

                    // Update userSession
                    val shopName: String = shopBasicData.name ?: ""
                    val shopAvatar = shopBasicData.logo ?: ""
                    userSession.shopName = shopName
                    userSession.shopAvatar = shopAvatar

                    this.shopBasicDataModel = shopBasicData
                    setUIShopBasicData(shopBasicData)
                }
                is Fail -> {
                    onErrorGetShopBasicData(it.throwable)
                }
            }
        })
    }

    private fun observeOsMerchantData() {
        shopSettingsInfoViewModel.checkOsMerchantTypeData.observe(this, Observer {
            when (it) {
                is Success -> {
                    it.data.getIsOfficial.let { osData ->
                        val errMessage = osData.messageError
                        val isOS = osData.data.isOfficial
                        val expiration = osData.data.expiredDate

                        if (errMessage.isEmpty() && isOS) {
                            showOfficialStore(expiration) // Set userSession isOS?
                        }
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.make(view, getString(R.string.error_get_os_merchant), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                    }
                }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_EDIT_SCHEDULE -> if (resultCode == Activity.RESULT_OK) {
                needReload = true
                if (requestCode == REQUEST_EDIT_SCHEDULE && data != null) {
                    val message: String = data.getStringExtra(ShopEditScheduleActivity.EXTRA_MESSAGE)
                    if (!message.isEmpty()) {
                        view?.let {
                            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        }
                    }
                }
            }
            REQUEST_EDIT_BASIC_INFO -> if (resultCode == Activity.RESULT_OK) {
                needReload = true
                if (requestCode == REQUEST_EDIT_BASIC_INFO && data != null) {
                    val message: String = data.getStringExtra(ShopEditBasicInfoActivity.EXTRA_MESSAGE)
                    if (!message.isEmpty()) {
                        view?.let {
                            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (needReload) {
            loadShopBasicData()
            needReload = false
        }
    }

    private fun loadShopBasicData() {
        showLoading()
        shopSettingsInfoViewModel.getShopData(
                shopId,
                includeOS = false
        )
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setUIShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        shopBasicDataModel.let { shopBasicData ->
            tvShopName.text = MethodChecker.fromHtml(shopBasicData.name)
            tvShopDomain.text = shopBasicData.domain?.let {
                if (URLUtil.isNetworkUrl(it)) {
                    it
                } else {
                    getString(com.tokopedia.design.R.string.tokopedia_domain) + "/$it"
                }
            }

            tvShopSlogan.text = shopBasicData.tagline
            tvShopDescription.text = shopBasicData.description

            val logoUrl = shopBasicData.logo
            if (TextUtils.isEmpty(logoUrl)) {
                ImageHandler.loadImage2(ivShopLogo, logoUrl, com.tokopedia.design.R.drawable.ic_shop_default_empty)
            } else {
                ImageHandler.LoadImage(ivShopLogo, logoUrl)
            }

            tvShopStatus.text = if (shopBasicData.isOpen) getString(com.tokopedia.design.R.string.label_open) else getString(com.tokopedia.design.R.string.label_close)
        }
    }

    private fun showRegularMerchantMembership(shopStatusModel: ShopStatusModel?) {
        shopStatusModel?.let {
            container_regular_merchant.visibility = View.VISIBLE
            container_power_merchant.visibility = View.GONE
            container_official_store.visibility = View.GONE
            tv_regular_merchant_type.text = getString(com.tokopedia.design.R.string.label_regular_merchant)
        }
    }

    private fun showPowerMerchant(shopStatusModel: ShopStatusModel) {
        container_power_merchant.visibility = View.VISIBLE
        container_regular_merchant.visibility = View.GONE
        container_official_store.visibility = View.GONE
        iv_logo_power_merchant.visibility = View.VISIBLE
        iv_logo_power_merchant.setImageResource(com.tokopedia.gm.common.R.drawable.ic_power_merchant)
        tv_power_merchant_type.text = getString(com.tokopedia.design.R.string.label_power_merchant)
        tv_power_merchant_expiration.text = "Berlaku hingga ${shopStatusModel.powerMerchant.expiredTime}"
    }

    private fun showOfficialStore(expirationDate: String) {
        container_official_store.visibility = View.VISIBLE
        container_regular_merchant.visibility = View.GONE
        container_power_merchant.visibility = View.GONE
        iv_logo_official_store.visibility = View.VISIBLE
        iv_logo_official_store.setImageResource(R.drawable.ic_official_store)
        tv_official_store.text = getString(com.tokopedia.design.R.string.label_official_store)
        tv_official_store_expiration.text = "Berlaku hingga $expirationDate"
    }

    private fun setTextViewClickSpan(textView: TextView, previousText: CharSequence, learnMoreString: String, onClickLearnMore: (() -> (Unit))) {
        val spannable = SpannableString(learnMoreString)
        val indexStart = 0
        val indexEnd = indexStart + learnMoreString.length

        val color = ContextCompat.getColor(context!!, com.tokopedia.design.R.color.tkpd_main_green)
        spannable.setSpan(ForegroundColorSpan(color), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickLearnMore.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }
        spannable.setSpan(clickableSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = SpannableStringBuilder(previousText).append(" ").append(spannable)
    }

//    private fun navigateToPMSubscribe() {
//        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
//    }

    private fun onErrorGetShopBasicData(throwable: Throwable) {
        hideLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.showEmptyState(context, view, message) { loadShopBasicData() }
    }

    private fun onSuccessUpdateShopSchedule(successMessage: String) {
        hideSubmitLoading()
        activity?.setResult(Activity.RESULT_OK)
        view?.let {
            Toaster.make(it, successMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
        }
        loadShopBasicData()
    }

    private fun onErrorUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    fun showSubmitLoading(message: String) {
        progressDialog = progressDialog ?: ProgressDialog(context)
        progressDialog!!.run {
            if (isShowing) {
                setMessage(message)
                isIndeterminate = true
                setCancelable(false)
                show()
            }
        }
    }

    fun hideSubmitLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shopSettingsInfoViewModel.detachView()
        shopSettingsInfoViewModel.shopBasicData.removeObservers(this)
        shopSettingsInfoViewModel.shopStatusData.removeObservers(this)
        shopSettingsInfoViewModel.checkOsMerchantTypeData.removeObservers(this)
        shopSettingsInfoViewModel.flush()
    }

    companion object {
        private val URL_GAINS_SCORE_POINT = "https://seller.tokopedia.com/edu/skor-toko"
        private val REQUEST_EDIT_BASIC_INFO = 781
        private val REQUEST_EDIT_SCHEDULE = 782

        fun newInstance(): ShopSettingsInfoFragment {
            return ShopSettingsInfoFragment()
        }
    }

}
