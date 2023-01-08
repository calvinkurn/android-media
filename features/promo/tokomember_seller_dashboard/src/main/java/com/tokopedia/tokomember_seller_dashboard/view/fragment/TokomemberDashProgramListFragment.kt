package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CANCEL
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ACTION
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.CANCEL
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.LOADED
import com.tokopedia.tokomember_seller_dashboard.util.LOADING_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH_PROGRAM_LIST
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashProgramAdapter
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.ProgramUpdateMapper
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_program_fragment.*
import kotlinx.android.synthetic.main.tm_layout_no_access.*
import javax.inject.Inject

class TokomemberDashProgramListFragment : BaseDaggerFragment(), ProgramActions {

    private var currentPage = 1
    private var hasNext = false
    private var linearLayoutManager: LinearLayoutManager? = null
    private var rvProgram: RecyclerView? = null
    private var tmTracker: TmTracker? = null
    private var shopId = 0
    private var cardId = 0
    private lateinit var homeFragmentCallback: TmProgramDetailCallback
    private var loaderDialog: LoaderDialog?=null

    private val tokomemberDashProgramAdapter: TokomemberDashProgramAdapter by lazy{
        TokomemberDashProgramAdapter(arrayListOf(), childFragmentManager, shopId, this, homeFragmentCallback)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
            shopId = it
        }
        arguments?.getInt(BUNDLE_CARD_ID, 0)?.let {
            cardId = it
        }

