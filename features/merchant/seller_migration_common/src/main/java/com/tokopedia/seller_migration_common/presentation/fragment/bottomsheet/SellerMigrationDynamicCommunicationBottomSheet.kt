package com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_migration_common.presentation.adapter.SellerMigrationBenefitPointAdapter
import com.tokopedia.seller_migration_common.presentation.model.DynamicCommunicationInfo
import com.tokopedia.seller_migration_common.presentation.util.BenefitPoints
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

class SellerMigrationDynamicCommunicationBottomSheet: SellerMigrationCommunicationBottomSheet() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): SellerMigrationDynamicCommunicationBottomSheet =
                SellerMigrationDynamicCommunicationBottomSheet().apply {
                    arguments = Bundle().apply {
                        val titleList = DynamicCommunicationInfo.communicationInfoList.map { context.getString(it.titleRes) }
                        val descList = DynamicCommunicationInfo.communicationInfoList.map { context.getString(it.descRes) }
                        val imageUrlList = DynamicCommunicationInfo.communicationInfoList.map { it.imageUrl }
                        val benefitPointsList = DynamicCommunicationInfo.communicationInfoList.map { info ->
                            BenefitPoints(info.benefitPointResList.map { resId ->
                                context.getString(resId)
                            })
                        }
                        putStringArrayList(TITLE_LIST_KEY, ArrayList(titleList))
                        putStringArrayList(DESC_LIST_KEY, ArrayList(descList))
                        putStringArrayList(IMAGE_URL_LIST_KEY, ArrayList(imageUrlList))
                        putParcelableArrayList(BENEFIT_POINTS_LIST_KEY, ArrayList(benefitPointsList))
                        putInt(CURRENT_STATE_KEY, 0)
                    }
                }

        private const val TITLE_LIST_KEY = "title_list"
        private const val DESC_LIST_KEY = "desc_list"
        private const val IMAGE_URL_LIST_KEY = "image_url_list"
        private const val BENEFIT_POINTS_LIST_KEY = "benefit_points_list"
        private const val CURRENT_STATE_KEY = "current_state"
    }

    private val communicationTitleList by lazy {
        arguments?.getStringArrayList(TITLE_LIST_KEY).orEmpty()
    }

    private val communicationDescList by lazy {
        arguments?.getStringArrayList(DESC_LIST_KEY).orEmpty()
    }

    private val communicationImageUrlList by lazy {
        arguments?.getStringArrayList(IMAGE_URL_LIST_KEY)
    }

    private val communicationBenefitPointsList by lazy {
        arguments?.getParcelableArrayList<BenefitPoints>(BENEFIT_POINTS_LIST_KEY).orEmpty()
    }

    private val currentState by lazy {
        arguments?.getInt(CURRENT_STATE_KEY).orZero()
    }

    private var currentStateIndex: Int = 0

    private var isAdapterInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentStateIndex = savedInstanceState?.getInt(CURRENT_STATE_KEY) ?: currentState
    }

    override fun setupView() {
        super.setupView()
        setupTitleAndDesc(currentStateIndex)
        setupCommunicationCarousel()
        setupNumberedPoints(currentStateIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_STATE_KEY, currentStateIndex)
    }

    private fun setupTitleAndDesc(index: Int) {
        sellerMigrationBottomSheetTitle?.text = communicationTitleList.getOrElse(index) { "" }
        sellerMigrationBottomSheetContent?.text = communicationDescList.getOrElse(index) { "" }
    }

    private fun setupCommunicationCarousel() {
        sellerMigrationCarousel?.run {
            addImages(communicationImageUrlList ?: arrayListOf())
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    currentStateIndex = current
                    setupTitleAndDesc(currentStateIndex)
                }
            }
            visible()
        }
    }

    private fun setupNumberedPoints(index: Int) {
        rv_seller_migration_points?.run {
            val adapterList = communicationBenefitPointsList.getOrNull(index)?.benefitPointsList.orEmpty()
            if (isAdapterInitialized) {
                (adapter as? SellerMigrationBenefitPointAdapter)?.benefitPoints = adapterList
                adapter?.notifyDataSetChanged()
            } else {
                adapter = SellerMigrationBenefitPointAdapter(adapterList)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                isAdapterInitialized = true
            }
            visible()
        }
    }
}