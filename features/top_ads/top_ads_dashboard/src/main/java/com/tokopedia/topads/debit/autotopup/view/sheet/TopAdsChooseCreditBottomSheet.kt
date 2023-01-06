package com.tokopedia.topads.debit.autotopup.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.model.TopAdsShopTierShopGradeData
import com.tokopedia.topads.debit.autotopup.data.model.TopUpCreditItemData
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsCreditListAdapter
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TopAdsChooseCreditBottomSheet : BottomSheetUnify() {
    private var listGroup: ListUnify? = null
    private var creditItemRecyclerView: RecyclerView? = null
    private var adapter: TopAdsCreditListAdapter? = null
    private var manualRadioButton: RadioButtonUnify? = null
    private var autoRadioButton: RadioButtonUnify? = null
    private var saveButton: UnifyButton? = null
    private var creditData: CreditResponse? = null
    private var selectedNominal: Int = 0
    var onSaved: ((positionSelected: Int) -> Unit)? = null

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    companion object {
        fun newInstance() = TopAdsChooseCreditBottomSheet()
    }

    private val viewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            ).get(TopAdsAutoTopUpViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initInjector()
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_dash_choose_credit_sheet, null)
        showCloseIcon = false
        clearContentPadding = true
        isDragable = true
        setChild(contentView)
        initView(contentView)
    }

    private fun initView(contentView: View?) {
        listGroup = contentView?.findViewById(R.id.listGroup)
        manualRadioButton = contentView?.findViewById(R.id.manualRadioButton)
        autoRadioButton = contentView?.findViewById(R.id.autoRadioButton)
        creditItemRecyclerView = contentView?.findViewById(R.id.creditItemRecyclerView)
        saveButton = contentView?.findViewById(R.id.saveButton)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getTopAdsCreditList()
        manualRadioButton?.setOnClickListener {
            if (autoRadioButton?.isChecked == true) {
                autoRadioButton?.isChecked = false
                creditItemRecyclerView?.show()
            }
        }
        autoRadioButton?.setOnClickListener {
            if (manualRadioButton?.isChecked == true) {
                manualRadioButton?.isChecked = false
                creditItemRecyclerView?.hide()
            }
        }
        saveButton?.setOnClickListener {
            dismiss()
            if (selectedNominal != 0) onSaved?.invoke(selectedNominal)
        }
        viewModel?.topAdsTopUpCreditData?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetCreditListData(it.data)
            }
        })
    }

    private fun onSuccessGetCreditListData(data: TopAdsShopTierShopGradeData.ShopInfoByID.Result) {
        val list = viewModel?.getCreditItemData2(creditData?.credit, data)
        addManualTopUp(list)
    }


    private fun addManualTopUp(list: MutableList<TopUpCreditItemData>?) {

        adapter = TopAdsCreditListAdapter() { position ->
            selectedNominal = position
            list?.forEach { it.clicked = false }
            list?.get(position)?.clicked = true
            if (list != null) {
                adapter?.submitList(list)
            }
        }
        creditItemRecyclerView?.adapter = adapter
        creditItemRecyclerView?.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)


        if (list != null) {
            adapter?.submitList(list)
        }
    }

    fun show(
        fragmentManager: FragmentManager, data: CreditResponse?,
    ) {
        this.creditData = data
        show(fragmentManager, "top_up_sheet")
    }
}
