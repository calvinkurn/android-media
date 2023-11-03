package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.FragmentShopInfoReimagineBinding
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.component.ShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.custom.ShopReviewView
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.info.view.viewmodel.ShopInfoReimagineViewModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class ShopInfoReimagineFragment : BaseDaggerFragment(), HasComponent<ShopInfoComponent> {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val MARGIN_4_DP = 4
        
        @JvmStatic
        fun newInstance(shopId: String): ShopInfoReimagineFragment {
            return ShopInfoReimagineFragment().apply {
                arguments = Bundle().apply {
                   putString(BUNDLE_KEY_SHOP_ID, shopId)
                }
            }
        }

    }

    private var binding by autoClearedNullable<FragmentShopInfoReimagineBinding>()
   

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface


    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopInfoReimagineViewModel::class.java] }

    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID, "").orEmpty() }

    override fun getScreenName(): String = ShopInfoReimagineFragment::class.java.canonicalName.orEmpty()

    override fun getComponent() = activity?.run {
        DaggerShopInfoComponent.builder()
            .shopInfoModule(ShopInfoModule())
            .shopComponent(ShopComponentHelper().getComponent(application, this))
            .build()
    }
    

    override fun initInjector() {
        component?.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopInfoReimagineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupView()
        observeUiState()

        val localCacheModel = ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: return
        viewModel.getShopInfo(shopId, localCacheModel)
        registerBackPressEvent()
    }

    private fun registerBackPressEvent() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupView() {
        binding?.run {
            tpgShopPharmacyCtaViewAll.setOnClickListener {  }
            tpgShopPharmacyCtaViewMaps.setOnClickListener {  }
        }
    }

    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }
    

    private fun handleUiState(uiState: ShopInfoUiState) {
        renderShopCoreInfo(uiState)
        renderShopInfo(uiState)
        renderShopPharmacy(uiState)
        renderShopRatingAndReview(uiState)
        renderShopPerformance(uiState)
        renderShopNotes(uiState)
        renderShopDescription(uiState)
        renderShopSupportedShipment(uiState)
    }
    
    private fun renderShopCoreInfo(uiState: ShopInfoUiState) {
        binding?.run {
            imgShop.loadImage(uiState.info.shopImageUrl)
            imgShopBadge.loadImage(uiState.info.shopBadgeUrl)
            tpgShopName.text = uiState.info.shopName
            tpgShopUsp.text = ""
            tpgLicensedPharmacy.isVisible = true
        }
    }
    
    private fun renderShopInfo(uiState: ShopInfoUiState) {
        binding?.run {
            tpgShopInfoLocation.text = uiState.info.mainLocation
            tpgShopInfoOtherLocation.text = uiState.info.otherLocations.firstOrNull().orEmpty()
            tpgShopInfoOtherLocation.isVisible = true
            renderOperationalHours(uiState.info.operationalHours)
            tpgShopInfoJoinDate.text = uiState.info.shopJoinDate
            tpgShopInfoTotalProduct.text = uiState.info.totalProduct.toString()
        }
    }
    
    private fun renderOperationalHours(operationalHours: Map<String, List<String>>) {
        val linearLayout = binding?.layoutOperationalHoursContainer
        operationalHours.forEach {
            val hour = it.key
            val days = it.value
            val groupedDays = days.joinToString(separator = ", ", postfix = ": ") { it }
            
            val textViewOperationalHour = Typography(context ?: return).apply {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = MARGIN_4_DP.toPx()
                layoutParams = params
            }
            
            textViewOperationalHour.text = groupedDays + hour
            linearLayout?.addView(textViewOperationalHour)
        }
        
    }
    
    private fun renderShopPharmacy(uiState: ShopInfoUiState) {
        binding?.run {
            tpgShopPharmacyNearestPickup.text = uiState.epharmacy.nearestPickupAddress
            tpgShopPharmacyPharmacistOpsHour.text = uiState.epharmacy.pharmacistOperationalHour
            tpgShopPharmacyPharmacistName.text = uiState.epharmacy.pharmacistName
            tpgShopPharmacySiaNumber.text = uiState.epharmacy.siaNumber
            tpgShopPharmacySipaNumber.text = uiState.epharmacy.sipaNumber
        }
    }

    private fun renderShopRatingAndReview(uiState: ShopInfoUiState) {
        val ratingAndReviewText = when {
            uiState.rating.totalRating.isMoreThanZero() && uiState.review.totalReviews.isMoreThanZero() -> {
                "${uiState.rating.totalRatingFmt} rating • ${uiState.review.totalReviews.splitByThousand()} ulasan"
            }
            uiState.rating.totalRating.isMoreThanZero() -> {
                "${uiState.rating.totalRatingFmt} rating"
            }
            uiState.review.totalReviews.isMoreThanZero() -> {
                "${uiState.review.totalReviews} ulasan"
            }
            else -> ""
        }
        val formattedRatingAndReviewText = "($ratingAndReviewText)"
        
        binding?.run {
            tpgShopRating.text = uiState.rating.ratingScore
            tpgRatingAndReviewText.text = formattedRatingAndReviewText
            tpgBuyerSatisfactionPercentage.text = "${uiState.rating.positivePercentageFmt}%"
        }
    }
    
    
    private fun renderShopPerformance(uiState: ShopInfoUiState) {
        binding?.run {
            labelProductSoldCount.text = uiState.shopPerformance.totalProductSoldCount
            labelChatPerformance.text = uiState.shopPerformance.chatPerformance
            labelOrderProcessTime.text = uiState.shopPerformance.orderProcessTime
        }
    }

    private fun renderShopNotes(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }
    private fun renderShopDescription(uiState: ShopInfoUiState) {
        binding?.tpgShopDescription?.text = MethodChecker.fromHtml(uiState.info.shopDescription)
    }

    private fun renderShopSupportedShipment(uiState: ShopInfoUiState) {
        binding?.run {

        }
    }


}
