package com.tokopedia.shop.settings.basicinfo.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.constant.ShopScheduleActionDef
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.analytics.ShopSettingsTracking
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopEditScheduleActivity
import com.tokopedia.shop.settings.basicinfo.view.activity.ShopSettingsInfoActivity
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsInfoViewModel
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.common.view.adapter.viewholder.MenuViewHolder
import com.tokopedia.shop.settings.common.view.bottomsheet.MenuBottomSheet
import com.tokopedia.shop.settings.databinding.FragmentShopSettingsInfoBinding
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.StringUtils.isEmptyNumber
import java.util.*
import javax.inject.Inject

class ShopSettingsInfoFragment : BaseDaggerFragment() {

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_IS_CLOSED_NOW = "extra_is_closed_now"
        const val EXTRA_SHOP_BASIC_DATA_MODEL = "extra_shop_basic_data_model"
        const val EXTRA_SAVE_INSTANCE_CACHE_MANAGER_ID = "extra_save_instance_cache_manager_id"
        const val REQUEST_EDIT_BASIC_INFO = "request_edit_basic_info"
        const val REQUEST_EDIT_SCHEDULE = 782
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    @Inject
    lateinit var shopSettingsInfoViewModel: ShopSettingsInfoViewModel

    private var binding by autoClearedNullable<FragmentShopSettingsInfoBinding>()

    private var shopBasicDataModel: ShopBasicDataModel? = null
    private var bottomSheet: MenuBottomSheet? = null
    private var snackbar: Snackbar? = null
    private var shopId: String = "0"     // 67726 for testing

    private var progressDialog: ProgressDialog? = null
    private var isShopClosedBySchedule: Boolean = false
    private var shopBadge: String = ""
    private var viewContent: ConstraintLayout? = null
    private var loadingView: LinearLayout? = null
    private var btnChangeShopInfo: View? = null
    private var vgShopStatusContainer: View? = null
    private var tvPowerMerchantType: Typography? = null
    private var ivLogoPowerMerchant: ImageView? = null
    private var tvOfficialStore: Typography? = null
    private var tvOfficialStoreExpiration: Typography? = null
    private var ivLogoOfficialStore: ImageView? = null
    private var tvShopName: Typography? = null
    private var tvShopDomain: Typography? = null
    private var ivShopLogo: ImageView? = null
    private var tvShopSloganTitle: Typography? = null
    private var tvShopSlogan: Typography? = null
    private var tvShopDescriptionTitle: Typography? = null
    private var tvShopDescription: Typography? = null
    private var tvShopStatus: Typography? = null
    private var containerRegularMerchant: View? = null
    private var containerPowerMerchant: View? = null
    private var containerOfficialStore: View? = null
    private var tvRegularMerchantType: Typography? = null

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId
        loadShopBasicData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShopSettingsInfoBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    private fun showShopStatusManageMenu() {
        shopBasicDataModel?.let { shopBasicDataModel ->
            val itemList = ArrayList<String>()
            if (shopBasicDataModel.isOpen) {
                if (isEmptyNumber(shopBasicDataModel.closeSchedule)) {
                    itemList.add(getString(R.string.schedule_your_shop_close))
                } else {
                    itemList.add(getString(R.string.change_schedule))
                    itemList.add(getString(R.string.remove_schedule))
                }
                itemList.add(getString(R.string.label_close_shop_now))
            } else {
                itemList.add(getString(R.string.change_schedule))
                itemList.add(getString(R.string.label_open_shop_now))
            }
            bottomSheet = MenuBottomSheet.newInstance(itemList)
            bottomSheet?.setTitle(getString(R.string.shop_settings_manage_status))
            bottomSheet?.setListener(object : MenuViewHolder.ItemMenuListener {
                override fun onItemMenuClicked(text: String, position: Int) {
                    itemMenuClicked(text, position)
                }

                override fun itemMenuSize(): Int = itemList.size

            })
            bottomSheet?.show(childFragmentManager, "menu_bottom_sheet")
        }
    }

