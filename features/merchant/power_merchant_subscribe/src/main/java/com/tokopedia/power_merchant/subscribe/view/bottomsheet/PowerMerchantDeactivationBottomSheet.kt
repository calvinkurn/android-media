package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetPowerMerchantDeactivationBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.LostBenefitPmDeactivationAdapter
import com.tokopedia.power_merchant.subscribe.view.model.LostBenefitPmDeactivationUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class PowerMerchantDeactivationBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomSheetPowerMerchantDeactivationBinding>()
    private var listener: BottomSheetCancelListener? = null
    private val powerMerchantTracking: PowerMerchantTracking by lazy {
        PowerMerchantTracking(UserSession(context))
    }

    companion object {
        private const val TAG: String = "PowerMerchantCancelBottomSheet"
        private const val ARGS_IS_PM_PRO = "is_pm_pro"

        @JvmStatic
        fun newInstance(isPmPro: Boolean): PowerMerchantDeactivationBottomSheet {
            return PowerMerchantDeactivationBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(ARGS_IS_PM_PRO, isPmPro)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearContentPadding = true
        val itemView = View.inflate(
            context,
            R.layout.bottom_sheet_power_merchant_deactivation,
            null
        )
        binding = BottomSheetPowerMerchantDeactivationBinding.bind(itemView)
        setChild(itemView)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isPmPro = arguments?.getBoolean(ARGS_IS_PM_PRO, false).orFalse()
        initView(isPmPro)
    }

    fun setListener(listener: BottomSheetCancelListener) {
        this.listener = listener
    }

    private fun initView(isPmPro: Boolean) {
        setupAffectPmDeactivation(isPmPro)
        binding?.run {
            btnCancel.setOnClickListener {
                powerMerchantTracking.sendEventClickConfirmToStopPowerMerchant()
                listener?.onClickCancelButton()
            }

            btnBack.setOnClickListener {
                listener?.onClickBackButton()
                dismiss()
            }

            imgAffectPmDeactivation.loadImage(if (context.isDarkMode()) Constant.Image.BG_AFFECT_PM_DEACTIVATION_DM else Constant.Image.BG_AFFECT_PM_DEACTIVATION)
            tvPmDeactivationTnC.movementMethod = LinkMovementMethod.getInstance()
            tvPmDeactivationTnC.text = getString(R.string.pm_pm_deactivation_be_rm_tnc)
        }
    }

    private fun setupAffectPmDeactivation(isPmPro: Boolean) {
        binding?.rvLostBenefitPmDeactivation?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = LostBenefitPmDeactivationAdapter(
                if (isPmPro) getPmProDeactivationList() else getPmDeactivationList()
            )
        }
    }

    private fun getPmDeactivationList(): List<LostBenefitPmDeactivationUiModel> {
        return listOf(
            LostBenefitPmDeactivationUiModel(
                title = getString(R.string.pm_topads_title),
                desc = getString(R.string.pm_topads_desc),
                imgUrl = Constant.Image.IC_TOPADS
            ),
            LostBenefitPmDeactivationUiModel(
                title = getString(R.string.pm_broadcast_chat_title),
                desc = getString(R.string.pm_broadcast_chat_desc),
                imgUrl = Constant.Image.IC_BROADCAST_CHAT
            ),
            LostBenefitPmDeactivationUiModel(
                title = getString(R.string.pm_tokopedia_play_title),
                desc = getString(R.string.pm_tokopedia_play_desc),
                imgUrl = Constant.Image.IC_TOKOPEDIA_PLAY
            )
        )
    }

    private fun getPmProDeactivationList(): List<LostBenefitPmDeactivationUiModel> {
        return listOf(
            LostBenefitPmDeactivationUiModel(
                title = getString(R.string.pm_flash_sale_shop_title),
                desc = getString(R.string.pm_flash_sale_shop_desc),
                imgUrl = Constant.Image.IC_FLASH_SALE_SHOP
            ),
            LostBenefitPmDeactivationUiModel(
                title = getString(R.string.pm_discount_shop_title),
                desc = getString(R.string.pm_discount_shop_desc),
                imgUrl = Constant.Image.IC_DISCOUNT_SHOP
            ),
            LostBenefitPmDeactivationUiModel(
                title = getString(R.string.pm_smart_reply_title),
                desc = getString(R.string.pm_smart_reply_desc),
                imgUrl = Constant.Image.IC_SMART_REPLY
            )
        )
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    interface BottomSheetCancelListener {
        fun onClickCancelButton()
        fun onClickBackButton()
    }
}
