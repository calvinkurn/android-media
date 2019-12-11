package com.tokopedia.digital.productV2.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.digital.R
import com.tokopedia.digital.productV2.di.DigitalProductComponent
import com.tokopedia.digital.productV2.presentation.viewmodel.SharedDigitalProductViewModel
import kotlinx.android.synthetic.main.fragment_digital_promo_list.*
import javax.inject.Inject

class DigitalProductPromoListFragment: BaseDaggerFragment(), TopupBillsPromoListWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: SharedDigitalProductViewModel

    private lateinit var promoList: ArrayList<TopupBillsPromo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_promo_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedDigitalProductViewModel::class.java)
        }
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
        viewModel.setPromoSelected(promoId)
        // TODO: Add tracking
    }

    override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {
        // TODO: Add tracking
    }

    override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DigitalProductComponent::class.java).inject(this)
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