    private fun itemMenuClicked(text: String, position: Int) {
        bottomSheet?.dismiss()
        when(text) {
            getString(R.string.label_close_shop_now) -> {
                shopBasicDataModel?.let { moveToShopEditScheduleFragment(it, true) }
            }
            getString(R.string.remove_schedule) -> {
                activity?.let { it ->
                    DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(R.string.remove_schedule))
                        setDescription(getString(R.string.remove_schedule_message))
                        setPrimaryCTAText(getString(R.string.action_delete))
                        setSecondaryCTAText(getString(R.string.label_cancel))
                        setPrimaryCTAClickListener {
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
                        setSecondaryCTAClickListener { dismiss() }
                        show()
                    }
                }
            }
            getString(R.string.label_open_shop_now) -> {
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
            else -> {
                shopBasicDataModel?.let { moveToShopEditScheduleFragment(it, shopBasicDataModel?.isClosed ?: false) }
            }
        }
    }

    private fun showLoading() {
        viewContent?.visibility = View.GONE
        loadingView?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewContent?.visibility = View.VISIBLE
        loadingView?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupToolbar()
        btnChangeShopInfo?.setOnClickListener {
            moveToShopEditBasicInfoFragment()
            ShopSettingsTracking.clickChange(shopId, getShopType())
        }

        vgShopStatusContainer?.setOnClickListener {
            moveToNewShopOperationalHours()
            ShopSettingsTracking.clickStatusToko(shopId, getShopType())
        }

        onFragmentResult()

        observeShopInfoData()
        observeShopOperationalHourList()
        observeShopBasicData()
        observeShopStatus()
        observeOsMerchantData()
        observeUpdateScheduleData()
    }

    private fun initView() {
        viewContent = binding?.viewContent?.viewMainContainer
        loadingView = binding?.loadingView?.loadingMainContainer
        btnChangeShopInfo = binding?.viewContent?.btnChangeShopInfo
        vgShopStatusContainer = binding?.viewContent?.vgShopStatusContainer
        tvPowerMerchantType = binding?.viewContent?.tvPowerMerchantType
        ivLogoPowerMerchant = binding?.viewContent?.ivLogoPowerMerchant
        tvOfficialStore = binding?.viewContent?.tvOfficialStore
        tvOfficialStoreExpiration = binding?.viewContent?.tvOfficialStoreExpiration
        ivLogoOfficialStore = binding?.viewContent?.ivLogoOfficialStore
        tvShopName = binding?.viewContent?.tvShopName
        tvShopDomain = binding?.viewContent?.tvShopDomain
        ivShopLogo = binding?.viewContent?.ivShopLogo
        tvShopSloganTitle = binding?.viewContent?.tvShopSloganTitle
        tvShopSlogan = binding?.viewContent?.tvShopSlogan
        tvShopDescriptionTitle = binding?.viewContent?.tvShopDescriptionTitle
        tvShopDescription = binding?.viewContent?.tvShopDescription
        tvShopStatus = binding?.viewContent?.tvShopStatus
        containerRegularMerchant = binding?.viewContent?.containerRegularMerchant
        containerPowerMerchant = binding?.viewContent?.containerPowerMerchant
        containerOfficialStore = binding?.viewContent?.containerOfficialStore
        tvRegularMerchantType = binding?.viewContent?.tvRegularMerchantType
    }

    private fun observeShopInfoData() {
        shopSettingsInfoViewModel.shopInfoData.observe(viewLifecycleOwner, Observer {
            if (it is Success) {
                val shopInfoData = it.data
                shopBadge = shopInfoData.goldOS.badge
                isShopClosedBySchedule = shopInfoData.closedInfo.closeDetail.status == ShopStatusDef.CLOSED
            }
        })
    }

