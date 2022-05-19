package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmCouponAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_coupon_fragment.*
import javax.inject.Inject

class TokomemberDashCouponFragment : BaseDaggerFragment(), ProgramActions, SortFilterBottomSheet.Callback{

    private var selectedType = 0
    private var selectedStatus = ""

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmCouponViewModel: TmCouponViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmCouponViewModel::class.java)
    }

    private val tmCouponAdapter: TmCouponAdapter by lazy{
        TmCouponAdapter(arrayListOf(), childFragmentManager, this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_coupon_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterData = ArrayList<SortFilterItem>()
        val filterStatus = SortFilterItem("Semua Status")
        filterStatus.listener = {
            filterStatus.type = if(filterStatus.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            filterStatus.selectedItem = arrayListOf("Semua Status", "Aktif", "Belum Aktif", "Sudah Berakhir")
        }
        filterData.add(filterStatus)
        val filterType = SortFilterItem("Semua Type")
        filterType.chevronListener
        filterType.iconDrawable = context?.let { getIconUnifyDrawable(it, IconUnify.ARROW_DOWN) }
        filterType.listener = {
            val filterList = ArrayList<Filter>()
            val options = ArrayList<Option>()
            options.add(Option(name = "Semua Status", key = "Semua Status", value = "Semua Status", inputType = Option.INPUT_TYPE_RADIO, inputState = "true"))
            options.add(Option(name = "Aktif", key = "Aktif", value = "Aktif", inputType = Option.INPUT_TYPE_RADIO))
            options.add(Option(name = "Belum Aktif", key = "Belum Aktif", value = "Belum Aktif", inputType = Option.INPUT_TYPE_RADIO))
            options.add(Option(name = "Sudah Berakhir", key = "Sudah Berakhir", value = "Sudah Berakhir", inputType = Option.INPUT_TYPE_RADIO))
//            filterList.add(Filter(title = "Status Kupon", options = options))
//            filterList.add(Filter(title = "Status Type", options = options))
//            val data = DataValue(filterList, sort = arrayListOf())
//            val dynamicFilterModel = DynamicFilterModel(data)
//            val sortFilterBottomSheet = SortFilterBottomSheet()
//            sortFilterBottomSheet.show(childFragmentManager, mapOf(), dynamicFilterModel = dynamicFilterModel, callback = this)
            options.forEach {
                if(options.indexOf(it) == selectedType){
                    it.inputState = "true"
                }
            }
            if(selectedType == 0){
                options[0].inputState = "true"
            }
            FilterGeneralDetailBottomSheet().show(
                parentFragmentManager,
                Filter(title = "Status Kupon", options = options),
                callback = object : FilterGeneralDetailBottomSheet.Callback{
                    override fun onApplyButtonClicked(optionList: List<Option>?) {
                        optionList?.forEachIndexed { index, option ->
                            if(option.inputState == "true"){
                                if(selectedType != index){
                                    selectedType = index
                                }
                                else{
                                    selectedType = 0
                                }
                            }
                        }
                        if(selectedType == 0){
                            filterType.type = ChipsUnify.TYPE_NORMAL
                        }
                        else{
                            filterType.type = ChipsUnify.TYPE_SELECTED
                        }
                    }
                },
            )
//            filterType.type = if(filterType.type == ChipsUnify.TYPE_NORMAL) {
//                ChipsUnify.TYPE_SELECTED
//            } else {
//                ChipsUnify.TYPE_NORMAL
//            }
//            filterType.selectedItem = arrayListOf("Semua Type", "Cashback", "Gratis Ongkir")
        }
        filterData.add(filterType)
        filter.addItem(filterData)
        filter.addItem(filterData)
        filter.parentListener = {
            Toast.makeText(context, "Type", Toast.LENGTH_SHORT).show()
        }
        rv_coupon.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = tmCouponAdapter
        }

        observeViewModel()
        tmCouponViewModel.getCouponList("0,1,2", 1)
    }

    private fun filterClick() {

    }

    private fun observeViewModel() {

        tmCouponViewModel.couponListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    tmCouponAdapter.vouchersItemList = it.data.merchantPromotionGetMVList?.data?.vouchers as ArrayList<VouchersItem>
                    tmCouponAdapter.notifyDataSetChanged()
                }
                is Fail -> {
                }
            }
        })
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    companion object {
        fun newInstance(): TokomemberDashCouponFragment {
            return TokomemberDashCouponFragment()
        }
    }

    override fun option(type: String, programId: Int, shopId: Int) {

    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        applySortFilterModel.selectedSortName
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        mapParameter
    }

}