package com.tokopedia.paylater.presentation.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.INTERNAL_URL
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PdpSimulationComponent
import com.tokopedia.paylater.domain.model.BankCardListItem
import com.tokopedia.paylater.domain.model.CreditCardItem
import com.tokopedia.paylater.presentation.adapter.CreditCardRegistrationAdapter
import com.tokopedia.paylater.presentation.viewModel.CreditCardViewModel
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
        val viewModelProvider = ViewModelProviders.of(parentFragment!!, viewModelFactory.get())
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

    private var listener: Listener? = null
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
        dismiss()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        /*baseList.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)
            true
        }*/
        baseList.adapter = CreditCardRegistrationAdapter(arrayListOf()) { creditCardList, bankName, bankSlug ->
            listener?.showCreditCardList(creditCardList, bankName, bankSlug)
            dismiss()
        }
        baseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDefaultParams() {
        setTitle("Ajukan kartu kredit apa?")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = getScreenHeight() / 2
    }

    fun setActionListener(listener: Listener) {
        this.listener = listener
    }

    private fun openUrlWebView(urlString: String) {
        val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewAppLink)
    }

    companion object {
        const val TAG = "CC_TAG"
        fun getInstance() = CreditCardRegistrationBottomSheet()
    }

    interface Listener {
        fun showCreditCardList(arrayList: ArrayList<CreditCardItem>, bankName: String?, bankSlug: String?)
    }
}