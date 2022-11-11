package com.tokopedia.privacycenter.main.section.privacypolicy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.common.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.databinding.PrivacyPolicyBottomSheetBinding
import com.tokopedia.privacycenter.main.section.privacypolicy.adapter.PrivacyPolicyAdapter
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDetailDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.webview.PrivacyPolicyWebViewActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PrivacyPolicySectionBottomSheet : BottomSheetUnify(), PrivacyPolicyAdapter.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            PrivacyPolicySectionViewModel::class.java
        )
    }

    private var viewBinding by autoClearedNullable<PrivacyPolicyBottomSheetBinding>()
    private val privacyPolicyAdapter = PrivacyPolicyAdapter(this)

    init {
        showCloseIcon = true
        isDragable = false
        showKnob = false
        clearContentPadding = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
    }

    override val isFromBottomSheet: Boolean = true

    private fun initInjector() {
        DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = PrivacyPolicyBottomSheetBinding.inflate(inflater, container, false)
        setChild(viewBinding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        setTitle(getString(R.string.privacy_policy_bottom_sheet_title))

        viewBinding?.listPrivacyPolicy?.apply {
            adapter = privacyPolicyAdapter
        }

        viewModel.getPrivacyPolicyAllList()

        // TODO: Remove if already tested with real API
        onSuccessGetPrivacyPolicyAllList(listOf(
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
            PrivacyPolicyDataModel("", "Privacy Policy 2022 - Version 1.0.0."),
        ))
    }

    private fun initObservers() {
        viewModel.privacyPolicyList.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Fail -> { } // TODO: handle error
                is PrivacyCenterStateResult.Loading -> { } // // TODO: handle loading state
                is PrivacyCenterStateResult.Success -> {
                    onSuccessGetPrivacyPolicyAllList(it.data)
                }
            }
        }

        viewModel.privacyPolicyDetail.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Fail -> { } // TODO: handle error
                is PrivacyCenterStateResult.Loading -> { } // // TODO: handle loading state
                is PrivacyCenterStateResult.Success -> {
                    onSuccessGetPrivacyPolicyDetail(it.data)
                }
            }
        }
    }

    private fun onSuccessGetPrivacyPolicyAllList(data: List<PrivacyPolicyDataModel>) {
        privacyPolicyAdapter.apply {
            clearAllItems()
            addItems(data)
            notifyItemRangeInserted(0, data.size)
        }
    }

    private fun onSuccessGetPrivacyPolicyDetail(data: PrivacyPolicyDetailDataModel) {
        openDetailPrivacyPolicy(data.sectionTitle, data.sectionContent)
    }

    private fun openDetailPrivacyPolicy(title: String, htmlContent: String) {
        val intent = Intent(context, PrivacyPolicyWebViewActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString(PrivacyPolicyWebViewActivity.KEY_TITLE, title)
                putString(PrivacyPolicyConst.KEY_HTML_CONTENT, htmlContent)
            })
        }

        context?.startActivity(intent)
    }

    override fun onItemClicked(item: PrivacyPolicyDataModel) {

    }

    companion object {
        const val TAG = "PrivacyPolicyBottomSheet"
    }
}
