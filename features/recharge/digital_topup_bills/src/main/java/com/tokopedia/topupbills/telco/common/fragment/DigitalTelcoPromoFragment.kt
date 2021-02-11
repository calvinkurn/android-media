package com.tokopedia.topupbills.telco.common.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.viewmodel.SharedTelcoViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class DigitalTelcoPromoFragment : BaseDaggerFragment() {

    private lateinit var viewModel: SharedTelcoViewModel
    private lateinit var promoListWidget: TopupBillsPromoListWidget

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedTelcoViewModel::class.java)
        }
    }

    override fun getScreenName(): String {
        return DigitalTelcoPromoFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(DigitalTelcoComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_telco_promo_list, container, false)
        promoListWidget = view.findViewById(R.id.telco_promo_list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.promos.observe(this, Observer {
            promoListWidget.setPromoList(it)
        })

        viewModel.titleMenu.observe(this, Observer {
            promoListWidget.toggleTitle(it)
        })

        viewModel.promoImpression.observe(this, Observer {
            viewModel.promos.value?.let {
                promoListWidget.getVisibleRecentItemsToUsersTracking(it)
            }
        })

        promoListWidget.setListener(object : TopupBillsPromoListWidget.ActionListener {
            override fun onCopiedPromoCode(promoId: Int, voucherCode: String) {
                clickCopyOnPromoCode(promoId)
                viewModel.promos.value?.run {
                    topupAnalytics.eventClickCopyPromoCode(voucherCode, this.indexOfFirst {
                        it.promoCode == voucherCode
                    })
                }

                activity?.let {
                    val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                            CLIP_DATA_VOUCHER_CODE_DIGITAL, voucherCode
                    )
                    clipboard.setPrimaryClip(clip)
                    view?.run {
                        Toaster.build(this,
                                getString(com.tokopedia.common.topupbills.R.string.common_topup_voucher_code_already_copied),
                                Snackbar.LENGTH_LONG).show()
                    }
                }
            }

            override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {
                topupAnalytics.impressionEnhanceCommercePromoList(topupBillsTrackPromoList)
            }

            override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {
                topupAnalytics.clickEnhanceCommercePromo(topupBillsPromo, position)
                if (!TextUtils.isEmpty(topupBillsPromo.urlBannerPromo)) {
                    RouteManager.route(activity, topupBillsPromo.urlBannerPromo)
                }
            }
        })
    }

    private fun clickCopyOnPromoCode(promoId: Int) {
        promoListWidget.notifyPromoItemChanges(promoId)
    }

    companion object {
        const val CLIP_DATA_VOUCHER_CODE_DIGITAL = "digital_telco_clip_data_promo"

        fun newInstance(): Fragment {
            return DigitalTelcoPromoFragment()
        }
    }
}