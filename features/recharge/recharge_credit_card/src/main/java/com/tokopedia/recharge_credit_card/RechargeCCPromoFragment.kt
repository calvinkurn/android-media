package com.tokopedia.recharge_credit_card

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.recharge_credit_card.databinding.FragmentRechargeCcPromoBinding
import com.tokopedia.recharge_credit_card.di.RechargeCCComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class RechargeCCPromoFragment: BaseDaggerFragment(), TopupBillsPromoListWidget.ActionListener {

    private var binding by autoClearedNullable<FragmentRechargeCcPromoBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var promoList: ArrayList<TopupBillsPromo> = arrayListOf()
    private var showTitle = true

    override fun getScreenName(): String {
        return RechargeCCPromoFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(RechargeCCComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRechargeCcPromoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            promoList = it.getParcelableArrayList(EXTRA_PARAM_PROMO) ?: arrayListOf()
            showTitle = it.getBoolean(EXTRA_PARAM_SHOW_TITLE, true)
        }
        initPromoListWidget()
    }

    private fun initPromoListWidget() {
        binding?.promoListWidget?.run {
            setListener(this@RechargeCCPromoFragment)
            if (promoList.isNotEmpty()) {
                setPromoList(promoList)
                toggleTitle(showTitle)
            }
        }
    }

    override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {
        if (!TextUtils.isEmpty(topupBillsPromo.urlBannerPromo)) {
            RouteManager.route(activity, topupBillsPromo.urlBannerPromo)
        }
    }

    override fun onCopiedPromoCode(promoId: String, voucherCode: String) {
        binding?.promoListWidget?.notifyPromoItemChanges(promoId)
        activity?.let {
            val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                CLIP_DATA_VOUCHER_CODE_DIGITAL,
                voucherCode
            )
            clipboard.setPrimaryClip(clip)

            view?.run {
                Toaster.build(
                    this,
                    getString(com.tokopedia.common.topupbills.R.string.common_topup_voucher_code_already_copied),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {
        // do nothing
    }

    companion object {

        private const val EXTRA_PARAM_PROMO = "EXTRA_PARAM_PROMO"
        private const val EXTRA_PARAM_SHOW_TITLE = "EXTRA_PARAM_SHOW_TITLE"

        const val CLIP_DATA_VOUCHER_CODE_DIGITAL = "digital_telco_clip_data_promo"

        fun newInstance(promos: List<TopupBillsPromo>, showTitle: Boolean = true): RechargeCCPromoFragment {
            val fragment = RechargeCCPromoFragment()
            val bundle = Bundle().apply {
                putParcelableArrayList(EXTRA_PARAM_PROMO, ArrayList(promos))
                putBoolean(EXTRA_PARAM_SHOW_TITLE, showTitle)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
