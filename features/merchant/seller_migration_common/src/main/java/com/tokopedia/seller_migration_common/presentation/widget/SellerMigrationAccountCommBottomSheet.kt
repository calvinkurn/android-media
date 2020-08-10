package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.adapter.SellerMigrationAccountBenefitAdapter
import kotlinx.android.synthetic.main.widget_seller_migration_account_comm_bottom_sheet.*

class SellerMigrationAccountCommBottomSheet: SellerMigrationBottomSheet(showWarningCard = false) {

    companion object {
        @JvmStatic
        fun createInstance(): SellerMigrationAccountCommBottomSheet = SellerMigrationAccountCommBottomSheet()
    }

    private val accountBenefitAdapter by lazy {
        context?.let { SellerMigrationAccountBenefitAdapter(it) }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun inflateChildView(context: Context) {
        val view = View.inflate(context, R.layout.widget_seller_migration_account_comm_bottom_sheet,null)
        setChild(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    override fun trackGoToSellerApp() {
        // No op
    }

    override fun trackGoToPlayStore() {
        // No op
    }

    override fun trackLearnMore() {
        // No op
    }

    private fun setupAdapter() {
        rv_seller_migration_account_desc?.run {
            adapter = accountBenefitAdapter
            layoutManager = linearLayoutManager
        }
    }

}