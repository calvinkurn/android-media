package com.tokopedia.rechargegeneral.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.viewmodel.SharedRechargeGeneralViewModel
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_recharge_general_promo_list.*
import javax.inject.Inject

class RechargeGeneralPromoListFragment : BaseDaggerFragment(), TopupBillsPromoListWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: SharedRechargeGeneralViewModel
    @Inject
    lateinit var rechargeGeneralAnalytics: RechargeGeneralAnalytics

    private lateinit var promoList: ArrayList<TopupBillsPromo>
    private var showTitle = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_general_promo_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedRechargeGeneralViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            promoList = it.getParcelableArrayList(EXTRA_PARAM_PROMO_LIST) ?: arrayListOf()
            showTitle = it.getBoolean(EXTRA_PARAM_SHOW_TITLE, true)
        }

        with(promo_list_widget) {
            setListener(this@RechargeGeneralPromoListFragment)
            if (::promoList.isInitialized && promoList.isNotEmpty()) {
                setPromoList(promoList)
                toggleTitle(showTitle)
            }
        }
    }

    override fun onCopiedPromoCode(promoId: Int, voucherCode: String) {
        rechargeGeneralAnalytics.eventClickCopyPromo(voucherCode, promoList.indexOfFirst { it.promoCode == voucherCode })

        promo_list_widget.notifyPromoItemChanges(promoId)
        activity?.let {
            val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                    CLIP_DATA_VOUCHER_CODE_DIGITAL, voucherCode
            )
            clipboard.setPrimaryClip(clip)

            view?.run {
                Toaster.build(this,
                        getString(com.tokopedia.common.topupbills.R.string.common_topup_voucher_code_already_copied), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {

    }

    override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {
        if (!TextUtils.isEmpty(topupBillsPromo.urlBannerPromo)) {
            RouteManager.route(activity, topupBillsPromo.urlBannerPromo)
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(RechargeGeneralComponent::class.java).inject(this)
    }

    companion object {
        private const val EXTRA_PARAM_PROMO_LIST = "EXTRA_PARAM_PROMO_LIST"
        private const val EXTRA_PARAM_SHOW_TITLE = "EXTRA_PARAM_SHOW_TITLE"

        const val CLIP_DATA_VOUCHER_CODE_DIGITAL = "digital_telco_clip_data_promo"

        fun newInstance(promos: List<TopupBillsPromo>, showTitle: Boolean = true): RechargeGeneralPromoListFragment {
            val fragment = RechargeGeneralPromoListFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_PARAM_PROMO_LIST, ArrayList(promos))
            bundle.putBoolean(EXTRA_PARAM_SHOW_TITLE, showTitle)
            fragment.arguments = bundle
            return fragment
        }
    }

}