package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberDashProgramAdapter
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashProgramViewmodel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokomemberDashProgramFragment : BaseDaggerFragment() {

    private var shopId = 6553698
    private var cardId = 3827

    private val tokomemberDashProgramAdapter: TokomemberDashProgramAdapter by lazy{
        TokomemberDashProgramAdapter(arrayListOf())
    }

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashProgramViewmodel: TokomemberDashProgramViewmodel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashProgramViewmodel::class.java)
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
        tokomemberDashProgramViewmodel.getProgramList(shopId, cardId)
    }

    private fun observeViewModel() {

        tokomemberDashProgramViewmodel.tokomemberProgramListResultLiveData.observe(viewLifecycleOwner, {
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
        fun newInstance(): TokomemberDashProgramFragment {
            return TokomemberDashProgramFragment()
        }
    }
}