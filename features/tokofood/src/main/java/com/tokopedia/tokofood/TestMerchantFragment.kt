package com.tokopedia.tokofood

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.FragmentTokofoodMerchantTestBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@FlowPreview
@ExperimentalCoroutinesApi
class TestMerchantFragment: BaseMultiFragment() {

    companion object {
        fun createInstance(): TestMerchantFragment {
            return TestMerchantFragment()
        }
    }

    private var binding by autoClearedNullable<FragmentTokofoodMerchantTestBinding>()

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    override fun getFragmentToolbar(): Toolbar? = null

    override fun getFragmentTitle(): String? = null

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodMerchantTestBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectValue()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        initializeMiniCartWidget()
    }

    private fun setupView() {

    }

    private fun collectValue() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationState?.collect { isSuccess ->
                if (isSuccess) {
                    goToPurchasePage()
                }
            }
        }
    }

    private fun initializeMiniCartWidget() {
        activityViewModel?.let {
            binding?.testMiniCart?.initialize(it, viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun goToPurchasePage() {
        navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
    }

}