package com.tokopedia.digital.productV2.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.digital.R
import kotlinx.android.synthetic.main.fragment_digital_promo_list.*

class DigitalProductPromoListFragment: BaseDaggerFragment(), TopupBillsPromoListWidget.ActionListener {

    private lateinit var promoList: ArrayList<TopupBillsPromo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_promo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            promoList = it.getParcelableArrayList(EXTRA_PARAM_PROMO_LIST) ?: arrayListOf()
        }

        with(promo_list_widget) {
            setListener(this@DigitalProductPromoListFragment)
            if (::promoList.isInitialized && promoList.isNotEmpty()) setPromoList(promoList)
        }
    }

    override fun onCopiedPromoCode(promoId: Int, voucherCode: String) {

    }

    override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {

    }

    override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
//        getComponent(DigitalProductComponent::class.java).inject(this)
    }

    companion object {
        private const val EXTRA_PARAM_PROMO_LIST = "EXTRA_PARAM_PROMO_LIST"

        fun newInstance(promos: List<TopupBillsPromo>): DigitalProductPromoListFragment {
            val fragment = DigitalProductPromoListFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_PARAM_PROMO_LIST, ArrayList(promos))
            fragment.arguments = bundle
            return fragment
        }
    }

}