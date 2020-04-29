package com.tokopedia.seller_migration_common.presentation.widget

import android.os.Bundle
import android.view.View
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class SellerMigrationBottomSheet : BottomSheetUnify() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun initView()
}