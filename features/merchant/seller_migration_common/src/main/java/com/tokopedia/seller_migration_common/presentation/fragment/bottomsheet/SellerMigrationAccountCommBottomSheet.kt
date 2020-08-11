package com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.presentation.adapter.SellerMigrationAccountBenefitAdapter
import com.tokopedia.seller_migration_common.presentation.util.setupMigrationFooter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_seller_migration_account_comm_bottom_sheet.*

class SellerMigrationAccountCommBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(userId: String): SellerMigrationAccountCommBottomSheet = SellerMigrationAccountCommBottomSheet().apply {
            arguments = Bundle().apply {
                putString(USER_ID_KEY, userId)
            }
        }

        private const val USER_ID_KEY = "user_id"
    }

    private val accountBenefitAdapter by lazy {
        context?.let { SellerMigrationAccountBenefitAdapter(it) }
    }

    private val userId by lazy {
        arguments?.getString(USER_ID_KEY).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.widget_seller_migration_account_comm_bottom_sheet,null)
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupAdapter()
        setupMigrationFooter(view, ::trackGoToSellerApp, ::trackGoToPlayStore)
    }

    private fun setupAdapter() {
        rv_seller_migration_account_desc?.run {
            adapter = accountBenefitAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun trackGoToSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT)
    }

    private fun trackGoToPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT)
    }

}