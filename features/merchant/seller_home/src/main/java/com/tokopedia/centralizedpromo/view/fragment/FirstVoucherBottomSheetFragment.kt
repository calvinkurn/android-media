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
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.FirstVoucherDataSource
import com.tokopedia.centralizedpromo.view.adapter.FirstVoucherAdapter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CentralizedPromoFirstVoucherBottomsheetLayoutBinding
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class FirstVoucherBottomSheetFragment : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(voucherType: String,
                           productId: String?) = FirstVoucherBottomSheetFragment().apply {
            val bundle = Bundle().apply {
                putString(SellerHomeApplinkConst.VOUCHER_TYPE, voucherType)
                if (productId != null) {
                    putString(SellerHomeApplinkConst.PRODUCT_ID, productId)
                }
            }
            arguments = bundle
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        private const val TAG = "first_voucher"
    }

    private val impressHolder: ImpressHolder = ImpressHolder()
    private var binding by autoClearedNullable<CentralizedPromoFirstVoucherBottomsheetLayoutBinding>()

    private val voucherType by lazy {
        arguments?.getString(SellerHomeApplinkConst.VOUCHER_TYPE).orEmpty()
    }

    private val productId by lazy {
        arguments?.getString(SellerHomeApplinkConst.PRODUCT_ID)
    }

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CentralizedPromoFirstVoucherBottomsheetLayoutBinding.inflate(inflater)
        binding?.let {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addOnImpressionListener(impressHolder) {
            CentralizedPromoTracking.sendFirstVoucherBottomSheetImpression(userSession.userId)
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
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initView() {
        setupCloseClickListener()
        setupBottomSheetText()
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
        when (voucherType) {
            SellerHomeApplinkConst.TYPE_PRODUCT -> {
                binding?.firstVoucherBottomSheetTitle?.text =
                    context?.getString(R.string.centralized_promo_bottomsheet_product_coupon_title)
                binding?.firstVoucherButton?.text =
                    context?.getString(R.string.centralized_promo_bottomsheet_product_coupon_next)
            }
            else -> {
                binding?.firstVoucherBottomSheetTitle?.text =
                    context?.getString(R.string.centralized_promo_bottomsheet_title)
                binding?.firstVoucherButton?.text =
                    context?.getString(R.string.centralized_promo_bottomsheet_next)
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.firstVoucherRecyclerView?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemList =
                when (voucherType) {
                    SellerHomeApplinkConst.TYPE_PRODUCT -> FirstVoucherDataSource.getFirstProductCouponInfoItems()
                    else -> FirstVoucherDataSource.getFirstVoucherCashbackInfoItems()
                }
            adapter = FirstVoucherAdapter(itemList)
        }
    }

    private fun setupTicker() {
        val isProductCoupon = voucherType == SellerHomeApplinkConst.TYPE_PRODUCT
        binding?.firstVoucherTicker?.showWithCondition(!isProductCoupon)
    }

    private fun setupButtonClick() {
        binding?.firstVoucherButton?.setOnClickListener {
            CentralizedPromoTracking.sendFirstVoucherBottomSheetClick(userSession.userId, false)
            val voucherApplink =
                if (voucherType == SellerHomeApplinkConst.TYPE_PRODUCT) {
                    setVoucherProductSharedPrefValue()
                    if (productId == null) {
                        ApplinkConst.SellerApp.CREATE_VOUCHER_PRODUCT
                    } else {
                        "${ApplinkConst.SellerApp.CREATE_VOUCHER_PRODUCT}/$productId"
                    }
                } else {
                    ApplinkConstInternalSellerapp.CREATE_VOUCHER
                }
            RouteManager.route(context, voucherApplink)
            this.dismiss()
        }
    }

    private fun setVoucherProductSharedPrefValue() {
        sharedPref.run {
            val isFirstTime = getBoolean(FirstVoucherDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
            if (isFirstTime) {
                edit().putBoolean(FirstVoucherDataSource.IS_PRODUCT_COUPON_FIRST_TIME, false)
                    .apply()
            }
        }
    }

}