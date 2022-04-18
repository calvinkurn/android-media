package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetCardList
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateCardViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_create_card.*
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import javax.inject.Inject

class TokomemberDashPreviewFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateCardViewModel: TokomemberDashCreateCardViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateCardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderHeader()
        observeViewModel()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberDashCreateCardViewModel.tokomemberCardPreviewLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                    renderPreviewUI(it.data.membershipGetCardList)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun renderHeader() {
        headerProgram?.apply {
            title = "Buat Program"
            subtitle = "Langkah 2 dari 4"
            isShowBackButton = true
        }
        progressProgram?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(50, false)
        }
    }

    private fun renderPreviewUI(membershipGetCardList: MembershipGetCardList?) {

    }

    companion object {

        fun newInstance(): TokomemberDashCreateProgramFragment {
            return TokomemberDashCreateProgramFragment()
        }
    }
}