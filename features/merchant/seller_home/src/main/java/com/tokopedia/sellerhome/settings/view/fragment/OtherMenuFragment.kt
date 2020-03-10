package com.tokopedia.sellerhome.settings.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.data.constant.SellerBaseUrl
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.DividerType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingErrorType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.OtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherMenuViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_other_menu.*
import javax.inject.Inject

class OtherMenuFragment: BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(), OtherMenuViewHolder.Listener{

    companion object {
        const val URL_KEY = "url"

        private const val PENGATURAN = "Pengaturan"
        private const val TINGKATKAN_PENJUALAN = "TINGKATKAN PENJUALAN"
        private const val STATISTIK_TOKO = "Statistik Toko"
        private const val IKLAN_DAN_PROMOSI_TOKO = "Iklan & Promosi Toko"
        private const val KABAR_PEMBELI = "KABAR PEMBELI"
        private const val ULASAN = "Ulasan"
        private const val DISKUSI = "Diskusi"
        private const val KOMPLAIN = "Komplain"
        private const val LAYANAN_KEUANGAN = "Layanan Keuangan"
        private const val PUSAT_EDUKASI_SELLER = "Pusat Edukasi Seller"
        private const val TOKOPEDIA_CARE = "Tokopedia Care"
        @JvmStatic
        fun createInstance(): OtherMenuFragment = OtherMenuFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    private var otherMenuViewHolder: OtherMenuViewHolder? = null
    private var generalShopInfoIsAlreadyLoaded = false
    private var shopBadgeIsAlreadyLoaded = false
    private var totalFollowersIsAlreadyLoaded = false

    private val otherMenuViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OtherMenuViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        getAllShopInfoData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        observeLiveData()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory()

    override fun onItemClicked(settingUiModel: SettingUiModel) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {

    }

    private fun observeLiveData() {
        with(otherMenuViewModel) {
            generalShopInfoLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showGeneralShopInfoSuccess(result.data)
                    is Fail -> showError(result.throwable, SettingErrorType.GENERAL_INFO_ERROR)
                }
            })
            shopBadgeLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showShopBadgeSuccess(result.data)
                    is Fail -> showError(result.throwable, SettingErrorType.BADGES_ERROR)
                }
            })
            totalFollowersLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showTotalFollowingSuccess(result.data)
                    is Fail -> showError(result.throwable, SettingErrorType.FOLLOWERS_ERROR)
                }
            })
            isGeneralShopInfoAlreadyLoaded.observe(viewLifecycleOwner, Observer {
                generalShopInfoIsAlreadyLoaded = it
            })
            isShopBadgeAlreadyLoadedLiveData.observe(viewLifecycleOwner, Observer {
                shopBadgeIsAlreadyLoaded = it
            })
            isTotalFollowersAlreadyLoadedLiveData.observe(viewLifecycleOwner, Observer {
                totalFollowersIsAlreadyLoaded = it
            })

        }
    }

    private fun populateAdapterData() {
        val settingList = mutableListOf(
                DividerUiModel(DividerType.THIN_FULL),
                DividerUiModel(),
                SettingTitleUiModel(TINGKATKAN_PENJUALAN),
                MenuItemUiModel(STATISTIK_TOKO, R.drawable.ic_statistic_setting, ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD),
                MenuItemUiModel(IKLAN_DAN_PROMOSI_TOKO, R.drawable.ic_ads_promotion, null /* TODO("Masukkin applink lw di sini cup, ganti nullnya. Utk saat ini hanya support applink") */),
                SettingTitleUiModel(KABAR_PEMBELI),
                MenuItemUiModel(ULASAN, R.drawable.ic_star_setting, ApplinkConst.REPUTATION),
                MenuItemUiModel(DISKUSI, R.drawable.ic_setting_discussion, ApplinkConst.TALK),
                MenuItemUiModel(KOMPLAIN, R.drawable.ic_complaint, null) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.SellerApp.WEBVIEW)
                    intent.putExtra(URL_KEY, SellerBaseUrl.HOSTNAME + SellerBaseUrl.RESO_INBOX_SELLER)
                    context?.startActivity(intent)
                },
                DividerUiModel(),
                MenuItemUiModel(LAYANAN_KEUANGAN, R.drawable.ic_finance),
                MenuItemUiModel(PUSAT_EDUKASI_SELLER, R.drawable.ic_seller_edu) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.WEBVIEW)
                    intent.putExtra(URL_KEY, SellerBaseUrl.HOSTNAME + SellerBaseUrl.SELLER_EDU)
                    context?.startActivity(intent)
                },
                MenuItemUiModel(TOKOPEDIA_CARE, R.drawable.ic_tokopedia_care, ApplinkConst.CONTACT_US_NATIVE),
                DividerUiModel(DividerType.THIN_PARTIAL),
                MenuItemUiModel(PENGATURAN, R.drawable.ic_setting, null) {
                    startActivity(Intent(context, MenuSettingActivity::class.java))
                }
        )
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)
    }

    private fun getAllShopInfoData() {
        showAllLoadingShimmering()
        otherMenuViewModel.getAllSettingShopInfo()
    }

    private fun showAllLoadingShimmering() {
        if (!generalShopInfoIsAlreadyLoaded)
            showGeneralShopInfoLoading()
        if (!shopBadgeIsAlreadyLoaded)
            showShopBadgeLoading()
        if (!totalFollowersIsAlreadyLoaded)
            showTotalFollowingLoading()
    }

    private fun showGeneralShopInfoSuccess(generalShopInfoUiModel: GeneralShopInfoUiModel) {
        generalShopInfoUiModel.run {
            otherMenuViewHolder?.onSuccessGetShopGeneralInfoData(this)
        }
    }

    private fun showShopBadgeSuccess(shopBadgeUrl: String) {
        otherMenuViewHolder?.onSuccessGetShopBadge(shopBadgeUrl)
    }

    private fun showTotalFollowingSuccess(totalFollowers: Int) {
        otherMenuViewHolder?.onSuccessGetTotalFollowing(totalFollowers)
    }

    private fun showGeneralShopInfoLoading() {
        otherMenuViewHolder?.onLoadingGetShopGeneralInfoData()
    }

    private fun showShopBadgeLoading() {
        otherMenuViewHolder?.onLoadingGetShopBadge()
    }

    private fun showTotalFollowingLoading() {
        otherMenuViewHolder?.onLoadingGetTotalFollowing()
    }

    private fun showError(throwable: Throwable, errorType: SettingErrorType) {
        throwable.message?.let { view?.showToasterError(it, errorType) }
        when(errorType) {
            is SettingErrorType.GENERAL_INFO_ERROR -> {
                if (!generalShopInfoIsAlreadyLoaded)
                    otherMenuViewHolder?.onErrorGetShopGeneralInfoData()
            }
            is SettingErrorType.BADGES_ERROR -> {
                if (!shopBadgeIsAlreadyLoaded)
                    otherMenuViewHolder?.onErrorGetShopBadge()
            }
            is SettingErrorType.FOLLOWERS_ERROR -> {
                if (!totalFollowersIsAlreadyLoaded)
                    otherMenuViewHolder?.onErrorGetTotalFollowing()
            }
        }
    }

    private fun retryFetchAfterError() {
        otherMenuViewModel.getAllSettingShopInfo()
    }

    private fun View.showToasterError(errorMessage: String, errorType: SettingErrorType) {
        Toaster.make(this,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                resources.getString(R.string.setting_error_retry),
                View.OnClickListener {
                    retryFetchAfterError()
                })
    }

    private fun setupView(view: View) {
        populateAdapterData()
        recycler_view.layoutManager = LinearLayoutManager(context)
        context?.let { otherMenuViewHolder = OtherMenuViewHolder(view, it, this)}
        otherMenuViewHolder?.initBindView()
    }

    override fun onShopNextClicked() {
        RouteManager.route(context, ApplinkConst.SHOP, userSession.shopId)
    }

    override fun onFollowersCountClicked() {

    }

    override fun onSaldoClicked() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            context?.startActivity(intent)
        }
    }

    override fun onKreditTopadsClicked() {

    }
}