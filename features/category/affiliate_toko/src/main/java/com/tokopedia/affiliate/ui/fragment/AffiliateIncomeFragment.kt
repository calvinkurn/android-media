package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.viewmodel.AffiliateIncomeViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliateIncomeFragment : BaseViewModelFragment<AffiliateIncomeViewModel>(),
        AffiliateDatePickerRangeChangeInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory


    private lateinit var affiliateIncomeViewModel : AffiliateIncomeViewModel

    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())
    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private var listSize = 0
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var selectedRange = AffiliateBottomDatePicker.TODAY

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliateIncomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_withdrawal_fragment_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun setObservers() {
        affiliateIncomeViewModel.getAffiliateBalanceData().observe(this, {
            if(it.status == 1)
                setAffiliateBalance(it)
            else
                setErrorState(it.error.message)
        })

        affiliateIncomeViewModel.getAffiliateDataItems().observe(this, {
            adapter.removeShimmer(listSize)
            if(it.isEmpty()){
                showGlobalErrorEmptyState()
            } else {
                listSize += it.size
                adapter.addMoreData(it)
                loadMoreTriggerListener?.updateStateAfterGetData()

            }
        })
        affiliateIncomeViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.addShimmer()
                else
                    adapter.removeShimmer(listSize)
            }
        })
        affiliateIncomeViewModel.getErrorMessage().observe(this, { error ->
            setErrorState(error.toString())
        })
    }

    private fun showGlobalErrorEmptyState() {
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.apply {
            show()
            errorTitle.text = getString(R.string.affiliate_empty_transaction)
            errorDescription.text = getString(R.string.affiliate_empty_transaction_description)
            setButtonFull(true)
            errorSecondaryAction.gone()
            errorAction.text = getString(R.string.affiliate_choose_date)
            setActionClickListener {
                hide()
                AffiliateBottomDatePicker.newInstance(AffiliateBottomDatePicker.TODAY,this@AffiliateIncomeFragment).show(childFragmentManager, "")
            }
        }
    }

    private fun setErrorState(message: String) {
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.apply {
            show()
            errorTitle.text = message
            setButtonFull(true)
            errorSecondaryAction.gone()
            setActionClickListener {
                hide()
                affiliateIncomeViewModel.getAffiliateBalance()
            }
        }
    }

    private fun setAffiliateBalance(affiliateBalance: AffiliateBalance.AffiliateBalance.Data) {
        affiliateBalance.apply {
            view?.findViewById<Typography>(R.id.saldo_amount_affiliate)?.let {
                it.text = amountFormatted
                it.show()
            }
            view?.findViewById<LoaderUnify>(R.id.affiliate_saldo_progress_bar)?.gone()
        }
    }

    private fun afterViewCreated() {
        affiliateIncomeViewModel.getAffiliateTransactionHistory(getStartFromDate(selectedRange), getEndFromDate(AffiliateBottomDatePicker.TODAY), 0)
        view?.findViewById<Typography>(R.id.withdrawal_user_name)?.text = affiliateIncomeViewModel.getUserName()
        ImageHandler.loadImageCircle2(context, view?.findViewById<ImageUnify>(R.id.withdrawal_user_image), affiliateIncomeViewModel.getUserProfilePicture())
        view?.findViewById<Typography>(R.id.date_range_text)?.text = AffiliateBottomDatePicker.TODAY
        view?.findViewById<ConstraintLayout>(R.id.date_range)?.setOnClickListener {
            AffiliateBottomDatePicker.newInstance(AffiliateBottomDatePicker.TODAY,this).show(childFragmentManager, "")
        }
        view?.findViewById<RecyclerView>(R.id.withdrawal_transactions_rv)?.let {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter

            loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
            it.adapter = adapter
            loadMoreTriggerListener?.let { listener ->
                it.addOnScrollListener(listener)
            }
        }
        view?.findViewById<NavToolbar>(R.id.withdrawal_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.affiliate_withdrawal)
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(affiliateIncomeViewModel.hasNext)
                    affiliateIncomeViewModel.getAffiliateTransactionHistory(getStartFromDate(selectedRange), getEndFromDate(selectedRange),page - 1)
            }
        }
    }

    private fun getStartFromDate(date: String): String {
        return ""
    }
    private fun getEndFromDate(date: String): String {
        return ""
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectIncomeFragment(this)
    }

    override fun getViewModelType(): Class<AffiliateIncomeViewModel> {
        return AffiliateIncomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateIncomeViewModel = viewModel as AffiliateIncomeViewModel
    }

    override fun rangeChanged(range: AffiliateDatePickerData) {
        selectedRange = range.text
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        view?.findViewById<Typography>(R.id.date_range_text)?.text = range.text
        view?.findViewById<ConstraintLayout>(R.id.date_range)?.setOnClickListener {
            AffiliateBottomDatePicker.newInstance(range.text,this).show(childFragmentManager, "")
        }
    }

    override fun onRangeSelectionButtonClicked() {

    }

}