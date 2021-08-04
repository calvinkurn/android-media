package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivity
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpPromoListSpaceID
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_emoney_promo_list.*
import javax.inject.Inject

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpPromoListFragment : BaseDaggerFragment(), TopupBillsPromoListWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_promo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
    }

    private fun renderView() {
        val promoData = arguments?.getParcelableArrayList<TopupBillsPromo>(EXTRA_PROMOS)
                ?: arrayListOf()
        emoneyPdpPromoListWidget.setPromoList(promoData)

        val showTitle = arguments?.getBoolean(EXTRA_SHOW_TITLE) ?: false
        emoneyPdpPromoListWidget.toggleTitle(showTitle)

        emoneyPdpPromoListWidget.getRecyclerView().addItemDecoration(EmoneyPdpPromoListSpaceID())
        emoneyPdpPromoListWidget.setListener(this)
    }

    override fun onCopiedPromoCode(promoId: Int, voucherCode: String) {
        emoneyPdpPromoListWidget.notifyPromoItemChanges(promoId)

        activity?.let {
            val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(CLIP_DATA_VOUCHER_CODE_DIGITAL, voucherCode)
            clipboard.setPrimaryClip(clip)
            if (it is EmoneyPdpActivity) {
                it.promoCode = voucherCode
            }
            view?.run {
                Toaster.build(this,
                        getString(com.tokopedia.common.topupbills.R.string.common_topup_voucher_code_already_copied),
                        Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {

    }

    override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {
        if (topupBillsPromo.urlBannerPromo.isNotEmpty()) {
            RouteManager.route(activity, topupBillsPromo.urlBannerPromo)
        }
    }

    companion object {
        const val CLIP_DATA_VOUCHER_CODE_DIGITAL = "digital_telco_clip_data_promo"
        const val EXTRA_SHOW_TITLE = "EXTRA_SHOW_TITLE"
        const val EXTRA_PROMOS = "EXTRA_PROMOS"

        fun newInstance(showTitle: Boolean = false, promoList: ArrayList<TopupBillsPromo>): Fragment = EmoneyPdpPromoListFragment().also {
            it.arguments = Bundle().apply {
                putBoolean(EXTRA_SHOW_TITLE, showTitle)
                putParcelableArrayList(EXTRA_PROMOS, promoList)
            }
        }
    }
}