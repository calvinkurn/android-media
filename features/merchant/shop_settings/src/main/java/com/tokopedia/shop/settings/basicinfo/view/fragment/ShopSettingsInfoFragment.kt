@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.router.ShopSettingRouter
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditBasicInfoActivity
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.view.presenter.ShopSettingsInfoPresenter
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.FORMAT_DATE
import com.tokopedia.shop.settings.common.util.toReadableString
import kotlinx.android.synthetic.main.fragment_shop_settings_info.*
import kotlinx.android.synthetic.main.partial_shop_settings_info_basic.*
import kotlinx.android.synthetic.main.partial_shop_settings_info_membership.*
import kotlinx.android.synthetic.main.partial_shop_settings_info_status.*
import java.util.*
import javax.inject.Inject

class ShopSettingsInfoFragment : BaseDaggerFragment(), ShopSettingsInfoPresenter.View {
    @Inject
    lateinit var shopSettingsInfoPresenter: ShopSettingsInfoPresenter

    private var needReload: Boolean = false
    private var shopBasicDataModel: ShopBasicDataModel? = null

    private var progressDialog: ProgressDialog? = null

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(context!!)
        super.onCreate(savedInstanceState)
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
                        setBtnCancel(getString(R.string.cancel))
                        setOnOkClickListener {
                            //remove schedule
                            showSubmitLoading(getString(R.string.title_loading))
                            shopSettingsInfoPresenter.updateShopSchedule(
                                    if (shopBasicDataModel!!.isClosed)
                                        ShopScheduleActionDef.CLOSED
                                    else
                                        ShopScheduleActionDef.OPEN,
                                    false, "", "", "")
                            dismiss()
                        }
                        setOnCancelClickListener { dismiss() }
                        show()
                    }
                }
            }
            itemMenuTitle.equals(getString(R.string.label_open_shop_now), ignoreCase = true) -> {
                // open now
                showSubmitLoading(getString(R.string.title_loading))
                shopSettingsInfoPresenter.updateShopSchedule(ShopScheduleActionDef.OPEN, false,
                        "", "", "")
            }
        }
    }

    fun showLoading() {
        scrollViewContent.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        scrollViewContent.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vgShopInfoContainer.setOnClickListener {
            val intent = ShopEditBasicInfoActivity.createIntent(context!!, shopBasicDataModel)
            startActivityForResult(intent, REQUEST_EDIT_BASIC_INFO)
        }

        vgShopStatusContainer.setOnClickListener { showShopStatusManageMenu() }
        loadShopBasicData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_EDIT_SCHEDULE -> if (resultCode == Activity.RESULT_OK) {
                needReload = true
                if (requestCode == REQUEST_EDIT_SCHEDULE && data != null) {
                    val message: String = data.getStringExtra(ShopEditScheduleActivity.EXTRA_MESSAGE)
                    if (!message.isEmpty()) {
                        ToasterNormal.show(activity!!, message)
                    }
                }
            }
            REQUEST_EDIT_BASIC_INFO -> if (resultCode == Activity.RESULT_OK) {
                needReload = true
                if (requestCode == REQUEST_EDIT_BASIC_INFO && data != null) {
                    val message: String = data.getStringExtra(ShopEditBasicInfoActivity.EXTRA_MESSAGE)
                    if (!message.isEmpty()) {
                        ToasterNormal.show(activity!!, message)
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
        shopSettingsInfoPresenter.getShopBasicData()
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        shopSettingsInfoPresenter.attachView(this)
    }

    override fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel?) {
        this.shopBasicDataModel = shopBasicDataModel
        hideLoading()
        shopBasicDataModel?.let {
            setUIShopBasicData(it)
            setUIStatus(it)
            setUIMembership(it)
        }
    }

    private fun setUIShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        tvShopName.text = MethodChecker.fromHtml(shopBasicDataModel.name)
        tvShopDomain.text = shopBasicDataModel.domain?.let {
            if (URLUtil.isNetworkUrl(it)) {
                it
            } else {
                getString(R.string.tokopedia_domain) + "/$it"
            }
        }
        if (shopBasicDataModel.tagline.isNullOrBlank()) {
            tvShopSloganTitle.visibility = View.GONE
            tvShopSlogan.visibility = View.GONE
        } else {
            tvShopSlogan.text = shopBasicDataModel.tagline
            tvShopSloganTitle.visibility = View.VISIBLE
            tvShopSlogan.visibility = View.VISIBLE
        }

        if (shopBasicDataModel.description.isNullOrBlank()) {
            tvShopDescriptionTitle.visibility = View.GONE
            tvShopDescription.visibility = View.GONE
        } else {
            tvShopDescription.text = shopBasicDataModel.description
            tvShopDescriptionTitle.visibility = View.VISIBLE
            tvShopDescription.visibility = View.VISIBLE
        }

        val logoUrl = shopBasicDataModel.logo
        if (TextUtils.isEmpty(logoUrl)) {
            ivShopLogo.setImageResource(R.drawable.ic_shop_default_empty)
        } else {
            ImageHandler.LoadImage(ivShopLogo, logoUrl)
        }
    }

    private fun setUIStatus(shopBasicDataModel: ShopBasicDataModel) {
        if (shopBasicDataModel.isOpen) {
            tvShopStatus.text = getString(R.string.label_open)

            val stringBuilder = StringBuilder()
            val closeScheduleUnixString = shopBasicDataModel.closeSchedule
            if (!StringUtils.isEmptyNumber(closeScheduleUnixString)) {
                val closeString = toReadableString(FORMAT_DATE, closeScheduleUnixString!!)
                stringBuilder.append(getString(R.string.closed_schedule, closeString))
            }

            val closeUntilUnixString = shopBasicDataModel.closeUntil
            if (!StringUtils.isEmptyNumber(closeUntilUnixString)) {
                val openString = toReadableString(FORMAT_DATE, closeUntilUnixString!!)
                stringBuilder.append(" - ")
                stringBuilder.append(openString)
            }
            val closeSchedulString = stringBuilder.toString()
            if (TextUtils.isEmpty(closeSchedulString)) {
                tvShopCloseSchedule.visibility = View.GONE
            } else {
                tvShopCloseSchedule.text = stringBuilder.toString()
                tvShopCloseSchedule.visibility = View.VISIBLE
            }
        } else {
            tvShopStatus.text = getString(R.string.label_close)

            val openScheduleUnixString = shopBasicDataModel.openSchedule
            var openScheduleString: String? = null
            if (!StringUtils.isEmptyNumber(openScheduleUnixString)) {
                val openString = toReadableString(FORMAT_DATE, openScheduleUnixString!!)
                openScheduleString = getString(R.string.open_schedule, openString)
            }

            if (TextUtils.isEmpty(openScheduleString)) {
                tvShopCloseSchedule.visibility = View.GONE
            } else {
                tvShopCloseSchedule.text = openScheduleString
                tvShopCloseSchedule.visibility = View.VISIBLE
            }
        }
    }

    private fun setUIMembership(shopBasicDataModel: ShopBasicDataModel) {
        if (shopBasicDataModel.isRegular) {
            ivShopMembership.setImageDrawable(GMConstant.getGMRegularBadgeDrawable(activity))
            ivShopMembership.setPadding(0, 0, 0, 0)
            tvMembershipName.text = getString(R.string.label_regular_merchant)

            val goldMerchantString = getString(GMConstant.getGMTitleResource(context))
            val goldMerchantInviteString = getString(R.string.shop_settings_gold_merchant_invite, goldMerchantString);
            val spannable = SpannableString(goldMerchantInviteString)
            val indexStart = goldMerchantInviteString.indexOf(goldMerchantString)
            val indexEnd = indexStart + goldMerchantString.length

            val color = ContextCompat.getColor(context!!, R.color.tkpd_main_green)
            spannable.setSpan(ForegroundColorSpan(color), indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    navigateToGMHome()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = color
                }
            }
            spannable.setSpan(clickableSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvMembershipDescription.movementMethod = LinkMovementMethod.getInstance()
            tvMembershipDescription.text = spannable

            vgMembershipContainer.setOnClickListener(null)
        } else if (shopBasicDataModel.isOfficialStore) {
            ivShopMembership.setImageResource(R.drawable.ic_badge_shop_official)
            val padding = resources.getDimensionPixelOffset(R.dimen.dp_8)
            ivShopMembership.setPadding(padding, padding, padding, padding)
            tvMembershipName.text = getString(R.string.label_official_store)
            tvMembershipDescription.text = getString(R.string.valid_until_x,
                    toReadableString(FORMAT_DATE, shopBasicDataModel.expired ?: ""))
            vgMembershipContainer.setOnClickListener(null)
        } else if (shopBasicDataModel.isGold) {
            ivShopMembership.setImageDrawable(GMConstant.getGMDrawable(context))
            ivShopMembership.setPadding(0, 0, 0, 0)
            tvMembershipName.text = getString(GMConstant.getGMTitleResource(context))
            tvMembershipDescription.text = getString(R.string.valid_until_x,
                    toReadableString(FORMAT_DATE, shopBasicDataModel.expired ?: ""))
            tvManageGmSubscribe.visibility = View.VISIBLE
            vgMembershipContainer.setOnClickListener { navigateToAboutGM() }
        }
    }

    private fun navigateToGMHome() {
        activity?.run {
            val router = application as? ShopSettingRouter
            router?.goToGMSubscribe(this)
        }
    }

    private fun navigateToAboutGM() {
        if (GlobalConfig.isSellerApp()) {
            (activity!!.application as ShopSettingRouter).goToGmSubscribeMembershipRedirect(activity!!)
        } else {
            (activity!!.application as ShopSettingRouter).goToMerchantRedirect(activity!!)
        }
    }

    override fun onErrorGetShopBasicData(throwable: Throwable) {
        hideLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.showEmptyState(context, view, message) { loadShopBasicData() }
    }

    override fun onSuccessUpdateShopSchedule(successMessage: String) {
        hideSubmitLoading()
        activity?.setResult(Activity.RESULT_OK)
        ToasterNormal.show(activity!!, successMessage)
        loadShopBasicData()
    }

    override fun onErrorUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorSubmitEdit(throwable)
    }

    private fun showSnackbarErrorSubmitEdit(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        ToasterError.showClose(activity!!, message)
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
        shopSettingsInfoPresenter.run { detachView() }
    }

    companion object {

        private val REQUEST_EDIT_BASIC_INFO = 781
        private val REQUEST_EDIT_SCHEDULE = 782

        fun newInstance(): ShopSettingsInfoFragment {
            return ShopSettingsInfoFragment()
        }
    }

}
