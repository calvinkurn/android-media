package com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.constants.INTERNAL_URL
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.creditcard.domain.model.BankCardListItem
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardItem
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.adapter.CreditCardRegistrationAdapter
import com.tokopedia.pdpsimulation.creditcard.viewmodel.CreditCardViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.base_list_bottomsheet_widget.*
import javax.inject.Inject


class CreditCardRegistrationBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var component: PdpSimulationComponent? = null

    private val credCardViewModel: CreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(CreditCardViewModel::class.java)
    }

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private var pdpSimulationCallback: PdpSimulationCallback? = null
    private val childLayoutRes = R.layout.base_list_bottomsheet_widget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setDefaultParams()
        initBottomSheet()
    }

    private fun initInjector() {
        component = PdpSimulationComponent::class.java.cast((activity as (HasComponent<PdpSimulationComponent>)).component)
        component?.inject(this) ?: dismiss()
    }

    private fun observeViewModel() {
        credCardViewModel.creditCardBankResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onBankListLoaded(it.data)
                is Fail -> onBankListLoadingFail(it.throwable)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        initAdapter()
        fbViewAllCards.apply {
            visible()
            addItem(arrayListOf(FloatingButtonItem(
                    context.getString(R.string.credit_card_view_all_cards)
            ) {
                pdpSimulationCallback?.sendAnalytics(PdpSimulationEvent.CreditCard.SeeMoreBankClickEvent("click"))
                openUrlWebView(INTERNAL_URL)
            }))
        }
    }

    private fun onBankListLoaded(data: ArrayList<BankCardListItem>) {
        (baseList.adapter as CreditCardRegistrationAdapter).run {
            bankList = data
            notifyDataSetChanged()
        }
    }

    private fun onBankListLoadingFail(throwable: Throwable) {

    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        baseList.adapter = CreditCardRegistrationAdapter(arrayListOf()) { creditCardList, bankName, bankSlug ->
            pdpSimulationCallback?.sendAnalytics(PdpSimulationEvent.CreditCard.ChooseBankClickEvent(bankName ?: ""))
            openBottomSheet(creditCardList, bankName, bankSlug)
            dismiss()
        }
        baseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun openBottomSheet(creditCardList: ArrayList<CreditCardItem>, bankName: String?, bankSlug: String?) {
        val bundle = Bundle().apply {
            putParcelableArrayList(CreditCardsListBottomSheet.CREDIT_CARD_DATA, creditCardList)
            putString(CreditCardsListBottomSheet.BANK_NAME, bankName)
            putString(CreditCardsListBottomSheet.BANK_SLUG, bankSlug)
        }
        pdpSimulationCallback?.openBottomSheet(bundle, CreditCardsListBottomSheet::class.java)
    }

    private fun setDefaultParams() {
        setTitle("Ajukan kartu kredit apa?")
        isDragable = true
        isHideable = true
        showCloseIcon = false
        showHeader = true
        showKnob = true
        showHeader = true
        customPeekHeight = getScreenHeight() / 2
    }

    private fun openUrlWebView(urlString: String) {
        val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewAppLink)
    }

    companion object {
        const val TAG = "CreditCardRegistrationBottomSheet"
        fun show(pdpSimulationCallback: PdpSimulationCallback, childFragmentManager: FragmentManager) {
            val fragment = CreditCardRegistrationBottomSheet().apply {
                this.pdpSimulationCallback = pdpSimulationCallback
            }
            fragment.show(childFragmentManager, TAG)
        }
    }
}