        if (context is TmProgramDetailCallback) {
            homeFragmentCallback =  context
        } else {
            throw RuntimeException(context.toString() )
        }
    }

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmProgramListViewModel: TmProgramListViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = activity?.let { ViewModelProvider(it, viewModelFactory.get()) }
        viewModelProvider?.get(TmProgramListViewModel::class.java)
    }
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_program_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmTracker = TmTracker()

        rvProgram = view.findViewById(R.id.rv_program)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvProgram?.apply {
            layoutManager = linearLayoutManager
            adapter = tokomemberDashProgramAdapter
        }
        observeViewModel()
        tmProgramListViewModel?.getInitalProgramList(shopId, cardId, page = currentPage)

        setToastOnProgramAction(arguments?.getInt(BUNDLE_PROGRAM_ACTION)?:0)
        btnCreateProgram.setOnClickListener {
            TmDashCreateActivity.openActivity(shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.CREATE_BUAT, REQUEST_CODE_REFRESH_PROGRAM_LIST, null, cardId)
            tmTracker?.clickProgramListButton(shopId.toString())
        }
        setEmptyProgramListData()
        handleProgramListPagination()
    }

    private fun setToastOnProgramAction(programActionType:Int){
        arguments?.putInt(BUNDLE_PROGRAM_ACTION, -1)
        when(programActionType){
            ProgramActionType.CREATE_BUAT -> {
                view?.let { Toaster.build(it, " Yay, pengaturan TokoMember sudah dibuat. Kamu bisa cek progresnya di menu Home.", Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show() }
            }
            ProgramActionType.EXTEND ->{
                view?.let { Toaster.build(it, "Yay! Program berhasil diperpanjang.", Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show() }
            }
        }
    }

    private fun observeViewModel() {
        tmProgramListViewModel?.tokomemberProgramListResultLiveData?.observe(viewLifecycleOwner){
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING ->{
                    viewFlipperProgramList.displayedChild = 0
                }
                TokoLiveDataResult.STATUS.INFINITE_LOADING ->{
                    viewFlipperProgramList.displayedChild = 1
                     tmProgramListViewModel?.let{ it1 ->
                         tokomemberDashProgramAdapter.programSellerList = it1.programList
                         tokomemberDashProgramAdapter.notifyItemInserted(it1.programList.size.toZeroIfNull() - 1)
                     }
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    tmProgramListViewModel?.refreshProgramList(LOADED)
                    tmProgramListViewModel?.programListLoadingState(LOADED)
                    hasNext = it.data?.membershipGetProgramList?.programSellerList?.size != 0
                    if(it.data?.membershipGetProgramList?.programSellerList.isNullOrEmpty() && tokomemberDashProgramAdapter.programSellerList.isNullOrEmpty()){
                        viewFlipperProgramList.displayedChild = 2
                    }
                    else {
                        viewFlipperProgramList.displayedChild = 1
                        tmProgramListViewModel?.let{ it1 ->
                            if(currentPage==1){
                                val prevSize = tokomemberDashProgramAdapter.programSellerList.size
                                tokomemberDashProgramAdapter.programSellerList = it1.programList
                                tokomemberDashProgramAdapter.notifyItemRangeChanged(prevSize,it?.data?.membershipGetProgramList?.programSellerList?.size ?: 0)
                            }
                            else{
                                val prevSize = tokomemberDashProgramAdapter.programSellerList.size
                                tokomemberDashProgramAdapter.notifyItemRemoved(prevSize)
                                tokomemberDashProgramAdapter.programSellerList = it1.programList
                                tokomemberDashProgramAdapter.notifyItemRangeChanged(prevSize,it?.data?.membershipGetProgramList?.programSellerList?.size ?: 0)
                            }
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    tmProgramListViewModel?.refreshProgramList(LOADED)
                }
            }
        }

        tmProgramListViewModel?.tmRefreshProgramListLiveData?.observe(viewLifecycleOwner){
            when (it) {
                REFRESH ->{
                    tmProgramListViewModel?.getProgramList(shopId, cardId)
                }
            }
        }

        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner){
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING -> {
                    openLoadingDialog()
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipGetProgramForm?.resultStatus?.code == "200") {
                        var periodInMonth = 0
                        it.data.membershipGetProgramForm.programForm?.timeWindow?.startTime?.let { it1 ->
                            it.data.membershipGetProgramForm.programForm.timeWindow.endTime?.let { it2 ->
                                periodInMonth = TmDateUtil.getTimeDuration(
                                    it1, it2
                                )
                            }
                        }
                        tmDashCreateViewModel.updateProgram(ProgramUpdateMapper.formToUpdateMapper(it.data.membershipGetProgramForm, ProgramActionType.CANCEL, periodInMonth, cardId))
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    closeLoadingDialog()
                }
            }
        }

        tmDashCreateViewModel.tokomemberProgramUpdateResultLiveData.observe(viewLifecycleOwner){
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if(it.data?.membershipCreateEditProgram?.resultStatus?.code=="200"){
                        closeLoadingDialog()
                        tmProgramListViewModel?.refreshProgramList(REFRESH)
                        view?.let { it1 -> Toaster.build(it1, "Program dibatalkan.", Toaster.LENGTH_SHORT).show() }
                    }
                    else{
                        closeLoadingDialog()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR ->{
                    closeLoadingDialog()
                }
            }
        }
        Log.d("REFRESH_TAG", "observeViewModel: " + tmProgramListViewModel?.tmRefreshProgramListLiveData?.hasActiveObservers())
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    companion object {
        fun newInstance(extras: Bundle?) = TokomemberDashProgramListFragment().apply {
            arguments = extras
        }
    }

    override fun option(type: String, programId: Int, shopId: Int) {
        val bundle = Bundle()
        bundle.putBoolean(BUNDLE_EDIT_PROGRAM, true)
        bundle.putInt(BUNDLE_SHOP_ID, shopId)
        bundle.putInt(BUNDLE_PROGRAM_ID, programId)
        when {
            type.equals(EXTEND) -> {
                TmDashCreateActivity.openActivity(shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.EXTEND, null, programId)

            }
            type.equals(CANCEL) -> {
                tmTracker?.clickProgramBsCancel(shopId.toString(), programId.toString())
                val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
                dialog?.setTitle("Yakin batalkan program?")
                dialog?.setDescription("Pengaturan yang dibuat akan hilang kalau kamu batalkan proses pengaturan TokoMember, lho.")
                dialog?.setPrimaryCTAText("Lanjutkan")
                dialog?.setSecondaryCTAText("Batalkan Program")
                dialog?.setPrimaryCTAClickListener {
                    tmTracker?.clickProgramCancelPopUpPrimary(shopId.toString(), programId.toString())
                    dialog.dismiss()
                }
                dialog?.setSecondaryCTAClickListener {
                    tmTracker?.clickProgramCancelPopUpSecondary(shopId.toString(), programId.toString())
                    tmDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_CANCEL)
                    dialog.dismiss()
                }
                dialog?.show()
            }
            type.equals(EDIT) -> {
                TmDashCreateActivity.openActivity(
                    shopId,
                    activity,
                    CreateScreenType.PROGRAM,
                    ProgramActionType.EDIT,
                    REQUEST_CODE_REFRESH_PROGRAM_LIST,
                    programId
                )
            }
        }
    }

    private fun setEmptyProgramListData() {
        iv_error.loadImage("https://images.tokopedia.net/img/android/res/singleDpi/tm_empty_coupon.png")
        tv_heading_error.text = "Buat program TokoMember, yuk!"
        tv_desc_error.text = "Program yang menarik bisa bikin member lebih sering berbelanja di tokomu."
        btn_error.text = "Buat Program TokoMember"
        btn_error.setOnClickListener {
            TmDashCreateActivity.openActivity(shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.CREATE_BUAT, null, null )
        }
    }

    private fun openLoadingDialog(){

        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(LOADING_TEXT))
        loaderDialog?.show()
    }

    private fun closeLoadingDialog(){
        loaderDialog?.dialog?.dismiss()
    }

    private fun handleProgramListPagination() {
        rvProgram?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int? = linearLayoutManager?.childCount
                val totalItemCount: Int? = linearLayoutManager?.itemCount
                val firstVisibleItemPosition: Int? = linearLayoutManager?.findFirstVisibleItemPosition()
                if ((tmProgramListViewModel?.tmProgramListLoadingStateLiveData?.value)?.equals(LOADED) == true) {
                    if (visibleItemCount != null && firstVisibleItemPosition != null && totalItemCount != null) {
                        if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition > 0) {
                            if(hasNext) {
                                tmProgramListViewModel?.programListLoadingState(REFRESH)
                                currentPage += 1
                                tmProgramListViewModel?.getProgramList(
                                    shopId,
                                    cardId,
                                    page = currentPage
                                )
                            }
                        }
                    }
                }
            }
        })
    }

}
