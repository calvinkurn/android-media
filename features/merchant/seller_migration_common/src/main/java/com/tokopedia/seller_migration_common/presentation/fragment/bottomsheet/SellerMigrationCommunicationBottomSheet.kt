package com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.CommunicationInfo
import com.tokopedia.seller_migration_common.presentation.model.DynamicCommunicationInfo
import com.tokopedia.seller_migration_common.presentation.model.SellerMigrationCommunication
import com.tokopedia.seller_migration_common.presentation.util.setupMigrationFooter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

open class SellerMigrationCommunicationBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           sellerMigrationCommunication: SellerMigrationCommunication): SellerMigrationCommunicationBottomSheet {
            return when(sellerMigrationCommunication) {
                is CommunicationInfo -> SellerMigrationStaticCommunicationBottomSheet.createInstance(context, sellerMigrationCommunication)
                is DynamicCommunicationInfo -> SellerMigrationDynamicCommunicationBottomSheet.createInstance(context)
                else -> SellerMigrationCommunicationBottomSheet()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

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

    protected open fun setupView() {
        setupPadding()
        layout_seller_migration_date?.visible()
        setupMigrationFooter(view, ::trackGoToSellerApp, ::trackGoToPlayStore)
    }

    protected open fun trackGoToSellerApp() {

    }

    protected open fun trackGoToPlayStore() {

    }

}