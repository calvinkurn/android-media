package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.CommunicationInfo
import com.tokopedia.seller_migration_common.presentation.model.DynamicCommunicationInfo
import com.tokopedia.seller_migration_common.presentation.model.SellerMigrationCommunication

class SellerMigrationCommunicationBottomSheet: SellerMigrationBottomSheet {

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           sellerMigrationCommunication: SellerMigrationCommunication): SellerMigrationCommunicationBottomSheet{
            return when(sellerMigrationCommunication) {
                is CommunicationInfo -> SellerMigrationCommunicationBottomSheet(context, sellerMigrationCommunication)
                is DynamicCommunicationInfo -> SellerMigrationCommunicationBottomSheet(sellerMigrationCommunication.carouselImageUrlList)
                else -> SellerMigrationCommunicationBottomSheet()
            }
        }
    }

    constructor(): super(showWarningCard = false)

    constructor(context: Context, communicationInfo: CommunicationInfo): super(
            titles = arrayListOf(context.getString(communicationInfo.titleRes)),
            contents = arrayListOf(context.getString(communicationInfo.descRes)),
            images = arrayListOf(communicationInfo.imageUrl),
            showWarningCard = false
    )

    constructor(carouselImageList: ArrayList<String>): super(
            images = carouselImageList,
            showWarningCard = false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun inflateChildView(context: Context) {
        val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
        setChild(view)
    }

    override fun trackGoToSellerApp() {
        //No op
    }

    override fun trackGoToPlayStore() {
        //No op
    }

    override fun trackLearnMore() {
        //No op
    }

    private fun setupView() {

    }
}