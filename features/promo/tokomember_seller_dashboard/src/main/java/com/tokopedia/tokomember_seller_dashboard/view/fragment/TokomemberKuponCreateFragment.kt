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
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_kupon_create.*
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import javax.inject.Inject

class TokomemberKuponCreateFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_kupon_create, container, false)
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

    }

    private fun renderHeader() {

        btnContinue.setOnClickListener {
            (activity as TokomemberDashCreateActivity).addFragment(
                fragment = TokomemberDashPreviewFragment(),"Preview"
            )
        }
   /*     headerProgram?.apply {
            title = "Buat Kupon"
            subtitle = "Langkah 4 dari 4"
            isShowBackButton = true
        }
        progressProgram?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(50, false)
        }*/
    }

    private fun renderPreviewUI(membershipGetCardList: MembershipGetCardList?) {

    }

    companion object {

        fun newInstance(): TokomemberKuponCreateFragment {
            return TokomemberKuponCreateFragment()
        }
    }
}