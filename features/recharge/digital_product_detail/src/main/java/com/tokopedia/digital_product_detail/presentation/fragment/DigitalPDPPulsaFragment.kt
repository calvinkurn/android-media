package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpPulsaBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPPulsaFragment : BaseDaggerFragment()  {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPPulsaViewModel

    private var binding by autoClearedNullable<FragmentDigitalPdpPulsaBinding>()

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPPulsaViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDigitalPdpPulsaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClientNumberWidget()
        observeData()
    }

    private fun observeData() {
        viewModel.dummy.observe(viewLifecycleOwner, {
            binding?.rechargePdpPulsaClientNumberWidget?.run {
                setLoading(false)
            }
        })
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
            val favoriteNumber = listOf<TopupBillsSeamlessFavNumberItem>(
                TopupBillsSeamlessFavNumberItem(
                    "", "081208120812", "", "", "", ""),
                TopupBillsSeamlessFavNumberItem(
                    "", "081208120812", "AAAAaaaaAA", "", "", ""),
                TopupBillsSeamlessFavNumberItem(
                    "", "081208120812", "BBBBBBBBbbbbbb", "", "", ""),
                TopupBillsSeamlessFavNumberItem(
                    "", "087808780878", "", "", "", ""),
            )

            setInputFieldStaticLabel(getString(
                com.tokopedia.recharge_component.R.string.label_recharge_client_number))
            setInputFieldType(RechargeClientNumberWidget.InputFieldType.Telco)

            // filter chip
            setFilterChipShimmer(false, favoriteNumber.isEmpty())
            setFavoriteNumber(favoriteNumber)
            setAutoCompleteList(favoriteNumber)
            setInputNumberValidator { true }
            setListener(
                inputFieldListener = object: RechargeClientNumberWidget.ClientNumberInputFieldListener {
                    override fun onRenderOperator(isDelayed: Boolean) {
                        binding?.rechargePdpPulsaClientNumberWidget?.setLoading(true)
                        viewModel.getDelayedResponse()
                    }
                },
                autoCompleteListener = object: RechargeClientNumberWidget.ClientNumberAutoCompleteListener {
                    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                        // do nothing
                    }
                },
                filterChipListener = object: RechargeClientNumberWidget.ClientNumberFilterChipListener {
                    override fun onShowFilterChip(isLabeled: Boolean) {
                        // do nothing
                    }

                    override fun onClickFilterChip(isLabeled: Boolean) {
                        // do nothing
                    }

                    override fun onClickIcon() {
                        // do nothing
                    }
                }
            )
        }
    }

    companion object {
        fun newInstance() = DigitalPDPPulsaFragment()
    }
}