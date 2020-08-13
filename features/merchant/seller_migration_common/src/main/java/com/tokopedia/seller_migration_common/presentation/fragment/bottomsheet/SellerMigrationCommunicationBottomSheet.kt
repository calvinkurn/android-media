package com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.model.CommunicationInfo
import com.tokopedia.seller_migration_common.presentation.model.DynamicCommunicationInfo
import com.tokopedia.seller_migration_common.presentation.model.SellerMigrationCommunication
import com.tokopedia.seller_migration_common.presentation.util.setupMigrationFooter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.partial_seller_migration_date.*
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*
import java.util.*

open class SellerMigrationCommunicationBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           sellerMigrationCommunication: SellerMigrationCommunication,
                           screenName: String,
                           shopId: String = ""): SellerMigrationCommunicationBottomSheet {
            return when(sellerMigrationCommunication) {
                is CommunicationInfo -> SellerMigrationStaticCommunicationBottomSheet.createInstance(context, sellerMigrationCommunication)
                is DynamicCommunicationInfo -> SellerMigrationDynamicCommunicationBottomSheet.createInstance(context)
                else -> SellerMigrationCommunicationBottomSheet()
            }.apply {
                arguments?.run {
                    putParcelable(COMMUNICATION_KEY, sellerMigrationCommunication)
                    putString(SCREEN_NAME_KEY, screenName)
                    putString(SHOP_ID, shopId)
                }
            }
        }

        private const val COMMUNICATION_KEY = "communication"
        private const val SCREEN_NAME_KEY = "screen_name"
        private const val SHOP_ID = "shop_id"
    }

    private val sellerMigrationCommunication by lazy {
        arguments?.getParcelable<SellerMigrationCommunication>(COMMUNICATION_KEY)
    }

    private val screenName by lazy {
        arguments?.getString(SCREEN_NAME_KEY).orEmpty()
    }

    private val shopId by lazy {
        arguments?.getString(SHOP_ID).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    protected open fun setupView() {
        setupPadding()
        setRedirectionDate()
        setupMigrationFooter(view, ::trackGoToSellerApp, ::trackGoToPlayStore, ::goToSellerFeature)
    }

    protected open fun trackGoToSellerApp() {}

    protected open fun trackGoToPlayStore() {}

    private fun initView() {
        val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
        setChild(view)
    }

    private fun setupPadding() {
        setShowListener {
            val headerMargin = 16.toPx()
            bottomSheetWrapper.setPadding(0, 0, 0, 0)
            (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(headerMargin, headerMargin, headerMargin, headerMargin)
        }
    }

    private fun setRedirectionDate() {
        layout_seller_migration_date?.visible()
        val remoteConfigDate = getSellerMigrationDate(context).let { date ->
            if (date.isEmpty()) {
                getString(R.string.seller_migration_bottom_sheet_redirected_dates)
            } else {
                date
            }
        }
        tv_seller_migration_start_date?.text = remoteConfigDate
    }

    private fun goToSellerFeature() {
        when(sellerMigrationCommunication) {
            CommunicationInfo.TopAds -> {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO)
                    add(ApplinkConst.CustomerApp.TOPADS_DASHBOARD)
                }
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_TOPADS, appLinks)
            }
            CommunicationInfo.BroadcastChat -> {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME_CHAT)
                }
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_SELLER_CHAT, appLinks)
            }
            CommunicationInfo.PostFeed -> {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(UriUtil.buildUri(ApplinkConst.SHOP, shopId))
                    add(ApplinkConst.CONTENT_CREATE_POST)
                }
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_POST_FEED, appLinks)
            }
            CommunicationInfo.ShopCapital, CommunicationInfo.PriorityBalance, DynamicCommunicationInfo -> {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(ApplinkConstInternalGlobal.SALDO_DEPOSIT)
                }
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_BALANCE, appLinks)
            }
        }
    }

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        val context = context
        if (context != null) {
            val intent = SellerMigrationActivity.createIntent(context, featureName, screenName, appLinks)
            startActivity(intent)
        }
    }

}