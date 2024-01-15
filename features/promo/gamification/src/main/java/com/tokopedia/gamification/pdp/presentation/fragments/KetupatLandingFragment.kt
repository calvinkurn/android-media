package com.tokopedia.gamification.pdp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.R
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.data.di.components.PdpComponent
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewmodels.KetupatLandingViewModel
import javax.inject.Inject

class KetupatLandingFragment : BaseViewModelFragment<KetupatLandingViewModel>() {

    private val adapter: KetupatLandingAdapter by lazy {
        KetupatLandingAdapter(KetupatLandingAdapterTypeFactory())
    }
    private var ketupatLandingViewModel: KetupatLandingViewModel? = null

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        ketupatLandingViewModel?.getAffiliateDataItems()?.observe(this) {
            adapter.addMoreData(it)
        }
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        ketupatLandingViewModel = viewModel as KetupatLandingViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ketupat_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Handler(Looper.getMainLooper()).postDelayed({
//            view.findViewById<Group>(R.id.shimmer_group)?.hide()
//        }, 1000)

        val ketupatRV = view.findViewById<RecyclerView>(R.id.ketupat_rv)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.setVisitables(ArrayList())

        ketupatRV?.layoutManager = layoutManager
        ketupatRV?.adapter = adapter

        ketupatLandingViewModel?.getScratchCardsLandingInfo("ketupat-thr-2024")
    }

    override fun getViewModelType(): Class<KetupatLandingViewModel> {
        return KetupatLandingViewModel::class.java
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectKetupatLandingFragment(this)
    }

    private fun getComponent(): PdpComponent =
        DaggerPdpComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .activityContextModule(context?.let { ActivityContextModule(it) })
            .build()

    companion object {
        @JvmStatic
        fun newInstance() = KetupatLandingFragment()
    }
}
