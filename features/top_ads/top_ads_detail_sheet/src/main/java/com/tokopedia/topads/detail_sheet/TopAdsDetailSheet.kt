package com.tokopedia.topads.detail_sheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.data.internal.AutoAdsStatus
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.view.widget.AutoAdsWidgetCommon
import com.tokopedia.topads.detail_sheet.data.AdData
import com.tokopedia.topads.detail_sheet.di.DaggerTopAdsSheetComponent
import com.tokopedia.topads.detail_sheet.di.TopAdsSheetComponent
import com.tokopedia.topads.detail_sheet.viewmodel.TopAdsSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_pdp_bottom_sheet_single_ad.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 07,May,2019
 */
class TopAdsDetailSheet : BottomSheetUnify() {

    private var groupId: String = "0"
    private val autoAdsWidget: AutoAdsWidgetCommon?
        get() = autoads_widget
    private var adId: String = "0"
    private var category: Int = 0
    private var adType: String = "1"
    private var currentAutoAdsStatus = 100

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TopAdsSheetViewModel

    private fun getComponent(context: Context): TopAdsSheetComponent = DaggerTopAdsSheetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            getComponent(it).inject(this)
        }
        viewModel = ViewModelProviders.of(context as BaseSimpleActivity, viewModelFactory).get(TopAdsSheetViewModel::class.java)
        setFragment()
        viewModel.autoAdsData.observe(viewLifecycleOwner, Observer {
            currentAutoAdsStatus = it.status
            if (category == TYPE_AUTO || isInProgress()) {
                if ((adId.isNotEmpty() && adId.toInt() > 0 && adType == AD_TYPE_PRODUCT) || isInProgress()) {
                    editAd?.text = getString(R.string.topads_detail_sheet_auto)
                } else {
                    editAd?.text = getString(R.string.topads_detail_sheet_dashboard)
                    tickerInfo?.visibility = View.VISIBLE
                    adInfo?.visibility = View.GONE
                }
            }
        })
        bottomSheetBehaviorKnob(view, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val childView = View.inflate(context, R.layout.topads_pdp_bottom_sheet_single_ad, null)
        setChild(childView)
        showCloseIcon = false
        setTitle(getString(R.string.detail_iklan))
    }

    private fun onSuccessPost(action: String) {
        btn_switch?.isChecked = action == ACTION_ACTIVATE
    }

    fun show(
            fragmentManager: FragmentManager,
            adTye: String,
            adId: String,
            categoryType: Int
    ) {

        this.adId = adId
        this.adType = adTye
        this.category = categoryType

        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }


    private fun setFragment() {
        viewModel.getAutoAdsStatus(userSession.shopId, resources)
        viewModel.getGroupId(userSession.shopId, adId, ::onSuccessGroupId)
        viewModel.getProductStats(resources, listOf(adId), ::onsuccessProductStats)
        when (category) {
            TYPE_MANUAL -> {
                autoAdsWidget?.visibility = View.GONE
            }
            TYPE_AUTO -> {
                imgProduct?.visibility = View.GONE
                txtTitleProduct?.visibility = View.GONE
                txtBudget?.visibility = View.GONE
                btn_switch?.visibility = View.GONE
                autoAdsWidget?.loadData(LOAD_DATA_SHEET)
                autoAdsWidget?.visibility = View.VISIBLE
            }
        }

        editAd.setOnClickListener {
            if (category == TYPE_MANUAL) {
                if (groupId == SINGLE_AD) {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_WITHOUT_GROUP).apply {
                        putExtra(GROUPID, adId.toInt())
                    }
                    startActivityForResult(intent, EDIT_WITHOUT_GROUP_REQUEST_CODE)
                } else {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                        putExtra(GROUPID, groupId)
                    }
                    startActivity(intent)
                }

            } else if (category == TYPE_AUTO) {
                if (adId.toInt() > 0 && adType == AD_TYPE_PRODUCT) {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
                    startActivity(intent)
                } else {
                    RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
                }
            }
        }

        btn_switch?.setOnClickListener {
            when ((it as Switch).isChecked) {
                true -> viewModel.setProductAction(::onSuccessPost, ACTION_ACTIVATE,
                        listOf(adId), resources, null)
                false -> viewModel.setProductAction(::onSuccessPost, ACTION_DEACTIVATE,
                        listOf(adId), resources, null)
            }
        }

        createAd?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
        }
    }

    private fun isInProgress(): Boolean {
        return (currentAutoAdsStatus == AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE ||
                currentAutoAdsStatus == AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE)
    }

    private fun onSuccessGroupId(data: List<AdData>) {
        data.firstOrNull().let {
            groupId = it?.groupID ?: SINGLE_AD
            btn_switch?.isChecked = it?.status == STATUS_ACTIVE || it?.status == STATUS_TIDAK_TAMPIL
            txtBudget?.text = String.format(getString(R.string.topads_detail_budget), it?.priceBid)
        }
        viewModel.getGroupProductData(resources, groupId.toInt(), ::onSuccessProductInfo)
        if (groupId == SINGLE_AD && category != TYPE_AUTO) {
            singleAd.visibility = View.VISIBLE
            txtBudget.visibility = View.GONE
            createGroup.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
            }
        }
    }

    private fun onSuccessProductInfo(data: List<WithoutGroupDataItem>) {
        data.forEachIndexed { index, it ->
            if (it.adId.toString() == adId) {
                ImageHandler.LoadImage(imgProduct, data[index].productImageUri)
                txtTitleProduct.text = data[index].productName
                pendapatan_count.text = data[index].statTotalGrossProfit
                return@forEachIndexed
            }
        }
    }

    private fun onsuccessProductStats(data: List<WithoutGroupDataItem>) {
        data.firstOrNull().let {
            tampil_count.text = it?.statTotalImpression
            klik_count.text = it?.statTotalClick
            persentase_klik_count.text = it?.statTotalCtr
            pengeluaran_count.text = it?.statTotalSpent
            produk_terjual_count.text = it?.statTotalConversion
        }
    }

    companion object {
        private const val ACTION_ACTIVATE = "toggle_on"
        private const val ACTION_DEACTIVATE = "toggle_off"
        private const val GROUPID = "groupId"
        private const val STATUS_ACTIVE = "1"
        private const val STATUS_TIDAK_TAMPIL = "2"
        private const val AD_TYPE_PRODUCT = "1"
        private const val LOAD_DATA_SHEET = 2
        private const val SINGLE_AD = "0"
        private const val TYPE_MANUAL = 3
        private const val TYPE_AUTO = 4
        private const val PRICEBID = "price_bid"
        private const val TOPADS_BOTTOM_SHEET_TAG = "SORT_FILTER_BOTTOM_SHEET_TAG"
        private const val EDIT_WITHOUT_GROUP_REQUEST_CODE = 47
        fun newInstance(): TopAdsDetailSheet = TopAdsDetailSheet()

    }
}