    private fun observeShopOperationalHourList() {
        shopSettingsInfoViewModel.shopOperationalHourList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    var operationalHourList = it.data.getShopOperationalHoursList?.data
                    if (operationalHourList?.isEmpty().orTrue()) {
                        operationalHourList = OperationalHoursUtil.generateDefaultOpsHourList()
                    }
                    val todayOrdinalDayOfWeek = OperationalHoursUtil.getOrdinalDate(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
                    operationalHourList?.get(todayOrdinalDayOfWeek-1)?.let { opsHour ->
                        val opsHourText = OperationalHoursUtil.generateDatetime(
                            opsHour.startTime,
                            opsHour.endTime,
                            opsHour.status
                        )
                        tvShopStatus?.text = if (opsHourText == OperationalHoursUtil.HOLIDAY_CAN_ATC || opsHourText == OperationalHoursUtil.HOLIDAY_CANNOT_ATC) {
                            opsHourText.trim()
                        } else if (opsHourText == OperationalHoursUtil.ALL_DAY) {
                            val opsHourTextParts = opsHourText.split(" ").toMutableList().apply {
                                remove(first())
                            }
                            getString(
                                R.string.shop_settings_info_operational_hours,
                                opsHourTextParts.joinToString(" ").trim()
                            )
                        } else {
                            getString(
                                R.string.shop_settings_info_operational_hours,
                                opsHourText.trim()
                            )
                        }
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        snackbar = Toaster.build(view, getString(R.string.error_get_shop_operational_hour_list), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        snackbar?.show()
                    }
                    ShopSettingsErrorHandler.logMessage(it.throwable.message ?: "")
                    ShopSettingsErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        dismissToaster()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_EDIT_SCHEDULE -> if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_EDIT_SCHEDULE && data != null) {
                    val message: String? = data.getStringExtra(EXTRA_MESSAGE)
                    if (!message.isNullOrBlank()) {
                        view?.let {
                            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        }
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        (activity as? ShopSettingsInfoActivity)?.binding?.header?.apply {
            actionTextView?.hide()
        }
    }

    private fun dismissToaster() {
        snackbar?.dismiss()
    }

    private fun moveToShopEditBasicInfoFragment() {
        val cacheManager = SaveInstanceCacheManager(requireContext(), true).apply {
            put(EXTRA_SHOP_BASIC_DATA_MODEL, shopBasicDataModel)
        }
        val destination = ShopSettingsInfoFragmentDirections.actionShopSettingsInfoFragmentToShopEditBasicInfoFragment()
        destination.cacheManagerId = cacheManager.id ?: "0"
        NavigationController.navigate(this@ShopSettingsInfoFragment, destination)
    }

    private fun moveToShopEditScheduleFragment(shopBasicDataModel: ShopBasicDataModel, isClosedNow: Boolean) {
        val cacheManager = SaveInstanceCacheManager(requireContext(), true).apply {
            put(EXTRA_SHOP_BASIC_DATA_MODEL, shopBasicDataModel)
            put(EXTRA_IS_CLOSED_NOW, isClosedNow)
        }
        val intent = ShopEditScheduleActivity.createIntent(requireContext(), cacheManager.id ?: "0")
        startActivityForResult(intent, REQUEST_EDIT_SCHEDULE)
    }

    private fun moveToNewShopOperationalHours() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS)
    }

    private fun observeUpdateScheduleData() {
        shopSettingsInfoViewModel.updateScheduleResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessUpdateShopSchedule(it.data)
                is Fail -> onErrorUpdateShopSchedule(it.throwable)
            }
        })
    }

    private fun observeShopStatus() {
        shopSettingsInfoViewModel.shopStatusData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    val pmStatus = it.data
                    userSession.setIsGoldMerchant(pmStatus.isPowerMerchant())

                    setUIShopBasicData(shopBasicDataModel ?: ShopBasicDataModel())
                    shopBasicDataModel?.isOfficialStore?.let { isOfficialStore ->
                        if (isOfficialStore) {
                            // get official store detail data
                            shopSettingsInfoViewModel.validateOsMerchantType(shopId.toIntOrZero())
                        } else {
                            if (pmStatus.isPowerMerchant()) {
                                // set power merchant UI
                                showPowerMerchant()
                            } else {
                                // set regular merchant UI
                                showRegularMerchantMembership()
                            }
                        }
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        snackbar = Toaster.build(view, getString(R.string.error_get_shop_status), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        snackbar?.show()
                    }
                    ShopSettingsErrorHandler.logMessage(it.throwable.message ?: "")
                    ShopSettingsErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun observeShopBasicData() {
        shopSettingsInfoViewModel.shopBasicData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hideLoading()
                    val shopBasicData = it.data

                    // Update userSession
                    val shopName: String = shopBasicData.name ?: ""
                    val shopAvatar = shopBasicData.logo ?: ""
                    userSession.shopName = shopName
                    userSession.shopAvatar = shopAvatar

                    shopBasicDataModel = shopBasicData.apply {
                        name = MethodChecker.fromHtml(name).toString()
                        domain = MethodChecker.fromHtml(domain).toString()
                        description = MethodChecker.fromHtml(description).toString()
                        tagline = MethodChecker.fromHtml(tagline).toString()
                    }
                }
                is Fail -> {
                    onErrorGetShopBasicData(it.throwable)
                }
            }
        })
    }

    private fun observeOsMerchantData() {
        shopSettingsInfoViewModel.checkOsMerchantTypeData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.let { osData ->
                        val errMessage = osData.messageError
                        val isOS = osData.data.isOfficial
                        val expiration = osData.data.expiredDate

                        if (errMessage.isEmpty() && isOS) {
                            showOfficialStore(dateFormatToBeReadable(expiration, OS_FORMAT_DATE, FORMAT_DAY_DATE) ?: "") // Set userSession isOS?
                        }
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        snackbar = Toaster.build(view, getString(R.string.error_get_os_merchant), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        snackbar?.show()
                    }
                    ShopSettingsErrorHandler.logMessage(it.throwable.message ?: "")
                    ShopSettingsErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadShopBasicData()
    }

    private fun loadShopBasicData() {
        showLoading()
        shopSettingsInfoViewModel.getShopData(
                shopId,
                includeOS = false
        )
    }

    private fun onFragmentResult() {
        getNavigationResult(REQUEST_EDIT_BASIC_INFO)?.observe(viewLifecycleOwner, Observer { bundle ->
            bundle?.let { data ->
                data.getString(EXTRA_MESSAGE)?.apply {
                    if (this.isNotBlank()) {
                        view?.let {
                            snackbar = Toaster.build(it, this, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                            snackbar?.show()
                        }
                    }
                }
            }
            removeNavigationResult(REQUEST_EDIT_BASIC_INFO)
        })
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((requireActivity().application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setUIShopBasicData(shopBasicDataModel: ShopBasicDataModel) {
        shopBasicDataModel.let { shopBasicData ->
            tvShopName?.text = shopBasicData.name
            tvShopDomain?.text = shopBasicData.domain?.let {
                if (URLUtil.isNetworkUrl(it)) {
                    it
                } else {
                    getString(R.string.tokopedia_domain) + "/$it"
                }
            }

            val logoUrl = shopBasicData.logo
            //avoid crash in ImageUnify when image url is returned as base64
            try {
                if (ivShopLogo?.context?.isValidGlideContext() == true) {
                    if (TextUtils.isEmpty(logoUrl)) {
                        ImageHandler.loadImage2(ivShopLogo, logoUrl, R.drawable.ic_shopdefault_empty)
                    } else {
                        ImageHandler.LoadImage(ivShopLogo, logoUrl)
                    }
                }
            }catch (e: Exception) {}

            if (shopBasicData.tagline.isNullOrBlank()) {
                tvShopSloganTitle?.visibility = View.GONE
                tvShopSlogan?.visibility = View.GONE
            } else {
                tvShopSloganTitle?.visibility = View.VISIBLE
                tvShopSlogan?.visibility = View.VISIBLE
                tvShopSlogan?.text = shopBasicData.tagline
            }

            if (shopBasicData.description.isNullOrBlank()) {
                tvShopDescriptionTitle?.visibility = View.GONE
                tvShopDescription?.visibility = View.GONE
            } else {
                tvShopDescriptionTitle?.visibility = View.VISIBLE
                tvShopDescription?.visibility = View.VISIBLE
                tvShopDescription?.text = shopBasicData.description
            }

            if (shopBasicData.isClosed) {
                if (isShopClosedBySchedule) {
                    // is shop closed by schedule, then show its schedule.
                    val startDate = Date(shopBasicData.closeSchedule.toLongOrZero() * 1000L)
                    val endDate = Date(shopBasicData.closeUntil.toLongOrZero() * 1000L)
                    tvShopStatus?.text = getString(
                            R.string.shop_settings_info_holiday_text,
                            OperationalHoursUtil.toIndonesianDateRangeFormat(startDate, endDate, isShortDateFormat = true)
                    )
                } else {
                    // shop shop closed by weekly operational, then get operational hour list
                    shopSettingsInfoViewModel.getOperationalHoursList(shopId)
                }
            } else {
                shopSettingsInfoViewModel.getOperationalHoursList(shopId)
            }
        }
    }

    private fun showRegularMerchantMembership() {
        containerRegularMerchant?.visibility = View.VISIBLE
        containerPowerMerchant?.visibility = View.GONE
        containerOfficialStore?.visibility = View.GONE
        tvRegularMerchantType?.text = getString(R.string.label_regular_merchant)
    }

    private fun showPowerMerchant() {
        containerPowerMerchant?.visibility = View.VISIBLE
        containerRegularMerchant?.visibility = View.GONE
        containerOfficialStore?.visibility = View.GONE
        ivLogoPowerMerchant?.visibility = View.VISIBLE
        ivLogoPowerMerchant?.loadImage(shopBadge)
        tvPowerMerchantType?.text = getString(R.string.label_power_merchant)
    }

    private fun showOfficialStore(expirationDate: String) {
        containerOfficialStore?.visibility = View.VISIBLE
        containerRegularMerchant?.visibility = View.GONE
        containerPowerMerchant?.visibility = View.GONE
        ivLogoOfficialStore?.visibility = View.VISIBLE
        ivLogoOfficialStore?.loadImage(shopBadge)
        tvOfficialStore?.text = getString(R.string.label_official_store)
        tvOfficialStoreExpiration?.text = "Berlaku hingga $expirationDate"
    }

    private fun onErrorGetShopBasicData(throwable: Throwable) {
        hideLoading()
        val message = ShopSettingsErrorHandler.getErrorMessage(context, throwable.cause)
        NetworkErrorHelper.showEmptyState(context, view, message) { loadShopBasicData() }
        ShopSettingsErrorHandler.logMessage(throwable.message ?: "")
        ShopSettingsErrorHandler.logExceptionToCrashlytics(throwable)
    }

    private fun onSuccessUpdateShopSchedule(successMessage: String) {
        hideSubmitLoading()
        activity?.setResult(Activity.RESULT_OK)
        view?.let {
            snackbar = Toaster.build(it, successMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
            snackbar?.show()
        }
        loadShopBasicData()
    }

    private fun onErrorUpdateShopSchedule(throwable: Throwable) {
        hideSubmitLoading()
        showSnackbarErrorUpdateShopSchedule(throwable)
        ShopSettingsErrorHandler.logMessage(throwable.message ?: "")
        ShopSettingsErrorHandler.logExceptionToCrashlytics(throwable)
    }

    private fun showSnackbarErrorUpdateShopSchedule(throwable: Throwable) {
        val message = ShopSettingsErrorHandler.getErrorMessage(context, throwable.cause)
        view?.let { view ->
            message?.let {
                snackbar = Toaster.build(view, it, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                snackbar?.show()
            }
        }
    }

    private fun getShopType(): String {
        return when {
            shopBasicDataModel?.isOfficialStore ?: false -> ShopTypeDef.OFFICIAL_STORE
            shopBasicDataModel?.isGold ?: false -> ShopTypeDef.GOLD_MERCHANT
            else -> ShopTypeDef.REGULAR_MERCHANT
        }
    }

    private fun showSubmitLoading(message: String) {
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

    private fun hideSubmitLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shopSettingsInfoViewModel.resetAllLiveData()
    }
}
