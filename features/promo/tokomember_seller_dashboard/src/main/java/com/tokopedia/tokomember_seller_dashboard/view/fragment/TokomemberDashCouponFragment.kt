package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.DELETE
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.STOP
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmCouponAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_coupon_fragment.*
import javax.inject.Inject


class TokomemberDashCouponFragment : BaseDaggerFragment(), TmCouponActions, SortFilterBottomSheet.Callback{

    private var voucherStatus = "1,2,3,4"
    private var voucherType = 0
    private var selectedType = "0"
    private lateinit var selectedStatus: StringBuilder
    private var selectedStatusList = arrayListOf<String>()

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
        return LayoutInflater.from(container?.context).inflate(R.layout.tm_dash_coupon_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterData = ArrayList<SortFilterItem>()
        val filterStatus = SortFilterItem("Semua Status")
        selectedStatus = StringBuilder()
        filterStatus.listener = {

            val options = ArrayList<Option>()

            options.add(Option(name = "Semua Status", key = "Semua Status", value = "1,2,3,4", inputType = Option.INPUT_TYPE_RADIO, inputState = "true"))
            options.add(Option(name = "Aktif", key = "Aktif", value = "2", inputType = Option.INPUT_TYPE_CHECKBOX))
            options.add(Option(name = "Belum Aktif", key = "Belum Aktif", value = "1", inputType = Option.INPUT_TYPE_CHECKBOX))
            options.add(Option(name = "Sudah Berakhir", key = "Sudah Berakhir", value = "4", inputType = Option.INPUT_TYPE_CHECKBOX))
            options.forEach {
                val cs: CharSequence = it.value
//                if(selectedStatus.contains(cs.toString(), false)){
                if(selectedStatusList.contains(it.value)){
                    it.inputState = "true"
                }
            }
            val cs: CharSequence = "0"
//            if(selectedStatus.contains(cs.toString(), false)){
            if(voucherStatus.contains("1,2,3,4")){
                options[0].inputState = "true"
            }
            else{
                options[0].inputState = ""
            }
            FilterGeneralDetailBottomSheet().show(
                parentFragmentManager,
                Filter(title = "Status Kupon", options = options),
                callback = object : FilterGeneralDetailBottomSheet.Callback{
                    override fun onApplyButtonClicked(optionList: List<Option>?) {
                        optionList?.forEachIndexed { index, option ->
                            if(option.inputState == "true"){
                                val cs: CharSequence = option.value
//                                if(!selectedStatus.contains(cs.toString(), false)){
                                if(!selectedStatusList.contains(option.value)){
//                                    selectedStatus.append(option.value + ",")
                                    selectedStatusList.add(option.value)
                                }
                                else{
//                                    selectedStatus = selectedStatus.removeRange(index-1, index+1) as StringBuilder
//                                    selectedStatusList.remove(option.value)
                                }
                            }
                            else{
                                selectedStatusList.remove(option.value)
                            }
//                            if(option.inputState == "false"){
//                                selectedStatus.removeRange(index, index+2)
//                            }
                        }
//                        selectedStatus = selectedStatus.removeRange(selectedStatus.length - 1, selectedStatus.length) as StringBuilder
                        if(selectedStatusList.isNullOrEmpty()){
                            filterStatus.type = ChipsUnify.TYPE_NORMAL
                        }
                        else{
                            filterStatus.type = ChipsUnify.TYPE_SELECTED
                        }
                        voucherStatus = selectedStatusList.toString().replace("[", "")
                        voucherStatus = voucherStatus.replace("]", "")
                        voucherStatus = voucherStatus.replace(" ", "")
                        if(selectedType.toInt() == 0){
                            tmCouponViewModel.getCouponList(voucherStatus, null)
                        }
                        else {
                            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
                        }
                    }
                },
            )
//            filterStatus.type = if(filterStatus.type == ChipsUnify.TYPE_NORMAL) {
//                ChipsUnify.TYPE_SELECTED
//            } else {
//                ChipsUnify.TYPE_NORMAL
//            }
//            filterStatus.selectedItem = arrayListOf("Semua Status", "Aktif", "Belum Aktif", "Sudah Berakhir")
        }
        filterData.add(filterStatus)
        val filterType = SortFilterItem("Semua Type")
        filterType.chevronListener
        filterType.iconDrawable = context?.let { getIconUnifyDrawable(it, IconUnify.ARROW_DOWN) }
        filterType.listener = {
            val filterList = ArrayList<Filter>()
            val options = ArrayList<Option>()
            options.add(Option(name = "Semua Type", key = "Semua Type", value = "0", inputType = Option.INPUT_TYPE_RADIO, inputState = "true"))
            options.add(Option(name = "Cashback", key = "Cashback", value = "3", inputType = Option.INPUT_TYPE_RADIO))
            options.add(Option(name = "Gratis Ongkir", key = "Gratis Ongkir", value = "1", inputType = Option.INPUT_TYPE_RADIO))
//            filterList.add(Filter(title = "Status Kupon", options = options))
//            filterList.add(Filter(title = "Status Type", options = options))
//            val data = DataValue(filterList, sort = arrayListOf())
//            val dynamicFilterModel = DynamicFilterModel(data)
//            val sortFilterBottomSheet = SortFilterBottomSheet()
//            sortFilterBottomSheet.show(childFragmentManager, mapOf(), dynamicFilterModel = dynamicFilterModel, callback = this)
            options.forEach {
                if(it.value == selectedType){
                    it.inputState = "true"
                }
            }
            if(selectedType == "0"){
                options[0].inputState = "true"
            }
            else{
                options[0].inputState = ""
            }
            FilterGeneralDetailBottomSheet().show(
                parentFragmentManager,
                Filter(title = "Status Kupon", options = options),
                callback = object : FilterGeneralDetailBottomSheet.Callback{
                    override fun onApplyButtonClicked(optionList: List<Option>?) {
                        optionList?.forEachIndexed { index, option ->
                            if(option.inputState == "true"){
                                if(selectedType != option.value){
                                    selectedType = option.value
                                }
                                else{
                                    selectedType = "0"
                                }
                            }
                            else{
                                if(selectedType == option.value){
                                    selectedType = "0"
                                }
                            }
                        }
                        if(selectedType == "0"){
                            filterType.type = ChipsUnify.TYPE_NORMAL
                        }
                        else{
                            filterType.type = ChipsUnify.TYPE_SELECTED
                        }
                        if(selectedType.toInt() == 0){
                            tmCouponViewModel.getCouponList(voucherStatus, null)
                        }
                        else {
                            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
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
        if(selectedType.toInt() == 0){
            tmCouponViewModel.getCouponList(voucherStatus, null)
        }
        else {
            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
        }
    }

    private fun observeViewModel() {

        tmCouponViewModel.couponListLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    tmCouponAdapter.vouchersItemList.clear()
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
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        fun newInstance(): TokomemberDashCouponFragment {
            return TokomemberDashCouponFragment()
        }
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        applySortFilterModel.selectedSortName
    }

    override fun getResultCount(mapParameter: Map<String, String>) {

    }

    override fun option(type: String, voucherId: String) {
        when(type){
            DELETE ->{
                val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
                dialog?.setTitle("Yakin batalkan program?")
                dialog?.setDescription("Pengaturan yang dibuat akan hilang kalau kamu batalkan proses pengaturan TokoMember, lho.")
                dialog?.setPrimaryCTAText("Lanjutkan")
                dialog?.setSecondaryCTAText("Batalkan Program")
                dialog?.setPrimaryCTAClickListener {
//                          val intent = Intent(requireContext(), TokomemberDashCreateProgramActivity::class.java)
//                        intent.putExtra(BUNDLE_EDIT_PROGRAM, true)
//                        intent.putExtra(BUNDLE_SHOP_ID, shopId)
//                        intent.putExtra(BUNDLE_PROGRAM_ID, programId)
//                        intent.putExtra(BUNDLE_PROGRAM_TYPE, ProgramType.EXTEND)
//                        requireContext().startActivity(intent)
                        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                }
                dialog?.setSecondaryCTAClickListener {
                    dialog.dismiss()
                }
                dialog?.show()
            }
            EDIT ->{

            }
            STOP ->{

            }
            ADD_QUOTA ->{
                TmAddQuotaBottomsheet.show(childFragmentManager, voucherId)
            }
        }
    }

}