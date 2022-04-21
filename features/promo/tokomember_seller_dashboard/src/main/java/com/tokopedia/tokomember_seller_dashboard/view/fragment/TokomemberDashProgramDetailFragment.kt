package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.TmProgramDetailQuery
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DETAIL
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashProgramViewmodel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_program_detail_fragment.*
import javax.inject.Inject

class TokomemberDashProgramDetailFragment : BaseDaggerFragment() {

    private var shopId = 0
    private var programId = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashProgramViewmodel: TokomemberDashProgramViewmodel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashProgramViewmodel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(BUNDLE_SHOP_ID, 0)?.let {
            shopId = it
        }
        arguments?.getInt(BUNDLE_PROGRAM_ID, 0)?.let {
            programId = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_program_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        tokomemberDashProgramViewmodel.getProgramInfo(programId, shopId, ACTION_DETAIL, TmProgramDetailQuery)
        setHeader()
    }


    private fun observeViewModel() {
        tokomemberDashProgramViewmodel.tokomemberProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                    setMainUi(it.data.membershipGetProgramForm)
                }
                is Fail -> {

                }
            }
        })
    }
    private fun setMainUi(membershipGetProgramForm: MembershipGetProgramForm?) {
        tvProgramPeriod.text = "${membershipGetProgramForm?.programForm?.timeWindow?.startTime} - ${membershipGetProgramForm?.programForm?.timeWindow?.endTime}"
        tvMemberCount.text = membershipGetProgramForm?.programForm?.analytics?.totalNewMember
        tvTransactionCount.text = membershipGetProgramForm?.programForm?.analytics?.trxCount
        tvIncome.text = membershipGetProgramForm?.programForm?.analytics?.totalIncome
        tvName.text = membershipGetProgramForm?.programForm?.name
        tvTransactionPremium.text = membershipGetProgramForm?.programForm?.programAttributes?.firstOrNull()?.minimumTransaction.toString()
        tvTransactionVip.text = membershipGetProgramForm?.programForm?.programAttributes?.get(1)?.minimumTransaction.toString()
        tvMemberCountPremium.text = membershipGetProgramForm?.levelInfo?.levelList?.firstOrNull()?.totalMember
        tvMemberCountVip.text = membershipGetProgramForm?.levelInfo?.levelList?.get(1)?.totalMember
    }

    private fun setHeader() {
        header_program_detail.title = "Detail Program"
        header_program_detail.setNavigationOnClickListener {

        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    companion object {
        fun newInstance(extras: Bundle?) = TokomemberDashProgramDetailFragment().apply {
            arguments = extras
        }
    }
}