package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmFilterCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.VouchersItem
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.DELETE
import com.tokopedia.tokomember_seller_dashboard.util.DUPLICATE
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.STOP
import com.tokopedia.tokomember_seller_dashboard.util.TM_EMPTY_COUPON
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TmCouponAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.tm_dash_coupon_fragment.viewFlipperCoupon
import kotlinx.android.synthetic.main.tm_dash_coupon_list.*
import kotlinx.android.synthetic.main.tm_layout_no_access.*
import javax.inject.Inject


class TokomemberDashCouponFragment : BaseDaggerFragment(), TmCouponActions, SortFilterBottomSheet.Callback,
    TmCouponListRefreshCallback, TmFilterCallback {

    private var showButton: Boolean = true
    private var filterStatus: SortFilterItem? = null
    private var filterType: SortFilterItem? = null
    private var voucherStatus = "1,2,3,4"
    private var voucherType = 0
    private var voucherIdToUpdate = 0
    private var voucherStatusToUpdate = ""
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
        return inflater.inflate(R.layout.tm_dash_coupon_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterData = ArrayList<SortFilterItem>()
        filterStatus = SortFilterItem("Semua Status")
        selectedStatus = StringBuilder()
        filterStatus?.listener = {

            val options = ArrayList<Option>()

            options.add(Option(name = "Semua Status", key = "Semua Status", value = "1,2,3,4", inputType = Option.INPUT_TYPE_RADIO, inputState = "true"))
            options.add(Option(name = "Aktif", key = "Aktif", value = "2", inputType = Option.INPUT_TYPE_RADIO))
            options.add(Option(name = "Belum Aktif", key = "Belum Aktif", value = "1", inputType = Option.INPUT_TYPE_RADIO))
            options.add(Option(name = "Sudah Berakhir", key = "Sudah Berakhir", value = "4", inputType = Option.INPUT_TYPE_RADIO))
            options.forEach {
                val cs: CharSequence = it.value
//                if(selectedStatus.contains(cs.toString(), false)){
                if(selectedStatusList.contains(it.value)){
                    it.inputState = "true"
                }
            }
            val cs: CharSequence = "0"
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
                                if(!selectedStatusList.contains(option.value)){
                                    selectedStatusList.add(option.value)
                                }
                            }
                            else{
                                selectedStatusList.remove(option.value)
                            }
                        }
                        voucherStatus = selectedStatusList.toString().replace("[", "")
                        voucherStatus = voucherStatus.replace("]", "")
                        voucherStatus = voucherStatus.replace(" ", "")
                        if(voucherStatus == "1,2,3,4"){
                            filterStatus?.type = ChipsUnify.TYPE_NORMAL
                        }
                        else{
                            filterStatus?.type = ChipsUnify.TYPE_SELECTED
                        }
                        if(selectedType.toInt() == 0){
                            tmCouponViewModel.getCouponList(voucherStatus, null)
                        }
                        else {
                            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
                        }
                    }
                }
            )
        }
        filterStatus?.let {
            filterData.add(it)
        }
        filterType = SortFilterItem("Semua Tipe")
        filterType?.chevronListener
        filterType?.iconDrawable = context?.let { getIconUnifyDrawable(it, IconUnify.ARROW_DOWN) }
        filterType?.listener = {
            val filterList = ArrayList<Filter>()
            val options = ArrayList<Option>()
            options.add(Option(name = "Semua Tipe", key = "Semua Tipe", value = "0", inputType = Option.INPUT_TYPE_RADIO, inputState = "true"))
            options.add(Option(name = "Cashback", key = "Cashback", value = "3", inputType = Option.INPUT_TYPE_RADIO))
            options.add(Option(name = "Gratis Ongkir", key = "Gratis Ongkir", value = "1", inputType = Option.INPUT_TYPE_RADIO))
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
                            filterType?.type = ChipsUnify.TYPE_NORMAL
                        }
                        else{
                            filterType?.type = ChipsUnify.TYPE_SELECTED
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
        }
        filterType?.let { filterData.add(it) }
        filter.addItem(filterData)
        filter.parentListener = {
            TmFilterBottomsheet.show(childFragmentManager, this, voucherStatus, selectedType)
        }
        filter_error.addItem(filterData)
        filter_error.parentListener = {
            TmFilterBottomsheet.show(childFragmentManager, this, voucherStatus, selectedType)
        }
        rv_coupon.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = tmCouponAdapter
        }

        if(showButton) {
            btn_create_coupon.show()
            btn_create_coupon.setOnClickListener {
                TmDashCreateActivity.openActivity(
                    activity,
                    CreateScreenType.COUPON_SINGLE,
                    null,
                    this,
                    edit = false
                )
            }
        }
        else{
            btn_create_coupon.visibility = View.INVISIBLE
        }

        observeViewModel()
        if(selectedType.toInt() == 0){
            tmCouponViewModel.getCouponList(voucherStatus, null)
        }
        else {
            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
        }

        setEmptyCouponListData()
    }

    private fun setEmptyCouponListData() {
        iv_error.loadImage(TM_EMPTY_COUPON)
        tv_heading_error.text = "Buat kupon TokoMember dulu, yuk!"
        tv_desc_error.text = "Semakin menarik kupon yang kamu buat, semakin banyak jumlah transaksi yang didapatkan dari member tokomu."
        btn_error.text = "Buat Kupon"
        btn_error.setOnClickListener {
            TmDashCreateActivity.openActivity(activity, CreateScreenType.COUPON_SINGLE, null, this, edit = false)
        }
    }

    private fun observeViewModel() {

        tmCouponViewModel.couponListLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING ->{
                    viewFlipperCoupon.displayedChild = 0
                    filter_error.hide()
                }
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    if(it.data?.merchantPromotionGetMVList?.data?.vouchers.isNullOrEmpty()){
                        viewFlipperCoupon.displayedChild = 2
                        filter_error.show()
                    }
                    else {
                        filter_error.hide()
                        viewFlipperCoupon.displayedChild = 1
                        tmCouponAdapter.vouchersItemList.clear()
                        tmCouponAdapter.vouchersItemList = it.data?.merchantPromotionGetMVList?.data?.vouchers as ArrayList<VouchersItem>
                        tmCouponAdapter.notifyDataSetChanged()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR-> {

                }
            }
        })

        tmCouponViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    it.data?.getInitiateVoucherPage?.data?.token?.let { it1 ->
                        tmCouponViewModel.updateStatus(voucherId = voucherIdToUpdate, status = voucherStatusToUpdate, token = it1)
                    }
                }
                TokoLiveDataResult.STATUS.ERROR ->{

                }
            }
        })

        tmCouponViewModel.tmCouponUpdateLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    if(it.data?.data?.status == 200) {
                        refreshCouponList(voucherStatusToUpdate)
                    }
                    else{
                        view?.let { it1 -> it.data?.data?.message?.let { it2 ->
                            Toaster.build(it1,
                                it2, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                        } }                    }
                }
                TokoLiveDataResult.STATUS.ERROR ->{

                }
            }
        })
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        fun newInstance(bundle: Bundle?, showButton: Boolean = true) = TokomemberDashCouponFragment().apply {
            arguments = bundle
            this.showButton = showButton
        }
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        applySortFilterModel.selectedSortName
    }

    override fun getResultCount(mapParameter: Map<String, String>) {

    }

    override fun option(type: String, voucherId: String, couponType: String, currentQuota: Int, maxCashback: Int) {
        when(type){
            DELETE ->{
                val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
                dialog?.setTitle("Yakin hapus kupon?")
                dialog?.setDescription("Kupon yang sudah dihapus tidak bisa diaktifkan kembali, lho.")
                dialog?.setPrimaryCTAText("Lanjut")
                dialog?.setSecondaryCTAText("Hapus Kupon")
                dialog?.setPrimaryCTAClickListener {
                        dialog.dismiss()
                }
                dialog?.setSecondaryCTAClickListener {
                    voucherIdToUpdate = voucherId.toInt()
                    voucherStatusToUpdate = DELETE
                    tmCouponViewModel.getInitialCouponData("update", couponType.lowercase())
                    dialog.dismiss()
                }
                dialog?.show()
            }
            EDIT ->{
                TmDashCreateActivity.openActivity(activity, CreateScreenType.COUPON_SINGLE, voucherId.toInt(), this, edit = true)
            }
            STOP ->{
                val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
                dialog?.setTitle("Yakin hentikan kupon?")
                dialog?.setDescription("Kupon yang sudah dihentikan tidak bisa diaktifkan kembali, lho.")
                dialog?.setPrimaryCTAText("Lanjut")
                dialog?.setSecondaryCTAText("Hentikan Kupon")
                dialog?.setPrimaryCTAClickListener {
                    dialog.dismiss()
                }
                dialog?.setSecondaryCTAClickListener {
                    voucherIdToUpdate = voucherId.toInt()
                    voucherStatusToUpdate = STOP
                    tmCouponViewModel.getInitialCouponData("update", couponType.lowercase())
                    dialog.dismiss()
                }
                dialog?.show()
            }
            ADD_QUOTA ->{
                TmAddQuotaBottomsheet.show(childFragmentManager, voucherId, currentQuota, couponType, this, maxCashback)
            }
            DUPLICATE ->{
                TmDashCreateActivity.openActivity(activity, CreateScreenType.COUPON_SINGLE, voucherId.toInt(), this, edit = false, duplicate = true)
            }
        }
    }

    override fun refreshCouponList(action: String) {
        if(selectedType.toInt() == 0){
            tmCouponViewModel.getCouponList(voucherStatus, null)
        }
        else {
            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
        }
        var message = ""
        when(action){
            ACTION_EDIT -> {
                message = "Yay, kupon TokoMember berhasil diubah!"
            }
            ACTION_CREATE ->{
                message = "Yay, kupon TokoMember berhasil dibuat!"
            }
            ADD_QUOTA ->{
                message = "Yay, kuota kupon berhasil ditambahkan!"
            }
            DELETE ->{
                message = "Kupon sudah dihapus."
            }
            STOP ->{
                message = "Kupon sudah dihentikan."
            }
        }
        view?.let { it1 ->
            Toaster.build(it1, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun selectedFilter(status: String, type: String) {
        voucherStatus = status
        selectedType = type
        if(voucherStatus == "1,2,3,4"){
            filterStatus?.type = ChipsUnify.TYPE_NORMAL
        }
        else{
            filterStatus?.type = ChipsUnify.TYPE_SELECTED
        }
        if(selectedType.toInt() == 0){
            filterType?.type = ChipsUnify.TYPE_NORMAL
            tmCouponViewModel.getCouponList(voucherStatus, null)
        }
        else {
            filterType?.type = ChipsUnify.TYPE_SELECTED
            tmCouponViewModel.getCouponList(voucherStatus, selectedType.toInt())
        }
    }
}