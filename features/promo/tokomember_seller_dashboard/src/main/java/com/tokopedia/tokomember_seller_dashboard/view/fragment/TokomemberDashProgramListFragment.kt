package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
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
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.CANCEL
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.LOADED
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashProgramAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import kotlinx.android.synthetic.main.tm_dash_program_fragment.*
import javax.inject.Inject

class TokomemberDashProgramListFragment : BaseDaggerFragment(), ProgramActions {

    private var tmTracker: TmTracker? = null
    private var shopId = 0
    private var cardId = 3668
    private lateinit var homeFragmentCallback: TmProgramDetailCallback

    private val tokomemberDashProgramAdapter: TokomemberDashProgramAdapter by lazy{
        TokomemberDashProgramAdapter(arrayListOf(), childFragmentManager, shopId, this, homeFragmentCallback)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
            shopId = it
        }

        if (context is TmProgramDetailCallback) {
            homeFragmentCallback =  context as TmProgramDetailCallback
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

        var rvProgram = view.findViewById<RecyclerView>(R.id.rv_program)
        rvProgram.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = tokomemberDashProgramAdapter
        }
        observeViewModel()
        tmProgramListViewModel?.getProgramList(arguments?.getInt(BUNDLE_SHOP_ID)?:0, cardId)
        tmTracker?.viewProgramListTabSection(arguments?.getInt(BUNDLE_SHOP_ID).toString())

        btnCreateProgram.setOnClickListener {
            //TODO
            TmDashCreateActivity.openActivity(shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.CREATE_BUAT, null, null, )
            tmTracker?.clickProgramListButton(shopId.toString())
        }
    }

    private fun observeViewModel() {
        tmProgramListViewModel?.tokomemberProgramListResultLiveData?.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING ->{
                    viewFlipperProgramList.displayedChild = 0
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    viewFlipperProgramList.displayedChild = 1
                    tokomemberDashProgramAdapter.programSellerList = it.data?.membershipGetProgramList?.programSellerList as ArrayList<ProgramSellerListItem>
                    tokomemberDashProgramAdapter.notifyDataSetChanged()
                    tmProgramListViewModel?.refreshList(LOADED)
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    tmProgramListViewModel?.refreshList(LOADED)
                }
            }
        })

        tmProgramListViewModel?.tokomemberProgramListLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                REFRESH ->{
                    tmProgramListViewModel?.getProgramList(arguments?.getInt(BUNDLE_SHOP_ID)?:0, cardId)
                }
            }
        })
        Log.d("REFRESH_TAG", "observeViewModel: " + tmProgramListViewModel?.tokomemberProgramListLiveData?.hasActiveObservers())
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
//                bundle.putInt(BUNDLE_PROGRAM_TYPE, ProgramType.EXTEND)
//                (activity as TokomemberDashHomeActivity).addFragment(TmProgramFragment.newInstance(bundle), TAG_HOME)
                TmDashCreateActivity.openActivity(shopId, activity, CreateScreenType.PROGRAM, ProgramActionType.EXTEND, null, programId)

            }
            type.equals(CANCEL) -> {
                val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
                dialog?.setTitle("Yakin batalkan program?")
                dialog?.setDescription("Pengaturan yang dibuat akan hilang kalau kamu batalkan proses pengaturan TokoMember, lho.")
                dialog?.setPrimaryCTAText("Lanjutkan")
                dialog?.setSecondaryCTAText("Batalkan Program")
                dialog?.setPrimaryCTAClickListener {
                /*      val intent = Intent(requireContext(), TokomemberDashCreateProgramActivity::class.java)
                    intent.putExtra(BUNDLE_EDIT_PROGRAM, true)
                    intent.putExtra(BUNDLE_SHOP_ID, shopId)
                    intent.putExtra(BUNDLE_PROGRAM_ID, programId)
                    intent.putExtra(BUNDLE_PROGRAM_TYPE, ProgramType.EXTEND)
                    requireContext().startActivity(intent)
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()*/
                }
                dialog?.setSecondaryCTAClickListener {
                    dialog.dismiss()
                }
                dialog?.show()
            }
            type.equals(EDIT) -> {
//                bundle.putInt(BUNDLE_PROGRAM_TYPE, ProgramType.EDIT)
                TmDashCreateActivity.openActivity(
                    shopId,
                    activity,
                    CreateScreenType.PROGRAM,
                    ProgramActionType.EDIT,
                    REQUEST_CODE_REFRESH,
                    programId
                )
//                (activity as TokomemberDashHomeActivity).addFragment(TmProgramFragment.newInstance(bundle), TAG_HOME)
            }
        }
    }

}