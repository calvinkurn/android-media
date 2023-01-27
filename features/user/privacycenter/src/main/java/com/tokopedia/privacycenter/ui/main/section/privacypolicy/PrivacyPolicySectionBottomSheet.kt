package com.tokopedia.privacycenter.ui.main.section.privacypolicy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.databinding.PrivacyPolicyBottomSheetBinding
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst.KEY_TITLE
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst.SECTION_ID
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.adapter.PrivacyPolicyAdapter
import com.tokopedia.privacycenter.ui.privacypolicywebview.PrivacyPolicyWebViewActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PrivacyPolicySectionBottomSheet : BottomSheetUnify(), PrivacyPolicyAdapter.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(
            PrivacyPolicySectionViewModel::class.java
        )
    }

    private var viewBinding by autoClearedNullable<PrivacyPolicyBottomSheetBinding>()
    private val privacyPolicyAdapter = PrivacyPolicyAdapter(this)

    init {
        showCloseIcon = true
        showKnob = false
        isDragable = false
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
    }

    private fun initObservers() {
        viewModel.bottomSheetState.observe(viewLifecycleOwner) {
            when (it) {
                PrivacyPolicyUiModel.InnerState.Error -> showLocalLoad()
                PrivacyPolicyUiModel.InnerState.Loading -> loadingPrivacyPolicyList()
                is PrivacyPolicyUiModel.InnerState.Success -> onSuccessGetPrivacyPolicyAllList(it.list)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSuccessGetPrivacyPolicyAllList(data: List<PrivacyPolicyDataModel>) {
        viewBinding?.apply {
            listPrivacyPolicy.show()
            loaderListPrivacyPolicy.hide()
            localLoadPrivacyPolicy.hide()
        }
        privacyPolicyAdapter.apply {
            clearAllItems()
            addItems(data)
            notifyDataSetChanged()
        }
    }

    private fun openDetailPrivacyPolicy(title: String, sectionId: String) {
        val intent = Intent(context, PrivacyPolicyWebViewActivity::class.java).apply {
            putExtras(
                Bundle().apply {
                    putString(KEY_TITLE, title)
                    putString(SECTION_ID, sectionId)
                }
            )
        }

        context?.startActivity(intent)
    }

    override fun onItemClicked(item: PrivacyPolicyDataModel) {
        openDetailPrivacyPolicy(item.sectionTitle, item.sectionId)
    }

    private fun loadingPrivacyPolicyList() {
        viewBinding?.apply {
            listPrivacyPolicy.hide()
            localLoadPrivacyPolicy.hide()
            loaderListPrivacyPolicy.show()
        }
    }

    private fun showLocalLoad() {
        viewBinding?.apply {
            listPrivacyPolicy.hide()
            loaderListPrivacyPolicy.hide()
            localLoadPrivacyPolicy.apply {
                localLoadTitle = context.getString(R.string.privacy_center_error_network_title)
                refreshBtn?.setOnClickListener {
                    viewModel.getPrivacyPolicyAllList()
                }
            }.show()
        }
    }

    companion object {
        const val TAG = "PrivacyPolicyBottomSheet"
    }
}
