package com.tokopedia.centralizedpromo.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.centralizedpromo.CentralizedPromoApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.R
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.databinding.CentralizedPromoFirstPromoBottomsheetLayoutBinding
import com.tokopedia.centralizedpromo.di.component.DaggerCentralizedPromoComponent
import com.tokopedia.centralizedpromo.view.FirstPromoDataSource
import com.tokopedia.centralizedpromo.view.adapter.FirstVoucherAdapter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class FirstTimePromoBottomSheetFragment : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(promoType: String,
                           productId: String?) = FirstTimePromoBottomSheetFragment().apply {
            val bundle = Bundle().apply {
                putString(CentralizedPromoApplinkConst.PROMO_TYPE, promoType)
                if (productId != null) {
                    putString(CentralizedPromoApplinkConst.PRODUCT_ID, productId)
                }
            }
            arguments = bundle
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        private const val TAG = "first_voucher"
    }

    private val impressHolder: ImpressHolder = ImpressHolder()
    private var binding by autoClearedNullable<CentralizedPromoFirstPromoBottomsheetLayoutBinding>()

    private var promoType = ""
    private var productId: String? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            initView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        initValues()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CentralizedPromoFirstPromoBottomsheetLayoutBinding.inflate(inflater)
        binding?.let {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addOnImpressionListener(impressHolder) {
            if (promoType == CentralizedPromoApplinkConst.TYPE_VOUCHER_PRODUCT) {
                CentralizedPromoTracking.sendFirstVoucherProductBottomSheetImpression(userSession.shopId)
            } else {
                CentralizedPromoTracking.sendFirstVoucherBottomSheetImpression(userSession.userId)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        if (fragmentManager.isStateSaved || isAdded) {
            return
        }
        show(fragmentManager, TAG)
    }

    private fun initInjector() {
        DaggerCentralizedPromoComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initValues() {
        promoType = arguments?.getString(CentralizedPromoApplinkConst.PROMO_TYPE).orEmpty()
        productId = arguments?.getString(CentralizedPromoApplinkConst.PRODUCT_ID)
    }

    private fun initView() {
        setupCloseClickListener()
        setupBottomSheetText()
        setupDetailText()
        setupRecyclerView()
        setupTicker()
        setupButtonClick()
    }

    private fun setupCloseClickListener() {
        setCloseClickListener {
            CentralizedPromoTracking.sendFirstVoucherBottomSheetClick(userSession.userId, true)
            dismiss()
        }
    }

    private fun setupBottomSheetText() {
        val bottomSheetTitle: String
        val buttonText: String
        when (promoType) {
            CentralizedPromoApplinkConst.TYPE_VOUCHER_PRODUCT -> {
                bottomSheetTitle =
                    context?.getString(R.string.centralized_promo_bottomsheet_product_coupon_title)
                        .orEmpty()
                buttonText =
                    context?.getString(R.string.centralized_promo_bottomsheet_product_coupon_next)
                        .orEmpty()
            }
            CentralizedPromoApplinkConst.TYPE_TOKOPEDIA_PLAY -> {
                bottomSheetTitle =
                    context?.getString(R.string.centralized_promo_bottomsheet_tokopedia_play_title).orEmpty()
                buttonText =
                    context?.getString(R.string.centralized_promo_bottomsheet_tokopedia_play_next).orEmpty()
            }
            else -> {
                bottomSheetTitle =
                    context?.getString(R.string.centralized_promo_bottomsheet_title).orEmpty()
                buttonText =
                    context?.getString(R.string.centralized_promo_bottomsheet_next).orEmpty()
            }
        }
        binding?.firstPromoBottomSheetTitle?.text = bottomSheetTitle
        binding?.firstPromoButton?.text = buttonText
    }

    private fun setupDetailText() {
        val isTokopediaPlay = promoType == CentralizedPromoApplinkConst.TYPE_TOKOPEDIA_PLAY
        binding?.firstPromoBottomSheetDetail?.showWithCondition(!isTokopediaPlay)
    }

    private fun setupRecyclerView() {
        binding?.firstPromoRecyclerView?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemList =
                when (promoType) {
                    CentralizedPromoApplinkConst.TYPE_VOUCHER_PRODUCT -> FirstPromoDataSource.getFirstProductCouponInfoItems()
                    CentralizedPromoApplinkConst.TYPE_TOKOPEDIA_PLAY -> FirstPromoDataSource.getTokopediaPlayInfoItems()
                    else -> FirstPromoDataSource.getFirstVoucherCashbackInfoItems()
                }
            adapter = FirstVoucherAdapter(itemList)
        }
    }

    private fun setupTicker() {
        val isCashbackCoupon = promoType == CentralizedPromoApplinkConst.TYPE_VOUCHER_CASHBACK
        binding?.firstPromoTicker?.showWithCondition(isCashbackCoupon)
    }

    private fun setupButtonClick() {
        binding?.firstPromoButton?.setOnClickListener {
            val voucherApplink =
                when(promoType) {
                    CentralizedPromoApplinkConst.TYPE_VOUCHER_PRODUCT -> {
                        CentralizedPromoTracking.sendFirstVoucherProductBottomSheetClick(userSession.shopId)
                        setVoucherProductSharedPrefValue()
                        ApplinkConstInternalSellerapp.SELLER_MVC_CREATE_PRODUCT_VOUCHER
                    }
                    CentralizedPromoApplinkConst.TYPE_VOUCHER_CASHBACK -> {
                        CentralizedPromoTracking.sendFirstVoucherBottomSheetClick(
                            userSession.userId,
                            false
                        )
                        ApplinkConstInternalSellerapp.SELLER_MVC_CREATE_SHOP_VOUCHER
                    }
                    CentralizedPromoApplinkConst.TYPE_TOKOPEDIA_PLAY -> {
                        setTokopediaPlaySharedPrefValue()
                        ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER
                    }
                    else -> ""
                }
            RouteManager.route(context, voucherApplink)
            this.dismiss()
        }
    }

    private fun setVoucherProductSharedPrefValue() {
        sharedPref.run {
            val isFirstTime = getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
            if (isFirstTime) {
                edit().putBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, false)
                    .apply()
            }
        }
    }

    private fun setTokopediaPlaySharedPrefValue() {
        sharedPref.run {
            val isFirstTime = getBoolean(FirstPromoDataSource.IS_TOKOPEDIA_PLAY_FIRST_TIME, true)
            if (isFirstTime) {
                edit().putBoolean(FirstPromoDataSource.IS_TOKOPEDIA_PLAY_FIRST_TIME, false)
                    .apply()
            }
        }
    }

}
