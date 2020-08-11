package com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_migration_common.presentation.adapter.SellerMigrationBenefitPointAdapter
import com.tokopedia.seller_migration_common.presentation.model.CommunicationInfo
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*
import timber.log.Timber

class SellerMigrationStaticCommunicationBottomSheet: SellerMigrationCommunicationBottomSheet() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           communicationInfo: CommunicationInfo): SellerMigrationStaticCommunicationBottomSheet =
                SellerMigrationStaticCommunicationBottomSheet().apply {
                    arguments = Bundle().apply {
                        val benefitArrayList: List<String> = communicationInfo.benefitPointResList.map { context.getString(it) }
                        putString(TITLE_KEY, context.getString(communicationInfo.titleRes))
                        putString(DESC_KEY, context.getString(communicationInfo.descRes))
                        putStringArrayList(BENEFIT_POINTS_KEY, ArrayList(benefitArrayList))
                    }
                }

        private const val TITLE_KEY = "title"
        private const val DESC_KEY = "desc"
        private const val IMAGE_URL_KEY = "image_url"
        private const val BENEFIT_POINTS_KEY = "benefit_points"
    }

    private val communicationTitle by lazy {
        arguments?.getString(TITLE_KEY).orEmpty()
    }

    private val communicationDesc by lazy {
        arguments?.getString(DESC_KEY).orEmpty()
    }

    private val communicationImageUrl by lazy {
        arguments?.getString(IMAGE_URL_KEY).orEmpty()
    }

    private val benefitPointList by lazy {
        arguments?.getStringArrayList(BENEFIT_POINTS_KEY).orEmpty()
    }

    private val benefitPointAdapter by lazy {
        SellerMigrationBenefitPointAdapter(benefitPointList)
    }

    override fun setupView() {
        super.setupView()
        setupTitleAndDesc()
        setupCommunicationImage()
        setupNumberedPoints()
    }

    private fun setupTitleAndDesc() {
        sellerMigrationBottomSheetTitle?.text = communicationTitle
        sellerMigrationBottomSheetContent?.text = communicationDesc
    }

    private fun setupCommunicationImage() {
        try {
            sellerMigrationBottomSheetImage?.run {
                visible()
                loadImageRounded(communicationImageUrl)
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun setupNumberedPoints() {
        rv_seller_migration_points?.run {
            adapter = benefitPointAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}