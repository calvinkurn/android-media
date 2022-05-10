package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.CANCEL
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashProgramAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokomemberDashProgramListFragment : BaseDaggerFragment(), ProgramActions {

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
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
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

        var rvProgram = view.findViewById<RecyclerView>(R.id.rv_program)
        rvProgram.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = tokomemberDashProgramAdapter
        }

        observeViewModel()
        tokomemberDashCreateViewModel.getProgramList(arguments?.getInt(BUNDLE_SHOP_ID)?:0, cardId)
    }

    private fun observeViewModel() {

        tokomemberDashCreateViewModel.tokomemberProgramListResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    tokomemberDashProgramAdapter.programSellerList = it.data.membershipGetProgramList?.programSellerList as ArrayList<ProgramSellerListItem>
                    tokomemberDashProgramAdapter.notifyDataSetChanged()
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
        fun newInstance(extras: Bundle?) = TokomemberDashProgramListFragment().apply {
            arguments = extras
        }
    }

    override fun option(type: String, shopId: Int, programId: Int) {
        val bundle = Bundle()
        bundle.putBoolean(BUNDLE_EDIT_PROGRAM, true)
        bundle.putInt(BUNDLE_SHOP_ID, shopId)
        bundle.putInt(BUNDLE_PROGRAM_ID, programId)
        when {
            type.equals(EXTEND) -> {
//                bundle.putInt(BUNDLE_PROGRAM_TYPE, ProgramType.EXTEND)
//                (activity as TokomemberDashHomeActivity).addFragment(TokomemberProgramFragment.newInstance(bundle), TAG_HOME)
                TokomemberDashCreateActivity.openActivity(shopId, activity, ProgramScreenType.PROGRAM, ProgramActionType.EXTEND, null, programId)

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
                TokomemberDashCreateActivity.openActivity(
                    shopId,
                    activity,
                    ProgramScreenType.PROGRAM,
                    ProgramActionType.EDIT,
                    REQUEST_CODE_REFRESH,
                    programId
                )
//                (activity as TokomemberDashHomeActivity).addFragment(TokomemberProgramFragment.newInstance(bundle), TAG_HOME)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_REFRESH){
            if(resultCode == Activity.RESULT_OK){
                tokomemberDashCreateViewModel.getProgramList(arguments?.getInt(BUNDLE_SHOP_ID)?:0, cardId)
            }
        }
    }
}